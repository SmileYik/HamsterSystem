package com.github.schooluniform.hamstersystem.fightsystem;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import org.bukkit.entity.LivingEntity;

import com.github.schooluniform.hamstersystem.data.entity.FightEntity;


public class FightSystem implements Runnable{
	private static int leaveFightTime = 5;
	//Integer: Entity ID
	private static HashMap<Integer,FightEntity> fightingEntity = new HashMap<>();
	private static HashMap<Integer,Integer> fightingEntityLeave = new HashMap<>();
	
	//
	private static LinkedList<Integer> leavingFightEntity = new LinkedList<>();
	private static LinkedList<LivingEntity> entringFightEntity = new LinkedList<>();

	@Override
	public void run() {
		for(Map.Entry<Integer,Integer> entry : fightingEntityLeave.entrySet()){
			entry.setValue(entry.getValue()-1);
			if(entry.getValue()<=0) leaveFight(entry.getKey());
		}
		
		for(int entityID:leavingFightEntity){
			fightingEntity.remove(entityID);
			fightingEntityLeave.remove(entityID);
		}
		
		for(LivingEntity entity : entringFightEntity){
			if(HealTask.containsKey(entity.getEntityId())){
				
				fightingEntity.put(entity.getEntityId(), HealTask.getFightEntityData(entity.getEntityId()));
				fightingEntityLeave.put(entity.getEntityId(), leaveFightTime);
			}else{
				fightingEntity.put(entity.getEntityId(), FightEntity.getFightEntity(entity));
				fightingEntityLeave.put(entity.getEntityId(), leaveFightTime);
			}
		}
	}
	
	public static void enterFight(LivingEntity entity){
		if(fightingEntity.containsKey(entity.getEntityId()))
			fightingEntityLeave.replace(entity.getEntityId(), leaveFightTime);
		else entringFightEntity.add(entity);
	}
	
	
	public static void leaveFight(int entityID){
		FightEntity entity = fightingEntity.get(entityID);
		if(!entity.getEntity().isDead()){
			HealTask.enter(entityID, entity);
		}
		leavingFightEntity.add(entityID);
		return;
	}

	
}
