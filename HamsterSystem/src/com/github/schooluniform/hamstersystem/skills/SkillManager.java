package com.github.schooluniform.hamstersystem.skills;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerAnimationEvent;
import org.bukkit.event.player.PlayerChangedMainHandEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;

import com.github.schooluniform.hamstersystem.fightsystem.FightSystem;
import com.github.schooluniform.hamstersystem.skills.skills.IceBomb;

public class SkillManager implements Listener{
	private HashMap<Integer,SkillType> cooldown = new HashMap<>();
	private List<String> castByKey = new LinkedList<>();
	
	@EventHandler
	public void onChangeItem(PlayerItemHeldEvent e) {
		castByKey.add(e.getPlayer().getName());
		if(castByKey.contains(e.getPlayer().getName())) {
			if(((e.getNewSlot()+1)>8?0:e.getNewSlot()+1) != e.getPreviousSlot() && 
					((e.getNewSlot()-1)<0?8:e.getNewSlot()-1) != e.getPreviousSlot()) {
				if(e.getNewSlot() == 0)
					new IceBomb().cast(FightSystem.fight(e.getPlayer()));
				e.getPlayer().getInventory().setHeldItemSlot(7);
			}
		}
	}
}
