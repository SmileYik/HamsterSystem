package com.github.schooluniform.hamstersystem.fightsystem;

import java.util.AbstractMap;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;

import com.github.schooluniform.hamstersystem.I18n;
import com.github.schooluniform.hamstersystem.entity.EntityAttribute;
import com.github.schooluniform.hamstersystem.entity.FightEntity;
import com.github.schooluniform.hamstersystem.fightsystem.base.BasicDamageData;
import com.github.schooluniform.hamstersystem.fightsystem.effect.ImpactEffect;
import com.github.schooluniform.hamstersystem.fightsystem.effect.PunctureEffect;
import com.github.schooluniform.hamstersystem.util.Util;
import com.github.schooluniform.hamstersystem.weapon.Weapon;
import com.github.schooluniform.hamstersystem.weapon.WeaponAttribute;
import com.github.schooluniform.hamstersystem.weapon.WeaponLauncher;
import com.github.schooluniform.hamstersystem.weapon.WeaponMelee;

public class DamageEvent implements Listener{
	//ID Damage Enegry
	private static HashMap<Integer,Map.Entry<Double, Double>> powerEnergy = new HashMap<>();
	
	
	@EventHandler
	public void onEntityFight(EntityDamageByEntityEvent e){
		if(!(e.getEntity() instanceof LivingEntity))return;
		
		FightEntity defander = FightSystem.fight((LivingEntity)e.getEntity());
		LivingEntity damager = null;
		
		if(defander == null)return;
		
		if(e.getDamager() instanceof LivingEntity){
			damager = (LivingEntity)e.getDamager();
			ItemStack weapon = damager.getEquipment().getItemInMainHand();
			if(ImpactEffect.getImpactEntity().containsKey(damager.getEntityId())){
				e.setCancelled(true);
				return;
			}
			
			if(!DamageSystem.isWeapon(weapon)){
				System.out.println("a");
				e.setDamage(getFinalDamage(e.getDamage(), defander));
				return;
			}
			BasicDamageData bdd = DamageSystem.getBasicDamageData(damager, weapon);
			if(bdd.getWeapon() instanceof WeaponLauncher) {
				e.setDamage(getFinalDamage(e.getDamage(), defander));
				return;
			}
			
			//Power Enegry
			if(powerEnergy.containsKey(damager.getEntityId())){
				Map.Entry<Double, Double> entry = powerEnergy.get(damager.getEntityId());
				FightEntity damagerData = FightSystem.fight(damager);
				if(damagerData.getEnergy()<entry.getValue()){
					I18n.senda(damager, "actionbar.fight.melee.no-enegry");
					powerEnergy.remove(damager.getEntityId());
				}else{
					bdd.setDamage(bdd.getDamage()*entry.getKey().doubleValue());
					damagerData.modifyEnergy(-entry.getValue());
				}
			}
			//Puncture
			bdd.setDamage(bdd.getDamage()*(1-PunctureEffect.getPunctureEffect(damager.getEntityId())));
			
			DamageSystem.triggerDamage(bdd,defander);
			double finalDamage = getFinalDamage(bdd.getDamageWithCirt(), defander);
			e.setDamage(finalDamage);
		}else{
			if(ShootSystem.getLaunchedProjectile().containsKey(e.getDamager().getEntityId())){
				BasicDamageData bdd = ShootSystem.getLaunchedProjectile().get(e.getDamager().getEntityId());
				damager = bdd.getAttacker();
				
				if(ImpactEffect.getImpactEntity().containsKey(bdd.getAttacker().getEntityId())){
					e.setCancelled(true);
					return;
				}
				//Puncture
				bdd.setDamage(bdd.getDamage()*(1-PunctureEffect.getPunctureEffect(bdd.getAttacker().getEntityId())));
				
				double finalDamage = getFinalDamage(bdd.getDamageWithCirt(), defander);
				DamageSystem.triggerDamage(bdd,defander);
				e.setDamage(finalDamage);
				ShootSystem.getLaunchedProjectile().remove(e.getDamager().getEntityId());
			}
		}
		e.setDamage(getFinalDamage(e.getDamage(), defander));
		DamageSystem.sendHealth(damager, defander);
	}
	
	@EventHandler
	public void onDeath(EntityDeathEvent e){
		FightSystem.leaveFight(e.getEntity().getEntityId());
		HealTask.stopHeal(e.getEntity().getEntityId());
		powerEnergy.remove(e.getEntity().getEntityId());
	}
	
	@EventHandler
	public void environmentDamage(EntityDamageEvent e){
		if(!(e.getEntity() instanceof LivingEntity))
			return;
		FightEntity defander = FightSystem.fight((LivingEntity)e.getEntity());
		if(defander == null)
			return;
		
		switch (e.getCause()) {
		case BLOCK_EXPLOSION:
			e.setDamage(getFinalDamage(e.getDamage(), defander));
			break;
		case CONTACT:
			e.setDamage(e.getDamage()/defander.getAttribute(EntityAttribute.Armor));
			break;
		case CRAMMING:
			e.setDamage(e.getDamage()/defander.getAttribute(EntityAttribute.Armor));
			break;
		case DRAGON_BREATH:
			e.setDamage(getFinalDamage(e.getDamage(), defander));
			break;
		case DROWNING:
			e.setDamage(getFinalDamage(e.getDamage(), defander));
			break;
		case FALL:
			e.setDamage(getFinalDamage(e.getDamage(), defander));
			break;
		case FALLING_BLOCK:
			e.setDamage(getFinalDamage(e.getDamage(), defander));
			break;
		case FIRE:
			e.setDamage(getFinalDamage(e.getDamage(), defander));
			break;
		case FIRE_TICK:
			e.setDamage(getFinalDamage(e.getDamage(), defander));
			break;
		case FLY_INTO_WALL:
			e.setDamage(getFinalDamage(e.getDamage(), defander));
			break;
		case HOT_FLOOR:
			e.setDamage(getFinalDamage(e.getDamage(), defander));
			break;
		case LAVA:
			e.setDamage(getFinalDamage(e.getDamage(), defander));
			break;
		case LIGHTNING:
			e.setDamage(getFinalDamage(e.getDamage(), defander));
			break;
		case MAGIC:
			e.setDamage(getFinalDamage(e.getDamage(), defander));
			break;
		case MELTING:
			e.setDamage(getFinalDamage(e.getDamage(), defander));
			break;
		case POISON:
			e.setDamage(e.getDamage()/defander.getAttribute(EntityAttribute.Armor));
			break;
		case SUFFOCATION:
			e.setDamage(getFinalDamage(e.getDamage(), defander));
			break;
		case THORNS:
			e.setDamage(getFinalDamage(e.getDamage(), defander));
			break;
		case VOID:
			e.setDamage(getFinalDamage(e.getDamage(), defander));
			break;
		case WITHER:
			e.setDamage(e.getDamage()/defander.getAttribute(EntityAttribute.Armor));
			break;
		default:
			break;
		}
	}

	@EventHandler
	public void onPlayerMove(PlayerMoveEvent e){
		if(ImpactEffect.getImpactEntity().containsKey(e.getPlayer().getEntityId())){
				e.setCancelled(true);
		}
	}
	
	@EventHandler
	public void onPlayerRightClickWeapon(PlayerInteractEvent e){
		if((e.getAction() == Action.RIGHT_CLICK_AIR || 
				e.getAction() == Action.RIGHT_CLICK_BLOCK) &&
				e.hasItem() && DamageSystem.isWeapon(e.getItem())){
			Weapon weapon = DamageSystem.getWeapon(e.getItem());
			if(!(weapon instanceof WeaponMelee))return;
			e.setCancelled(true);
			WeaponMelee melee= (WeaponMelee ) weapon;
			if(powerEnergy.containsKey(e.getPlayer().getEntityId())){
				powerEnergy.remove(e.getPlayer().getEntityId());
				I18n.senda(e.getPlayer(),"actionbar.fight.melee.cancel-power-enegry");
			}else{
				powerEnergy.put(e.getPlayer().getEntityId(), 
						new AbstractMap.SimpleEntry<Double,Double>(melee.getAttribute(WeaponAttribute.Charging_Damage),melee.getAttribute(WeaponAttribute.Charging_Efficiency)));
				I18n.senda(e.getPlayer(), "actionbar.fight.melee.power-enegry", Util.formatNum(melee.getAttribute(WeaponAttribute.Charging_Damage), 2));
			}
		}
	}
	
	@EventHandler
	public void onPlayerSwapItem(PlayerItemHeldEvent e){
		if(powerEnergy.containsKey(e.getPlayer().getEntityId())){
			powerEnergy.remove(e.getPlayer().getEntityId());
			I18n.senda(e.getPlayer(), "actionbar.fight.melee.cancel-power-enegry");
		}
	}
	
	private double getFinalDamage(double damage,FightEntity defander){
		double finalDamage;
		if(damage>defander.getShield()){
			finalDamage = (damage-defander.getShield())/defander.getAttribute(EntityAttribute.Armor);
			defander.setShield(0);
		}else{
			finalDamage = 0;
			defander.modifyShield(-damage);
		}
		return finalDamage;
	}
}
