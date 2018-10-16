package com.github.schooluniform.hamstersystem.weapon;

import java.util.HashMap;

import com.github.schooluniform.hamstersystem.fightsystem.base.DamageType;
import com.github.schooluniform.hamstersystem.fightsystem.base.ElementalDamageType;

public class WeaponMelee extends Weapon{

	protected WeaponMelee(String name, String lore, HashMap<DamageType, Double> damages,
			HashMap<ElementalDamageType, Double> elementalDamages, HashMap<WeaponAttribute, Double> attributes) {
		super(name, lore, damages, elementalDamages, attributes,WeaponType.Melee);
		// TODO Auto-generated constructor stub
	}
	
	public WeaponMelee clone(){
		HashMap<DamageType, Double> damages = new HashMap<>();
		HashMap<ElementalDamageType, Double> elementalDamages = new HashMap<>();
		HashMap<WeaponAttribute, Double> attributes = new HashMap<>();
		damages.putAll(this.getDamages());
		elementalDamages.putAll(this.getElementalDamages());
		attributes.putAll(this.getAttributes());
		return new WeaponMelee(getName(), getLore(), damages, elementalDamages, attributes);
		
	}
}
