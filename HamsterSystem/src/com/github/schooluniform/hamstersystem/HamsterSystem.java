package com.github.schooluniform.hamstersystem;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import com.github.schooluniform.hamstersystem.data.Data;
import com.github.schooluniform.hamstersystem.data.PlayerData;
import com.github.schooluniform.hamstersystem.fightsystem.DamageEvent;
import com.github.schooluniform.hamstersystem.fightsystem.ShootSystem;
import com.github.schooluniform.hamstersystem.fightsystem.base.BasicEvent;
import com.github.schooluniform.hamstersystem.weapon.Weapon;
import com.github.schooluniform.hamstersystem.weapon.WeaponAttribute;
import com.github.schooluniform.hamstersystem.weapon.WeaponLauncher;
import com.github.schooluniform.hamstersystem.weapon.WeaponTag;

import java.io.File;

public class HamsterSystem extends JavaPlugin{
	public static HamsterSystem plugin;
	//La vie est drole
	
	@Override
	public void onEnable() {
		plugin = this;
		firstLoad();
		if(!Data.init(getConfig()))
			return;

		addOnlinePlayer();
		
		this.getServer().getPluginManager().registerEvents(new ShootSystem(), this);
		this.getServer().getPluginManager().registerEvents(new DamageEvent(), this);
		this.getServer().getPluginManager().registerEvents(new BasicEvent(), this);
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
		if(!new File(getDataFolder()+File.separator+"config.yml").exists())
			saveDefaultConfig();
		try{reloadConfig();}catch (Exception e){}
	}
	
	private void addOnlinePlayer(){
		for(Player p : getServer().getOnlinePlayers())
			Data.addPlayer(p.getName());
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	//Test
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if(!(label.equalsIgnoreCase("HamsterSystem")||label.equalsIgnoreCase("HS")))return true;
		switch(args.length){
			case 0:
			
				break;
			case 1:
				if(args[0].equalsIgnoreCase("demo")){
					PlayerData pd = Data.getPlayerData(sender.getName());
					pd.setEnergy(100);
					
				}
				break;
			case 2:
				if(args[0].equalsIgnoreCase("setWeapon") && (sender instanceof Player) &&
						(sender.isOp() || sender.hasPermission("HamsterSystem.Commands"))){
					Player p = (Player)sender;
					if(!Data.contansWeapon(args[1])){
						sender.sendMessage("Weapon "+args[1]+" is Not Find");
						return true;
					}
					if(p.getInventory().getItemInMainHand() == null || p.getInventory().getItemInMainHand().getType() == Material.AIR){
						sender.sendMessage("You have nothing in your hand");
						return true;
					}
					//Set Weapon
					{
						Weapon w = Data.getWeapon(args[1]);
						ItemStack weapon = p.getInventory().getItemInMainHand();
						weapon = Data.NBTTag.addNBT(weapon, WeaponTag.HSWLT.name(), args[1]);
						weapon = Data.NBTTag.addNBT(weapon, WeaponTag.HSWInfo.name(), new int[10]);
						weapon = Data.NBTTag.addNBT(weapon, WeaponTag.HSWEL.name(), new int[]{0,0});
						weapon = Data.NBTTag.addNBT(weapon, WeaponTag.HSWMOD.name(), new int[9]);
						weapon = Data.NBTTag.addNBT(weapon, WeaponTag.HSWME.name(), new int[9]);
						if(w instanceof WeaponLauncher){
							weapon = Data.NBTTag.addNBT(weapon, WeaponTag.HSWClip.name(), w.getAttribute(WeaponAttribute.Clip).intValue());
							weapon = Data.NBTTag.addNBT(weapon, WeaponTag.HSCSize.name(), 0);							
						}
						p.getInventory().setItemInMainHand(weapon);
					}
				}
				break;
			case 4:
			   if(args[0].equalsIgnoreCase("setammo") && (sender instanceof Player) &&
						(sender.isOp() || sender.hasPermission("HamsterSystem.Commands"))){
					Player p = (Player)sender;
					if(p.getInventory().getItemInMainHand() == null || p.getInventory().getItemInMainHand().getType() == Material.AIR){
						sender.sendMessage("You have nothing in your hand");
						return true;
					}
					//Set Weapon
					{
						ItemStack weapon = p.getInventory().getItemInMainHand();
						weapon = Data.NBTTag.addNBT(weapon, WeaponTag.HSWLT.name(), args[1]);
						weapon = Data.NBTTag.addNBT(weapon, WeaponTag.HSCSize.name(), Integer.parseInt(args[2]));
						weapon = Data.NBTTag.addNBT(weapon, WeaponTag.Consumable.name(), Boolean.parseBoolean(args[3]));
						
						p.getInventory().setItemInMainHand(weapon);
					}
			   }
			   break;
				
			default: break;
		}
		
		
		return true;
	}
	
	
}
