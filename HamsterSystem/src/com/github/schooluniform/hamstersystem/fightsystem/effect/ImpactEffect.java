package com.github.schooluniform.hamstersystem.fightsystem.effect;

import java.util.AbstractMap;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import com.github.schooluniform.hamstersystem.HamsterSystem;
import com.github.schooluniform.hamstersystem.data.entity.FightEntity;

public class ImpactEffect implements Runnable{
	private static int duration = 6;
	private static int id = -1;
	
	public static void init(int duration){
		ImpactEffect.duration = duration;
	}
	
	//实体ID 战斗单位 时间
	private static HashMap<Integer, Map.Entry<FightEntity, Integer>> impactEntity = new HashMap<>();
	
	@Override
	public void run(){
		if(impactEntity.isEmpty()){
			Bukkit.getScheduler().cancelTask(id);
			id = -1;
			return;
		}
		
		Iterator<Map.Entry<Integer, Map.Entry<FightEntity, Integer>>> iterator = impactEntity.entrySet().iterator();
		while (iterator.hasNext()){
			Map.Entry<Integer, Map.Entry<FightEntity,Integer>> entry = iterator.next();
			try{
				entry.getValue().setValue(entry.getValue().getValue()-1);
				if(entry.getValue().getValue()<=0){
					if(!(entry.getValue().getKey() instanceof Player)){
						entry.getValue().getKey().getEntity().setAI(true);
					}
					iterator.remove();
					impactEntity.remove(entry.getKey());
				}else if(entry.getValue().getKey().getEntity().hasAI()){
					entry.getValue().getKey().getEntity().setAI(false);
				}
			}catch(Exception e){
				iterator.remove();
				impactEntity.remove(entry.getKey());
			}
			
		}
		
	}
	
	/**
	 * 打击效果
	 * @param entity 战斗单位
	 * @param time 对持续时间进行修改(百分数) (不修改填0)
	 */
	public static void impact(FightEntity entity,double time){
		double finalTime = ImpactEffect.duration * (1+time/100D);
		if(impactEntity.containsKey(entity.getEntity().getEntityId())){
			impactEntity.replace(entity.getEntity().getEntityId(), 
					new AbstractMap.SimpleEntry<FightEntity,Integer>(entity,(int)finalTime));
		}else{
			impactEntity.put(entity.getEntity().getEntityId(), 
					new AbstractMap.SimpleEntry<FightEntity,Integer>(entity,(int)finalTime));
		}
		if(id == -1){
			id = Bukkit.getScheduler().runTaskTimerAsynchronously(HamsterSystem.plugin, new ImpactEffect(), 0, 20).getTaskId();
		}
	}
	
	protected static void impactByTime(FightEntity entity,int time){
		if(impactEntity.containsKey(entity.getEntity().getEntityId())){
			impactEntity.replace(entity.getEntity().getEntityId(), 
					new AbstractMap.SimpleEntry<FightEntity,Integer>(entity,time));
		}else{
			impactEntity.put(entity.getEntity().getEntityId(), 
					new AbstractMap.SimpleEntry<FightEntity,Integer>(entity,time));
		}
		if(id == -1){
			id = Bukkit.getScheduler().runTaskTimerAsynchronously(HamsterSystem.plugin, new ImpactEffect(), 0, 20).getTaskId();
		}
	}
	
	public static HashMap<Integer, Map.Entry<FightEntity, Integer>> getImpactEntity(){
		return impactEntity;
	}
}
