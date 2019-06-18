package com.github.schooluniform.hamstersystem.fightsystem;

import java.util.HashMap;
import java.util.LinkedList;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerPickupArrowEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.util.Vector;

import com.github.schooluniform.hamstersystem.HamsterSystem;
import com.github.schooluniform.hamstersystem.I18n;
import com.github.schooluniform.hamstersystem.data.Data;
import com.github.schooluniform.hamstersystem.fightsystem.base.BasicDamageData;
import com.github.schooluniform.hamstersystem.fightsystem.base.ProjectileType;
import com.github.schooluniform.hamstersystem.fightsystem.effect.BlastEffect;
import com.github.schooluniform.hamstersystem.fightsystem.effect.GasEffect;
import com.github.schooluniform.hamstersystem.fightsystem.effect.ImpactEffect;
import com.github.schooluniform.hamstersystem.util.Util;
import com.github.schooluniform.hamstersystem.weapon.Weapon;
import com.github.schooluniform.hamstersystem.weapon.WeaponAttribute;
import com.github.schooluniform.hamstersystem.weapon.WeaponLauncher;
import com.github.schooluniform.hamstersystem.weapon.WeaponTag;

public class ShootSystem implements Listener{
	private LinkedList<String> shootingCD = new LinkedList<>();
	private LinkedList<String> reloading = new LinkedList<>();
	private HashMap<Integer,BasicDamageData> onShooting = new HashMap<>();
	private HashMap<Integer,Integer> launcherAmount = new HashMap<>();
	private static HashMap<Integer,BasicDamageData> launchedProjectile = new HashMap<>();
	private LinkedList<Integer> swapItem = new LinkedList<>();
	
	@EventHandler
	public void onShooting(PlayerInteractEvent e){
		Player p = e.getPlayer();
		
		if(ImpactEffect.getImpactEntity().containsKey(p.getEntityId())){
			e.setCancelled(true);
			return;
		}
			
		//On Shooting
		if(e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK){
			if(!e.hasItem()) return;
			ItemStack weapon = e.getItem();
			if(isAmmo(weapon))return;
			if(!DamageSystem.isWeapon(weapon)) return;
			BasicDamageData bdd = DamageSystem.getBasicDamageData(p, weapon);
			if(!(bdd.getWeapon() instanceof WeaponLauncher)) return;
			if(Data.NBTTag.getInt(weapon, WeaponTag.HSCSize.name())<1) {
				I18n.senda(e.getPlayer(), "actionbar.fight.shoot.no-ammo");
				return;
			}
			e.setCancelled(true);
			
			WeaponLauncher launcher = (WeaponLauncher)bdd.getWeapon();
			
			String key1 = getCDKey(e.getHand(),launcher.getName(), p.getEntityId()),
					key2 = getReloadingKey(launcher.getName(), p.getEntityId());
			
			if(shootingCD.contains(key1))return;
			if(reloading.contains(key2)){
				I18n.senda(p, "actionbar.fight.shoot.reloading");
				return;
			}
			
			for(int i = 0;i<launcher.getLaunchAmount();i++){
				launchProjectile(p, launcher.getAttribute(WeaponAttribute.Precise_X),
						launcher.getAttribute(WeaponAttribute.Precise_Y), 
						launcher.getAttribute(WeaponAttribute.Precise_Z), 
						launcher.getAttribute(WeaponAttribute.Rate_of_Fire),
						launcher.getProjectileType());				
			}
			p.getLocation().add(p.getEyeLocation().getDirection().multiply(-1*launcher.getAttribute(WeaponAttribute.Recoil)));
			p.setVelocity(p.getEyeLocation().getDirection().multiply(-1*launcher.getAttribute(WeaponAttribute.Recoil)));
			
			p.playSound(p.getLocation(), launcher.getSound(),1.0F,1.0F);
			shootingCD.add(key1);
			onShooting.put(p.getEntityId(), bdd);
			double cd = launcher.getAttribute(WeaponAttribute.Attack_Interval);
			Bukkit.getScheduler().runTaskLater(HamsterSystem.plugin, new Runnable() {
				@Override
				public void run() {
					shootingCD.remove(key1);
				}
			},cd>(int)cd?(int)cd+1:(int)cd);
			launcherAmount.put(p.getEntityId(), 0);
			
			weapon = Data.NBTTag.addNBT(weapon, WeaponTag.HSCSize.name(), 
					Data.NBTTag.getInt(weapon, WeaponTag.HSCSize.name())-1);

			if(e.getHand() == EquipmentSlot.HAND){
				p.getInventory().setItemInMainHand(weapon);
			}else if(e.getHand() == EquipmentSlot.OFF_HAND){
				p.getInventory().setItemInOffHand(weapon);
			}
		}
	}
	
/*	@EventHandler
	public void onShooting(EntityInteractEvent e){
		if(e.getEntity() instanceof Player) return;
		if(!(e.getEntity() instanceof LivingEntity))return;
		
		LivingEntity le = (LivingEntity)e.getEntity();
		ItemStack weapon = e.getItem();
		if(!DamageSystem.isWeapon(weapon)) return;
	}*/
	
	@EventHandler
	public void reloadingClip(final PlayerSwapHandItemsEvent e){
		ItemStack weapon = e.getPlayer().getInventory().getItemInMainHand(),
				ammo = e.getPlayer().getInventory().getItemInOffHand();
		if(!DamageSystem.isWeapon(weapon) || !isAmmo(ammo))return;
		Weapon weaponData = DamageSystem.getWeapon(weapon);
		if(!(weaponData instanceof WeaponLauncher))return;
		String key = getReloadingKey(weaponData.getName(), e.getPlayer().getEntityId());
		if(reloading.contains(key)){
			e.setCancelled(true);
			return;
		}
		String linkTo = Data.NBTTag.getString(ammo, WeaponTag.HSWLT.name());
		int ammoSize = Data.NBTTag.getInt(ammo, WeaponTag.HSCSize.name());
		int basicSize = ammoSize;
		int weaponCSize = Data.NBTTag.getInt(weapon, WeaponTag.HSCSize.name());
		double reloadingTime = weaponData.getAttribute(WeaponAttribute.Reload_Speed);
		boolean consumable = Data.NBTTag.getBoolean(ammo, WeaponTag.Consumable.name());
		int amount = 1;
		reloadingTime = (reloadingTime>(int)reloadingTime?(int)reloadingTime+1:reloadingTime);
		System.out.println(ammoSize+" "+weaponCSize);
		if(!linkTo.contains(weaponData.getName())){
			I18n.senda(e.getPlayer(), "actionbar.fight.shoot.ammo-not-fit");
			return;
		}
		
		if(ammoSize <= 0){
			I18n.senda(e.getPlayer(), "actionbar.fight.shoot.reload-no-ammo");
			return;
		}
		
		e.setCancelled(true);
		
		while(ammoSize < weaponData.getAttribute(WeaponAttribute.Clip)-weaponCSize &&
				amount+1<=ammo.getAmount()){
			ammoSize+=basicSize;
			amount++;
		}
		
		final int ammoSizeTemp = ammoSize,
				amountTemp = amount;
		
		reloading.add(key);
		swapItem.remove((Integer)e.getPlayer().getEntityId());
		I18n.senda(e.getPlayer(), "actionbar.fight.shoot.reload", Util.formatNum(reloadingTime/20D, 2));
		Bukkit.getScheduler().runTaskLaterAsynchronously(HamsterSystem.plugin, ()->{
			if(!reloading.contains(key)){
				I18n.senda(e.getPlayer(), "actionbar.fight.shoot.reload-failed");
				return;
			}
			if(swapItem.contains(e.getPlayer().getEntityId())){
				reloading.remove(key);
				swapItem.remove((Integer)e.getPlayer().getEntityId());
				I18n.senda(e.getPlayer(), "actionbar.fight.shoot.reload-failed");
				return;
			}
			if(!DamageSystem.isWeapon(e.getPlayer().getInventory().getItemInMainHand())){
				reloading.remove(key);
				swapItem.remove((Integer)e.getPlayer().getEntityId());
				I18n.senda(e.getPlayer(), "actionbar.fight.shoot.reload-failed");
				return;
			}
			int ammoSize2,weaponCSize2;
			
			if(ammoSizeTemp + weaponCSize <= weaponData.getAttribute(WeaponAttribute.Clip)){
				ammoSize2 = 0;
				weaponCSize2 = ammoSizeTemp + weaponCSize;
			}else{
				ammoSize2 = ammoSizeTemp - (int)(weaponData.getAttribute(WeaponAttribute.Clip) - weaponCSize);
				weaponCSize2 = weaponData.getAttribute(WeaponAttribute.Clip).intValue();
			}
			
			ItemStack weapon2 = Data.NBTTag.addNBT(weapon, WeaponTag.HSCSize.name(), weaponCSize2);
			ItemStack ammo2 = null,ammo3 = null,ammo4 = null;
					//ammo3 = ammo.clone();
			System.out.println(amountTemp+"_"+ammoSize2);
			if(amountTemp == ammo.getAmount() &&ammoSize2 == 0 && consumable){
				ammo2 = null;
			}else if(amountTemp == ammo.getAmount()&& consumable){
				ammo2 = Data.NBTTag.addNBT(ammo, WeaponTag.HSCSize.name(), ammoSize2);
				ammo2.setAmount(1);
			}else if(amountTemp == ammo.getAmount()){
				ammo2 = Data.NBTTag.addNBT(ammo, WeaponTag.HSCSize.name(), ammoSize2);
				ammo2.setAmount(1);
				if(ammo.getAmount() != 1){
					ammo3 = Data.NBTTag.addNBT(ammo, WeaponTag.HSCSize.name(), ammoSize2);					
					ammo3.setAmount(amountTemp-1);
				}
			}else if(ammoSize2 == 0 && consumable){
				ammo2 = ammo.clone();
				ammo2.setAmount(ammo2.getAmount()-amountTemp);
			}else if(ammoSize2 == 0){
				ammo2 = ammo.clone();
				ammo2.setAmount(ammo2.getAmount()-amountTemp);
				ammo3 = Data.NBTTag.addNBT(ammo, WeaponTag.HSCSize.name(), ammoSize2);
				ammo3.setAmount(amountTemp);
			}else{
				ammo2 = Data.NBTTag.addNBT(ammo, WeaponTag.HSCSize.name(), ammoSize2);
				ammo2.setAmount(1);
				if(amountTemp-1 != 0){
					ammo3 = Data.NBTTag.addNBT(ammo, WeaponTag.HSCSize.name(), 0);					
					ammo3.setAmount(amountTemp-1);
				}
				if(ammo.getAmount() - amountTemp != 0){
					ammo4 = ammo2;
					ammo2 = ammo.clone();
					ammo2.setAmount(ammo2.getAmount()-amountTemp);					
				}
			}
			e.getPlayer().getInventory().setItemInMainHand(weapon2);
			e.getPlayer().getInventory().setItemInOffHand(ammo2);
			
			if(ammo3 != null){
				 if(!e.getPlayer().getInventory().addItem(ammo3).isEmpty())
					 e.getPlayer().getWorld().dropItem(e.getPlayer().getLocation(), ammo3);					 
			}
			if(ammo4 != null){
				 if(!e.getPlayer().getInventory().addItem(ammo4).isEmpty())
					 e.getPlayer().getWorld().dropItem(e.getPlayer().getLocation(), ammo4);					 
			}
			
			reloading.remove(key);
			I18n.senda(e.getPlayer(), "actionbar.fight.shoot.reload-succeed");
		}, (long)reloadingTime);
	}
	
	@EventHandler
	public void swapItem(PlayerItemHeldEvent e){
		if(!swapItem.contains(e.getPlayer().getEntityId())){
			swapItem.add(e.getPlayer().getEntityId());
		}
	}
	
	@EventHandler
	public void onLaunched(ProjectileLaunchEvent e){
		Projectile pro = e.getEntity();
		if(pro.getShooter() instanceof LivingEntity){
			int id = ((LivingEntity)pro.getShooter()).getEntityId();
			if(onShooting.containsKey(id)){
				BasicDamageData bdd = onShooting.get(id);
				launchedProjectile.put(pro.getEntityId(), bdd);
				pro.setGravity(((WeaponLauncher)bdd.getWeapon()).isGravity());
				if(((WeaponLauncher)bdd.getWeapon()).getProjectileType().isFire()){
					pro.setFireTicks(1000);
				}
				if(pro.getType() == EntityType.ARROW){
					pro.setMetadata("picup", new FixedMetadataValue(HamsterSystem.plugin, "no"));
				}
				launcherAmount.replace(id, launcherAmount.get(id)+1);
				Bukkit.getScheduler().runTaskLaterAsynchronously(HamsterSystem.plugin, ()->{
					launchedProjectile.remove(e.getEntity().getEntityId());
					e.getEntity().remove();				
				}, 600L);
				if(launcherAmount.get(id)>=((WeaponLauncher)bdd.getWeapon()).getLaunchAmount()){
					onShooting.remove(id);
					launcherAmount.remove(id);
				}
			}
		}
	}
	
	@EventHandler
	public void onProjectileHit(ProjectileHitEvent e){
		if(launchedProjectile.containsKey(e.getEntity().getEntityId())){
			BasicDamageData bdd = launchedProjectile.get(e.getEntity().getEntityId());
			triggerDamage(bdd, e.getEntity().getLocation());
			Bukkit.getScheduler().runTaskLaterAsynchronously(HamsterSystem.plugin, ()->{
				launchedProjectile.remove(e.getEntity().getEntityId());
				e.getEntity().remove();				
			}, 40L);
		}
	}
	
	@SuppressWarnings("deprecation")
	@EventHandler
	public void playerPicupPro(PlayerPickupArrowEvent e){
		if(e.getArrow().hasMetadata("picup"))
			e.setCancelled(true);
	}
	
	private boolean isAmmo(ItemStack ammo){
		if(ammo == null || ammo.getType() == Material.AIR){
			return false;
		}else{
			if(!Data.NBTTag.contantsNBT(ammo, WeaponTag.Consumable.name()))return false;
			if(!Data.NBTTag.contantsNBT(ammo, WeaponTag.HSCSize.name())) return false;
			if(!Data.NBTTag.contantsNBT(ammo, WeaponTag.HSWLT.name()))return false;
			return true;
		}
	}
	
	private void launchProjectile(Player p,double x,double y, double z,double ejectionSpeed,ProjectileType pt){
		Vector v = p.getEyeLocation().getDirection().multiply(ejectionSpeed)
				.add(new Vector(random(1D-x/100D),random(1D-y/100D),random(1D-z/100D)));
		p.launchProjectile(pt.getProjectileClass(), v);
	}
	
	private void triggerDamage(BasicDamageData bdd,Location l){
		if(bdd.getTriggerDamage() == null)return;
		switch (bdd.getTriggerDamage()) {
		case Blast:
			BlastEffect.blast(l, 0);
			break;
		case Gas:
			GasEffect.gas(l, bdd.getDamage()*0.35, 0);
			break;
		default:
			break;
		}
	}
	
	private double random(double number){
		double a = Math.random()*number;
		return Math.random()*100<50?a:-a;
	}
	
	private String getCDKey(EquipmentSlot hand,String weaponName,int entityID){
		return hand+"-"+weaponName+"-"+entityID;
	}
	private String getReloadingKey(String weaponName,int entityID){
		return weaponName+"-"+entityID;
	}
	
	public static HashMap<Integer,BasicDamageData> getLaunchedProjectile(){
		return launchedProjectile;
	}
}
