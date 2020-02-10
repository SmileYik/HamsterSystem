package com.github.schooluniform.hamstersystem.event;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import com.github.schooluniform.hamstersystem.data.Data;

public class BasicEvent implements Listener{
	
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent e){
		Data.addPlayer(e.getPlayer());
	}
	
	@EventHandler
	public void onPlayerLeave(PlayerQuitEvent e){
		Data.removePlayer(e.getPlayer().getName());
	}

}
