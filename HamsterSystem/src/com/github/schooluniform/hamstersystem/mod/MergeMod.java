package com.github.schooluniform.hamstersystem.mod;

import java.util.HashMap;
import java.util.Map;

import com.github.schooluniform.hamstersystem.data.Data;
import com.github.schooluniform.hamstersystem.entity.EntityAttribute;
import com.github.schooluniform.hamstersystem.fightsystem.base.DamageType;
import com.github.schooluniform.hamstersystem.fightsystem.base.ElementalDamageType;
import com.github.schooluniform.hamstersystem.weapon.WeaponAttribute;

public class MergeMod {
	private HashMap<DamageType, Double> damages ;
	private HashMap<ElementalDamageType, Double> elementalDamages ;
	private HashMap<WeaponAttribute,Double> weaponAttributes ;
	private HashMap<EntityAttribute, Double> entityAttributes;
	public MergeMod(HashMap<DamageType, Double> damages, HashMap<ElementalDamageType, Double> elementalDamages,
			HashMap<WeaponAttribute, Double> weaponAttributes, HashMap<EntityAttribute, Double> entityAttributes) {
		super();
		this.damages = damages;
		this.elementalDamages = elementalDamages;
		this.weaponAttributes = weaponAttributes;
		this.entityAttributes = entityAttributes;
	}
	
	
	public static MergeMod getWeaponMergeMod(int[] modsId, int[] modsLevel){
		HashMap<DamageType, Double> damages = new HashMap<>();
		HashMap<ElementalDamageType, Double> elementalDamages = new HashMap<>();
		HashMap<WeaponAttribute,Double> weaponAttributes = new HashMap<>();
		
		if(modsId == null)
			return new MergeMod(damages, elementalDamages, weaponAttributes, new HashMap<>());
			
		int size = modsId.length>modsLevel.length?modsLevel.length:modsId.length;
		
		for(int i = 0 ; i<size ;i++){
			int modId = modsId[i];
			if(modId<0 || !Data.contansMod(modId))continue;
			Mod mod = new Mod(Data.getMod(modId));
			mod.up(modsLevel[i]);
			
			for(Map.Entry<DamageType, Double> entry : mod.getDamages().entrySet()){
				if(damages.containsKey(entry.getKey()))
					damages.replace(entry.getKey(), damages.get(entry.getKey())+entry.getValue());
				else 
					damages.put(entry.getKey(), entry.getValue());
			}
			
			for(Map.Entry<ElementalDamageType, Double> entry : mod.getElementalDamages().entrySet()){
				if(elementalDamages.containsKey(entry.getKey()))
					elementalDamages.replace(entry.getKey(), elementalDamages.get(entry.getKey())+entry.getValue());
				else 
					elementalDamages.put(entry.getKey(), entry.getValue());
			}
			
			for(Map.Entry<WeaponAttribute,Double> entry : mod.getWeaponAttributes().entrySet()){
				if(weaponAttributes.containsKey(entry.getKey()))
					weaponAttributes.replace(entry.getKey(), weaponAttributes.get(entry.getKey())+entry.getValue());
				else 
					weaponAttributes.put(entry.getKey(), entry.getValue());
			}
		}
		return new MergeMod(damages, elementalDamages, weaponAttributes, new HashMap<>());
	}
	
	public static MergeMod getEntityMergeMod(int[] modsId, int[] modsLevel){
		HashMap<DamageType, Double> damages = new HashMap<>();
		HashMap<ElementalDamageType, Double> elementalDamages = new HashMap<>();
		HashMap<EntityAttribute,Double> entityAttributes = new HashMap<>();
		
		if(modsId == null){			
			return new MergeMod(damages, elementalDamages,new HashMap<>(),entityAttributes);
		}
		
		int size = modsId.length>modsLevel.length?modsLevel.length:modsId.length;
		for(int i = 0 ; i<size ;i++){
			int modId = modsId[i];
			if(modId<0 || !Data.contansMod(modId))continue;
			Mod mod = new Mod(Data.getMod(modId));
			mod.up(modsLevel[i]);
			
			for(Map.Entry<DamageType, Double> entry : mod.getDamages().entrySet()){
				if(damages.containsKey(entry.getKey()))
					damages.replace(entry.getKey(), damages.get(entry.getKey())+entry.getValue());
				else 
					damages.put(entry.getKey(), entry.getValue());
			}
			
			for(Map.Entry<ElementalDamageType, Double> entry : mod.getElementalDamages().entrySet()){
				if(elementalDamages.containsKey(entry.getKey()))
					elementalDamages.replace(entry.getKey(), elementalDamages.get(entry.getKey())+entry.getValue());
				else 
					elementalDamages.put(entry.getKey(), entry.getValue());
			}
			
			for(Map.Entry<EntityAttribute,Double> entry : mod.getEntityAttritubes().entrySet()){
				if(entityAttributes.containsKey(entry.getKey()))
					entityAttributes.replace(entry.getKey(), entityAttributes.get(entry.getKey())+entry.getValue());
				else 
					entityAttributes.put(entry.getKey(), entry.getValue());
			}
		}
		return new MergeMod(damages, elementalDamages,new HashMap<>(),entityAttributes);
	}


	public HashMap<DamageType, Double> getDamages() {
		return damages;
	}
	
	public Double getDamage(DamageType type) {
		return damages.get(type);
	}


	public HashMap<ElementalDamageType, Double> getElementalDamages() {
		return elementalDamages;
	}

	public Double getElementalDamage(ElementalDamageType type) {
		return elementalDamages.get(type);
	}

	public HashMap<WeaponAttribute, Double> getWeaponAttributes() {
		return weaponAttributes;
	}

	public Double getWeaponAttribute(WeaponAttribute type) {
		return weaponAttributes.get(type);
	}
	
	public HashMap<EntityAttribute, Double> getEntityAttributes() {
		return entityAttributes;
	}
	
	public Double getEntityAttribute(EntityAttribute type) {
		return entityAttributes.get(type);
	}
}
