package com.github.schooluniform.hamstersystem;

import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public class HamsterSystem extends JavaPlugin{
	public static HamsterSystem plugin;
	
	
	@Override
	public void onEnable() {
		plugin = this;
		firstLoad();
	}
	
	
	private void firstLoad(){
		if(!new File(getDataFolder()+File.separator+"playerdata").exists())
			new File(getDataFolder()+File.separator+"playerdata").mkdirs();
		if(!new File(getDataFolder()+File.separator+"weapon").exists())
			new File(getDataFolder()+File.separator+"weapon").mkdir();
		if(!new File(getDataFolder()+File.separator+"mod").exists())
			new File(getDataFolder()+File.separator+"mod").mkdir();
		if(!new File(getDataFolder()+File.separator+"mob").exists())
			new File(getDataFolder()+File.separator+"mob").mkdir();
		if(!new File(getDataFolder()+File.separator+"config.yml").exists())
			saveDefaultConfig();
		try{reloadConfig();}catch (Exception e){}
	}
}
