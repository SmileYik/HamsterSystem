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
import com.github.schooluniform.hamstersystem.data.entity.FightEntity;


public class FightSystem implements Runnable{
	private static int leaveFightTime = 5;
	private static int id = -1;
	
	private static HashMap<Integer,Map.Entry<FightEntity,Integer>> fightData = new HashMap<>();
	private static LinkedList<Integer> leaveEntity = new LinkedList<>();
/*	@Test
	public void test(){
		HashMap<String,Integer> a = new HashMap<>();
		String str = "ABCDEFGHIJKLMNOPQ";
		int i = 0;
		for(char c : str.toCharArray()){
			a.put(c+"", i++);
		}
		
		Iterator<Map.Entry<String, Integer>>  iterator = a.entrySet().iterator();
		System.out.println(a);
		
		while(iterator.hasNext()){
			Map.Entry<String, Integer> entry = iterator.next();
			if(Math.random()*100<30){
				System.out.println("Remove key: "+entry.getKey()+" Value: "+entry.getValue());
				iterator.remove();
				a.remove(entry.getKey());
				continue;
			}else if(Math.random()*100<30){
				entry.setValue(entry.getValue()+1);
				System.out.println("Change key: "+entry.getKey()+" Value: "+entry.getValue());
			}
		}
		
		System.out.println(a);
		
		
	}*/
	
	
	
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
				iterator.remove();
				leaveEntity.remove(entry.getKey());
				fightData.remove(entry.getKey());
				continue;
			}else if(entry.getValue().getValue()>0){
				entry.getValue().setValue(entry.getValue().getValue()-1);
			}else{
				//TODO
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
				entityData = FightEntity.getFightEntity(entity);
			}
			fightData.put(entity.getEntityId(), new AbstractMap.SimpleEntry<FightEntity,Integer>(entityData,leaveFightTime));
			I18n.senda(entity, "actionbar.fight.enter-fight");
			return entityData;
		}
	}
	
	public static void leaveFight(int entityId){
		leaveEntity.add(entityId);
	}
	
	
	
	
	
	
	
	
	
	
	
/*	//Integer: Entity ID
	private static HashMap<Integer,FightEntity> fightingEntity = new HashMap<>();
	private static HashMap<Integer,Integer> fightingEntityLeave = new HashMap<>();
	
	//
	private static LinkedList<Integer> leavingFightEntity = new LinkedList<>();
	private static LinkedList<FightEntity> entringFightEntity = new LinkedList<>();

	@Override
	public void run() {
		for(Map.Entry<Integer,Integer> entry : fightingEntityLeave.entrySet()){
			entry.setValue(entry.getValue()-1);
			if(entry.getValue()<=0) leaveFight(entry.getKey());
		}
		//out
		for(int entityID:leavingFightEntity){
			fightingEntity.remove(entityID);
			fightingEntityLeave.remove(entityID);
		}
		//in
		for(FightEntity entity : entringFightEntity){
			if(entity == null){
				continue;
			}
			if(HealTask.containsKey(entity.getEntity().getEntityId())){
				fightingEntity.put(entity.getEntity().getEntityId(), HealTask.getFightEntityData(entity.getEntity().getEntityId()));
				fightingEntityLeave.put(entity.getEntity().getEntityId(), leaveFightTime);
			}else{
				fightingEntity.put(entity.getEntity().getEntityId(),entity);
				fightingEntityLeave.put(entity.getEntity().getEntityId(), leaveFightTime);
			}
		}
	}
	
	public static FightEntity enterFight(LivingEntity entity){
		if(fightingEntity.containsKey(entity.getEntityId())){
			fightingEntityLeave.replace(entity.getEntityId(), leaveFightTime);	
			return fightingEntity.get(entity.getEntityId());
		}else{
			FightEntity entityData = FightEntity.getFightEntity(entity);
			entringFightEntity.add(entityData);
			return entityData;
		}
	}
	
	
	public static void leaveFight(int entityID){
		FightEntity entity = fightingEntity.get(entityID);
		if(entity.getEntity()!= null && !entity.getEntity().isDead()){
			HealTask.enter(entityID, entity);
		}
		leavingFightEntity.add(entityID);
		return;
	}*/

	
}
