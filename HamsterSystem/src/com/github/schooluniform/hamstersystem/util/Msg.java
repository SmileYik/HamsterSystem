package com.github.schooluniform.hamstersystem.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.MessageFormat;
import java.util.*;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import com.github.schooluniform.hamstersystem.HamsterSystem;
import com.github.schooluniform.hamstersystem.data.Data;

public class Msg {
	private static Msg msg;
	private String playerState1="",playerState2="";
	private String prefix;
	private final HashMap<String, String[]> defualt;
	
	public Msg(){
		checkMessagesYml();
		
		HashMap<String, String[]> defualt = new HashMap<>();
		YamlConfiguration config = YamlConfiguration.loadConfiguration(new File(HamsterSystem.plugin.getDataFolder()+"/messages.yml"));
		for(String key:config.getKeys(true)){
			if(key.equals("player-state-1")) {
				List<String> msgs = config.getStringList(key);
				String[] msgsString = msgs.toArray(new String[msgs.size()]);
				for(String str:msgsString) playerState1 += str+"\n";
				playerState1 = playerState1.substring(0, playerState1.length()-2);
			}else if(key.equals("player-state-2")){
				List<String> msgs = config.getStringList(key);
				String[] msgsString = msgs.toArray(new String[msgs.size()]);
				for(String str:msgsString) playerState2 += str+"\n";
				playerState2 = playerState2.substring(0, playerState2.length()-2);
			}else if(key.equals("prefix")){
				prefix = config.getString(key);
			}else{
				List<String> msgs = config.getStringList(key);
				defualt.put(key, msgs.toArray(new String[msgs.size()]));
			}
		}
		this.defualt = defualt;
		msg = this;
	}
	
	private String getMessages(String key){
		String[] msgs = defualt.get(key);
		return msgs[(int)(Math.random()*msgs.length)];
	}
	
	public static String getPlayerState1(Object ...objects ){
		return MessageFormat.format(msg.playerState1,objects);
	}
	public static String getPlayerState2(Object ...objects ){
		return MessageFormat.format(msg.playerState2,objects);
	}
	public static String getPrefix(){
		return msg.prefix;
	}
	
	public static String tr(String key,Object ... objs){
		return MessageFormat.format(msg.getMessages(key), objs);
	}
	
	public static String tr(String key){
		return msg.getMessages(key);
	}
	
	public static String trp(String key,Object ... objs){
		return msg.prefix+MessageFormat.format(msg.getMessages(key), objs);
	}
	
	public static String trp(String key){
		return msg.prefix+msg.getMessages(key);
	}
	
	public static void send(Player p,String key){
		if(Data.isEnablePrefixInTitle())sendP(p,key);
		else sendNP(p,key);
	}
	
	public static void send(Player p,String key,Object ... objs){
		if(Data.isEnablePrefixInTitle())sendP(p,key,objs);
		else sendNP(p,key,objs);
	}
	
	public static void sendNP(Player p,String key){
		if(!Data.actionbar.sendActionbar(p, tr(key))){
			p.sendMessage(tr(key));
		}
	}
	
	public static void sendNP(Player p,String key,Object ... objs){
		if(!Data.actionbar.sendActionbar(p, tr(key,objs))){
			p.sendMessage(tr(key,objs));
		}
	}
	
	public static void sendP(Player p,String key){
		if(!Data.actionbar.sendActionbar(p, msg.prefix+tr(key))){
			p.sendMessage(msg.prefix+tr(key));
		}
	}
	
	public static void sendP(Player p,String key,Object ... objs){
		if(!Data.actionbar.sendActionbar(p, msg.prefix+tr(key,objs))){
			p.sendMessage(msg.prefix+tr(key,objs));
		}
	}
	
	private void checkMessagesYml(){
		if(!new File(HamsterSystem.plugin.getDataFolder()+File.separator+"messages.yml").exists()){
			writerYml("messages_"+HamsterSystem.plugin.getConfig().getString("language"));
		}else if(YamlConfiguration.loadConfiguration(new File(HamsterSystem.plugin.getDataFolder()+File.separator+"messages.yml")).getString("language",null)==null || !YamlConfiguration.loadConfiguration(new File(HamsterSystem.plugin.getDataFolder()+File.separator+"messages.yml")).getString("language",null).equalsIgnoreCase(HamsterSystem.plugin.getConfig().getString("language"))){
			writerYml("messages_"+HamsterSystem.plugin.getConfig().getString("language"));
		}
	}
	
	private void writerYml(String name){
		InputStream is=null;
		OutputStream os=null;
		try {
			is=Msg.class.getResourceAsStream("/lang/"+name+".yml");
			os=new FileOutputStream(new File(HamsterSystem.plugin.getDataFolder()+"/messages.yml"));
			int i;
			byte[] buffer = new byte[1024];
			while((i=is.read(buffer))!=-1)
				os.write(buffer,0,i);
			os.flush();
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}finally {
			if(is!=null)
				try {
					is.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			if(os!=null)
				try {
					os.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
		}
		
	}
}