package com.github.schooluniform.hamstersystem.fightsystem.effect;

import com.github.schooluniform.hamstersystem.data.entity.FightEntity;

public class HeatEffect {
	private static int duration = 6;
	
	public static void init(int duration){
		HeatEffect.duration = duration;
	}
	/**
	 * 燃烧效果
	 * @param entity 战斗单位
	 * @param time 对持续时间进行修改(百分数) (不修改填0)
	 */
	public static void heat(FightEntity entity,double time){
		entity.getEntity().setFireTicks((int)(duration*(1+time/100D)*20));
	}
}
