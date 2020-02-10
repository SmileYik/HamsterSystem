package com.github.schooluniform.hamstersystem.entity.mob;

import java.util.HashMap;

import com.github.schooluniform.hamstersystem.HamsterSystem;

public class MobSpawnerManager {
	
	private static HashMap<String,MobSpawner> spawners = new HashMap<>();
	private static HashMap<String,Integer> spawnersTimer=new HashMap<>();
	
	
	public static void add(MobSpawner ms){
		spawners.put(ms.getId(), ms);
		spawnersTimer.put(ms.getId(), HamsterSystem.plugin.getServer().getScheduler().runTaskTimer(HamsterSystem.plugin, ms, 0, ms.getSpawnSpeed()).getTaskId());
	}
	
	public static void mobDeath(String spawnerNmae){
		if(spawners.containsKey(spawnerNmae))
			spawners.get(spawnerNmae).subAmount(1);
	}
	
	public static void stopSpawner(String spanwerName){
		if(spawnersTimer.containsKey(spanwerName))
			HamsterSystem.plugin.getServer().getScheduler().cancelTask(spawnersTimer.get(spanwerName));
	}
}
