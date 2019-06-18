package com.github.schooluniform.hamstersystem.fightsystem.base;

import java.util.HashMap;

import org.bukkit.entity.LivingEntity;

import com.github.schooluniform.hamstersystem.weapon.Weapon;

public class BasicDamageData {
	private LivingEntity attacker;
	private HashMap<DamageType,Double> basicDamages;
	private Weapon weapon;
	private DamageType triggerDamage;
	private double cirtDamage;
	private double tatolDamage;
	
	public BasicDamageData(LivingEntity attacker, HashMap<DamageType, Double> basicDamages,
			Weapon weapon, DamageType triggerDamage, double cirtDamage) {
		super();
		this.attacker = attacker;
		this.basicDamages = basicDamages;
		this.weapon = weapon;
		this.triggerDamage = triggerDamage;
		this.cirtDamage = cirtDamage;
		for(double damage : basicDamages.values())
			tatolDamage+=damage;
	}
	
	public BasicDamageData(LivingEntity attacker) {
		super();
		this.attacker = attacker;
		this.basicDamages = null;
		this.weapon  = null;
		this.triggerDamage  = null;
		this.cirtDamage  = 1;
		for(double damage : basicDamages.values())
			tatolDamage+=damage;
	}

	public Double getDamage(DamageType type){
		if(basicDamages.containsKey(type))
			return basicDamages.get(type);
		return null;
	}
	
	public double getDamage(){
		return tatolDamage;
	}
	
	public void setDamage(double damage){
		this.tatolDamage = damage;
	}
	
	public double getDamageWithCirt(){
		return tatolDamage*cirtDamage;
	}
	
	public LivingEntity getAttacker() {
		return attacker;
	}
	public void setAttacker(LivingEntity attacker) {
		this.attacker = attacker;
	}
	public HashMap<DamageType, Double> getBasicDamages() {
		return basicDamages;
	}
	public void setBasicDamages(HashMap<DamageType, Double> basicDamages) {
		this.basicDamages = basicDamages;
	}
	public DamageType getTriggerDamage() {
		return triggerDamage;
	}
	public void setTriggerDamage(DamageType triggerDamage) {
		this.triggerDamage = triggerDamage;
	}
	public double getCirtDamage() {
		return cirtDamage;
	}
	public void setCirtDamage(double cirtDamage) {
		this.cirtDamage = cirtDamage;
	}
	public Weapon getWeapon(){
		return weapon;
	}
	
	
}
