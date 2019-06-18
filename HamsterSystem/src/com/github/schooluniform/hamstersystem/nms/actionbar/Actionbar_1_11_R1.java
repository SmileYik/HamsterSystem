package com.github.schooluniform.hamstersystem.nms.actionbar;

import org.bukkit.craftbukkit.v1_11_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

import net.minecraft.server.v1_11_R1.IChatBaseComponent;
import net.minecraft.server.v1_11_R1.IChatBaseComponent.ChatSerializer;
import net.minecraft.server.v1_11_R1.PacketPlayOutChat;

public class Actionbar_1_11_R1 implements Actionbar{

	@Override
	public void sendActionbar(Player p, String msg) {
		IChatBaseComponent.ChatSerializer. icbc = ChatSerializer.a("{\"text\": \"" + msg + "\"}");
        ((CraftPlayer) p).getHandle().playerConnection.sendPacket(new PacketPlayOutChat(icbc, (byte) 2));
	}
}
