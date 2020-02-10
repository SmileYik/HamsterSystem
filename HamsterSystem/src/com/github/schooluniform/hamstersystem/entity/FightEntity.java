package com.github.schooluniform.hamstersystem.entity;

import java.io.File;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.attribute.Attribute;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import com.github.schooluniform.hamstersystem.HamsterSystem;
import com.github.schooluniform.hamstersystem.I18n;
import com.github.schooluniform.hamstersystem.data.Data;
import com.github.schooluniform.hamstersystem.fightsystem.base.DamageType;
import com.github.schooluniform.hamstersystem.mod.MergeMod;
import com.github.schooluniform.hamstersystem.util.Util;
import com.github.schooluniform.hamstersystem.weapon.Calculation;

public class FightEntity{
	private UUID entityUUID;
	private EntityType entityType;
	
	private double health;
	private double shield ;
	private double energy;
	private List<DamageType> damageSigns = new LinkedList<>();
	protected HashMap<EntityAttribute, Double> attributes;
	private BossBar healthBar,energyBar;
	
	//Clone
	public FightEntity(FightEntity entity){
		this.entityUUID = entity.entityUUID;
		this.entityType = entity.entityType;
		this.health = entity.health;
		this.shield = entity.shield;
		this.energy = entity.energy;
		
		HashMap<EntityAttribute,Double> attributes = new HashMap<>();
		attributes.putAll(entity.attributes);
		
		this.attributes = attributes;
		if(!attributes.containsKey(EntityAttribute.Armor))
			attributes.put(EntityAttribute.Armor, 1D);
	}
	
	public FightEntity(FightEntity entity,int[] modsId, int[] modsLevel){
		this.entityUUID = entity.entityUUID;
		this.entityType = entity.entityType;
		this.health = entity.health;
		this.shield = entity.shield;
		this.energy = entity.energy;
		
		HashMap<EntityAttribute,Double> attributes = new HashMap<>();
		attributes.putAll(entity.attributes);
		
		if(!attributes.containsKey(EntityAttribute.Armor))
			attributes.put(EntityAttribute.Armor, 1D);
		
		MergeMod mod = MergeMod.getEntityMergeMod(modsId, modsLevel);
		
		for(Map.Entry<EntityAttribute, Double> entry : mod.getEntityAttributes().entrySet()){
			if(entry.getKey().getWay() == Calculation.Multiplication){
				if(attributes.containsKey(entry.getKey()))
					attributes.replace(entry.getKey(), (1+entry.getValue()/100D)*attributes.get(entry.getKey()));
			}else if(entry.getKey().getWay() == Calculation.Addition){
				if(attributes.containsKey(entry.getKey()))
					attributes.replace(entry.getKey(), entry.getValue()+attributes.get(entry.getKey()));
				else
					attributes.put(entry.getKey(), entry.getValue());
			}
		}
		
		this.attributes = attributes;
		
	}
	
	public FightEntity(UUID entityUUID, EntityType entityType, double health, double shield, double energy,
			HashMap<EntityAttribute, Double> attributes) {
		super();
		this.entityUUID = entityUUID;
		this.entityType = entityType;
		this.health = health;
		this.shield = shield;
		this.energy = energy;
		this.attributes = attributes;
		if(!attributes.containsKey(EntityAttribute.Armor))
			attributes.put(EntityAttribute.Armor, 1D);
	}
	
	//create new
	public static FightEntity getFightEntity(LivingEntity entity){
		if(entity instanceof Player) return Data.getPlayerData(entity.getName()).getFight();
		
		HashMap<EntityAttribute, Double> attributes;
		if(entity.hasMetadata(EntityTag.LinkTo.name())){
			String linkTo = entity.getMetadata(EntityTag.LinkTo.name()).get(0).asString();
			YamlConfiguration data = YamlConfiguration.loadConfiguration(new File(HamsterSystem.plugin.getDataFolder()+"/mob/"+linkTo+".yml"));
			attributes = new HashMap<>();
			for(String attribute : data.getKeys(false))
				attributes.put(EntityAttribute.valueOf(attribute), data.getDouble(attribute));
		}else{
			attributes = Util.getDefaultEntityAttributes();
		}
		
		entity.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(attributes.get(EntityAttribute.Health));
		entity.setHealth(attributes.get(EntityAttribute.Health));
		return new FightEntity(entity.getUniqueId(),entity.getType(),
				attributes.get(EntityAttribute.Health),
				attributes.get(EntityAttribute.Shield),
				0,
				attributes);
		
	}

	public LivingEntity getEntity(){
		return (LivingEntity)Bukkit.getEntity(entityUUID);
	}
	
	public EntityType getEntityType(){
		return entityType;
	}
	
	public void setEntityType(EntityType type) {
		this.entityType = type;
	}
	
	public void setEntityUUID(UUID uuid) {
		this.entityUUID = uuid;
	}
	
	public double getAttribute(EntityAttribute attribute){
		if(!attributes.containsKey(attribute))return 0;
		return attributes.get(attribute);
	}
	
	public void setAttribute(EntityAttribute attribute,double value){
		attributes.replace(attribute, value);
	}
	
	public double getHealth() {
		return health;
	}

	public void setHealth(double health) {
		this.health = health;
	}
	
	public void modifyHealth(double health) {
		this.health += health;
	}

	public double getShield() {
		return shield;
	}

	public void setShield(double shield) {
		this.shield = shield;
	}
	
	public void modifyShield(double shield) {
		this.shield += shield;
	}

	public double getEnergy() {
		return energy;
	}

	public void setEnergy(double energy) {
		this.energy = energy;
	}
	
	public void modifyEnergy(double energy) {
		this.energy += energy;
	}
	
	public void setDamageSign(DamageType type,boolean enable) {
		if(enable) {
			if(!damageSigns.contains(type))damageSigns.add(type);
		}else {
			if(damageSigns.contains(type))damageSigns.remove(type);
		}
	}
	
	public HashMap<EntityAttribute, Double> getAttributes() {
		return attributes;
	}

	public String getUpdateSign() {
		//"||||||||||||||||||||"
		//蓝->红->黑
		try {
			String sign = "||||||||||||||||||||||||||||||||||||||||";
			LivingEntity entity = getEntity();
			if(entity == null) {
				return "null";
			}
			String name = entity.getCustomName();
			if(name == null) {
				name = entity.getName();
			}
			if(entity instanceof Player) {
				if(healthBar == null) {
					healthBar = Bukkit.createBossBar("", BarColor.BLUE, BarStyle.SEGMENTED_20);
					healthBar.addPlayer((Player) entity);
				}
				if(energyBar == null) {
					energyBar = Bukkit.createBossBar("", BarColor.BLUE, BarStyle.SEGMENTED_20);
					energyBar.addPlayer((Player) entity);
				}
			}
			if(shield > 0) {
				int index = (int)(shield/(attributes.get(EntityAttribute.Shield)/sign.length()));
				if(index < sign.length()) {
					sign = "§b"+sign.substring(0, index)+"§c"+sign.substring(index);			
				}else {
					sign = "§b"+sign;					
				}
				if(healthBar != null) {
					healthBar.setColor(BarColor.BLUE);
					double value= shield/(attributes.get(EntityAttribute.Shield));
					if(value>1) {
						value = 1;
					}
					healthBar.setProgress(value);
				}
			}else {
				int index = (int)(entity.getHealth()/(entity.getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue()/sign.length()));
				if(index < sign.length()) {
					sign = "§c"+sign.substring(0, index)+"§8"+sign.substring(index);					
				}else {
					sign = "§c"+sign;					
				}
				if(healthBar != null) {
					healthBar.setColor(BarColor.RED);
					double value= entity.getHealth()/entity.getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue();
					if(value>1) {
						value = 1;
					}
					healthBar.setProgress(value);
				}
			}
			
			if(!damageSigns.isEmpty()) {
				sign+=" §c(";
				try {
					for(DamageType type : damageSigns) {
						sign+=type.getSign();
					}					
				} catch (Exception e) {
					
				}
				sign+="§c)";
			}
			if(healthBar != null) {
				healthBar.setTitle(name+": "+sign);
			}
			if(energyBar != null) {
				energyBar.setTitle(I18n.tr("1",energy));
				double energy = getAttribute(EntityAttribute.Energy);
				double value= this.energy/energy==0?1:energy;
				if(value>1) {
					value = 1;
				}else if(value<0) {
					value = 0;
				}
				energyBar.setProgress(value);
			}
			return name+": "+sign;
		}catch (Exception e) {
			e.printStackTrace();
			return "null";
		}
	}
	
	public void resetBar() {
		if(healthBar != null) {
			healthBar.removeAll();
			healthBar = null;			
		}
		if(energyBar != null) {
			energyBar.removeAll();
			energyBar = null;
		}
	}
}
