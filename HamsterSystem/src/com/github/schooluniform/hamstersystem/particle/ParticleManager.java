package com.github.schooluniform.hamstersystem.particle;

import java.awt.Color;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Player;

import com.github.schooluniform.hamstersystem.HamsterSystem;

public class ParticleManager {
	public static void test(Player p) {
		ParticleColor color = ParticleColor.getRGB(Color.BLUE);
		Location loc = p.getLocation();
		
		Bukkit.getScheduler().runTaskTimerAsynchronously(HamsterSystem.plugin, ()->{
			double radius = 1.5;
			double particAmount = 120;
			for(int angle = 0;angle<360;angle+=360/particAmount) {
				double x = radius * Math.cos(Math.toRadians(angle));
				double z = radius * Math.sin(Math.toRadians(angle));
				Location point = loc.clone().add(x, 0, z);
				point.getWorld().spawnParticle(Particle.REDSTONE, point,0,color.getRed(),color.getGreen(),color.getBlue());
			}
		}, 0, 20);
		
		
		
	}
	
	public static void test2(Player p) {
		ParticleColor color = ParticleColor.getRGB(Color.BLUE);
		Location loc = p.getLocation();
		double radius = 1.5;
		double particAmount = 30*4;
		
		Bukkit.getScheduler().runTaskTimerAsynchronously(HamsterSystem.plugin, ()->{
			for(int angle = 0;angle<360;angle+=360/particAmount) {
				double x = radius * Math.cos(Math.toRadians(angle))*Math.cos(Math.toRadians(loc.getYaw()));
				double z = radius * Math.cos(Math.toRadians(angle))*Math.sin(Math.toRadians(loc.getYaw()));
				double y = radius * Math.sin(Math.toRadians(angle));
				
				Location point = loc.clone().add(x, y, z);
				point.getWorld().spawnParticle(Particle.REDSTONE, point,0,color.getRed(),color.getGreen(),color.getBlue());
			}
		}, 0, 20);
		
		
	}
	
	public static void test3(Player p) {
		ParticleColor color = ParticleColor.getRGB(Color.BLUE);
		Bukkit.getScheduler().runTaskTimerAsynchronously(HamsterSystem.plugin, ()->{
			Location loc = p.getLocation();
			double radius = 1.5;
			double particAmount = 30*4;
			double officeXZ = 3;
			loc = loc.add(officeXZ*Math.sin(Math.toRadians(360-loc.getYaw())),0,officeXZ*Math.cos(Math.toRadians(360-loc.getYaw())));
			for(int angle = 0;angle<360;angle+=360/particAmount) {
				double x = radius * Math.cos(Math.toRadians(angle))*Math.cos(Math.toRadians(loc.getYaw()));
				double z = radius * Math.cos(Math.toRadians(angle))*Math.sin(Math.toRadians(loc.getYaw()));
				double y = radius * Math.sin(Math.toRadians(angle));
				
				Location point = loc.clone().add(x, y, z);
				point.getWorld().spawnParticle(Particle.REDSTONE, point,0,color.getRed(),color.getGreen(),color.getBlue());
			}
		}, 0, 20);
		
		
	}
	
	public static void test5(Player p) {
		ParticleColor color = ParticleColor.getRGB(Color.ORANGE);
		Bukkit.getScheduler().runTaskTimerAsynchronously(HamsterSystem.plugin, ()->{
			Location loc = p.getLocation();
			double officeXZ = 0.5;
			double officeY = p.getEyeHeight()/2;
			loc = loc.add(officeXZ*Math.sin(Math.toRadians(270-loc.getYaw())),
					officeY,
					officeXZ*Math.cos(Math.toRadians(270-loc.getYaw())));
			for(int i = 0;i<5;i++) {
				
				loc.getWorld().spawnParticle(Particle.REDSTONE, loc,0,color.getRed(),color.getGreen(),color.getBlue());
			}
			
		}, 0, 1);
		
		
	}
	
	public static void test4(Player p) {
		
		
		Bukkit.getScheduler().runTaskTimerAsynchronously(HamsterSystem.plugin, new Runnable() {
			double officeXZ = 0;
			@Override
			public void run() {
				ParticleColor color = ParticleColor.getRGB(Color.ORANGE);
				Location loc = p.getLocation();
				double radius = 1.5;
				double particAmount = 30*4;
				loc = loc.add(officeXZ*Math.sin(Math.toRadians(360-loc.getYaw())),p.getEyeHeight()/1.5,officeXZ*Math.cos(Math.toRadians(360-loc.getYaw())));
				
				for(int angle = 270;angle<360;angle+=450/particAmount) {
					double x = radius * Math.cos(Math.toRadians(angle));
					double z = radius * Math.sin(Math.toRadians(angle));
					Location point = loc.clone().add(x, 0, z);
					point.getWorld().spawnParticle(Particle.REDSTONE, point,0,color.getRed(),color.getGreen(),color.getBlue());
				}
				officeXZ++;
				
			}
		}, 0, 10);
		
	}
}
