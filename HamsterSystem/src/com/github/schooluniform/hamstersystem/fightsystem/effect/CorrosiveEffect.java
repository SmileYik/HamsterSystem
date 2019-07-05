package com.github.schooluniform.hamstersystem.fightsystem.effect;

import com.github.schooluniform.hamstersystem.entity.EntityAttribute;
import com.github.schooluniform.hamstersystem.entity.FightEntity;
import com.github.schooluniform.hamstersystem.fightsystem.base.DamageType;

public class CorrosiveEffect {
	//减少护甲百分比
	private static double effect = 30;
	
	public static void init(double effect){
		CorrosiveEffect.effect = effect;
	}
	
	/**
	 * 腐蚀效果: 减少护甲
	 * @param entity 战斗单位
	 */
	public static void corrosive(FightEntity entity){
		entity.setDamageSign(DamageType.Corrosive, true);
		double armor = entity.getAttribute(EntityAttribute.Armor)*(1-effect/100D);
		entity.setAttribute(EntityAttribute.Armor, armor<=1?1:armor);
	}
}
