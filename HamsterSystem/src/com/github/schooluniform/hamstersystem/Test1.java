package com.github.schooluniform.hamstersystem;

import java.io.File;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Scanner;

import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.libs.jline.internal.TestAccessible;
import org.junit.Test;

import com.github.schooluniform.hamstersystem.entity.EntityAttribute;
import com.github.schooluniform.hamstersystem.weapon.Weapon;
import com.github.schooluniform.hamstersystem.weapon.WeaponAttribute;

public class Test1 {
	
	
	@Test
	public void test(){
		/*
		 * File f = new File("E:/aaa/abcd.txt");
		 * System.out.println(f.getAbsolutePath().substring(f.getAbsolutePath().
		 * lastIndexOf('.')));
		 * System.out.println("/"+f.getAbsolutePath().substring("E:/aaa/".length()));
		 * 
		 * System.out.println( Bukkit.getVersion() );
		 */
		HashMap<WeaponAttribute,Double> h = new HashMap<>();
		double index = 0;
		for(WeaponAttribute wa : WeaponAttribute.values())
			h.put(wa, index++);
		for(Object obj : h.values().toArray())
			System.out.println(obj);
		for(Object obj : h.keySet().toArray())
			System.out.println(obj+": "+h.get(obj));
		System.out.println(MessageFormat.format("a: {0}\nb: {1}\nc: {2}", h.values().toArray()));
	}
}
