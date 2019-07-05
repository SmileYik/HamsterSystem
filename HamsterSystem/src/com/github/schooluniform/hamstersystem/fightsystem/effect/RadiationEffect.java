package com.github.schooluniform.hamstersystem.fightsystem.effect;

import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import com.github.schooluniform.hamstersystem.entity.FightEntity;

public class RadiationEffect {
	/**
	 * 致盲目标
	 */
	private static int duration = 6;
	private static int level = 1;
	
	public static void init(int duration,int level){
		RadiationEffect.duration = duration;
		RadiationEffect.level = level;
	}
	
	/**
	 * 辐射效果 : 致盲目标
	 * @param entity 战斗单位
	 * @param time 对持续时间进行修改(百分数) (不修改填0)
	 */
	public static void radiation(FightEntity entity,double time){
		time = duration*(1+time/100D);
		entity.getEntity().addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, (int)time*20, level));
	}
	
	
}
