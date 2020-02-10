package com.github.schooluniform.hamstersystem.fightsystem;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.function.BiConsumer;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.github.schooluniform.hamstersystem.HamsterSystem;
import com.github.schooluniform.hamstersystem.I18n;
import com.github.schooluniform.hamstersystem.data.Data;
import com.github.schooluniform.hamstersystem.entity.EntityAttribute;
import com.github.schooluniform.hamstersystem.entity.FightEntity;
import com.github.schooluniform.hamstersystem.fightsystem.base.BasicDamageData;
import com.github.schooluniform.hamstersystem.fightsystem.base.DamageType;
import com.github.schooluniform.hamstersystem.fightsystem.base.ElementalDamageType;
import com.github.schooluniform.hamstersystem.fightsystem.effect.BlastEffect;
import com.github.schooluniform.hamstersystem.fightsystem.effect.ColdEffect;
import com.github.schooluniform.hamstersystem.fightsystem.effect.CorrosiveEffect;
import com.github.schooluniform.hamstersystem.fightsystem.effect.ElectricityEffect;
import com.github.schooluniform.hamstersystem.fightsystem.effect.GasEffect;
import com.github.schooluniform.hamstersystem.fightsystem.effect.HeatEffect;
import com.github.schooluniform.hamstersystem.fightsystem.effect.ImpactEffect;
import com.github.schooluniform.hamstersystem.fightsystem.effect.MagneticEffect;
import com.github.schooluniform.hamstersystem.fightsystem.effect.PunctureEffect;
import com.github.schooluniform.hamstersystem.fightsystem.effect.RadiationEffect;
import com.github.schooluniform.hamstersystem.fightsystem.effect.SlashEffect;
import com.github.schooluniform.hamstersystem.fightsystem.effect.ToxinEffect;
import com.github.schooluniform.hamstersystem.fightsystem.effect.ViralEffect;
import com.github.schooluniform.hamstersystem.mod.MergeMod;
import com.github.schooluniform.hamstersystem.mod.ModTag;
import com.github.schooluniform.hamstersystem.weapon.Calculation;
import com.github.schooluniform.hamstersystem.weapon.Weapon;
import com.github.schooluniform.hamstersystem.weapon.WeaponAttribute;
import com.github.schooluniform.hamstersystem.weapon.WeaponTag;

public class DamageSystem {
	
	public static boolean isWeapon(ItemStack item){
		if(item == null || item.getType() == Material.AIR)
			return false;
		if(Data.NBTTag.contantsNBT(item, WeaponTag.HSWLT.name())&&
				Data.contansWeapon(Data.NBTTag.getString(item, WeaponTag.HSWLT.name())))
			return true;
		return false;
	}
	
	public static boolean isMod(ItemStack item) {
		if(item == null || item.getType() == Material.AIR)
			return false;
		if(Data.NBTTag.contantsNBT(item, ModTag.HSMLE.name())&&
				Data.contansMod(Data.NBTTag.getString(item, ModTag.HSMLT.name())) &&
				Data.NBTTag.contantsNBT(item, ModTag.HSMINFO.name()))
			return true;
		return false;
	}
	
	public static Weapon getBasicWeapon(ItemStack item) {
		if(!isWeapon(item))return null;
		return Data.getWeapon(Data.NBTTag.getString(item, WeaponTag.HSWLT.name())).clone();
	}
	
	public static Weapon getWeapon(ItemStack item){
		if(!isWeapon(item))return null;
		HashMap<WeaponAttribute,Double> weaponAttributes = new HashMap<>();
		String hswlt = Data.NBTTag.getString(item, WeaponTag.HSWLT.name());
		int[] modsId = Data.NBTTag.getIntArray(item, WeaponTag.HSWMOD.name());
		int[] modsLevel = Data.NBTTag.getIntArray(item, WeaponTag.HSWML.name());
		Weapon weapon = Data.getWeapon(hswlt).clone();
		MergeMod mod = MergeMod.getWeaponMergeMod(modsId, modsLevel);
		weaponAttributes.putAll(weapon.getAttributes());
		
		for(Map.Entry<WeaponAttribute, Double> entry : mod.getWeaponAttributes().entrySet()){
			if(entry.getKey().getWay() == Calculation.Multiplication){
				if(weaponAttributes.containsKey(entry.getKey()))
					weaponAttributes.replace(entry.getKey(), (1+entry.getValue()/100D)*weaponAttributes.get(entry.getKey()));
			}else if(entry.getKey().getWay() == Calculation.Addition){
				if(weaponAttributes.containsKey(entry.getKey()))
					weaponAttributes.replace(entry.getKey(), entry.getValue()+weaponAttributes.get(entry.getKey()));
				else
					weaponAttributes.put(entry.getKey(), entry.getValue());
			}
		}
		
		weapon.getAttributes().clear();
		weapon.getAttributes().putAll(weaponAttributes);
		return weapon;
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
			if(entry.getKey().getWay() == Calculation.Multiplication){
				if(weaponAttributes.containsKey(entry.getKey()))
					weaponAttributes.replace(entry.getKey(), (1+entry.getValue()/100D)*weaponAttributes.get(entry.getKey()));
			}else if(entry.getKey().getWay() == Calculation.Addition){
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
		
		
		return new BasicDamageData(attacker, damages, weapon, triggerDamage, 
				getCritDamage(weaponAttributes.get(WeaponAttribute.Crit_Chance), weaponAttributes.get(WeaponAttribute.Crit_Damage)));
	}
	
	public static Weapon getModWeapon(ItemStack item){
		if(!isWeapon(item))return null;
		
		HashMap<DamageType, Double> damages = new HashMap<>();
		HashMap<ElementalDamageType, Double> elementalDamages = new HashMap<>();
		HashMap<WeaponAttribute,Double> weaponAttributes = new HashMap<>();
		LinkedList<ElementalDamageType> elementalDamagesRemover = new LinkedList<>();
		
		String hswlt = Data.NBTTag.getString(item, WeaponTag.HSWLT.name());
		int[] modsId = Data.NBTTag.getIntArray(item, WeaponTag.HSWMOD.name());
		int[] modsLevel = Data.NBTTag.getIntArray(item, WeaponTag.HSWML.name());
		Weapon weapon = Data.getWeapon(hswlt).clone();
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
			if(entry.getKey().getWay() == Calculation.Multiplication){
				if(weaponAttributes.containsKey(entry.getKey()))
					weaponAttributes.replace(entry.getKey(), (1+entry.getValue()/100D)*weaponAttributes.get(entry.getKey()));
			}else if(entry.getKey().getWay() == Calculation.Addition){
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

		weapon.getAttributes().clear();
		weapon.getAttributes().putAll(weaponAttributes);
		weapon.getDamages().clear();
		weapon.getDamages().putAll(damages);
		
		return weapon;
	}
	
	
	public static double getCritDamage(Double critChance,Double critDamage){
		if(critChance == null || critDamage == null)return 1;
		critDamage += critChance/100D;
		critChance %= 100D;
		if(Math.random() * 100 < critChance)
			return critDamage;
		return critDamage-1;
	}

	public static void sendHealth(LivingEntity attacker,FightEntity defender) {
		if(attacker == null)return;
		if(attacker instanceof Player) {
			Bukkit.getScheduler().runTaskLaterAsynchronously(HamsterSystem.plugin, ()->{
				Data.actionbar.sendActionbar((Player) attacker,  defender.getUpdateSign());			
			}, 5);
		}else if(defender.getEntity() instanceof Player) {
			defender.getUpdateSign();
		}
	}
	
	public static void triggerDamage(FightEntity attacker,FightEntity defander,DamageType damageType,double damage) {
		if(damageType == null)return;
		double extraArmor = defander.getAttribute(EntityAttribute.valueOf(damageType.name()+"Armor"));
		if(extraArmor <0) {
			
			return;
		}
		switch (damageType) {
		case Blast:
			I18n.senda(defander.getEntity(), "actionbar.fight.effect.be-blast");
			I18n.senda(attacker.getEntity(), "actionbar.fight.effect.blast",
					defander.getEntity().isCustomNameVisible()?defander.getEntity().getCustomName():defander.getEntity().getName());
			BlastEffect.blast(defander, 0);
			break;
		case Cold:
			I18n.senda(defander.getEntity(), "actionbar.fight.effect.be-cold");
			I18n.senda(attacker.getEntity(), "actionbar.fight.effect.cold",
					defander.getEntity().isCustomNameVisible()?defander.getEntity().getCustomName():defander.getEntity().getName());
			ColdEffect.cold(defander, 0);
			break;
		case Corrosive:
			I18n.senda(defander.getEntity(), "actionbar.fight.effect.be-corrosive");
			I18n.senda(attacker.getEntity(), "actionbar.fight.effect.corrosive",
					defander.getEntity().isCustomNameVisible()?defander.getEntity().getCustomName():defander.getEntity().getName());
			CorrosiveEffect.corrosive(defander,extraArmor);
			break;
		case Electricity:
			I18n.senda(defander.getEntity(), "actionbar.fight.effect.be-electricity");
			I18n.senda(attacker.getEntity(), "actionbar.fight.effect.electricity",
					defander.getEntity().isCustomNameVisible()?defander.getEntity().getCustomName():defander.getEntity().getName());
			ElectricityEffect.electicity(defander, damage/extraArmor, 0);
			break;
		case Gas:
			I18n.senda(defander.getEntity(), "actionbar.fight.effect.be-gas");
			I18n.senda(attacker.getEntity(), "actionbar.fight.effect.gas",
					defander.getEntity().isCustomNameVisible()?defander.getEntity().getCustomName():defander.getEntity().getName());
			GasEffect.gas(defander, damage, 0);
			break;
		case Heat:
			I18n.senda(defander.getEntity(), "actionbar.fight.effect.be-heat");
			I18n.senda(attacker.getEntity(), "actionbar.fight.effect.heat",
					defander.getEntity().isCustomNameVisible()?defander.getEntity().getCustomName():defander.getEntity().getName());
			HeatEffect.heat(defander, 0);
			break;
		case Impact:
			I18n.senda(defander.getEntity(), "actionbar.fight.effect.be-impact");
			I18n.senda(attacker.getEntity(), "actionbar.fight.effect.impact",
					defander.getEntity().isCustomNameVisible()?defander.getEntity().getCustomName():defander.getEntity().getName());
			ImpactEffect.impact(defander, 0);
			break;
		case Magnetic:
			I18n.senda(defander.getEntity(), "actionbar.fight.effect.be-magnetic");
			I18n.senda(attacker.getEntity(), "actionbar.fight.effect.magnetic",
					defander.getEntity().isCustomNameVisible()?defander.getEntity().getCustomName():defander.getEntity().getName());
			MagneticEffect.magnetic(defander);
			break;
		case Puncture:
			I18n.senda(defander.getEntity(), "actionbar.fight.effect.be-puncture");
			I18n.senda(attacker.getEntity(), "actionbar.fight.effect.puncture",
					defander.getEntity().isCustomNameVisible()?defander.getEntity().getCustomName():defander.getEntity().getName());
			PunctureEffect.puncture(defander, 0);
			break;
		case Radiation:
			I18n.senda(defander.getEntity(), "actionbar.fight.effect.be-radiation");
			I18n.senda(attacker.getEntity(), "actionbar.fight.effect.radiation",
					defander.getEntity().isCustomNameVisible()?defander.getEntity().getCustomName():defander.getEntity().getName());
			RadiationEffect.radiation(defander, 0);
			break;
		case Slash:
			I18n.senda(defander.getEntity(), "actionbar.fight.effect.be-slash");
			I18n.senda(attacker.getEntity(), "actionbar.fight.effect.slash",
					defander.getEntity().isCustomNameVisible()?defander.getEntity().getCustomName():defander.getEntity().getName());
			SlashEffect.slash(defander, damage, 0);
			break;
		case Toxin:
			I18n.senda(defander.getEntity(), "actionbar.fight.effect.be-toxin");
			I18n.senda(attacker.getEntity(), "actionbar.fight.effect.toxin",
					defander.getEntity().isCustomNameVisible()?defander.getEntity().getCustomName():defander.getEntity().getName());
			ToxinEffect.toxin(defander, 0);
			break;
		case Viral:
			I18n.senda(defander.getEntity(), "actionbar.fight.effect.be-viral");
			I18n.senda(attacker.getEntity(), "actionbar.fight.effect.viral",
					defander.getEntity().isCustomNameVisible()?defander.getEntity().getCustomName():defander.getEntity().getName());
			ViralEffect.viral(defander);
			break;
		default:
			break;
		}
	}
	
	public static void triggerDamage(BasicDamageData bdd,FightEntity defander){
		if(bdd.getTriggerDamage() == null)return;
		double extraArmor = defander.getAttribute(EntityAttribute.valueOf(bdd.getTriggerDamage().name()+"Armor"));
		if(extraArmor <0) {
			
			return;
		}
		switch (bdd.getTriggerDamage()) {
		case Blast:
			I18n.senda(defander.getEntity(), "actionbar.fight.effect.be-blast");
			I18n.senda(bdd.getAttacker(), "actionbar.fight.effect.blast",
					defander.getEntity().isCustomNameVisible()?defander.getEntity().getCustomName():defander.getEntity().getName());
			BlastEffect.blast(defander, 0);
			break;
		case Cold:
			I18n.senda(defander.getEntity(), "actionbar.fight.effect.be-cold");
			I18n.senda(bdd.getAttacker(), "actionbar.fight.effect.cold",
					defander.getEntity().isCustomNameVisible()?defander.getEntity().getCustomName():defander.getEntity().getName());
			ColdEffect.cold(defander, 0);
			break;
		case Corrosive:
			I18n.senda(defander.getEntity(), "actionbar.fight.effect.be-corrosive");
			I18n.senda(bdd.getAttacker(), "actionbar.fight.effect.corrosive",
					defander.getEntity().isCustomNameVisible()?defander.getEntity().getCustomName():defander.getEntity().getName());
			CorrosiveEffect.corrosive(defander,extraArmor);
			break;
		case Electricity:
			I18n.senda(defander.getEntity(), "actionbar.fight.effect.be-electricity");
			I18n.senda(bdd.getAttacker(), "actionbar.fight.effect.electricity",
					defander.getEntity().isCustomNameVisible()?defander.getEntity().getCustomName():defander.getEntity().getName());
			ElectricityEffect.electicity(defander, bdd.getDamage()/extraArmor, 0);
			break;
		case Gas:
			I18n.senda(defander.getEntity(), "actionbar.fight.effect.be-gas");
			I18n.senda(bdd.getAttacker(), "actionbar.fight.effect.gas",
					defander.getEntity().isCustomNameVisible()?defander.getEntity().getCustomName():defander.getEntity().getName());
			GasEffect.gas(defander, bdd.getDamage(), 0);
			break;
		case Heat:
			I18n.senda(defander.getEntity(), "actionbar.fight.effect.be-heat");
			I18n.senda(bdd.getAttacker(), "actionbar.fight.effect.heat",
					defander.getEntity().isCustomNameVisible()?defander.getEntity().getCustomName():defander.getEntity().getName());
			HeatEffect.heat(defander, 0);
			break;
		case Impact:
			I18n.senda(defander.getEntity(), "actionbar.fight.effect.be-impact");
			I18n.senda(bdd.getAttacker(), "actionbar.fight.effect.impact",
					defander.getEntity().isCustomNameVisible()?defander.getEntity().getCustomName():defander.getEntity().getName());
			ImpactEffect.impact(defander, 0);
			break;
		case Magnetic:
			I18n.senda(defander.getEntity(), "actionbar.fight.effect.be-magnetic");
			I18n.senda(bdd.getAttacker(), "actionbar.fight.effect.magnetic",
					defander.getEntity().isCustomNameVisible()?defander.getEntity().getCustomName():defander.getEntity().getName());
			MagneticEffect.magnetic(defander);
			break;
		case Puncture:
			I18n.senda(defander.getEntity(), "actionbar.fight.effect.be-puncture");
			I18n.senda(bdd.getAttacker(), "actionbar.fight.effect.puncture",
					defander.getEntity().isCustomNameVisible()?defander.getEntity().getCustomName():defander.getEntity().getName());
			PunctureEffect.puncture(defander, 0);
			break;
		case Radiation:
			I18n.senda(defander.getEntity(), "actionbar.fight.effect.be-radiation");
			I18n.senda(bdd.getAttacker(), "actionbar.fight.effect.radiation",
					defander.getEntity().isCustomNameVisible()?defander.getEntity().getCustomName():defander.getEntity().getName());
			RadiationEffect.radiation(defander, 0);
			break;
		case Slash:
			I18n.senda(defander.getEntity(), "actionbar.fight.effect.be-slash");
			I18n.senda(bdd.getAttacker(), "actionbar.fight.effect.slash",
					defander.getEntity().isCustomNameVisible()?defander.getEntity().getCustomName():defander.getEntity().getName());
			SlashEffect.slash(defander, bdd.getDamage(), 0);
			break;
		case Toxin:
			I18n.senda(defander.getEntity(), "actionbar.fight.effect.be-toxin");
			I18n.senda(bdd.getAttacker(), "actionbar.fight.effect.toxin",
					defander.getEntity().isCustomNameVisible()?defander.getEntity().getCustomName():defander.getEntity().getName());
			ToxinEffect.toxin(defander, 0);
			break;
		case Viral:
			I18n.senda(defander.getEntity(), "actionbar.fight.effect.be-viral");
			I18n.senda(bdd.getAttacker(), "actionbar.fight.effect.viral",
					defander.getEntity().isCustomNameVisible()?defander.getEntity().getCustomName():defander.getEntity().getName());
			ViralEffect.viral(defander);
			break;
		default:
			break;
		}
	}
}
