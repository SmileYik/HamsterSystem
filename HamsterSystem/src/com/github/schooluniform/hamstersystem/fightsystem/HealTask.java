package com.github.schooluniform.hamstersystem.fightsystem;

import java.util.ArrayList;
import java.util.HashMap;

import com.github.schooluniform.hamstersystem.data.entity.FightEntity;
import com.github.schooluniform.hamstersystem.entity.EntityAttribute;

public class HealTask implements Runnable{
	//after fighted, entity who be hurt will in this HashMap until it be heal.
	private static HashMap<Integer,FightEntity> fightedEntity = new HashMap<>();
	
	private static ArrayList<Integer> stopHealList = new ArrayList<>();
	private static HashMap<Integer,FightEntity> enterHealList = new HashMap<>();
	
	@Override
	public void run() {
		
		
	}
	
	public static void enter(int entityID,FightEntity entity){
		if(fightedEntity.containsKey(entityID))return;
		if(entity.getShield() == entity.getAttribute(EntityAttribute.Shield) &&
				entity.getEntity().getHealth() == entity.getAttribute(EntityAttribute.Health))return;
		enterHealList.put(entityID, entity);
	}
	
	public static boolean containsKey(int entityID){
		return fightedEntity.containsKey(entityID);
	}
	
	public static FightEntity getFightEntityData(int entityID){
		return fightedEntity.get(entityID);
	}
	
	public static void stopHeal(int entityID){
		stopHealList.add(entityID);
	}
}
