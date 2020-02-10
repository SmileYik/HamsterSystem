package com.github.schooluniform.hamstersystem.skills;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;

import com.github.schooluniform.hamstersystem.entity.EntityAttribute;
import com.github.schooluniform.hamstersystem.entity.FightEntity;
import com.github.schooluniform.hamstersystem.fightsystem.base.DamageType;
import com.github.schooluniform.hamstersystem.fightsystem.base.ProjectileType;

/**
 *  花一点时间和能量释放技能,
 *  有冷却时间, 持续时间,
 *  技能范围,造成伤害(负数为加血)以及伤害效果
 *  若为发出投射物的技能则需要
 *  有投射物种类.
 *  若为放置方块类则需要
 *  方块的血量,若血量为0则为持续一段时间消失
 * @author miSkYle
 *
 */
public abstract class AbstractSkill {
	private int delay;
	private int duration;
	private int cooldown;
	/** 1=buff;2=projectile;3=block*/
	private int skillType;
	private double radius;
	private double energy;
	private double damage;
	private double durationEnergy;
	private DamageType damageType = null;
	private ProjectileType projectile;
	private Material blockMateral;
	private SkillType type;

	public AbstractSkill(int delay, int duration, int cooldown, int skillType, double radius, double energy,
			double damage, double durationEnergy, DamageType damageType, ProjectileType projectile,
			Material blockMateral,SkillType type) {
		super();
		this.delay = delay;
		this.duration = duration;
		this.cooldown = cooldown;
		this.skillType = skillType;
		this.radius = radius;
		this.energy = energy;
		this.damage = damage;
		this.durationEnergy = durationEnergy;
		this.damageType = damageType;
		this.projectile = projectile;
		this.blockMateral = blockMateral;
		this.type = type;
	}

	public abstract void cast(FightEntity caster,Object ... args);
	public abstract void onHit(Location loc,FightEntity caster,Entity hitEntity,Object ... args);
	
	public boolean canCast(FightEntity caster) {
		double needEnergy = energy;
		if(caster.getAttributes().containsKey(EntityAttribute.EnergyEfficiency)) {
			needEnergy = energy*(2-caster.getAttribute(EntityAttribute.EnergyEfficiency)/100D);			
		}
		if(caster.getEnergy()<needEnergy) {
			return false;
		}else{
			caster.modifyEnergy(-needEnergy);
			return true;
		}
	}
	
	public double getEnergyPerSecond(FightEntity caster) {
		double duration = caster.getAttribute(EntityAttribute.Duration);
		if(duration <= 0 ) {
			duration = 1;
		}
		return durationEnergy/duration;
	}

	public int getDelay() {
		return delay;
	}

	public void setDelay(int delay) {
		this.delay = delay;
	}

	public int getDuration() {
		return duration;
	}

	public void setDuration(int duration) {
		this.duration = duration;
	}

	public int getCooldown() {
		return cooldown;
	}

	public void setCooldown(int cooldown) {
		this.cooldown = cooldown;
	}

	public int getSkillType() {
		return skillType;
	}

	public void setSkillType(int skillType) {
		this.skillType = skillType;
	}

	public double getRadius() {
		return radius;
	}

	public void setRadius(double radius) {
		this.radius = radius;
	}

	public double getEnergy() {
		return energy;
	}

	public void setEnergy(double energy) {
		this.energy = energy;
	}

	public double getDamage() {
		return damage;
	}

	public void setDamage(double damage) {
		this.damage = damage;
	}

	public double getDurationEnergy() {
		return durationEnergy;
	}

	public void setDurationEnergy(double durationEnergy) {
		this.durationEnergy = durationEnergy;
	}

	public DamageType getDamageType() {
		return damageType;
	}

	public void setDamageType(DamageType damageType) {
		this.damageType = damageType;
	}

	public ProjectileType getProjectile() {
		return projectile;
	}

	public void setProjectile(ProjectileType projectile) {
		this.projectile = projectile;
	}

	public Material getBlockMateral() {
		return blockMateral;
	}

	public void setBlockMateral(Material blockMateral) {
		this.blockMateral = blockMateral;
	}

	public SkillType getType() {
		return type;
	}

	public void setType(SkillType type) {
		this.type = type;
	}

}
