package com.github.schooluniform.hamstersystem.mod;

import java.io.File;
import java.text.MessageFormat;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.github.schooluniform.hamstersystem.HamsterSystem;
import com.github.schooluniform.hamstersystem.I18n;
import com.github.schooluniform.hamstersystem.data.Data;
import com.github.schooluniform.hamstersystem.entity.EntityAttribute;
import com.github.schooluniform.hamstersystem.fightsystem.base.DamageType;
import com.github.schooluniform.hamstersystem.fightsystem.base.ElementalDamageType;
import com.github.schooluniform.hamstersystem.weapon.WeaponAttribute;

public class Mod {
	/*
	 * NBT
	 * HSMLE: 等级与经验 int[]
	 * HSMINFO: 极性
	 * HSMLT: 指向文件
	 */
	private int id;
	private int basePoint;
	private String fileName;
	private String name;
	private String lore;
	private ItemStack item;
	private ModPolarity polarity;
	private ModRarity rarity;
	private ModType type;
	private String typeDisplay = null;
	private HashMap<DamageType, Double> damages = new HashMap<DamageType, Double>();
	private HashMap<ElementalDamageType, Double> elementalDamages = new HashMap<ElementalDamageType, Double>();
	private HashMap<WeaponAttribute,Double> weaponAttributes = new HashMap<WeaponAttribute, Double>();
	private HashMap<EntityAttribute,Double> entityAttritubes = new HashMap<EntityAttribute, Double>();
	
	
	
	protected Mod(String fileName,int id, int basePoint, String name, String lore, ModPolarity polarity, ModRarity rarity, ModType type,
			String typeDisplay, HashMap<DamageType, Double> damages,
			HashMap<ElementalDamageType, Double> elementalDamages, HashMap<WeaponAttribute, Double> weaponAttributes,
			HashMap<EntityAttribute, Double> entityAttritubes,ItemStack item) {
		super();
		this.fileName = fileName;
		this.id = id;
		this.basePoint = basePoint;
		this.name = name;
		this.lore = lore;
		this.polarity = polarity;
		this.rarity = rarity;
		this.type = type;
		this.typeDisplay = typeDisplay;
		this.damages = damages;
		this.elementalDamages = elementalDamages;
		this.weaponAttributes = weaponAttributes;
		this.entityAttritubes = entityAttritubes;
		this.item = item;
	}
	
	public Mod(String fileName){
		YamlConfiguration mod = YamlConfiguration.loadConfiguration(new File(getDefaultPath()+"/"+fileName+".yml"));
		//LinkedList<ElementalDamageType> elementalDamagesRemover = new LinkedList<>();
		if(mod.contains("base-damages.impact"))
			damages.put(DamageType.Impact, mod.getDouble("base-damages.impact"));
		if(mod.contains("base-damages.slash"))
			damages.put(DamageType.Slash, mod.getDouble("base-damages.slash"));
		if(mod.contains("base-damages.puncture"))
			damages.put(DamageType.Puncture, mod.getDouble("base-damages.puncture"));
		
		for(String damage:mod.getStringList("base-damages.elemental-damages")){
			String[] data = damage.split(":");
			try{
				elementalDamages.put(ElementalDamageType.valueOf(data[0]), Double.parseDouble(data[1]));				
			}catch (Exception e) {
				HamsterSystem.plugin.getLogger().warning("Mod Setting Wrong! Mod: "+fileName+".yml; Path: base-damages.elemental-damages - "+damage);
			}
		}
		
		for(WeaponAttribute key : WeaponAttribute.values())
			if(mod.contains("weapon-attributes."+key))
				weaponAttributes.put(key, mod.getDouble("weapon-attributes."+key));
		
		for(EntityAttribute key : EntityAttribute.values())
			if(mod.contains("entity-attributes."+key))
				entityAttritubes.put(key, mod.getDouble("entity-attributes."+key));
		
		this.fileName = fileName;
		this.id = mod.getInt("id");
		this.basePoint = mod.getInt("mod-point",1);
		this.name = mod.getString("name");
		this.lore = mod.getString("lore","Mod");
		this.polarity = ModPolarity.valueOf(mod.getString("polarity", ModPolarity.Null.name()));
		this.rarity = ModRarity.valueOf(mod.getString("rarity",ModRarity.Comment.name()));
		this.type = ModType.valueOf(mod.getString("mod-type",ModType.Display.name()));
		this.typeDisplay = mod.getString("mod-type-display");
		this.item = mod.getItemStack("item");
	}
	
	public Mod(Mod mod){
		damages.putAll(mod.getDamages());
		elementalDamages.putAll(mod.getElementalDamages());
		weaponAttributes.putAll(mod.getWeaponAttributes());
		entityAttritubes.putAll(mod.getEntityAttritubes());
		this.fileName = mod.fileName;
		this.id = mod.id;
		this.basePoint = mod.basePoint;
		this.name = mod.name;
		this.lore = mod.lore;
		this.polarity = mod.polarity;
		this.rarity = mod.rarity;
		this.type = mod.type;
		this.typeDisplay = mod.typeDisplay;
		this.item = mod.item;
	}
	
	
	public static File getDefaultPath(){
		return new File(HamsterSystem.plugin.getDataFolder()+File.separator+"mod/");
	}

	public void up(int level){
		if(level <= 0)return;
		
		YamlConfiguration mod = YamlConfiguration.loadConfiguration(new File(getDefaultPath()+fileName+".yml"));
		boolean add = mod.getBoolean("update.is-add");
		//base-damages.elemental-damages
		for(Map.Entry<DamageType, Double> entry : damages.entrySet()){
			if(mod.contains("update.base-damages."+entry.getKey())){
				double num = mod.getDouble("update.base-damages."+entry.getKey());
				if(add)
					entry.setValue(entry.getValue()+num);
				else
					entry.setValue(entry.getValue()*(1+num/100D));
			}
		}
		
		for(Map.Entry<ElementalDamageType, Double> entry : elementalDamages.entrySet()){
			if(mod.contains("update.base-damages.elemental-damages."+entry.getKey())){
				double num = mod.getDouble("update.base-damages.elemental-damages."+entry.getKey());
				if(add)
					entry.setValue(entry.getValue()+num);
				else
					entry.setValue(entry.getValue()*(1+num/100D));
			}
		}
		
		for(Map.Entry<WeaponAttribute, Double> entry : weaponAttributes.entrySet()){
			if(mod.contains("update.weapon-attributes."+entry.getKey())){
				double num = mod.getDouble("update.weapon-attributes."+entry.getKey());
				if(add)
					entry.setValue(entry.getValue()+num);
				else
					entry.setValue(entry.getValue()*(1+num/100D));
			}
		}
		
		for(Map.Entry<EntityAttribute, Double> entry : entityAttritubes.entrySet()){
			if(mod.contains("update.entity-attributes."+entry.getKey())){
				double num = mod.getDouble("update.entity-attributes."+entry.getKey());
				if(add)
					entry.setValue(entry.getValue()+num);
				else
					entry.setValue(entry.getValue()*(1+num/100D));
			}
		}
		
	}
	
	public ItemStack getItem(int level,int exp,int polarityID) {
		ItemStack item = this.item.clone();
		ItemMeta im = item.getItemMeta();
		im.setLore(Arrays.asList(
				MessageFormat.format(
						lore, 
						I18n.tr("mod.polarity."+polarity.getType()),
						level,
						I18n.tr("mod.rarity."+rarity.getLevel()),
						type.getID()==3?typeDisplay:I18n.tr("mod.type."+type.getID()),
						level+basePoint
				).split(";")));
		item.setItemMeta(im);
		item = Data.NBTTag.addNBT(item, ModTag.HSMINFO.name(), polarityID);
		item = Data.NBTTag.addNBT(item, ModTag.HSMLE.name(), new int[] {level,exp});
		item = Data.NBTTag.addNBT(item, ModTag.HSMLT.name(), name);
		return item;
	}
	
	public Double getDamage(DamageType type){
		return damages.get(type);
	}
	
	public Double getWeaponAttribute(WeaponAttribute attribute){
		return weaponAttributes.get(attribute);
	}
	
	public Double getElementalDamage(ElementalDamageType type){
		return elementalDamages.get(type);
	}
	
	public Double getEntityAttritubes(EntityAttribute attritube){
		return entityAttritubes.get(attritube);
	}
	
	public int getId() {
		return id;
	}
	public String getName() {
		return name;
	}
	public String getLore() {
		return lore;
	}
	public ModPolarity getPolarity() {
		return polarity;
	}
	public ModRarity getRarity() {
		return rarity;
	}
	public ModType getType() {
		return type;
	}
	public String getTypeDisplay() {
		return typeDisplay;
	}
	public HashMap<DamageType, Double> getDamages() {
		return damages;
	}
	public HashMap<WeaponAttribute, Double> getWeaponAttributes() {
		return weaponAttributes;
	}
	public HashMap<EntityAttribute, Double> getEntityAttritubes() {
		return entityAttritubes;
	}
	public HashMap<ElementalDamageType, Double> getElementalDamages() {
		return elementalDamages;
	}
	public int getBasePoint() {
		return basePoint;
	}
	
}
