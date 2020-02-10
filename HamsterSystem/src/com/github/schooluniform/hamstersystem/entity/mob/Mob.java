package com.github.schooluniform.hamstersystem.entity.mob;

import java.io.File;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.attribute.Attribute;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;

import com.github.schooluniform.hamstersystem.HamsterSystem;
import com.github.schooluniform.hamstersystem.data.Data;
import com.github.schooluniform.hamstersystem.entity.EntityAttribute;
import com.github.schooluniform.hamstersystem.entity.FightEntity;
import com.github.schooluniform.hamstersystem.util.Util;

import io.lumine.xikage.mythicmobs.api.exceptions.InvalidMobTypeException;

public class Mob extends FightEntity{
	//Mod
	private int[] modId,modLevel;
	private String name;
	private String mmId;
	private String displayName;
	private HashMap<Attribute, Double> originAttribute = new HashMap<Attribute, Double>();
	private HashMap<ItemStack,String> drops = new HashMap<ItemStack, String>();
	//几率 数量1 数量2
	
	private Mob(String name,UUID entityUUID, EntityType entityType, double health, double shield, double energy,
			HashMap<EntityAttribute, Double> attributes, HashMap<Attribute,Double> attributeOri,
			int[] modId,int[] modLevel,String mmId,String displayName,HashMap<ItemStack,String> drops) {
		super(entityUUID, entityType, health, shield, energy, attributes);
		this.name = name;
		this.modId = modId;
		this.modLevel = modLevel;
		this.originAttribute = attributeOri;
		this.mmId = mmId;
		this.displayName = displayName;
		this.drops = drops;
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
			HashMap<ItemStack,String> drops = new HashMap<ItemStack, String>();
			
			for(EntityAttribute attribute : EntityAttribute.values())
				if(config.contains("attributes."+attribute))
					attributes.put(attribute, config.getDouble("attributes."+attribute));
			
			for(Attribute attribute : Attribute.values())
				if(config.contains("minecraft-attributes."+attribute))
					attributesOri.put(attribute, config.getDouble("mirecraft-attributes."+attribute));
			
			int[][] modData = Util.modDataToArray(config.getString("mods"));
			
			int index = 0;
			while(config.contains("drops."+index+".item")) {
				drops.put(config.getItemStack("drops."+index+".item"), config.getString("drops."+ index++ +".data"));
			}
			
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
					modData[1],
					config.getString("mythicmobs-id",null),
					config.getString("display-name",null),
					drops);
		}
	}
	
	public FightEntity getMobFightEntity(LivingEntity entity) {
		FightEntity fe = new FightEntity(this, modId, modLevel);
		fe.setEntityUUID(entity.getUniqueId());
		fe.setEntityType(entity.getType());
		return fe;
	}
	
	public void spawn(Location loc) {
		//if(mmId == null || Data.mm == null) {
			LivingEntity entity = (LivingEntity) loc.getWorld().spawn(loc, getEntityType().getEntityClass());
			for(Map.Entry<Attribute, Double> entry : originAttribute.entrySet()) {
				entity.getAttribute(entry.getKey()).setBaseValue(entry.getValue());
			}
			if(displayName != null) {
				entity.setCustomName(displayName);
			}
			entity.setMetadata("HamsterSystemMob", new FixedMetadataValue(HamsterSystem.plugin, name));
//		}else{
//			try {
//				LivingEntity entity = (LivingEntity) Data.mm.spawnMythicMob(mmId, loc);
//				entity.setMetadata("HamsterSystemMob", new FixedMetadataValue(HamsterSystem.plugin, name));
//			} catch (InvalidMobTypeException e) {
//				e.printStackTrace();
//			}
//		}
	}
	
	public void spawn(Location loc,String spawnerId) {
		//if(mmId == null || Data.mm == null) {
			LivingEntity entity = (LivingEntity) loc.getWorld().spawn(loc, getEntityType().getEntityClass());
			for(Map.Entry<Attribute, Double> entry : originAttribute.entrySet()) {
				entity.getAttribute(entry.getKey()).setBaseValue(entry.getValue());
			}
			if(displayName != null) {
				entity.setCustomName(displayName);
			}
			entity.setMetadata("HamsterSystemMob", new FixedMetadataValue(HamsterSystem.plugin, name));
			entity.setMetadata("HamsterSystemSpawner", new FixedMetadataValue(HamsterSystem.plugin, spawnerId));
//		}else{
//			try {
//				LivingEntity entity = (LivingEntity) Data.mm.spawnMythicMob(mmId, loc);
//				entity.setMetadata("HamsterSystemMob", new FixedMetadataValue(HamsterSystem.plugin, name));
//			} catch (InvalidMobTypeException e) {
//				e.printStackTrace();
//			}
//		}
	}
	
	public String getName() {
		return name;
	}

	public int[] getModId() {
		return modId;
	}

	public void setModId(int[] modId) {
		this.modId = modId;
	}

	public int[] getModLevel() {
		return modLevel;
	}

	public void setModLevel(int[] modLevel) {
		this.modLevel = modLevel;
	}

	public String getMmId() {
		return mmId;
	}

	public void setMmId(String mmId) {
		this.mmId = mmId;
	}

	public HashMap<Attribute, Double> getOriginAttribute() {
		return originAttribute;
	}

	public void setOriginAttribute(HashMap<Attribute, Double> originAttribute) {
		this.originAttribute = originAttribute;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public List<ItemStack> getDrops(){
		List<ItemStack> drops = new LinkedList<ItemStack>();
		for(Map.Entry<ItemStack, String> entry : this.drops.entrySet()) {
			String data[] = entry.getValue().split(" ");
			if(Math.random()*100<Double.parseDouble(data[0])) {
				ItemStack item = entry.getKey().clone();
				item.setAmount(random(Double.parseDouble(data[1]), Double.parseDouble(data[2])));
				drops.add(item);
			}
		}
		return drops;
	}
	
	private int random(double num1,double num2) {
		return (int)(Math.random()*(num2-num1)+num1);
	}
}
