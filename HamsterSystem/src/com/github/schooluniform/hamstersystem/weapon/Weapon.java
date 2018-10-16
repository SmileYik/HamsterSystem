package com.github.schooluniform.hamstersystem.weapon;

import java.io.File;
import java.text.MessageFormat;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;

import com.github.schooluniform.hamstersystem.HamsterSystem;
import com.github.schooluniform.hamstersystem.fightsystem.base.DamageType;
import com.github.schooluniform.hamstersystem.fightsystem.base.ElementalDamageType;
import com.github.schooluniform.hamstersystem.fightsystem.base.ProjectileType;

public class Weapon {
/*
 * TODO
 * 
 * Weapon Item NBT
 * 
 * 指向武器数据标签:
 *   key: HSWLT; value: String
 * 武器弹夹标签:
 *   key: HSWClip; value: int
 * 武器等级经验标签:
 *   key: HSWEL; value: int[]; 0: Exp, 1: Level
 * 武器基本信息标签:
 *   key: HSWInfo; value: int[]; 0: 是否双倍(0否1是), 1~9: 极性
 * 武器MOD标签:
 *   key: HSWMOD; value: int[]
 */
	private String name;
	private String lore;
	private HashMap<DamageType, Double> damages;
	private HashMap<ElementalDamageType, Double> elementalDamages;
	private HashMap<WeaponAttribute,Double> attributes;
	private WeaponType weaponType;
	
	protected Weapon(String name, String lore, HashMap<DamageType, Double> damages,HashMap<ElementalDamageType, Double> elementalDamages,
			HashMap<WeaponAttribute, Double> attributes,WeaponType weaponType) {
		super();
		this.name = name;
		this.lore = lore;
		this.damages = damages;
		this.elementalDamages = elementalDamages;
		this.attributes = attributes;
		this.weaponType = weaponType;
	}
	
	public static File defaultPath(){
		return new File(HamsterSystem.plugin.getDataFolder()+File.separator+"weapon/");
	}
	
	public static Weapon load(String name){
		YamlConfiguration weapon = YamlConfiguration.loadConfiguration(new File(defaultPath()+name+".yml"));
		HashMap<DamageType, Double> damages = new HashMap<>();
		HashMap<WeaponAttribute,Double> attributes = new HashMap<>();
		HashMap<ElementalDamageType, Double> elementalDamages = new HashMap<>();
		//LinkedList<ElementalDamageType> elementalDamagesRemover = new LinkedList<>();
		WeaponType weaponType = WeaponType.valueOf(weapon.getString("weapon-type"));
		
		if(weapon.contains("base-damages.impact"))damages.put(DamageType.Impact, weapon.getDouble("base-damages.impact"));
		if(weapon.contains("base-damages.slash"))damages.put(DamageType.Slash, weapon.getDouble("base-damages.slash"));
		if(weapon.contains("base-damages.puncture"))damages.put(DamageType.Puncture, weapon.getDouble("base-damages.puncture"));
		
		for(String damage:weapon.getStringList("base-damages.elemental-damages")){
			String[] data = damage.split(":");
			try{
				elementalDamages.put(ElementalDamageType.valueOf(data[0]), Double.parseDouble(data[1]));				
			}catch (Exception e) {
				Bukkit.getLogger().warning("Weapon Setting Wrong! Weapon: "+name+"; Path: base-damages.elemental-damages - "+damage);
			}
		}
		
/*		elementalDamages.forEach(new BiConsumer<ElementalDamageType, Double>() {
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
			elementalDamages.remove(type);*/
		
		for(WeaponAttribute key : WeaponAttribute.values()){
			if(weapon.contains("attributes."+key))
				attributes.put(key, weapon.getDouble("attributes."+key));
		}
		
		switch (weaponType) {
		case Launcher:
			return (Weapon)new WeaponLauncher(
					weapon.getString("name"), 
					weapon.getString("lore"), 
					damages, elementalDamages, attributes, 
					(short) weapon.getInt("launch-amount",1), 
					weapon.getDouble("range",30), 
					weapon.getBoolean("gravity",true), 
					ProjectileType.valueOf(weapon.getString("projectile-type","SNOWBALL")));
		case Melee:
			return (Weapon)new WeaponMelee(
					weapon.getString("name"), 
					weapon.getString("lore"), 
					damages, elementalDamages, attributes);
		default:
			return null;
		
		}
		
	}
	
	public Weapon clone(){
		HashMap<DamageType, Double> damages = new HashMap<>();
		HashMap<ElementalDamageType, Double> elementalDamages = new HashMap<>();
		HashMap<WeaponAttribute, Double> attributes = new HashMap<>();
		damages.putAll(this.getDamages());
		elementalDamages.putAll(this.getElementalDamages());
		attributes.putAll(this.getAttributes());
		switch (weaponType) {
		case Launcher:
			WeaponLauncher launcher = (WeaponLauncher) this;
			return new WeaponLauncher(getName(), getLore(), damages, elementalDamages, attributes, launcher.getLaunchAmount(), launcher.getRange(), launcher.isGravity(), launcher.getProjectileType());
		case Melee:
			return new WeaponMelee(getName(), getLore(), damages, elementalDamages, attributes);
		default:
			return new Weapon(name, lore, damages, elementalDamages, attributes, weaponType);
		}
	}

	public String getName(){
		return name;
	}
	
	public String getLore(int level,int exp){
		return MessageFormat.format(lore, level,exp);
	}
	
	public String getLore(){
		return lore;
	}
	
	public Double getDamage(DamageType type){
		if(damages.containsKey(type))return damages.get(type);
		return null;
	}
	
	public HashMap<ElementalDamageType, Double> getElementalDamages(){
		return elementalDamages;
	}
	
	public Double getElementalDamage(ElementalDamageType type){
		if(elementalDamages.containsKey(type))return elementalDamages.get(type);
		return null;
	}
	
	public Double getAttribute(WeaponAttribute type){
		if(attributes.containsKey(type))return attributes.get(type);
		return null;
	}
	
	public HashMap<DamageType, Double> getDamages(){
		return damages;
	}
	
	public HashMap<WeaponAttribute,Double> getAttributes(){
		return attributes;
	}
	
}
