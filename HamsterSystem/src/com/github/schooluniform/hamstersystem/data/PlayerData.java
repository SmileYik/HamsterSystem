package com.github.schooluniform.hamstersystem.data;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.attribute.Attribute;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.github.schooluniform.hamstersystem.HamsterSystem;
import com.github.schooluniform.hamstersystem.entity.EntityAttribute;
import com.github.schooluniform.hamstersystem.entity.FightEntity;
import com.github.schooluniform.hamstersystem.fightsystem.DamageSystem;
import com.github.schooluniform.hamstersystem.util.ModUtils;
import com.github.schooluniform.hamstersystem.util.Util;

public class PlayerData extends FightEntity{
	
	//Mod
	private int[] modId,modLevel,modExp;
	private List<ItemStack> mods;
	
	
	public PlayerData(UUID entityUUID, EntityType entityType, double health, double shield, double energy,
			HashMap<EntityAttribute, Double> attributes, int[] modId,int[] modLevel,int[] modExp, List<ItemStack> mods) {
		super(entityUUID, entityType, health, shield, energy, attributes);
		this.modId = modId;
		this.modLevel = modLevel;
		this.modExp = modExp;
		this.mods = mods;
	}

	public FightEntity getFight(){
		return new FightEntity(this,modId,modLevel);
	}
	
	public static PlayerData createNewPlayerData(Player p){
		return new PlayerData(
				p.getUniqueId(),
				EntityType.PLAYER,
				p.getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue(),
				100,0,
				Util.getDefaultEntityAttributes(),
				new int[10],new int[10],new int[10],new LinkedList<ItemStack>());
	}
	
	public static PlayerData  laod(String playerName){
		File file = new File(HamsterSystem.plugin.getDataFolder()+"/playerdata/"+playerName+".yml");
		
		if(!file.exists()){
			return null;
		}

		YamlConfiguration data = YamlConfiguration.loadConfiguration(file);
		
		HashMap<EntityAttribute,Double> attributes = new HashMap<>();
		for(EntityAttribute attribute : EntityAttribute.values())
			if(data.contains("attributes."+attribute))
				attributes.put(attribute, data.getDouble("attributes."+attribute));
				
		int[][] modData = Util.modDataToArray(data.getString("mods"));
		List<ItemStack> mods = new LinkedList<ItemStack>();
		int index = 0;
		while(data.contains("mods.items."+index)) {
			mods.add(data.getItemStack("mods.items."+index++));
		}
		
		return new PlayerData(
				UUID.fromString(data.getString("UUID")),
				EntityType.PLAYER,
				data.getDouble("health"),
				data.getDouble("shield"),
				data.getDouble("energy"),
				attributes,modData[0],modData[1],modData[2],mods);
	}
	
	public void save(){
		File file = new File(HamsterSystem.plugin.getDataFolder()+"/playerdata/"+this.getEntity().getName()+".yml");
		YamlConfiguration data = YamlConfiguration.loadConfiguration(file);
		data.set("UUID", getEntity().getUniqueId().toString());
		data.set("health", this.getHealth());
		data.set("shield", getShield());
		data.set("energy", getEnergy());
		for(Map.Entry<EntityAttribute, Double> entry : attributes.entrySet())
			data.set("attributes."+entry.getKey(), entry.getValue());
		data.set("mods", Util.modDataToString(modId, modLevel, modExp));
		
		int index = 0;
		for(ItemStack item : mods) {
			data.set("mods.items."+index++, item);
		}
		
		try {
			data.save(file);
		} catch (IOException e) {
			Bukkit.getServer().getLogger().warning("Player Data save Failed! Player Name: "+getEntity().getName());
			e.printStackTrace();
		}
	}
	
	public void setMod(List<ItemStack> mods) {
		int[][] modsData = ModUtils.getModsData(mods);
		this.modId = modsData[0];
		this.modLevel = modsData[1];
		this.modExp = modsData[2];
		this.mods = mods;
	}

	public List<ItemStack> getMods() {
		return mods;
	}
	
	
}
