package com.github.schooluniform.hamstersystem.fightsystem.effect;

import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import com.github.schooluniform.hamstersystem.data.entity.FightEntity;

public class ColdEffect {
	private static int duration = 6;
	private static int level = 1;
	
	public static void init(int duration,int level){
		ColdEffect.duration = duration;
		ColdEffect.level = level;
	}
	
	/**
	 * 冰冻效果
	 * @param entity 战斗实体
	 * @param time 
	 */
	public static void cold(FightEntity entity,double time){
		entity.getEntity().addPotionEffect(new PotionEffect(PotionEffectType.SLOW, (int)(duration*(1+time/100D))*20, level), true);
	}
}
