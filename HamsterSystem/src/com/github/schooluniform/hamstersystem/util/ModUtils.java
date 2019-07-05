package com.github.schooluniform.hamstersystem.util;

import java.util.List;

import org.bukkit.inventory.ItemStack;

import com.github.schooluniform.hamstersystem.data.Data;
import com.github.schooluniform.hamstersystem.fightsystem.DamageSystem;
import com.github.schooluniform.hamstersystem.mod.Mod;
import com.github.schooluniform.hamstersystem.mod.ModTag;
import com.github.schooluniform.hamstersystem.weapon.WeaponTag;

public class ModUtils {
	/**
	 * 返回一个mod物品所携带的mod数据
	 * @param item mod物品
	 * @return 若不符合mod要求则返回null 反之返回长度为3的数组, ID Level Exp.
	 */
	public static int[] getModData(ItemStack item) {
		if(!DamageSystem.isMod(item))return null;
		String hsmlt = Data.NBTTag.getString(item, ModTag.HSMLT.name());
		int[] modLevel = Data.NBTTag.getIntArray(item, ModTag.HSMLE.name());
		return new int[] {Data.getMod(hsmlt).getId(),modLevel[0],modLevel[1]};
	}
	
	/**
	 * 返回一个mod物品列表中所有数据
	 * @param mods mod物品列表
	 * @return 长度为3的二维数组, ID Level Exp.
	 */
	public static int[][] getModsData(List<ItemStack> mods) {
		int[] modsId = new int[mods.size()],
				modsLevel = new int[mods.size()],
				modsExp = new int[mods.size()];
		int index = 0;
		for(ItemStack mod : mods) {
			int[] modData = getModData(mod);
			modsId[index] = modData[0];
			modsLevel[index] = modData[1];
			modsExp[index++] = modData[2];
		}
		return new int[][] {modsId,modsLevel,modsExp};
	}
	
	/**
	 * 获取mod物品的极性
	 * @param mod mod物品
	 * @return 极性代码
	 */
	public static int getModPolarity(ItemStack mod) {
		return Data.NBTTag.getInt(mod, ModTag.HSMINFO.name());
	}
	
	public static int getWeaponInfoFirst(ItemStack weapon) {
		if(Data.NBTTag.getIntArray(weapon,WeaponTag.HSWInfo.name()) == null)
			return 0;
		else
			return Data.NBTTag.getIntArray(weapon,WeaponTag.HSWInfo.name())[0];
	}
	
	/**
	 * 获取mod物品列表的极性
	 * @param mods mod物品列表
	 * @return 长度为mod物品列表长度+1的int数组. 第一个为是否双倍(0否1是) 默认为0
	 */
	public static int[] getModsPolarity(List<ItemStack> mods,ItemStack weapon) {
		int[] rarity = new int[mods.size()+1];
		rarity[0] = getWeaponInfoFirst(weapon);
		int index = 1;
		for(ItemStack mod : mods) {
			rarity[index++] = getModPolarity(mod);
		}
		return rarity;
	}
	
	/**
	 * 设定武器mod信息
	 * @param item 武器物品
	 * @param modData getModsData(List<ItemStack> mods)方法返回的mod信息
	 * @param rarity mod极性类信息
	 * @return 设定好的武器
	 */
	public static ItemStack setWeaponMod(ItemStack item,int[][] modData,int[] rarity) {
		item = Data.NBTTag.addNBT(item, WeaponTag.HSWInfo.name(), rarity);
		item = Data.NBTTag.addNBT(item, WeaponTag.HSWML.name(), modData[1]);
		item = Data.NBTTag.addNBT(item, WeaponTag.HSWME.name(), modData[2]);
		item = Data.NBTTag.addNBT(item, WeaponTag.HSWMOD.name(), modData[0]);
		return item;
	}
	
	/**
	 * 设定武器mod信息
	 * @param weapon 武器物品
	 * @param mods 要设定的mod
	 * @return 设定好的武器
	 */
	public static ItemStack setWeaponMod(ItemStack weapon,List<ItemStack> mods) {
		return setWeaponMod(weapon, getModsData(mods), getModsPolarity(mods, weapon));
	}
	
	public static Mod getBasicMod(ItemStack item) {
		if(!DamageSystem.isMod(item))return null;
		int modData[] = getModData(item);
		return Data.getMod(modData[0]);
	}
	
	/**
	 * 获取一个武器所带mod
	 * @param item 武器物品
	 * @return 一个长度为3的二维数组,1为modId,2为modLevel,3为modExp,4为极性列表
	 */
	public static int[][] getWeaponMods(ItemStack item){
		int[][] a = new int[][] {
			Data.NBTTag.getIntArray(item, WeaponTag.HSWMOD.name()),
			Data.NBTTag.getIntArray(item, WeaponTag.HSWML.name()),
			Data.NBTTag.getIntArray(item, WeaponTag.HSWME.name()),
			Data.NBTTag.getIntArray(item, WeaponTag.HSWInfo.name())
		};
		for(int[] b : a)
			for(int v:b)
				System.out.println(v);
		return new int[][] {
			Data.NBTTag.getIntArray(item, WeaponTag.HSWMOD.name()),
			Data.NBTTag.getIntArray(item, WeaponTag.HSWML.name()),
			Data.NBTTag.getIntArray(item, WeaponTag.HSWME.name()),
			Data.NBTTag.getIntArray(item, WeaponTag.HSWInfo.name())
		};
	}
}
