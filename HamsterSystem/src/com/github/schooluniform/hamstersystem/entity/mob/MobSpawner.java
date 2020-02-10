package com.github.schooluniform.hamstersystem.entity.mob;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Location;

import com.github.schooluniform.hamstersystem.data.Data;

public class MobSpawner implements Runnable{
	private String id;
	private Location loc1,loc2;
	private int maxMob;
	private int spawnSpeed;
	private HashMap<String,Double> mobs =  new HashMap<>();
	
	private int amount = 0;
	
	@Override
	public void run() {
		spawnMob();
	}
	
	private void spawnMob() {
		if(amount>=maxMob) {
			return;
		}
		Location loc = getLocation();
		for(Map.Entry<String, Double> entry : mobs.entrySet()) {
			if(Math.random()*100<entry.getValue()) {
				Mob mob = Data.getMob(entry.getKey());
				mob.spawn(loc,id);
				amount++;
			}
		}
	}
	
	private Location getLocation() {
		Location loc = new Location(loc1.getWorld(),
				random(loc1.getX(),loc2.getX()), 
				random(loc1.getY(), loc2.getY()), 
				random(loc1.getZ(), loc2.getZ()));
		if(checkLocation(loc)){
			loc = fixLocation(loc);
			if(loc==null)return getLocation();
			return loc;
		}
		return getLocation();
		
	}
	
	private boolean checkLocation(Location l){
		if(!l.clone().add(0, 1, 0).getBlock().isEmpty())return false;
		return true;
	}
	
	private Location fixLocation(Location l){
		while(true){
			if(l.clone().add(0,-1,0).getBlock().isEmpty()){
				l.add(0, -1, 0);
			}else if((l.getY()>=loc1.getY()&&l.getY()<=loc2.getY())||(l.getY()<=loc1.getY()&&l.getY()>=loc2.getY())){
				return l;
			}else{
				return null;
			}
		}
	}
	
	private double random(double num1,double num2) {
		return Math.random()*Math.abs((num2-num1))+Math.min(num1, num2);
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Location getLoc1() {
		return loc1;
	}

	public void setLoc1(Location loc1) {
		this.loc1 = loc1;
	}

	public Location getLoc2() {
		return loc2;
	}

	public void setLoc2(Location loc2) {
		this.loc2 = loc2;
	}

	public int getMaxMob() {
		return maxMob;
	}

	public void setMaxMob(int maxMob) {
		this.maxMob = maxMob;
	}

	public int getSpawnSpeed() {
		return spawnSpeed;
	}

	public void setSpawnSpeed(int spawnSpeed) {
		this.spawnSpeed = spawnSpeed;
	}

	public HashMap<String, Double> getMobs() {
		return mobs;
	}

	public void setMobs(HashMap<String, Double> mobs) {
		this.mobs = mobs;
	}
	
	
	public void subAmount(int amount) {
		this.amount-=amount;
	}
	
	
}
