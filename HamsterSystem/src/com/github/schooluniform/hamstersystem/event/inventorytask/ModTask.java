package com.github.schooluniform.hamstersystem.event.inventorytask;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.function.BiConsumer;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.github.schooluniform.hamstersystem.HamsterSystem;
import com.github.schooluniform.hamstersystem.I18n;
import com.github.schooluniform.hamstersystem.data.Data;
import com.github.schooluniform.hamstersystem.entity.EntityAttribute;
import com.github.schooluniform.hamstersystem.entity.FightEntity;
import com.github.schooluniform.hamstersystem.fightsystem.DamageSystem;
import com.github.schooluniform.hamstersystem.fightsystem.base.DamageType;
import com.github.schooluniform.hamstersystem.gui.ModGui;
import com.github.schooluniform.hamstersystem.util.ModUtils;
import com.github.schooluniform.hamstersystem.util.Util;
import com.github.schooluniform.hamstersystem.weapon.Weapon;
import com.github.schooluniform.hamstersystem.weapon.WeaponAttribute;

/**
 * 每秒计算Mod带来的属性加成
 * @author MiSkYle
 *
 */
public class ModTask implements Runnable{
	//PlayerName  TaskID
	private static HashMap<String, Integer> tasks = new HashMap<String, Integer>();
	
	private Player p;
	//true entity | false weapon
	private boolean entityOrWeapon;
	private ItemStack[] mods = new ItemStack[ModGui.getModSlots().size()];
	
	private ModTask(Player player,boolean entityOrWeapon) {
		this.p = player;
		this.entityOrWeapon = entityOrWeapon;
	}
	
	@Override
	public void run() {
		Inventory inv = p.getOpenInventory().getTopInventory();
		if(inv.getTitle().equalsIgnoreCase(Data.getModGuiTitle())) {
			boolean changed = true;
//			int index = 0;
//			for(int slot : ModGui.getModSlots()) {
//				ItemStack mod = inv.getItem(slot);
//				if((mod != null && mods[index] == null) ||
//						(mod == null && mods[index] != null) ||
//						(mod != null && mods[index] != null && !mod.equals(mods[index]))) {
//					mods[index] = mod;
//					changed = true;
//					continue;
//				}
//				index ++;
//			}
			int index = 0;
			for(int slot : ModGui.getModSlots()) {
				mods[index++] = inv.getItem(slot);
			}
			if(changed) {
				if(entityOrWeapon) {
					int[][] modData = ModUtils.getModsData(mods);
					FightEntity fe = new FightEntity(Data.getPlayerData(p.getName()),modData[0],modData[1]);
					List<String> lore = Arrays.asList(getString(fe.getAttributes()));
					inv.setItem(ModGui.getInfoSlot2(), getSignItem(lore));
				}else {
					Weapon weapon = DamageSystem.getModWeapon(ModUtils.setWeaponMod(inv.getItem(ModGui.getItemSlot()).clone(), mods));
					List<String> lore = Arrays.asList(getString(weapon.getAttributes(),weapon.getDamages()));
					inv.setItem(ModGui.getInfoSlot2(), getSignItem(lore));
				}
			}
		}else {
			cancel(p.getName());
		}
	}
	
	public static void cancel(String playerName) {
		if(tasks.containsKey(playerName)) {
			Bukkit.getScheduler().cancelTask(tasks.get(playerName));
			tasks.remove(playerName);
		}
	}
	
	public static void start(Player player,boolean entityOrWeapon) {
		if(tasks.containsKey(player.getName()))return;
		ModTask mod = new ModTask(player, entityOrWeapon);
		tasks.put(player.getName(), HamsterSystem.plugin.getServer().getScheduler().runTaskTimerAsynchronously(HamsterSystem.plugin,
				mod, 0, 20).getTaskId());
		
	}
	
	private String[] getString(HashMap<WeaponAttribute, Double> attribute,HashMap<DamageType,Double> damage) {
		StringBuilder sb = new StringBuilder();
		damage.forEach(new BiConsumer<DamageType,Double>() {
			@Override
			public void accept(DamageType t, Double u) {
				sb.append(I18n.tr("damage-type."+t.name())+": "+Util.formatNum(u, 2)+"\n");
			}
		});
		
		attribute.forEach(new BiConsumer<WeaponAttribute,Double>() {
			@Override
			public void accept(WeaponAttribute t, Double u) {
				sb.append(I18n.tr("weapon-attribute."+t.name())+": "+Util.formatNum(u, 2)+"\n");
			}
		});
		
		return sb.toString().split("\n");
	}
	
	private String[] getString(HashMap<EntityAttribute, Double> attribute) {
		StringBuilder sb = new StringBuilder();
		attribute.forEach(new BiConsumer<EntityAttribute,Double>() {
			@Override
			public void accept(EntityAttribute t, Double u) {
				sb.append(I18n.tr("weapon-attribute."+t.name())+": "+Util.formatNum(u, 2)+"\n");
			}
		});
		
		return sb.toString().split("\n");
	}
	
	private ItemStack getSignItem(List<String> lore) {
		ItemStack item = new ItemStack(Material.SIGN);
		ItemMeta im = item.getItemMeta();
		im.setDisplayName(I18n.tr("mod-gui.info-sign"));
		im.setLore(lore);
		item.setItemMeta(im);
		return item.clone();
	}
}
