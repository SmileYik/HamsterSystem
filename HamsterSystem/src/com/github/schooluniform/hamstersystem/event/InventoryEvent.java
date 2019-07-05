package com.github.schooluniform.hamstersystem.event;

import java.util.LinkedList;
import java.util.List;

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
		//Weapon MOD
		if(e.getInventory().getTitle().equalsIgnoreCase(Data.getModGuiTitle())) {
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
	
	
	@EventHandler
	public void onPlayerCloseInv(InventoryCloseEvent e){
		//Weapon Mod
		if(e.getInventory().getTitle().equalsIgnoreCase(Data.getModGuiTitle())) {
			List<ItemStack> list = new LinkedList<ItemStack>();
			ItemStack weapon = e.getPlayer().getInventory().getItemInMainHand();
			Weapon weaponData = DamageSystem.getBasicWeapon(weapon);
			if(weaponData == null) {
				for(int i : ModGui.getModSlots()) {
					giveItem((Player) e.getPlayer(), e.getInventory().getItem(i));
				}
				return;
			}
			for(int i : ModGui.getModSlots()) {
				ItemStack item = e.getInventory().getItem(i);
				if(DamageSystem.isMod(item)) {
					Mod mod = ModUtils.getBasicMod(item);
					if((mod.getType() == ModType.Launcher && (weaponData instanceof WeaponLauncher)) ||
							(mod.getType() == ModType.Melee && (weaponData instanceof WeaponMelee)) ||
							(mod.getType() == ModType.Display && weaponData.getName().equalsIgnoreCase(mod.getTypeDisplay()))){
						if(item.getAmount()>1) {
							ItemStack item2 = item.clone();
							item2.setAmount(1);
							item.setAmount(item.getAmount()-1);
							list.add(item2);
							giveItem((Player) e.getPlayer(), item);
						}else {
							list.add(item);
						}
					}else {
						giveItem((Player) e.getPlayer(), item);
					}
				}else{
					giveItem((Player) e.getPlayer(), item);
				}
			}
			weapon = ModUtils.setWeaponMod(weapon, list);
			e.getPlayer().getInventory().setItemInMainHand(weapon);
		}else if(e.getInventory().getTitle().equalsIgnoreCase("EntityMod")) {
			//Entity Mod
			List<ItemStack> list = new LinkedList<ItemStack>();
			for(int i : ModGui.getModSlots()) {
				ItemStack item = e.getInventory().getItem(i);
				if(DamageSystem.isMod(item)) {
					if(item.getAmount()>1) {
						ItemStack item2 = item.clone();
						item2.setAmount(1);
						item.setAmount(item.getAmount()-1);
						list.add(item2);
						giveItem((Player) e.getPlayer(), item);
					}else {
						list.add(item);
					}
				}else {
					giveItem((Player) e.getPlayer(), item);
				}
			}
			
		}
	}
	
	private void giveItem(Player p,ItemStack item) {
		if(item == null)return;
		if(!p.getInventory().addItem(item).isEmpty())
			p.getWorld().dropItem(p.getLocation(), item);
	}
}
