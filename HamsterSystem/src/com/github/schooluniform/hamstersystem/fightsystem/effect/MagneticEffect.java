package com.github.schooluniform.hamstersystem.fightsystem.effect;

import com.github.schooluniform.hamstersystem.data.entity.FightEntity;
import com.github.schooluniform.hamstersystem.entity.EntityAttribute;

public class MagneticEffect {
	/**
	 * 永久降低护盾上限并且减少一定能量
	 */
	private static double energyEffect = 30;
	private static double shield = 30;
	
	public static void init(double energyEffect,double shield){
		MagneticEffect.energyEffect = energyEffect;
		MagneticEffect.shield = shield;
	}
	
	/**
	 * 永久降低护盾上限并减少一定能量
	 * @param entity 战斗单位
	 */
	public static void magnetic(FightEntity entity){
		entity.setAttribute(EntityAttribute.Shield, entity.getAttribute(EntityAttribute.Shield)*(1-shield/100D));
		entity.setEnergy(entity.getEnergy()*(1-energyEffect/100D));
	}

}
