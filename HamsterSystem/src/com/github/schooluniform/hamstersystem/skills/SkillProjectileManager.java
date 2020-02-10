package com.github.schooluniform.hamstersystem.skills;

import java.util.AbstractMap;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.ProjectileHitEvent;

import com.github.schooluniform.hamstersystem.entity.FightEntity;

public class SkillProjectileManager implements Listener{
	//ID 
	private static HashMap<Integer, Map.Entry<FightEntity, AbstractSkill>> projectiles = new HashMap<>();
	
	@EventHandler
	public void onHit(ProjectileHitEvent e) {
		if(projectiles.containsKey(e.getEntity().getEntityId())) {
			Map.Entry<FightEntity, AbstractSkill> entry = projectiles.get(e.getEntity().getEntityId());
			entry.getValue().onHit(e.getEntity().getLocation(),entry.getKey(),null);
			projectiles.remove(e.getEntity().getEntityId());
		}
	}
	
	@EventHandler
	public void onDamage(EntityDamageByEntityEvent e) {
		if(projectiles.containsKey(e.getDamager().getEntityId())) {
			Map.Entry<FightEntity, AbstractSkill> entry = projectiles.get(e.getEntity().getEntityId());
			if(entry == null)return;
			entry.getValue().onHit(e.getEntity().getLocation(),entry.getKey(),e.getEntity());
			projectiles.remove(e.getEntity().getEntityId());
			e.setCancelled(true);
		}
	}
	
	public static void removeProjectile(Projectile pro,int duration) {
		
	}
	
	public static void addProjectiles(Integer id,AbstractSkill skill,FightEntity caster) {
		if(projectiles.containsKey(id))return;
		projectiles.put(id, new AbstractMap.SimpleEntry<FightEntity,AbstractSkill>(caster, skill));
	}
}
