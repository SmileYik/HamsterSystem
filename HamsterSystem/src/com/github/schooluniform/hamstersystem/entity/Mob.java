package com.github.schooluniform.hamstersystem.entity;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.attribute.Attribute;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;

import com.github.schooluniform.hamstersystem.HamsterSystem;
import com.github.schooluniform.hamstersystem.util.Util;

public class Mob extends FightEntity{
	//Mod
	private int[] modId,modLevel;
	private String name;
	private HashMap<Attribute, Double> originAttribute = new HashMap<Attribute, Double>();
	
	private Mob(String name,UUID entityUUID, EntityType entityType, double health, double shield, double energy,
			HashMap<EntityAttribute, Double> attributes, HashMap<Attribute,Double> attributeOri,
			int[] modId,int[] modLevel) {
		super(entityUUID, entityType, health, shield, energy, attributes);
		this.name = name;
		this.modId = modId;
		this.modLevel = modLevel;
		this.originAttribute = attributeOri;
		// TODO Auto-generated constructor stub
	}
	
	public static File getDefaultPath() {
		return new File(HamsterSystem.plugin.getDataFolder()+"/mob");
	}
	
	public static Mob getMob(String fileName) {
		File file = new File(getDefaultPath()+"/"+fileName+".yml");
		if(!file.exists()) {
			return null;
		}else {
			YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
			
			HashMap<EntityAttribute,Double> attributes = new HashMap<>();
			HashMap<Attribute,Double> attributesOri = new HashMap<Attribute, Double>();
			
			for(EntityAttribute attribute : EntityAttribute.values())
				if(config.contains("attributes."+attribute))
					attributes.put(attribute, config.getDouble("attributes."+attribute));
			
			for(Attribute attribute : Attribute.values())
				if(config.contains("minecraft-attributes."+attribute))
					attributesOri.put(attribute, config.getDouble("mirecraft-attributes."+attribute));
			
			int[][] modData = Util.modDataToArray(config.getString("mods"));
			
			return new Mob(
					config.getString("name"),
					null, 
					null, 
					config.getDouble("health"),
					config.getDouble("shield"), 
					config.getDouble("energy"),
					attributes,
					attributesOri,
					modData[0],
					modData[1]);
		}
	}
	
	public FightEntity getMobFightEntity(LivingEntity entity) {
		for(Map.Entry<Attribute, Double> entry : originAttribute.entrySet()) {
			entity.getAttribute(entry.getKey()).setBaseValue(entry.getValue());
		}
		
		FightEntity fe = new FightEntity(this, modId, modLevel);
		fe.setEntityUUID(entity.getUniqueId());
		fe.setEntityType(entity.getType());
		return fe;
	}
	
	public String getName() {
		return name;
	}
}
