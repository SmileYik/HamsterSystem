package com.github.schooluniform.hamstersystem.fightsystem;

import java.util.AbstractMap;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.entity.LivingEntity;

import com.github.schooluniform.hamstersystem.HamsterSystem;
import com.github.schooluniform.hamstersystem.I18n;
import com.github.schooluniform.hamstersystem.data.Data;
import com.github.schooluniform.hamstersystem.entity.FightEntity;


public class FightSystem implements Runnable{
	private static int leaveFightTime = 5;
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
			if(leaveEntity.contains(entry.getKey())){
				HealTask.fixPlayerData(entry.getValue().getKey());
				iterator.remove();
				leaveEntity.remove(entry.getKey());
				fightData.remove(entry.getKey());
				continue;
			}else if(entry.getValue().getValue()>0){
				entry.getValue().setValue(entry.getValue().getValue()-1);
			}else{
				HealTask.heal(entry.getValue().getKey());
				iterator.remove();
				fightData.remove(entry.getKey());
			}
		}
	}
	
	public static FightEntity fight(LivingEntity entity){
		if(id == -1){
			id = Bukkit.getScheduler().runTaskTimerAsynchronously(HamsterSystem.plugin, new FightSystem(), 0, 20).getTaskId();
		}
		
		if(fightData.containsKey(entity.getEntityId())){
			fightData.get(entity.getEntityId()).setValue(leaveFightTime);
			return fightData.get(entity.getEntityId()).getKey();
		}else{
			FightEntity entityData = HealTask.stopHeal(entity.getEntityId());
			if(entityData== null){
				if(entity.hasMetadata("HamsterSystemMob")){
					entityData = Data.getMob(entity.getMetadata("HamsterSystemMob").get(0).asString()).getMobFightEntity(entity);
				}else {					
					entityData = FightEntity.getFightEntity(entity);
				}
			}
			fightData.put(entity.getEntityId(), new AbstractMap.SimpleEntry<FightEntity,Integer>(entityData,leaveFightTime));
			I18n.senda(entity, "actionbar.fight.enter-fight");
			return entityData;
		}
	}
	
	public static void leaveFight(int entityId){
		leaveEntity.add(entityId);
	}
}
