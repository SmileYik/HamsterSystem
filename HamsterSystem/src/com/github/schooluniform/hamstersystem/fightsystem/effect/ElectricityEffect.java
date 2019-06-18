package com.github.schooluniform.hamstersystem.fightsystem.effect;

import java.util.AbstractMap;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;

import com.github.schooluniform.hamstersystem.HamsterSystem;
import com.github.schooluniform.hamstersystem.data.entity.FightEntity;
import com.github.schooluniform.hamstersystem.fightsystem.FightSystem;

public class ElectricityEffect implements Runnable{
	private static int duration = 6;
	//造成伤害超过该数值则停止行动
	private static double damage = 100;
	private static double x=3,y=3,z=3;
	private static int id = -1;
	//持续时间,有效个体,伤害值
	private static HashMap<FightEntity,Map.Entry<Double,Integer>> electricityEntity = new HashMap<>();
	private LinkedList<Integer> entitys = new LinkedList<>();
	
	public static void init(int duration, double damage,double x,double y,double z){
		ElectricityEffect.damage = damage;
		ElectricityEffect.duration = duration;
		ElectricityEffect.x = x;
		ElectricityEffect.y  = y;
		ElectricityEffect.z = z;
	}
	
	@Override
	public void run(){
		if(electricityEntity.isEmpty()){
			Bukkit.getScheduler().cancelTask(id);
			id = -1;
			return;
		}
		
		Iterator<Map.Entry<FightEntity,Map.Entry<Double,Integer>>> iterator = electricityEntity.entrySet().iterator();
		while(iterator.hasNext()){
			Map.Entry<FightEntity,Map.Entry<Double,Integer>> entry = iterator.next();
			try{
				entry.getValue().setValue(entry.getValue().getValue()-1);
				if(entry.getValue().getValue()<=0){
					iterator.remove();
					electricityEntity.remove(entry.getKey());
				}else{
					entitys.clear();
					damage(entry.getKey().getEntity(),entry.getValue().getKey(),entry.getValue().getValue());
/*					for(Entity entity : entry.getKey().getEntity().getWorld()
							.getNearbyEntities(entry.getKey().getEntity().getLocation(), x, y, z)){
						if(entity instanceof LivingEntity){
							((LivingEntity)entity).damage(entry.getValue().getKey());
							if(entry.getValue().getKey()>damage){
								FightEntity defander = FightSystem.fight(((LivingEntity)entity));
								ImpactEffect.impactByTime(defander, entry.getValue().getValue());
							}
						}
					}*/
				}				
			}catch(Exception e){
				iterator.remove();
				electricityEntity.remove(entry.getKey());
			}
		}
	}
	
	private void damage(LivingEntity livingEntity,double damage,int time){
		for(Entity entity : livingEntity.getWorld()
				.getNearbyEntities(livingEntity.getLocation(), x, y, z)){
			if(entity == null || entitys.contains(entity.getEntityId()))return;
			if(entity instanceof LivingEntity){
				((LivingEntity)entity).damage(damage);
				if(damage>ElectricityEffect.damage){
					ImpactEffect.impactByTime(FightSystem.fight(((LivingEntity)entity)), time);
				}
				entitys.add(entity.getEntityId());
				damage(((LivingEntity)entity),damage,time);
			}
		}
	}
	
	/**
	 * 电击效果
	 * @param entity 战斗实体
	 * @param damage 每次电击造成伤害
	 * @param time 对持续时间进行修改(百分数) (不修改填0)
	 */
	public static void electicity(FightEntity entity,double damage, double time){
/*		if(damage>=ElectricityEffect.damage){
			ImpactEffect.impact(entity, time);
		}else*/{
			time = duration*(1+time/100D);
			if(electricityEntity.containsKey(entity)){
				electricityEntity.replace(entity, new AbstractMap.SimpleEntry<Double,Integer>(damage, (int) time));
			}else{
				electricityEntity.put(entity, new AbstractMap.SimpleEntry<Double,Integer>(damage, (int) time));
			}
		}
		if(id == -1){
			id = Bukkit.getScheduler().runTaskTimerAsynchronously(HamsterSystem.plugin, new ElectricityEffect(), 0, 20).getTaskId();
		}
	}
}
