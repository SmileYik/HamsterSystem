package com.github.schooluniform.hamstersystem.fightsystem;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

import org.bukkit.entity.LivingEntity;

import com.github.schooluniform.hamstersystem.data.FightData;

public class FightSystem implements Runnable{

	private static int leaveFightTime = 5;
	private static HashMap<Integer, Integer> onFightLeaveTimer = new HashMap<>();
	private static HashMap<Integer, FightData> onFightData = new HashMap<>();
	private static HashMap<Integer, FightData> reloadingShield = new HashMap<>();
	private static Stack<Integer> onFightIOEntityId = new Stack<>();
	private static Stack<FightData> onFightIOEntityFightData = new Stack<>();
	
	@Override
	public void run() {
		for(Map.Entry<Integer, Integer> entry : onFightLeaveTimer.entrySet()){
			entry.setValue(entry.getValue()-1);
			if(entry.getValue()<=0)
				onFightIOEntityId.push(entry.getKey()*10+0);
		}
		
		
		
		while(!onFightIOEntityId.isEmpty()){
			int id = onFightIOEntityId.pop();
			if(id%10 == 0){
				onFightLeaveTimer.remove(id/10);
				onFightData.remove(id/10);
				reloadShield(onFightData.get(id/10));
			}else{
				onFightData.put(id/10, onFightIOEntityFightData.pop());
				onFightLeaveTimer.put(id/10, leaveFightTime);
				//Bukkit.geten
			}
		}
	}
	
	public static void leaveFight(int entityId){
		onFightIOEntityId.push(entityId*10+0);
	}
	
	public static void enterFight(LivingEntity entity){
		onFightIOEntityId.push(entity.getEntityId()*10+1);
		if(reloadingShield.containsKey(entity.getEntityId()))
			onFightIOEntityFightData.push(reloadingShield.get(entity.getEntityId()));
		
	}
	
	public static void reloadShield(FightData fightData){
		if(fightData.getShield()<fightData.getMaxShield())
			reloadingShield.put(fightData.getEntity().getEntityId(), fightData);
	}
	
}
