package com.github.schooluniform.hamstersystem.fightsystem.effect;

import java.util.AbstractMap;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.bukkit.Bukkit;

import com.github.schooluniform.hamstersystem.HamsterSystem;
import com.github.schooluniform.hamstersystem.entity.FightEntity;

public class PunctureEffect implements Runnable{
	private static int duration = 6;
	private static double effect = 30;
	private static int id = -1;
	//实体ID 减少伤害百分比 时间
	private static HashMap<Integer,Map.Entry<Double, Integer>> punctureEntity = new HashMap<>();
	
	public static void init(int duration,double effect){
		PunctureEffect.duration = duration;
		PunctureEffect.effect = effect;
	}
	
	@Override
	public void run(){
		if(punctureEntity.isEmpty()){
			Bukkit.getScheduler().cancelTask(id);
			id = -1;
			return;
		}
		Iterator<Map.Entry<Integer,Map.Entry<Double, Integer>>> iterator = punctureEntity.entrySet().iterator();
		
		while(iterator.hasNext()){
			Map.Entry<Integer,Map.Entry<Double, Integer>> entry = iterator.next();
			try{
				entry.getValue().setValue(entry.getValue().getValue()-1);
				if(entry.getValue().getValue()<=0){
					iterator.remove();
					punctureEntity.remove(entry.getKey());
				}
			}catch(Exception e){
				iterator.remove();
				punctureEntity.remove(entry.getKey());
			}
		}
	}
	
	/**
	 * 穿刺效果
	 * @param entity 战斗实体
	 * @param effect 减少武器伤害百分比
	 * @param time 对持续时间进行修改(百分数) (不修改填0)
	 */
	public static void puncture(FightEntity entity,double time){
		double finalTime = PunctureEffect.duration * (1+time/100D);
		if(punctureEntity.containsKey(entity.getEntity().getEntityId())){
			punctureEntity.replace(entity.getEntity().getEntityId(), 
					new AbstractMap.SimpleEntry<Double,Integer>(effect,(int)finalTime));
		}else{
			punctureEntity.put(entity.getEntity().getEntityId(), 
					new AbstractMap.SimpleEntry<Double,Integer>(effect,(int)finalTime));
		}
		if(id == -1){
			id = Bukkit.getScheduler().runTaskTimerAsynchronously(HamsterSystem.plugin, new PunctureEffect(), 0, 20).getTaskId();
		}
	}
	
	/**
	 * 获取对武器减少伤害的效果(百分数)
	 * @param entityId 实体ID
	 * @return 对武器减少伤害的效果(百分数) 无效果返回 0
	 */
	public static double getPunctureEffect(int entityId){
		if(punctureEntity.containsKey(entityId)){
			return punctureEntity.get(entityId).getKey();
		}else{
			return 0;
		}
	}
}
