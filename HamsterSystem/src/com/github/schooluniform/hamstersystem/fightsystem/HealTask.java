package com.github.schooluniform.hamstersystem.fightsystem;

import java.util.AbstractMap;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.EntityType;

import com.github.schooluniform.hamstersystem.HamsterSystem;
import com.github.schooluniform.hamstersystem.I18n;
import com.github.schooluniform.hamstersystem.data.Data;
import com.github.schooluniform.hamstersystem.entity.EntityAttribute;
import com.github.schooluniform.hamstersystem.entity.FightEntity;
import com.github.schooluniform.hamstersystem.particle.ParticleFight;

public class HealTask implements Runnable{
	private static int leaveHealTime = 5;
	private static int id = -1;
	
	private static HashMap<Integer,Map.Entry<FightEntity,Integer>> fightData = new HashMap<>();
	private static LinkedList<Integer> leaveEntity = new LinkedList<>();
	
	@Override
	public void run() {
		
		if(fightData.isEmpty()){
			Bukkit.getScheduler().cancelTask(id);
			id = -1;
			return;
		}
		Iterator<Map.Entry<Integer,Map.Entry<FightEntity,Integer>>>  iterator = fightData.entrySet().iterator();
		
		while(iterator.hasNext()){
			Map.Entry<Integer,Map.Entry<FightEntity,Integer>> entry = iterator.next();
			if(entry.getValue().getKey().getEntity() == null) {
				iterator.remove();
				leaveEntity.remove(entry.getKey());
				fightData.remove(entry.getKey());
				fixPlayerData(entry.getValue().getKey());
				continue;
			}else if(leaveEntity.contains(entry.getKey())){
				iterator.remove();
				leaveEntity.remove(entry.getKey());
				fightData.remove(entry.getKey());
				fixPlayerData(entry.getValue().getKey());
				continue;
			}else if(entry.getValue().getKey().getShield()<entry.getValue().getKey().getAttribute(EntityAttribute.Shield)){
				entry.getValue().getKey().modifyShield(entry.getValue().getKey().getAttribute(EntityAttribute.Shield) *
						entry.getValue().getKey().getAttribute(EntityAttribute.ShieldRefresh)/100D);
				entry.getValue().getKey().getUpdateSign();
				ParticleFight.playHealShield(entry.getValue().getKey().getEntity());
			}else if(entry.getValue().getValue()>0){
				entry.getValue().setValue(entry.getValue().getValue()-1);
			}else{
				I18n.senda(entry.getValue().getKey().getEntity(), "actionbar.fight.quit-heal");
				iterator.remove();
				fightData.remove(entry.getKey());
				fixPlayerData(entry.getValue().getKey());
				continue;
			}
		}
	}
	
	public static void heal(FightEntity entity){
		if(entity == null)return;
		if(entity.getEntity() == null)return;
		if(fightData.containsKey(entity.getEntity().getEntityId()))return;
		//Entity needn't heal
		if(entity.getShield() >= entity.getAttribute(EntityAttribute.Shield)){
			fixPlayerData(entity);
			return;
		}
		I18n.senda(entity.getEntity(), "actionbar.fight.enter-heal");
		fightData.put(entity.getEntity().getEntityId(), new AbstractMap.SimpleEntry<FightEntity,Integer>(entity,leaveHealTime));
		
		if(id == -1){
			id = Bukkit.getScheduler().runTaskTimerAsynchronously(HamsterSystem.plugin, new HealTask(), 0, 20).getTaskId();
		}
	}
	
	
	public static FightEntity stopHeal(int entityId){
		if(!fightData.containsKey(entityId))return null;
		FightEntity data = fightData.get(entityId).getKey();
		leaveEntity.add(entityId);
		return data;
	}
	
	public static void fixPlayerData(FightEntity entity){
		if(entity.getEntityType() == EntityType.PLAYER){
			//Fix Player's Health
			if(entity.getEntity().getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue() != 
					Data.getPlayerData(entity.getEntity().getName()).getAttribute(EntityAttribute.Health)){
				entity.getEntity().getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(
						Data.getPlayerData(entity.getEntity().getName()).getAttribute(EntityAttribute.Health));
				I18n.senda(entity.getEntity(), "actionbar.fight.quit-fight");
			}
			//update player's energy
			Data.getPlayerData(entity.getEntity().getName()).setEnergy(entity.getEnergy());
			
			entity.resetBar();
		}

	}
}
