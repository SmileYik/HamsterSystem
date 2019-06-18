package com.github.schooluniform.hamstersystem.fightsystem.effect;

import org.bukkit.attribute.Attribute;

import com.github.schooluniform.hamstersystem.data.entity.FightEntity;
import com.github.schooluniform.hamstersystem.entity.EntityAttribute;

public class ViralEffect {
	private static double healthEffect = 30;
	
	public static void init(double effect){
		healthEffect = effect;
	}
	
	/**
	 * 病毒效果,减小血量上限
	 * @param entity 战斗实体
	 */
	public static void viral(FightEntity entity){
		entity.setAttribute(EntityAttribute.Health,entity.getAttribute(EntityAttribute.Health)*(1-healthEffect/100D));
		entity.getEntity().getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(
				entity.getAttribute(EntityAttribute.Health));
	}
}
