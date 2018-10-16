package com.github.schooluniform.hamstersystem.fightsystem;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.function.BiConsumer;

import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.ItemStack;

import com.github.schooluniform.hamstersystem.data.Data;
import com.github.schooluniform.hamstersystem.fightsystem.base.BasicDamageData;
import com.github.schooluniform.hamstersystem.fightsystem.base.DamageType;
import com.github.schooluniform.hamstersystem.fightsystem.base.ElementalDamageType;
import com.github.schooluniform.hamstersystem.mod.MergeMod;
import com.github.schooluniform.hamstersystem.weapon.Weapon;
import com.github.schooluniform.hamstersystem.weapon.WeaponAttribute;
import com.github.schooluniform.hamstersystem.weapon.WeaponTag;

public class DamageSystem {
	
	public static boolean isWeapon(ItemStack item){
		if(Data.NBTTag.contantsNBT(item, WeaponTag.HSWLT.name())&&
				Data.contansWeapon(Data.NBTTag.getString(item, WeaponTag.HSWLT.name())))
			return true;
		return false;
	}
	
	public static BasicDamageData getBasicDamageData(LivingEntity attacker,ItemStack item){
		if(!isWeapon(item))return null;
		
		HashMap<DamageType, Double> damages = new HashMap<>();
		HashMap<ElementalDamageType, Double> elementalDamages = new HashMap<>();
		HashMap<WeaponAttribute,Double> weaponAttributes = new HashMap<>();
		LinkedList<ElementalDamageType> elementalDamagesRemover = new LinkedList<>();
		DamageType triggerDamage = null;
		
		String hswlt = Data.NBTTag.getString(item, WeaponTag.HSWLT.name());
		int[] modsId = Data.NBTTag.getIntArray(item, WeaponTag.HSWMOD.name());
		int[] modsLevel = Data.NBTTag.getIntArray(item, WeaponTag.HSWML.name());
		Weapon weapon = Data.getWeapon(hswlt);
		MergeMod mod = MergeMod.getWeaponMergeMod(modsId, modsLevel);
		
		damages.putAll(weapon.getDamages());
		elementalDamages.putAll(weapon.getElementalDamages());
		weaponAttributes.putAll(weapon.getAttributes());
		
		for(Map.Entry<DamageType, Double> entry : mod.getDamages().entrySet())
			if(damages.containsKey(entry.getKey()))
				damages.replace(entry.getKey(), (1+entry.getValue()/100D)*damages.get(entry.getKey()));
		
		double baseDamage = 0;
		
		for(Double temp : damages.values())
			baseDamage += temp;
		
		for(Map.Entry<ElementalDamageType, Double> entry : mod.getElementalDamages().entrySet())
			if(elementalDamages.containsKey(entry.getKey()))
				elementalDamages.replace(entry.getKey(),(1+entry.getValue()/100D)*elementalDamages.get(entry.getKey()));
			else
				elementalDamages.put(entry.getKey(), (baseDamage/4D)*(1+entry.getValue()/100D));
		
		for(Map.Entry<WeaponAttribute, Double> entry : mod.getWeaponAttributes().entrySet()){
			if(entry.getKey().getWay() == '*'){
				if(weaponAttributes.containsKey(entry.getKey()))
					weaponAttributes.replace(entry.getKey(), (1+entry.getValue()/100D)*weaponAttributes.get(entry.getKey()));
			}else if(entry.getKey().getWay() == '+'){
				if(weaponAttributes.containsKey(entry.getKey()))
					weaponAttributes.replace(entry.getKey(), entry.getValue()+weaponAttributes.get(entry.getKey()));
				else
					weaponAttributes.put(entry.getKey(), entry.getValue());
			}
		}
		
		elementalDamages.forEach(new BiConsumer<ElementalDamageType, Double>() {
			int sum = 0;
			HashMap<ElementalDamageType, Double> index = new HashMap<>();
			@Override
			public void accept(ElementalDamageType key, Double value) {
				index.put(key, value);
				sum+=key.getID();
				if(index.size()==2){
					double damage = 0;
					DamageType type = null;
					for(double temp : index.values())
						damage+=temp/2D;
					
					switch (sum) {
					case 11:
						type = DamageType.Magnetic;
						break;
					case 101:
						type = DamageType.Blast;
						break;
					case 1001:
						type = DamageType.Viral;
						break;
					case 110:
						type = DamageType.Radiation;
						break;
					case 1010:
						type = DamageType.Corrosive;
						break;
					case 1100:
						type = DamageType.Gas;
						break;
					default:
						break; 
					}
					damages.put(type, damage);
					elementalDamagesRemover.addAll(index.keySet());
					index.clear();
					sum = 0;
				}
			}
		});
		
		for(ElementalDamageType type:elementalDamagesRemover)
			elementalDamages.remove(type);
		
		for(Map.Entry<ElementalDamageType, Double> entry : elementalDamages.entrySet())
			damages.put(entry.getKey().getDamageType(), entry.getValue());
		
		//TriggerDamage
		if(weaponAttributes.containsKey(WeaponAttribute.Trigger_Adds) && 
				Math.random()*100<weaponAttributes.get(WeaponAttribute.Trigger_Adds))
			triggerDamage = damages.keySet().toArray(new DamageType[damages.size()])[(int)(Math.random()*damages.size())];
		
		
		return new BasicDamageData(attacker, damages, weaponAttributes, triggerDamage, 
				getCritDamage(weaponAttributes.get(WeaponAttribute.Crit_Chance), weaponAttributes.get(WeaponAttribute.Crit_Damage)));
	}
	
	
	public static double getCritDamage(Double critChance,Double critDamage){
		if(critChance == null || critDamage == null)return 1;
		critDamage += critChance/100D;
		critChance %= 100D;
		if(Math.random() * 100 < critChance)
			return critDamage;
		return critDamage-1;
	}
	
	
	
}
