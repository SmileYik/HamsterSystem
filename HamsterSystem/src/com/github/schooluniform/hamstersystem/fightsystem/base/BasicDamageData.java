package com.github.schooluniform.hamstersystem.fightsystem.base;

import java.util.HashMap;

import org.bukkit.entity.LivingEntity;

import com.github.schooluniform.hamstersystem.weapon.WeaponAttribute;

public class BasicDamageData {
	private LivingEntity attacker;
	private HashMap<DamageType,Double> basicDamages;
	private HashMap<WeaponAttribute,Double> weaponAttritubes;
	private DamageType triggerDamage;
	private double cirtDamage;
	
	public BasicDamageData(LivingEntity attacker, HashMap<DamageType, Double> basicDamages,
			HashMap<WeaponAttribute, Double> weaponAttritubes, DamageType triggerDamage, double cirtDamage) {
		super();
		this.attacker = attacker;
		this.basicDamages = basicDamages;
		this.weaponAttritubes = weaponAttritubes;
		this.triggerDamage = triggerDamage;
		this.cirtDamage = cirtDamage;
	}
	
	public BasicDamageData(LivingEntity attacker) {
		super();
		this.attacker = attacker;
		this.basicDamages = null;
		this.weaponAttritubes  = null;
		this.triggerDamage  = null;
		this.cirtDamage  = 1;
	}

	public Double getDamage(DamageType type){
		if(basicDamages.containsKey(type))
			return basicDamages.get(type);
		return null;
	}
	
	public Double getWeaponAttribute(WeaponAttribute attribute){
		return weaponAttritubes.get(attribute);
	}
	
	public double getDamage(){
		double tatolDamage = 0;
		for(double damage : basicDamages.values())
			tatolDamage+=damage;
		return tatolDamage;
	}
	
	public double getDamageWithCirt(){
		double tatolDamage = 0;
		for(double damage : basicDamages.values())
			tatolDamage+=damage;
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

	public HashMap<WeaponAttribute, Double> getWeaponAttritubes() {
		return weaponAttritubes;
	}

	public void setWeaponAttritubes(HashMap<WeaponAttribute, Double> weaponAttritubes) {
		this.weaponAttritubes = weaponAttritubes;
	}
	
	
}
