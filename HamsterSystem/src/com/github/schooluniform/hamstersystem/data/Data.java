package com.github.schooluniform.hamstersystem.data;

import java.io.File;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import com.github.schooluniform.hamstersystem.HamsterSystem;
import com.github.schooluniform.hamstersystem.I18n;
import com.github.schooluniform.hamstersystem.fightsystem.effect.BlastEffect;
import com.github.schooluniform.hamstersystem.fightsystem.effect.ColdEffect;
import com.github.schooluniform.hamstersystem.fightsystem.effect.CorrosiveEffect;
import com.github.schooluniform.hamstersystem.fightsystem.effect.ElectricityEffect;
import com.github.schooluniform.hamstersystem.fightsystem.effect.HeatEffect;
import com.github.schooluniform.hamstersystem.fightsystem.effect.ImpactEffect;
import com.github.schooluniform.hamstersystem.fightsystem.effect.MagneticEffect;
import com.github.schooluniform.hamstersystem.fightsystem.effect.PunctureEffect;
import com.github.schooluniform.hamstersystem.fightsystem.effect.RadiationEffect;
import com.github.schooluniform.hamstersystem.fightsystem.effect.SlashEffect;
import com.github.schooluniform.hamstersystem.fightsystem.effect.ToxinEffect;
import com.github.schooluniform.hamstersystem.fightsystem.effect.ViralEffect;
import com.github.schooluniform.hamstersystem.mod.Mod;
import com.github.schooluniform.hamstersystem.nms.actionbar.Actionbar;
import com.github.schooluniform.hamstersystem.nms.actionbar.Actionbar_1_12_R1;
import com.github.schooluniform.hamstersystem.nms.nbt.NBTItem;
import com.github.schooluniform.hamstersystem.nms.nbt.NBT_V1_12;
import com.github.schooluniform.hamstersystem.weapon.Weapon;

public class Data {
	public static NBTItem NBTTag;
	public static Actionbar actionbar;
	
	private static HashMap<String,Weapon>weapons = new HashMap<>();
	private static HashMap<Integer,Mod> mods = new HashMap<>();
	private static HashMap<String, Integer> modsByName = new HashMap<>();
	
	private static HashMap<String,PlayerData> playerDatas = new HashMap<>();
	
	
	private static boolean enablePrefixInTitle;
	
	public static boolean init(FileConfiguration config){
		new I18n(config.getString("language",""));
		
		//NMS
		{
			switch (Bukkit.getBukkitVersion().split("-")[0]) {
			case "1.12.2":
				NBTTag = new NBT_V1_12();
				actionbar = new Actionbar_1_12_R1();
				break;

			default:
				HamsterSystem.plugin.getLogger().warning(I18n.tr("on-enable.enable-failed"));
				if(Bukkit.getPluginManager().isPluginEnabled(HamsterSystem.plugin)){
					Bukkit.getPluginManager().disablePlugin(HamsterSystem.plugin);
					return false;
				}
				break;
			}
		}
		
		//Effects
		{
			ImpactEffect.init(config.getInt("fight-effects.impact.duration"));
			
			PunctureEffect.init(config.getInt("fight-effects.puncture.duration"), 
					config.getDouble("fight-effects.puncture.effect"));
			
			SlashEffect.init(config.getInt("fight-effects.slash.duration"),
					config.getDouble("fight-effects.slash.effect"));
			
			ColdEffect.init(config.getInt("fight-effects.cold.duration"), 
					config.getInt("fight-effects.cold.level"));
			
			ElectricityEffect.init(config.getInt("fight-effects.electricity.duration"), 
					config.getDouble("fight-effects.electricity.limit-damage"), 
					config.getDouble("fight-effects.electricity.x"), 
					config.getDouble("fight-effects.electricity.y"), 
					config.getDouble("fight-effects.electricity.z"));
			
			HeatEffect.init(config.getInt("fight-effects.heat.duration"));
			
			ToxinEffect.init(config.getInt("fight-effects.toxin.duration"), 
					config.getInt("fight-effects.toxin.level"));
			
			BlastEffect.init(
					config.getInt("fight-effects.blast.duration"),
					config.getInt("fight-effects.blast.x"), 
					config.getInt("fight-effects.blast.y"), 
					config.getInt("fight-effects.blast.z"), 
					config.getInt("fight-effects.blast.damage"));
			
			CorrosiveEffect.init(config.getDouble("fight-effects.corrosive.effect"));
			
			MagneticEffect.init(config.getDouble("fight-effects.magnetic.energy-effect"), 
					config.getDouble("fight-effects.magnetic.shield-effect"));
			
			RadiationEffect.init(config.getInt("fight-effects.radiation.duration"),
					config.getInt("fight-effects.radiation.level"));
			
			ViralEffect.init(config.getDouble("fight-effects.viral.effect"));
		}
		
		//Other
		{
			loadWeapons(Weapon.defaultPath());
			HamsterSystem.plugin.getLogger().info(I18n.tr("on-enable.print-weapon-amount",weapons.size()));
		}
		
		
		return true;
	}
	
	private static void loadWeapons(File directroy){
		for(File f : directroy.listFiles()){
			if(f.isDirectory()){
				loadWeapons(f);
			}else if(f.getAbsolutePath().substring(f.getAbsolutePath().lastIndexOf('.')).equalsIgnoreCase(".yml")){
				addWeapon("/"+f.getAbsolutePath().substring(Weapon.defaultPath().getAbsolutePath().length(),f.getAbsolutePath().lastIndexOf('.')));
			}
		}
	}
	
	public static void addWeapon(String name){
		Weapon w = Weapon.load(name);
		weapons.put(w.getName(),w );
	}
	
	public static void addPlayer(String playerName){
		if(playerDatas.containsKey(playerName))return;
		PlayerData pd = PlayerData.laod(playerName);
		if(pd == null)
			pd = PlayerData.createNewPlayerData(HamsterSystem.plugin.getServer().getPlayer(playerName));
		playerDatas.put(playerName, pd);
	}
	
	public static void addPlayer(Player p){
		if(playerDatas.containsKey(p.getName()))return;
		PlayerData pd = PlayerData.laod(p.getName());
		if(pd == null)
			pd = PlayerData.createNewPlayerData(p);
		playerDatas.put(p.getName(), pd);
	}
	
	public static  void removePlayer(String playerName){
		playerDatas.remove(playerName).save();
	}
	
	public static PlayerData getPlayerData(String playerName){
		return playerDatas.get(playerName);
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
	
	public static boolean isEnablePrefixInTitle(){
		return enablePrefixInTitle;
	}
}
