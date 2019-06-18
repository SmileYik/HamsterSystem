package com.github.schooluniform.hamstersystem.fightsystem.effect;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.entity.AreaEffectCloud;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;

import com.github.schooluniform.hamstersystem.HamsterSystem;
import com.github.schooluniform.hamstersystem.data.entity.FightEntity;
import com.github.schooluniform.hamstersystem.fightsystem.FightSystem;

public class GasEffect implements Runnable{
	private static int duration = 6;
	private static double radius = 0.5;
	private static int id = -1;
	
	private static HashMap<AreaEffectCloud,Double> gases = new HashMap<>();
	
	public static void init(int duration,double radius){
		GasEffect.duration = duration;
		GasEffect.radius = radius;
	}
	
	@Override
	public void run(){
		if(gases.isEmpty()){
			Bukkit.getScheduler().cancelTask(id);
			id = -1;
			return;
		}
		Iterator<Map.Entry<AreaEffectCloud,Double>> iterator = gases.entrySet().iterator();
		while(iterator.hasNext()){
			Map.Entry<AreaEffectCloud,Double> entry = iterator.next();
			if(entry.getKey().isDead()){
				iterator.remove();
				gases.remove(entry.getKey());
				return;
			}
			for(Entity entity : entry.getKey().getNearbyEntities(radius, radius, radius)){
				if(entity instanceof LivingEntity){
					try{
						FightSystem.fight((LivingEntity)entity).getEntity().damage(entry.getValue(), entry.getKey());						
					}catch(Exception e){
						continue;
					}
				}
			}
		}
		
	}
	
	/**
	 * 毒气效果
	 * @param entity 战斗单位
	 * @param damage 伤害
	 * @param time 对持续时间进行修改(百分数) (不修改填0)
	 */
	public static void gas(FightEntity entity,double damage,double time){
		AreaEffectCloud aec = (AreaEffectCloud)entity.getEntity().getWorld().spawnEntity(
				entity.getEntity().getLocation(), EntityType.AREA_EFFECT_CLOUD);
		aec.setSource(entity.getEntity());
		aec.setColor(Color.GREEN);
		aec.setRadius((int)(radius*6));
		aec.setDuration((int)(duration*(1+time/100D))*20);
		gases.put(aec, damage);
		
		if(id == -1){
			id = Bukkit.getScheduler().runTaskTimerAsynchronously(HamsterSystem.plugin, new GasEffect(), 0, 20).getTaskId();
		}
	}
	
	/**
	 * 毒气效果
	 * @param l 地点
	 * @param damage 伤害
	 * @param time 对持续时间进行修改(百分数) (不修改填0)
	 */
	public static void gas(Location l,double damage,double time){
		AreaEffectCloud aec = (AreaEffectCloud)l.getWorld().spawnEntity(l, EntityType.AREA_EFFECT_CLOUD);
		aec.setColor(Color.GREEN);
		aec.setRadius((int)(radius*6));
		aec.setDuration((int)(duration*(1+time/100D))*20);
		gases.put(aec, damage);
		
		if(id == -1){
			id = Bukkit.getScheduler().runTaskTimerAsynchronously(HamsterSystem.plugin, new GasEffect(), 0, 20).getTaskId();
		}
	}
}
