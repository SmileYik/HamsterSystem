package com.github.schooluniform.hamstersystem.fightsystem.effect;

import java.util.AbstractMap;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.bukkit.Bukkit;

import com.github.schooluniform.hamstersystem.HamsterSystem;
import com.github.schooluniform.hamstersystem.data.entity.FightEntity;

public class SlashEffect implements Runnable{
	private static int duration = 6;
	private static double effect = 30;
	private static int id = -1;
	//有效个体,伤害值,持续时间
	private static HashMap<FightEntity,Map.Entry<Double,Integer>> slashEntity = new HashMap<>();
	
	public static void init(int duration,double effect){
		SlashEffect.duration = duration;
		SlashEffect.effect = effect;
	}
	
	@Override
	public void run(){
		if(slashEntity.isEmpty()){
			Bukkit.getScheduler().cancelTask(id);
			id = -1;
			return;
		}
		
		Iterator<Map.Entry<FightEntity, Map.Entry<Double, Integer>>> iterator = slashEntity.entrySet().iterator();
		
		while(iterator.hasNext()){
			Map.Entry<FightEntity, Map.Entry<Double, Integer>> entry = iterator.next();
			try{
				entry.getKey().getEntity().damage(entry.getValue().getKey());
				entry.getValue().setValue(entry.getValue().getValue()-1);
				if(entry.getValue().getValue() <= 0){
					iterator.remove();
					slashEntity.remove(entry.getKey());
				}
			}catch(Exception e){
				iterator.remove();
				slashEntity.remove(entry.getKey());
			}
		}
		
	}
	
	/**
	 * 切割伤害
	 * @param entity 战斗单位
	 * @param damage 基础伤害
	 * @param time 对持续时间进行修改(百分数) (不修改填0)
	 */
	public static void slash(FightEntity entity, double damage, double time){
		damage *= effect/100D;
		double finalTime = SlashEffect.duration * (1+time/100D);
		if(slashEntity.containsKey(entity)){
			slashEntity.replace(entity, new AbstractMap.SimpleEntry<Double,Integer>(damage, (int)finalTime));
		}else{
			slashEntity.put(entity, new AbstractMap.SimpleEntry<Double,Integer>(damage, (int)finalTime));			
		}
		if(id == -1){
			id = Bukkit.getScheduler().runTaskTimerAsynchronously(HamsterSystem.plugin, new SlashEffect(), 0, 20).getTaskId();
		}
		
	}

}
