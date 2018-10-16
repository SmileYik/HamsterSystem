package com.github.schooluniform.hamstersystem.weapon;

import java.util.HashMap;

import com.github.schooluniform.hamstersystem.fightsystem.base.DamageType;
import com.github.schooluniform.hamstersystem.fightsystem.base.ElementalDamageType;
import com.github.schooluniform.hamstersystem.fightsystem.base.ProjectileType;

public class WeaponLauncher extends Weapon{
	private short launchAmount;
	private double range;
	private boolean gravity;
	private ProjectileType projectileType;
	
	protected WeaponLauncher(String name, String lore, HashMap<DamageType, Double> damages,
			HashMap<ElementalDamageType, Double> elementalDamages, HashMap<WeaponAttribute, Double> attributes,
			short launchAmount, double range, boolean gravity, ProjectileType projectileType) {
		super(name, lore, damages, elementalDamages, attributes,WeaponType.Launcher);
		this.launchAmount = launchAmount;
		this.range = range;
		this.gravity = gravity;
		this.projectileType = projectileType;
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
		return new WeaponLauncher(getName(), getLore(), damages, elementalDamages, attributes, launchAmount, range, gravity, projectileType);
	}
}
