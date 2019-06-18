package com.github.schooluniform.hamstersystem.nms.actionbar;

import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

import net.minecraft.server.v1_12_R1.ChatComponentText;
import net.minecraft.server.v1_12_R1.ChatMessageType;
import net.minecraft.server.v1_12_R1.IChatBaseComponent;
import net.minecraft.server.v1_12_R1.PacketPlayOutChat;

public class Actionbar_1_12_R1 implements Actionbar{

	@Override
	public boolean sendActionbar(Player p, String msg) {
		try{
			IChatBaseComponent icbc = new ChatComponentText(msg);
			((CraftPlayer) p).getHandle().playerConnection.sendPacket(new PacketPlayOutChat(icbc, ChatMessageType.GAME_INFO));			
		}catch(Exception e){
			return false;
		}
		return true;
	}

}
