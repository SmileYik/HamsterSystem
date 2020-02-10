package com.github.schooluniform.hamstersystem.particle;

import java.awt.Color;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.LivingEntity;

import com.github.schooluniform.hamstersystem.HamsterSystem;

public class ParticleFight {
	public static void playHealShield(LivingEntity entity) {
		if(entity == null || !entity.isValid()) {
			return;
		}
		Bukkit.getScheduler().runTaskLaterAsynchronously(HamsterSystem.plugin, ()->{
			ParticleColor color = ParticleColor.getRGB(Color.BLUE);
			Location loc = entity.getLocation().add(0,entity.getEyeHeight()/2,0);		
			double radius = 0.8;
			double particAmount = 15;
			for(int angle = 0;angle<360;angle+=360/particAmount) {
				double x = radius * Math.cos(Math.toRadians(angle));
				double z = radius * Math.sin(Math.toRadians(angle));
				Location point = loc.clone().add(x, 0, z);
				point.getWorld().spawnParticle(Particle.REDSTONE, point,0,color.getRed(),color.getGreen(),color.getBlue());
			}
		}, 1);
	}
	public static void playDamageHealth(LivingEntity entity) {
		if(entity == null || !entity.isValid()) {
			return;
		}
		Bukkit.getScheduler().runTaskLaterAsynchronously(HamsterSystem.plugin, ()->{
			ParticleColor color = ParticleColor.getRGB(Color.RED);
			Location loc = entity.getLocation().add(0,entity.getEyeHeight()/2,0);		
			double radius = 0.8;
			double particAmount = 15;
			for(int angle = 0;angle<360;angle+=360/particAmount) {
				double x = radius * Math.cos(Math.toRadians(angle));
				double z = radius * Math.sin(Math.toRadians(angle));
				Location point = loc.clone().add(x, 0, z);
				point.getWorld().spawnParticle(Particle.REDSTONE, point,0,color.getRed(),color.getGreen(),color.getBlue());
			}
		}, 1);
	}
	
	public static void playGunFire(LivingEntity entity) {
		if(entity == null || !entity.isValid()) {
			return;
		}
		Bukkit.getScheduler().runTaskLaterAsynchronously(HamsterSystem.plugin, ()->{
			ParticleColor color = ParticleColor.getRGB(Color.ORANGE);
			Location loc = entity.getLocation().add(0,entity.getEyeHeight()/2,0);		
			double radius = 0.8;
			double particAmount = 15;
			for(int angle = 0;angle<360;angle+=360/particAmount) {
				double x = radius * Math.cos(Math.toRadians(angle));
				double z = radius * Math.sin(Math.toRadians(angle));
				Location point = loc.clone().add(x, 0, z);
				point.getWorld().spawnParticle(Particle.REDSTONE, point,0,color.getRed(),color.getGreen(),color.getBlue());
			}
		}, 1);
	}
}
