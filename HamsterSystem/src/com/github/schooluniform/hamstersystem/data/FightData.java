package com.github.schooluniform.hamstersystem.data;

import java.util.HashMap;

import org.bukkit.entity.LivingEntity;

import com.github.schooluniform.hamstersystem.entity.EntityAttribute;

public class FightData {
	private HashMap<EntityAttribute, Double> attributes;
	private double energy,shield;
	private LivingEntity entity;
	
	public FightData(LivingEntity entity,HashMap<EntityAttribute, Double> attributes){
		this.entity = entity;
		this.attributes = attributes;
		this.energy = 0;
		this.shield = attributes.get(EntityAttribute.Shield);
		if(!attributes.containsKey(EntityAttribute.Armor))
			attributes.put(EntityAttribute.Armor, 1D);
		
	}
	
	public void damage(double damage){
		
	}
	
	public LivingEntity getEntity(){
		return entity;
	}
	
	public double getHealth(){
		return entity.getHealth();
	}
	
	public double getMaxHealth(){
		return attributes.get(EntityAttribute.Health);
	}
	
	public double getShield(){
		return shield;
	}
	
	public double getMaxShield(){
		return attributes.get(EntityAttribute.Shield);
	}
	
	public double getEnergy(){
		return energy;
	}
	
	public double getMaxEnergy(){
		return attributes.get(EntityAttribute.Energy);
	}
	
	public double getArmor(){
		return attributes.get(EntityAttribute.Armor);
	}
	
	public double getEntityAttribute(EntityAttribute type){
		if(attributes.containsKey(type))
			return attributes.get(type);
		return 0;
	}
	
	public double getEntityAttribute(EntityAttribute type,double defualtValue){
		if(attributes.containsKey(type))
			return attributes.get(type);
		return defualtValue;
	}
}
