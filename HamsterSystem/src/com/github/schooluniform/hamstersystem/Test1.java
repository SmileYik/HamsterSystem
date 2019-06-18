package com.github.schooluniform.hamstersystem;

import java.io.File;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.junit.Test;

import com.github.schooluniform.hamstersystem.entity.EntityAttribute;
import com.github.schooluniform.hamstersystem.weapon.Weapon;

public class Test1 {
	
	@Test
	public void test(){
		File f = new File("E:/aaa/abcd.txt");
		System.out.println(f.getAbsolutePath().substring(f.getAbsolutePath().lastIndexOf('.')));
		System.out.println("/"+f.getAbsolutePath().substring("E:/aaa/".length()));
		
		System.out.println(
				Bukkit.getVersion()
				);
	}
}
