package com.github.schooluniform.hamstersystem.weapon;

import java.util.HashMap;

import org.bukkit.Sound;

import com.github.schooluniform.hamstersystem.fightsystem.base.DamageType;
import com.github.schooluniform.hamstersystem.fightsystem.base.ElementalDamageType;
import com.github.schooluniform.hamstersystem.fightsystem.base.ProjectileType;

public class WeaponLauncher extends Weapon{
	/*
	 * 关于换弹问题
	 * 换弹物品用HSWLT标签表示支持的武器类型
	 * HSCSize标签表示换弹物品中含有的子弹含量
	 * Consumable表示换弹物品用完是否被消耗
	 * 即格式如下
	 * HSWLT: weapon1;weapon2;weapon3
	 * HSCSize: int
	 * Consumable: Boolean
	 */
	
	//发射子弹数量
	private short launchAmount;
	private double range;
	private boolean gravity;
	private String sound;
	private ProjectileType projectileType;
	
	protected WeaponLauncher(String name, String lore, HashMap<DamageType, Double> damages,
			HashMap<ElementalDamageType, Double> elementalDamages, HashMap<WeaponAttribute, Double> attributes,
			short launchAmount, double range, boolean gravity, ProjectileType projectileType,String sound) {
		super(name, lore, damages, elementalDamages, attributes,WeaponType.Launcher);
		this.launchAmount = launchAmount;
		this.range = range;
		this.gravity = gravity;
		this.projectileType = projectileType;
		this.sound = sound;
	}
	
	public short getLaunchAmount() {
		return launchAmount;
	}
	public double getRange() {
		return range;
	}
	public boolean isGravity() {
		return gravity;
	}
	public Sound getSound(){
		return Sound.valueOf(sound);
	}
	public ProjectileType getProjectileType() {
		return projectileType;
	}
	
	public WeaponLauncher clone(){
		HashMap<DamageType, Double> damages = new HashMap<>();
		HashMap<ElementalDamageType, Double> elementalDamages = new HashMap<>();
		HashMap<WeaponAttribute, Double> attributes = new HashMap<>();
		damages.putAll(this.getDamages());
		elementalDamages.putAll(this.getElementalDamages());
		attributes.putAll(this.getAttributes());
		return new WeaponLauncher(getName(), getLore(), damages, elementalDamages, attributes, launchAmount, range, gravity, projectileType,sound);
	}
}
