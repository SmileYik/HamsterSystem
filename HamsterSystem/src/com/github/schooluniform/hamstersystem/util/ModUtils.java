package com.github.schooluniform.hamstersystem.util;

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
	 * @return 若不符合mod要求则返回{-1,-1,-1,-1} 反之返回长度为3的数组, ID Level Exp Polarity.
	 */
	public static int[] getModData(ItemStack item) {
		if(!DamageSystem.isMod(item))return new int[] {-1,-1,-1,-1};
		String hsmlt = Data.NBTTag.getString(item, ModTag.HSMLT.name());
		int[] modLevel = Data.NBTTag.getIntArray(item, ModTag.HSMLE.name());
		return new int[] {
				Data.getMod(hsmlt).getId(),
				modLevel[0],
				modLevel[1],
				Data.NBTTag.getInt(item, ModTag.HSMINFO.name())};
	}
	
	/**
	 * 返回一个mod物品列表中所有数据
	 * @param mods mod物品列表
	 * @return 长度为4的二维数组, ID Level Exp Polarity.
	 */
	public static int[][] getModsData(ItemStack[] mods) {
		int[] modsId = new int[mods.length],
				modsLevel = new int[mods.length],
				modsExp = new int[mods.length],
				modsPolarity = new int[mods.length+1];
		int index = 0;
		for(ItemStack mod : mods) {
			int[] modData = getModData(mod);
			modsId[index] = modData[0];
			modsLevel[index] = modData[1];
			modsExp[index++] = modData[2];
			modsPolarity[index] = modData[3];
		}
		return new int[][] {modsId,modsLevel,modsExp};
	}
	
	public static int getWeaponInfoFirst(ItemStack weapon) {
		if(Data.NBTTag.getIntArray(weapon,WeaponTag.HSWInfo.name()) == null)
			return 0;
		else
			return Data.NBTTag.getIntArray(weapon,WeaponTag.HSWInfo.name())[0];
	}
	
	/**
	 * 设定武器mod信息
	 * @param item 武器物品
	 * @param modData getModsData(List<ItemStack> mods)方法返回的mod信息
	 * @return 设定好的武器
	 */
	public static ItemStack setWeaponMod(ItemStack item,int[][] modData) {
		item = Data.NBTTag.addNBT(item, WeaponTag.HSWInfo.name(), modData[3]);
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
	public static ItemStack setWeaponMod(ItemStack weapon,ItemStack[] mods) {
		int[][] modData = getModsData(mods);
		modData[3][0] = getWeaponInfoFirst(weapon);
		return setWeaponMod(weapon, modData);
	}
	
	public static Mod getBasicMod(ItemStack item) {
		if(!DamageSystem.isMod(item))return null;
		int modData[] = getModData(item);
		return Data.getMod(modData[0]);
	}
	
	/**
	 * 获取一个武器所带mod
	 * @param item 武器物品
	 * @return 一个长度为4的二维数组,1为modId,2为modLevel,3为modExp,4为极性列表
	 */
	public static int[][] getWeaponMods(ItemStack item){
		return new int[][] {
			Data.NBTTag.getIntArray(item, WeaponTag.HSWMOD.name()),
			Data.NBTTag.getIntArray(item, WeaponTag.HSWML.name()),
			Data.NBTTag.getIntArray(item, WeaponTag.HSWME.name()),
			Data.NBTTag.getIntArray(item, WeaponTag.HSWInfo.name())
		};
	}
}
