package com.github.schooluniform.hamstersystem.party;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;

import com.github.schooluniform.hamstersystem.I18n;

public class Party {
	private static int maxPartyMenber = 4;
	private static double shareExp = 30;
	private static double shareExpRadius = 10;
	/** Key: 队长 Value: 所有成员*/
	private static HashMap<String, List<String>> partys = new HashMap<String, List<String>>();
	//加入队伍的人, Key: 加入队伍的人, Value: 所在队伍的队长
	private static HashMap<String,String> menber = new HashMap<String, String>();
	
	public static void createParty(String playerName) {
		if(partys.containsKey(playerName)) {
			I18n.send(Bukkit.getPlayer(playerName), "party.create-party.had-party");
			return;
		}
		if(menber.containsKey(playerName)) {
			String leader = menber.get(playerName);
			partys.get(leader).remove(playerName);
			partys.put(playerName, new ArrayList<String>());
			menber.replace(playerName, playerName);
			I18n.send(Bukkit.getPlayer(playerName), "party.quit-party",leader);
			I18n.send(Bukkit.getPlayer(playerName), "party.create-party.succeed");
		}
	}
	
	public static void leaveParty(String playerName) {
		if(!partys.containsKey(playerName)) {
			if(!menber.containsKey(playerName)) {
				I18n.send(Bukkit.getPlayer(playerName), "party.quit-party.none-party");
				return;
			}else {
				String leader = menber.get(playerName);
				partys.get(leader).remove(playerName);
				menber.remove(playerName);
				I18n.send(Bukkit.getPlayer(playerName), "party.quit-party",leader);
				return;
			}
		}else{
			List<String> menbers = partys.get(playerName);
			partys.remove(playerName);
			menber.remove(playerName);
			menbers.remove(playerName);
			if(menbers.isEmpty()) {
				I18n.send(Bukkit.getPlayer(playerName), "party.quit-party.self-1");
				return;
			}else {
				String leader = menbers.get(0);
				partys.put(leader, menbers);
				for(String p : menbers) {
					menber.replace(p, leader);
				}
				I18n.send(Bukkit.getPlayer(playerName), "party.quit-party.self-2",leader);
			}
		}
	}
	
	public static void shareExp(String sharer,double exp) {
		List<String> menbers = partys.get(menber.get(sharer));
		for(String p : menbers) {
			if(p.equals(sharer)) {
				continue;
			}else {
				//TODO
			}
		}
	}
	
	public static boolean hasParty(String playerName) {
		return menber.containsKey(playerName);
	}
	
	public static boolean isSameParty(String playerName1,String playerName2) {
		if(!hasParty(playerName2)||!hasParty(playerName1)) {
			return false;
		}else if(menber.get(playerName1).equals(menber.get(playerName2))) {
			return true;
		}else {
			return false;
		}
	}

	public static int getMaxPartyMenber() {
		return maxPartyMenber;
	}

	public static double getShareExp() {
		return shareExp;
	}

	public static double getShareExpRadius() {
		return shareExpRadius;
	}

	public static HashMap<String, List<String>> getPartys() {
		return partys;
	}

	public static HashMap<String, String> getMenber() {
		return menber;
	}
	
	
}
