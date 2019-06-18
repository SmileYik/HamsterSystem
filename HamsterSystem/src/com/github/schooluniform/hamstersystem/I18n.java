package com.github.schooluniform.hamstersystem;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.logging.Level;

import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import com.github.schooluniform.hamstersystem.data.Data;
import com.github.schooluniform.hamstersystem.util.Msg;

public class I18n{
	
	private static I18n i18n;
	private final ResourceBundle defual;
	private ResourceBundle custum,local;
	
	public I18n(String language){
		defual = ResourceBundle.getBundle("lang/messages", Locale.CHINESE);
		local = ResourceBundle.getBundle("lang/messages", Locale.getDefault());
		try{
			custum = ResourceBundle.getBundle("lang/messages", new Locale(language));
		}catch (Exception e) {
			custum = null;
			HamsterSystem.plugin.getLogger().log(Level.WARNING,"Missing Locale: "+language+"! Using Defual Locale: "+Locale.CHINESE);
		}
		i18n  = this;
	}
	
	private String tran(String key){
		if(key==null || key.isEmpty())return "";
		try {
			return custum.getString(key);
		} catch (Exception e) {
			try {
				return local.getString(key);
			} catch (Exception e2) {
				try {
					return defual.getString(key);									
				} catch (Exception e3) {
					return "Missing Key: "+key+". Please Notification op.";
				}
			}
		}
	}
	
	public static String tr(String key,Object ... objs){
		if(objs==null||objs.length==0)return i18n.tran(key);
		else return MessageFormat.format(i18n.tran(key), objs);
	}
	
	public static String tr(String key){
		return i18n.tran(key);
	}
	
	public static String trp(String key,Object ... objs){
		return Msg.getPrefix()+tr(key,objs);
	}
	
	public static String trp(String key){
		return Msg.getPrefix()+tr(key);
	}
	
	public static void send(LivingEntity entity,String key){
		if(entity instanceof Player){
			entity.sendMessage(tr(key));
		}
	}
	
	public static void sendp(LivingEntity entity,String key){
		if(entity instanceof Player){
			entity.sendMessage(trp(key));
		}
	}
	
	public static void send(LivingEntity entity,String key,Object ... objs){
		if(entity instanceof Player){
			entity.sendMessage(tr(key,objs));
		}
	}
	
	public static void sendp(LivingEntity entity,String key,Object ... objs){
		if(entity instanceof Player){
			entity.sendMessage(trp(key,objs));
		}
	}
	
	public static void senda(LivingEntity entity,String key){
		if(entity instanceof Player){
			if(!Data.actionbar.sendActionbar((Player)entity,tr(key)))
				send(entity,key);
		}
	}
	
	public static void senda(LivingEntity entity,String key,Object ... objs){
		if(entity instanceof Player){
			if(!Data.actionbar.sendActionbar((Player)entity,tr(key,objs)))
				send(entity,key,objs);
		}
	}
	
	public static void sendap(LivingEntity entity,String key){
		if(entity instanceof Player){
			if(!Data.actionbar.sendActionbar((Player)entity,trp(key)))
				sendp(entity,key);
		}
	}
	
	public static void sendap(LivingEntity entity,String key,Object ... objs){
		if(entity instanceof Player){
			if(!Data.actionbar.sendActionbar((Player)entity,trp(key,objs)))
				sendp(entity,key,objs);
		}
	}
	
}
