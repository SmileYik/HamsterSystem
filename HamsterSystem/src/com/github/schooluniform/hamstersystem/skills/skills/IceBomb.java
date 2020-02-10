package com.github.schooluniform.hamstersystem.skills.skills;

import java.awt.Color;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Projectile;

import com.github.schooluniform.hamstersystem.HamsterSystem;
import com.github.schooluniform.hamstersystem.entity.FightEntity;
import com.github.schooluniform.hamstersystem.fightsystem.DamageSystem;
import com.github.schooluniform.hamstersystem.fightsystem.FightSystem;
import com.github.schooluniform.hamstersystem.fightsystem.base.DamageType;
import com.github.schooluniform.hamstersystem.fightsystem.base.ProjectileType;
import com.github.schooluniform.hamstersystem.particle.ParticleColor;
import com.github.schooluniform.hamstersystem.skills.AbstractSkill;
import com.github.schooluniform.hamstersystem.skills.SkillProjectileManager;
import com.github.schooluniform.hamstersystem.skills.SkillType;

public class IceBomb extends AbstractSkill{

	public IceBomb() {
		super(1*20, 3*20, 10*20, 2, 1, 25, 100, 0, DamageType.Cold, ProjectileType.SNOWBALL,null,SkillType.IceBomb);
		
	}

	@Override
	public void cast(FightEntity caster, Object... args) {
		if(!canCast(caster))return;
		Projectile pro = caster.getEntity().launchProjectile(getProjectile().getProjectileClass());
		SkillProjectileManager.addProjectiles(pro.getEntityId() ,this,caster);
	}

	@Override
	public void onHit(Location loc,FightEntity caster, Entity hitEntity,Object...args) {
		loc.getWorld().createExplosion(loc.getX(), loc.getY(), loc.getZ(), 0, false, false);
		if(hitEntity != null && (hitEntity instanceof LivingEntity)) {
			FightEntity entity = FightSystem.fight((LivingEntity)hitEntity);
			entity.getEntity().damage(getDamage()*0.3,caster.getEntity());
		}
		for(Entity entity : loc.getWorld().getNearbyEntities(loc, getRadius(), getRadius(), getRadius())) {
			if(entity instanceof LivingEntity) {
				LivingEntity lEntity = (LivingEntity)entity;
				lEntity.damage(getDamage(), caster.getEntity());
				DamageSystem.triggerDamage(caster,FightSystem.fight(lEntity),getDamageType(),getDamage());
			}
		}
		playEffect(loc);
		Block main = loc.getBlock(); 
		simpleBlock(main);
		simpleBlock(main.getRelative(BlockFace.EAST));
		simpleBlock(main.getRelative(BlockFace.WEST));
		simpleBlock(main.getRelative(BlockFace.NORTH));
		simpleBlock(main.getRelative(BlockFace.SOUTH));
		simpleBlock(main.getRelative(BlockFace.SOUTH_EAST));
		simpleBlock(main.getRelative(BlockFace.SOUTH_WEST));
		simpleBlock(main.getRelative(BlockFace.NORTH_EAST));
		simpleBlock(main.getRelative(BlockFace.NORTH_WEST));
	}
	
	@SuppressWarnings("deprecation")
	private void simpleBlock(Block b) {
		if(b.isEmpty()) {
			b.setTypeId(78);
			returnAir(b.getLocation());
		}else if(b.isLiquid()){
			switch (b.getType()) {
			case WATER:
				b.setType(Material.FROSTED_ICE);
				break;
			case STATIONARY_WATER:
				b.setType(Material.FROSTED_ICE);
				break;
			default:
				break;
			}
		}
	}
	
	private void returnAir(Location loc) {
		Bukkit.getScheduler().runTaskLater(HamsterSystem.plugin, ()->{
			loc.getBlock().setType(Material.AIR);
		}, (int)random(80, 20));
	}
	
	private void playEffect(Location loc) {
		Bukkit.getScheduler().runTaskLaterAsynchronously(HamsterSystem.plugin, ()->{
			ParticleColor color = ParticleColor.getRGB(Color.white);	
			int particAmount = 30;
			
			for(int i=0;i<particAmount;i++) {
				Location point = new Location(loc.getWorld(), 
						random(loc.getX(), 1),random(loc.getY(), 1), random(loc.getZ(), 1));
				
				loc.getWorld().spawnParticle(Particle.REDSTONE, point,0,color.getRed(),color.getGreen(),color.getBlue());
			}
			
		}, 1);
	}
	
	private double random(double num1,double num2) {
		double num3 = num1-num2;
		num1+=num2;
		return Math.random()*(num3-num1)+num1;
	}
	

}
