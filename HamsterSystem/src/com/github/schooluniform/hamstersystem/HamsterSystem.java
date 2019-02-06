package com.github.schooluniform.hamstersystem;

import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import com.github.schooluniform.hamstersystem.data.Data;

import java.io.File;

public class HamsterSystem extends JavaPlugin{
	public static HamsterSystem plugin;
	//La vie est drole
	
	@Override
	public void onEnable() {
		plugin = this;
		firstLoad();
		
		addOnlinePlayer();
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
	
	private void addOnlinePlayer(){
		for(Player p : getServer().getOnlinePlayers())
			Data.addPlayer(p.getName());
	}
	
}
