package com.github.schooluniform.hamstersystem.event;

import java.util.Stack;

import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.Event.Result;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType.SlotType;
import org.bukkit.inventory.ItemStack;

import com.github.schooluniform.hamstersystem.data.Data;
import com.github.schooluniform.hamstersystem.fightsystem.DamageSystem;
import com.github.schooluniform.hamstersystem.gui.ModGui;
import com.github.schooluniform.hamstersystem.mod.Mod;
import com.github.schooluniform.hamstersystem.mod.ModType;
import com.github.schooluniform.hamstersystem.util.ModUtils;
import com.github.schooluniform.hamstersystem.weapon.Weapon;
import com.github.schooluniform.hamstersystem.weapon.WeaponLauncher;
import com.github.schooluniform.hamstersystem.weapon.WeaponMelee;

public class InventoryEvent implements Listener{
	
	@EventHandler
	public void onPlayerClick(InventoryClickEvent e) {
		if(e.getWhoClicked().getType() != EntityType.PLAYER) return;
		if(e.getClick() == ClickType.WINDOW_BORDER_LEFT || e.getClick() == ClickType.WINDOW_BORDER_RIGHT) return;
		if(e.getSlotType() == SlotType.OUTSIDE) return;
		if(e.getInventory().getTitle().equalsIgnoreCase(Data.getModGuiTitle())) {
			if(e.getInventory().getItem(ModGui.getItemSlot()).getType() == Material.SKULL_ITEM) {
				if(e.getRawSlot()<27) {
					if(!ModGui.getModSlots().contains(e.getRawSlot())){
						e.setResult(Result.DENY);
						e.setCancelled(true);
						return;
					}
				}				
			}else {
				//Weapon MOD
				if(e.getRawSlot()<27) {
					if(!ModGui.getModSlots().contains(e.getRawSlot())){
						e.setResult(Result.DENY);
						e.setCancelled(true);
						return;
					}
				}else if(e.getClickedInventory().getItem(e.getSlot())!=null){
					if(e.getClickedInventory().getItem(e.getSlot()).equals(e.getInventory().getItem(ModGui.getItemSlot()))){
						e.setResult(Result.DENY);
						e.setCancelled(true);
						return;
					}
				}				
			}
		}

	}
	
	
	@EventHandler
	public void onPlayerCloseInv(InventoryCloseEvent e){
		if(e.getInventory().getTitle().equalsIgnoreCase(Data.getModGuiTitle())) {
			if(e.getInventory().getItem(ModGui.getItemSlot()).getType() == Material.SKULL_ITEM) {
				//EntityMod
				ItemStack[] mods = new ItemStack[8];
				Stack<String> addedMods = new Stack<String>();
				int index = 0;
				int modPoint = 0;
				int maxModPoint = 30;
				for(int i : ModGui.getModSlots()) {
					ItemStack item = e.getInventory().getItem(i);
					if(modPoint >= maxModPoint) {
						mods[index] = null;
						giveItem((Player) e.getPlayer(), item);
						continue;
					}
					if(DamageSystem.isMod(item)) {
						Mod mod = ModUtils.getBasicMod(item);
						if((mod.getType() == ModType.Player)){
							if(addedMods.contains(mod.getName())) {
								mods[index] = null;
								giveItem((Player) e.getPlayer(), item);
								continue;
							}else{
								int modPoint2 = mod.getBasePoint()+ModUtils.getModData(item)[1];
								if(modPoint+modPoint2 > maxModPoint) {
									mods[index] = null;
									giveItem((Player) e.getPlayer(), item);
									continue;
								}else if(item.getAmount()>1) {
									ItemStack item2 = item.clone();
									item2.setAmount(1);
									item.setAmount(item.getAmount()-1);
									mods[index] = item2;
									giveItem((Player) e.getPlayer(), item);
								}else {
									mods[index] = item;
								}
								addedMods.add(mod.getName());
								modPoint+=modPoint2;
							}
						}else{
							giveItem((Player) e.getPlayer(), item);
							mods[index] = null;
							continue;
						}
					}else{
						giveItem((Player) e.getPlayer(), item);
						mods[index] = null;
						continue;
					}
					
					index++;
				}
				Data.getPlayerData(e.getPlayer().getName()).setMod(mods);
				
			}else {
				ItemStack[] mods = new ItemStack[8];
				ItemStack weapon = e.getPlayer().getInventory().getItemInMainHand();
				Weapon weaponData = DamageSystem.getBasicWeapon(weapon);
				Stack<String> addedMods = new Stack<String>();
				if(weaponData == null) {
					for(int i : ModGui.getModSlots()) {
						giveItem((Player) e.getPlayer(), e.getInventory().getItem(i));
					}
					return;
				}
				
				int index = 0;
				int modPoint = 0;
				int maxModPoint = ModUtils.getWeaponInfoFirst(weapon) == 0?30:60;
				for(int i : ModGui.getModSlots()) {
					ItemStack item = e.getInventory().getItem(i);
					if(modPoint >= maxModPoint) {
						mods[index] = null;
						giveItem((Player) e.getPlayer(), item);
						continue;
					}
					if(DamageSystem.isMod(item)) {
						Mod mod = ModUtils.getBasicMod(item);
						if((mod.getType() == ModType.Launcher && (weaponData instanceof WeaponLauncher)) ||
								(mod.getType() == ModType.Melee && (weaponData instanceof WeaponMelee)) ||
								(mod.getType() == ModType.Display && weaponData.getName().equalsIgnoreCase(mod.getTypeDisplay()))){
							if(addedMods.contains(mod.getName())) {
								mods[index] = null;
								giveItem((Player) e.getPlayer(), item);
								continue;
							}else{
								int modPoint2 = mod.getBasePoint()+ModUtils.getModData(item)[1];
								if(modPoint+modPoint2 > maxModPoint) {
									mods[index] = null;
									giveItem((Player) e.getPlayer(), item);
									continue;
								}else if(item.getAmount()>1) {
									ItemStack item2 = item.clone();
									item2.setAmount(1);
									item.setAmount(item.getAmount()-1);
									mods[index] = item2;
									giveItem((Player) e.getPlayer(), item);
								}else {
									mods[index] = item;
								}
								addedMods.add(mod.getName());
								modPoint+=modPoint2;
							}
						}else{
							giveItem((Player) e.getPlayer(), item);
							mods[index] = null;
							continue;
						}
					}else{
						giveItem((Player) e.getPlayer(), item);
						mods[index] = null;
						continue;
					}
					
					index++;
				}
				weapon = ModUtils.setWeaponMod(weapon, mods);
				e.getPlayer().getInventory().setItemInMainHand(weapon);
			}
		}
	}
	
	private void giveItem(Player p,ItemStack item) {
		if(item == null)return;
		if(!p.getInventory().addItem(item).isEmpty())
			p.getWorld().dropItem(p.getLocation(), item);
	}
}
