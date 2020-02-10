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
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;

import com.github.schooluniform.hamstersystem.I18n;
import com.github.schooluniform.hamstersystem.data.Data;
import com.github.schooluniform.hamstersystem.entity.EntityAttribute;
import com.github.schooluniform.hamstersystem.entity.FightEntity;
import com.github.schooluniform.hamstersystem.entity.mob.Mob;
import com.github.schooluniform.hamstersystem.fightsystem.base.BasicDamageData;
import com.github.schooluniform.hamstersystem.fightsystem.effect.ImpactEffect;
import com.github.schooluniform.hamstersystem.fightsystem.effect.PunctureEffect;
import com.github.schooluniform.hamstersystem.particle.ParticleFight;
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
				DamageSystem.sendHealth(damager, defander);
				return;
			}
			
			if(!DamageSystem.isWeapon(weapon)){
				e.setDamage(getFinalDamage(e.getDamage(), defander));
				DamageSystem.sendHealth(damager, defander);
				return;
			}
			BasicDamageData bdd = DamageSystem.getBasicDamageData(damager, weapon);
			if(bdd.getWeapon() instanceof WeaponLauncher) {
				e.setDamage(getFinalDamage(e.getDamage(), defander));
				DamageSystem.sendHealth(damager, defander);
				return;
			}
			
			//Power Enegry
			if(powerEnergy.containsKey(damager.getEntityId())){
				Map.Entry<Double, Double> entry = powerEnergy.get(damager.getEntityId());
				FightEntity damagerData = FightSystem.fight(damager);
				if(damagerData.getEnergy()<entry.getValue()){
					I18n.senda(damager, "actionbar.fight.melee.no-energy");
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
			if(finalDamage > 0) {
				ParticleFight.playDamageHealth(defander.getEntity());
			}
			e.setDamage(finalDamage);
		}else{
			if(ShootSystem.getLaunchedProjectile().containsKey(e.getDamager().getEntityId())){
				BasicDamageData bdd = ShootSystem.getLaunchedProjectile().get(e.getDamager().getEntityId());
				damager = bdd.getAttacker();
				
				if(ImpactEffect.getImpactEntity().containsKey(bdd.getAttacker().getEntityId())){
					e.setCancelled(true);
					DamageSystem.sendHealth(damager, defander);
					return;
				}
				//Puncture
				bdd.setDamage(bdd.getDamage()*(1-PunctureEffect.getPunctureEffect(bdd.getAttacker().getEntityId())));
				
				double finalDamage = getFinalDamage(bdd.getDamageWithCirt(), defander);
				DamageSystem.triggerDamage(bdd,defander);
				if(finalDamage > 0) {
					ParticleFight.playDamageHealth(defander.getEntity());
				}
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
		
		if(e.getEntity().hasMetadata("HamsterSystemMob")) {
			String mobName = e.getEntity().getMetadata("HamsterSystemMob").get(0).asString();
			Mob mob = Data.getMob(mobName);
			if(mob!=null) {
				e.getDrops().addAll(mob.getDrops());
			}
		}
	}
	
	@EventHandler
	public void onPickupItem(EntityPickupItemEvent e){
		int itemEnergy = 25;
		ItemStack item = e.getItem().getItemStack();
		if(Data.NBTTag.contantsNBT(item, "HS-ENERGY")) {
			e.setCancelled(true);
			FightEntity entity = FightSystem.fight(e.getEntity());
			double needEnergy = entity.getAttribute(EntityAttribute.Energy)-entity.getEnergy();
			if(needEnergy<=0) {
				return;
			}else{
				int needItem = (int) (needEnergy/itemEnergy);
				int amount;
				if(needEnergy % itemEnergy != 0) {
					needItem++;
				}
				if(item.getAmount()<=needItem) {
					amount = item.getAmount();
					entity.modifyEnergy(itemEnergy*amount);
					e.getItem().remove();
				}else {
					amount = item.getAmount()-needItem;
					item.setAmount(amount);
					e.getItem().setItemStack(item);
				}
				I18n.senda(e.getEntity(), "actionbar.fight.pickup-energy-item",itemEnergy*amount);
			}
		}
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
		defander.getUpdateSign();
	}

	@EventHandler
	public void onPlayerMove(PlayerMoveEvent e){
		if(ImpactEffect.getImpactEntity().containsKey(e.getPlayer().getEntityId())){
				e.setCancelled(true);
		}
	}
	
	@EventHandler
	public void onPlayerRightClickMelee(PlayerInteractEvent e){
		if((e.getAction() == Action.RIGHT_CLICK_AIR || 
				e.getAction() == Action.RIGHT_CLICK_BLOCK) &&
				e.hasItem() && DamageSystem.isWeapon(e.getItem())){
			Weapon weapon = DamageSystem.getWeapon(e.getItem());
			if(!(weapon instanceof WeaponMelee))return;
			e.setCancelled(true);
			WeaponMelee melee= (WeaponMelee ) weapon;
			if(powerEnergy.containsKey(e.getPlayer().getEntityId())){
				powerEnergy.remove(e.getPlayer().getEntityId());
				I18n.senda(e.getPlayer(),"actionbar.fight.melee.cancel-power-energy");
			}else{
				powerEnergy.put(e.getPlayer().getEntityId(), 
						new AbstractMap.SimpleEntry<Double,Double>(melee.getAttribute(WeaponAttribute.Charging_Damage),melee.getAttribute(WeaponAttribute.Charging_Efficiency)));
				I18n.senda(e.getPlayer(), "actionbar.fight.melee.power-energy", Util.formatNum(melee.getAttribute(WeaponAttribute.Charging_Damage), 2));
			}
		}
	}
	
	@EventHandler
	public void onPlayerSwapItem(PlayerItemHeldEvent e){
		if(powerEnergy.containsKey(e.getPlayer().getEntityId())){
			powerEnergy.remove(e.getPlayer().getEntityId());
			I18n.senda(e.getPlayer(), "actionbar.fight.melee.cancel-power-energy");
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
