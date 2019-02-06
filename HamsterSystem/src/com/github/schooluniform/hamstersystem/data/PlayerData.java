package com.github.schooluniform.hamstersystem.data;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.EntityType;

import com.github.schooluniform.hamstersystem.HamsterSystem;
import com.github.schooluniform.hamstersystem.data.entity.FightEntity;
import com.github.schooluniform.hamstersystem.entity.EntityAttribute;
import com.github.schooluniform.hamstersystem.util.Util;

public class PlayerData extends FightEntity{
	
	//Mod
	private int[] modId,modLevel,modExp;
	
	//
	
	public PlayerData(UUID entityUUID, EntityType entityType, double health, double shield, double energy,
			HashMap<EntityAttribute, Double> attributes, int[] modId,int[] modLevel,int[] modExp) {
		super(entityUUID, entityType, health, shield, energy, attributes);
		this.modId = modId;
		this.modLevel = modLevel;
		this.modExp = modExp;
	}

	public FightEntity getFight(){
		return new FightEntity(this);
	}
	
	public static PlayerData  laod(String playerName){
		File file = new File(HamsterSystem.plugin.getDataFolder()+"/playerdata/"+playerName+".yml");
		YamlConfiguration data = YamlConfiguration.loadConfiguration(file);
		
		HashMap<EntityAttribute,Double> attributes = new HashMap<>();
		for(EntityAttribute attribute : EntityAttribute.values())
			if(data.contains("attributes."+attribute))
				attributes.put(attribute, data.getDouble("attributes."+attribute));
				
		int[][] modData = Util.modDataToArray(data.getString("mods"));
		
		return new PlayerData(
				UUID.fromString(data.getString("UUID")),
				EntityType.PLAYER,
				data.getDouble("health"),
				data.getDouble("shield"),
				data.getDouble("energy"),
				attributes,modData[0],modData[1],modData[2]);
		
	}
	
	public void save(){
		File file = new File(HamsterSystem.plugin.getDataFolder()+"/playerdata/"+this.getEntity().getName()+".yml");
		YamlConfiguration data = YamlConfiguration.loadConfiguration(file);
		data.set("UUID", getEntity().getUniqueId());
		data.set("health", this.getHealth());
		data.set("shield", getShield());
		data.set("energu", getEnergy());
		for(Map.Entry<EntityAttribute, Double> entry : attributes.entrySet())
			data.set("attributes."+entry.getKey(), entry.getValue());
		data.set("mods", Util.modDataToString(modId, modLevel, modExp));
		try {
			data.save(file);
		} catch (IOException e) {
			Bukkit.getServer().getLogger().warning("Player Data save Failed! Player Name: "+getEntity().getName());
			e.printStackTrace();
		}
	}
}
