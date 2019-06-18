package com.github.schooluniform.hamstersystem.fightsystem.effect;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;

import com.github.schooluniform.hamstersystem.data.entity.FightEntity;
import com.github.schooluniform.hamstersystem.fightsystem.FightSystem;

public class BlastEffect {
	private static int duration = 6;
	private static int x=3,y=3,z=3;
	private static float damage = 4;
	
	public static void init(int duration,int x, int y,int z,float damage){
		BlastEffect.duration = duration;
		BlastEffect.x = x;
		BlastEffect.y = y;
		BlastEffect.z = z;
		BlastEffect.damage = damage;
	}
	
	/**
	 * 爆炸效果
	 * @param entity 战斗单位
	 * @param time 持续时间
	 */
	public static void blast(FightEntity entity,double time){
		entity.getEntity().getWorld().createExplosion(
				entity.getEntity().getLocation().getX(), 
				entity.getEntity().getLocation().getY(), 
				entity.getEntity().getLocation().getZ(), damage, false, false);
		time = duration*(time/100D+1);
		for(Entity e : entity.getEntity().getNearbyEntities(x, y, z)){
			if(e instanceof LivingEntity){
				ImpactEffect.impactByTime(FightSystem.fight((LivingEntity)e), (int)time);
			}
		}
	}
	
	/**
	 * 爆炸效果
	 * @param l 地点
	 * @param time 持续时间
	 */
	public static void blast(Location l,double time){
		l.getWorld().createExplosion(l.getX(), l.getY(), l.getZ(), damage, false, false);
		time = duration*(time/100D+1);
		for(Entity e : l.getWorld().getNearbyEntities(l,x, y, z)){
			if(e instanceof LivingEntity){
				ImpactEffect.impactByTime(FightSystem.fight((LivingEntity)e), (int)time);
			}
		}
	}
}
