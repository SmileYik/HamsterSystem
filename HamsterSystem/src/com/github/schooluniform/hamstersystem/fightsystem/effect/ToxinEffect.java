package com.github.schooluniform.hamstersystem.fightsystem.effect;

import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import com.github.schooluniform.hamstersystem.data.entity.FightEntity;

public class ToxinEffect {
private static int duration = 6;
private static int level = 1;

public static void init(int duration, int level){
	ToxinEffect.duration = duration;
	ToxinEffect.level = level;
}
	
/**
 * 毒素效果
 * @param entity 战斗个体
 * @param time 对持续时间进行修改(百分数) (不修改填0)
 */
	public static void toxin(FightEntity entity,double time){
		entity.getEntity().addPotionEffect(new PotionEffect(PotionEffectType.POISON, (int)(duration*(1+time/100D))*20, level), true);
	}
}
