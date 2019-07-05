package com.github.schooluniform.hamstersystem.gui;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import com.github.schooluniform.hamstersystem.I18n;
import com.github.schooluniform.hamstersystem.data.Data;
import com.github.schooluniform.hamstersystem.data.PlayerData;
import com.github.schooluniform.hamstersystem.event.inventorytask.ModTask;
import com.github.schooluniform.hamstersystem.fightsystem.DamageSystem;
import com.github.schooluniform.hamstersystem.mod.Mod;
import com.github.schooluniform.hamstersystem.util.ModUtils;

public class ModGui {
	private static List<Integer> modSlots = Arrays.asList(9,10,11,15,16,17,18,26);
	private static int itemSlot = 4;
	private static int infoSlot1 = 13;
	private static int infoSlot2 = 22;
	
	private static Inventory getInventory() {
		Inventory inv = Bukkit.createInventory(null, 27, Data.getModGuiTitle());
		ItemStack itemP = new ItemStack(Material.STAINED_GLASS_PANE,1,(short)0);
		for(int i = 0;i<27;i++) {
			if(!modSlots.contains(i)) {
				inv.setItem(i, itemP);
			}
		}
		inv.setItem(0, new ItemStack(Material.STAINED_GLASS_PANE,1,(short)15));
		inv.setItem(8, new ItemStack(Material.STAINED_GLASS_PANE,1,(short)15));
		inv.setItem(infoSlot1, new ItemStack(Material.STAINED_GLASS_PANE,0,(short)5));
		inv.setItem(infoSlot2, new ItemStack(Material.STAINED_GLASS_PANE,0,(short)5));
		return inv;
	}
	
	public static void openGuiWeapon(Player p) {
		Inventory inv = getInventory();
		ItemStack item = p.getInventory().getItemInMainHand().clone();
		if(!DamageSystem.isWeapon(item)) {
			I18n.send(p, "mod-gui.item-is-not-weapon");
			return;
		}
		inv.setItem(itemSlot,item);
		int[][] modsData = ModUtils.getWeaponMods(item);
		if(modsData[0]!=null) {
			int index = 0;
			for(int slot : modSlots) {
				if(index<modsData[0].length) {
					Mod mod = Data.getMod(modsData[0][index]);
					inv.setItem(slot, mod.getItem(modsData[1][index], modsData[2][index], modsData[3][index+1]));
					index++;
				}else {
					break;
				}
			}			
		}
		//Player
		/*
		 * PlayerData pd = Data.getPlayerData(p.getName()); int index = 0; for(ItemStack
		 * item : pd.getMods()) { inv.setItem(modSlots.get(index++), item); }
		 */
		p.openInventory(inv);
		ModTask.start(p, false);
	}
	
	public static void openGUIEntity(Player p) {
		
	}

	public static List<Integer> getModSlots() {
		return modSlots;
	}

	public static int getItemSlot() {
		return itemSlot;
	}

	public static int getInfoSlot1() {
		return infoSlot1;
	}

	public static int getInfoSlot2() {
		return infoSlot2;
	}
}
