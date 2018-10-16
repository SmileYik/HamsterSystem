package com.github.schooluniform.hamstersystem.data;

import java.util.HashMap;

import com.github.schooluniform.hamstersystem.mod.Mod;
import com.github.schooluniform.hamstersystem.nms.nbt.NBTItem;
import com.github.schooluniform.hamstersystem.weapon.Weapon;

public class Data {
	public static NBTItem NBTTag;
	
	private static HashMap<String,Weapon>weapons = new HashMap<>();
	private static HashMap<Integer,Mod> mods = new HashMap<>();
	private static HashMap<String, Integer> modsByName = new HashMap<>();
	
	public static void init(){
		
	}
	
	public static Weapon getWeapon(String name){
		return weapons.get(name);
	}
	
	public static boolean contansWeapon(String name){
		return weapons.containsKey(name);
	}
	
	public static Mod getMod(String name){
		return mods.get(modsByName.get(name));
	}
	
	public static boolean contansMod(String name){
		return modsByName.containsKey(name);
	}
	
	public static boolean contansMod(int modId){
		return mods.containsKey(modId);
	}
	
	public static Mod getMod(int modId){
		return mods.get(modId);
	}
}
