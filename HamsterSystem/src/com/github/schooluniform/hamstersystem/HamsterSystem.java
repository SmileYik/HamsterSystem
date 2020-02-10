package com.github.schooluniform.hamstersystem;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import com.github.schooluniform.hamstersystem.data.Data;
import com.github.schooluniform.hamstersystem.event.BasicEvent;
import com.github.schooluniform.hamstersystem.event.InventoryEvent;
import com.github.schooluniform.hamstersystem.fightsystem.DamageEvent;
import com.github.schooluniform.hamstersystem.fightsystem.ShootSystem;
import com.github.schooluniform.hamstersystem.gui.ModGui;
import com.github.schooluniform.hamstersystem.mod.Mod;
import com.github.schooluniform.hamstersystem.weapon.Weapon;
import com.github.schooluniform.hamstersystem.weapon.WeaponAttribute;
import com.github.schooluniform.hamstersystem.weapon.WeaponLauncher;
import com.github.schooluniform.hamstersystem.weapon.WeaponTag;

import java.io.File;

public class HamsterSystem extends JavaPlugin{
	public static HamsterSystem plugin;
	Object obj[] = new Object[10];
	//La vie est drole
	
	@Override
	public void onEnable() {
		plugin = this;
		firstLoad();
		if(!Data.init(getConfig()))return;

		addOnlinePlayer();
		
		this.getServer().getPluginManager().registerEvents(new ShootSystem(), this);
		this.getServer().getPluginManager().registerEvents(new DamageEvent(), this);
		this.getServer().getPluginManager().registerEvents(new BasicEvent(), this);
		this.getServer().getPluginManager().registerEvents(new InventoryEvent(), this);
		//this.getServer().getPluginManager().registerEvents(new SkillProjectileManager(), this);
		//this.getServer().getPluginManager().registerEvents(new SkillManager(), this);
		//this.getServer().getScheduler().runTaskTimerAsynchronously(this, new FightSystem(),0,20);
		//this.getServer().getScheduler().runTaskTimerAsynchronously(this, new HealTask(),0,20);
		
		this.getLogger().info(I18n.tr("on-enable.print-info"));
		
	}
	
	
	private void firstLoad(){
		if(!new File(getDataFolder()+File.separator+"playerdata").exists())
			new File(getDataFolder()+File.separator+"playerdata").mkdirs();
		if(!new File(getDataFolder()+File.separator+"weapon").exists())
			new File(getDataFolder()+File.separator+"weapon").mkdir();
		if(!new File(getDataFolder()+File.separator+"mod").exists())
			new File(getDataFolder()+File.separator+"mod").mkdir();
		if(!new File(getDataFolder()+File.separator+"mob").exists())
			new File(getDataFolder()+File.separator+"mob").mkdir();
		if(!new File(getDataFolder()+File.separator+"spawner").exists())
			new File(getDataFolder()+File.separator+"spawner").mkdir();
		if(!new File(getDataFolder()+File.separator+"config.yml").exists())
			saveDefaultConfig();
		try{reloadConfig();}catch (Exception e){}
	}
	
	private void addOnlinePlayer(){
		for(Player p : getServer().getOnlinePlayers())
			Data.addPlayer(p.getName());
	}
	
	
	public static Player getPlayer(String playerName) {
		return HamsterSystem.getPlayer(playerName);
	}
	
	@Override
	public void onDisable() {
		for(Player p : getServer().getOnlinePlayers())
			Data.removePlayer(p.getName());
	}
	
	private String[] getHelpCommand(String command,boolean admin) {
		if(admin) {
			return new String[] {
					"=========Hamster System=========",
					"/"+command+" admin help - "+I18n.tr("command.des.2.admin-help"),
					"/"+command+" setWeapon <WeaponName> - "+I18n.tr("command.des.2.setWeapon"),
					"/"+command+" setMod <ModName> - "+I18n.tr("command.des.2.setMod"),
					"/"+command+" setAmmo <WeaponName> <Size> <true|false>- "+I18n.tr("command.des.2.setMod"),
					"=========Hamster System=========",
			};
		}else {
			return new String[] {
					"=========Hamster System=========",
					"/"+command+" oemg - "+I18n.tr("command.des.1.oemg"),
					"/"+command+" owmg - "+I18n.tr("command.des.1.owmg"),
					"/"+command+" admin help - "+I18n.tr("command.des.2.admin-help"),
					"=========Hamster System========="
			};
		}
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if(!(label.equalsIgnoreCase("HamsterSystem")||label.equalsIgnoreCase("HS")))return true;
		
		switch (args.length) {
			case 1:{
				if(!(sender instanceof Player)) {
					sender.sendMessage(I18n.tr("command.only-player"));
					return true;
				}
				Player p = (Player) sender;
				if(args[0].equalsIgnoreCase("oemg") 
							&& (p.hasPermission("HamsterSystem.Command.oemg")
							|| p.hasPermission("HamsterSystem.Command.*")
							|| p.isOp())) {
					//Open Entity Mod Gui
					ModGui.openGUIEntity(p);
					return true;
				}else if(args[0].equalsIgnoreCase("owmg")
							&& (p.hasPermission("HamsterSystem.Command.owmg")
							|| p.hasPermission("HamsterSystem.Command.*")
							|| p.isOp())) {
					//Open Weapon Mod Gui
					ModGui.openGuiWeapon(p);
					return true;
				}
				break;
			}case 2:{
				if(!(sender instanceof Player)) {
					sender.sendMessage(I18n.tr("command.only-player"));
					return true;
				}
				Player p = (Player) sender;
				if(args[0].equalsIgnoreCase("admin")
					&& args[1].equalsIgnoreCase("help")
							&& (p.hasPermission("HamsterSystem.Command.Admin.*")
							|| p.hasPermission("HamsterSystem.Command.Admin.Help")
							|| p.isOp())) {
					p.sendMessage(getHelpCommand(label, true));
					return true;
				}else if(args[0].equalsIgnoreCase("setMod")
							&& (p.hasPermission("HamsterSystem.Command.Admin.*")
							|| p.hasPermission("HamsterSystem.Command.Admin.setmod")
							|| p.isOp())) {
					if(!Data.contansMod(args[1])){
						sender.sendMessage(I18n.tr("command.mod-is-not-find", args[1]));
						return true;
					}else if(p.getInventory().getItemInMainHand() == null || p.getInventory().getItemInMainHand().getType() == Material.AIR){
						sender.sendMessage(I18n.tr("command.nothing-in-your-hand"));
						return true;
					}else {
						Mod modData = Data.getMod(args[1]);
						ItemStack mod = modData.getItem(0, 0, modData.getPolarity().getType());
						p.getInventory().setItemInMainHand(mod);
						return true;
					}
					
				}else if(args[0].equalsIgnoreCase("setWeapon")
						&& (p.hasPermission("HamsterSystem.Command.Admin.*")
						|| p.hasPermission("HamsterSystem.Command.Admin.setweapon")
						|| p.isOp())) {
					if(!Data.contansWeapon(args[1])){
						sender.sendMessage(I18n.tr("command.weapon-is-not-find", args[1]));
						return true;
					}else if(p.getInventory().getItemInMainHand() == null || p.getInventory().getItemInMainHand().getType() == Material.AIR){
						sender.sendMessage(I18n.tr("command.nothing-in-your-hand"));
						return true;
					}else {
						Weapon w = Data.getWeapon(args[1]);
						ItemStack weapon = p.getInventory().getItemInMainHand();
						weapon = Data.NBTTag.addNBT(weapon, WeaponTag.HSWLT.name(), args[1]);
						weapon = Data.NBTTag.addNBT(weapon, WeaponTag.HSWEL.name(), new int[]{0,0});
						if(w instanceof WeaponLauncher){
							weapon = Data.NBTTag.addNBT(weapon, WeaponTag.HSWClip.name(), w.getAttribute(WeaponAttribute.Clip).intValue());
							weapon = Data.NBTTag.addNBT(weapon, WeaponTag.HSCSize.name(), 0);							
						}
						p.getInventory().setItemInMainHand(w.getLore(weapon));
						return true;
					}
				}
			}case 4:{
				if(!(sender instanceof Player)) {
					sender.sendMessage(I18n.tr("command.only-player"));
					return true;
				}
				Player p = (Player) sender;
				if(args[0].equalsIgnoreCase("setammo") 
							&& (p.hasPermission("HamsterSystem.Command.Admin.*")
							|| p.hasPermission("HamsterSystem.Command.Admin.setammo")
							|| p.isOp())) {
					if(p.getInventory().getItemInMainHand() == null || p.getInventory().getItemInMainHand().getType() == Material.AIR){
						sender.sendMessage(I18n.tr("command.nothing-in-your-hand"));
						return true;
					}
					ItemStack ammo = p.getInventory().getItemInMainHand();
					if(ShootSystem.isAmmo(ammo)) {
						String linkTo = Data.NBTTag.getString(ammo, WeaponTag.HSWLT.name());
						ammo = Data.NBTTag.addNBT(ammo, WeaponTag.HSWLT.name(), linkTo+";"+args[1]);
					}else {
						ammo = Data.NBTTag.addNBT(ammo, WeaponTag.HSWLT.name(), args[1]);
						ammo = Data.NBTTag.addNBT(ammo, WeaponTag.HSCSize.name(), Integer.parseInt(args[2]));
						ammo = Data.NBTTag.addNBT(ammo, WeaponTag.Consumable.name(), Boolean.parseBoolean(args[3]));						
					}
					p.getInventory().setItemInMainHand(ShootSystem.setAmmoLore(ammo));
					return true;
			   }
			}default:{
				sender.sendMessage(getHelpCommand(label, false));
				return true;
			}
		}
		return false;
	}
	
	
}
