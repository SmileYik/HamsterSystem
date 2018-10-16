package com.github.schooluniform.hamstersystem.nms.nbt;

import org.bukkit.craftbukkit.v1_11_R1.inventory.CraftItemStack;

import net.minecraft.server.v1_11_R1.ItemStack;
import net.minecraft.server.v1_11_R1.NBTTagCompound;

public class NBT_V1_11 implements NBTItem{
	
	private ItemStack getItemStack(org.bukkit.inventory.ItemStack is){
		if(is==null)return null;
		ItemStack item=CraftItemStack.asNMSCopy(is);
		if(!item.hasTag())return null;
		return item;
	}
	
	@Override
	public org.bukkit.inventory.ItemStack addNBT(org.bukkit.inventory.ItemStack is, String key, Object value) {
		if(is==null)return null;
		ItemStack item=CraftItemStack.asNMSCopy(is);
		NBTTagCompound nbttc = item.hasTag()?item.getTag():new NBTTagCompound();
		if(value instanceof Integer)
			nbttc.setInt(key, (int)value);
		else if(value instanceof Double)
			nbttc.setDouble(key, (double)value);
		else if(value instanceof Boolean)
			nbttc.setBoolean(key, (boolean)value);
		else if(value instanceof Integer[])
			nbttc.setIntArray(key, (int[]) value);
		else if(value instanceof Byte)
			nbttc.setByte(key, (byte) value);
		else if(value instanceof Byte[])
			nbttc.setByteArray(key, (byte[]) value);
		else if(value instanceof Float)
			nbttc.setFloat(key, (float) value);
		else if(value instanceof Long)
			nbttc.setLong(key, (long) value);
		else if(value instanceof Short)
			nbttc.setShort(key, (short) value);
		else if(value instanceof String)
			nbttc.setString(key, (String) value);
		item.setTag(nbttc);
		return CraftItemStack.asBukkitCopy(item);
	}

	@Override
	public String getString(org.bukkit.inventory.ItemStack is, String key) {
		ItemStack item=getItemStack(is);
		if(item == null)return null;
		NBTTagCompound nbttc = item.getTag();
		if(!nbttc.hasKey(key))return null;
		return nbttc.getString(key);
	}

	@Override
	public Integer getInt(org.bukkit.inventory.ItemStack is, String key) {
		ItemStack item=getItemStack(is);
		if(item == null)return null;
		NBTTagCompound nbttc = item.getTag();
		if(!nbttc.hasKey(key))return null;
		return nbttc.getInt(key);
	}

	@Override
	public int[] getIntArray(org.bukkit.inventory.ItemStack is, String key) {
		ItemStack item=getItemStack(is);
		if(item == null)return null;
		NBTTagCompound nbttc = item.getTag();
		if(!nbttc.hasKey(key))return null;
		return nbttc.getIntArray(key);
	}

	@Override
	public Double getDouble(org.bukkit.inventory.ItemStack is, String key) {
		ItemStack item=getItemStack(is);
		if(item == null)return null;
		NBTTagCompound nbttc = item.getTag();
		if(!nbttc.hasKey(key))return null;
		return nbttc.getDouble(key);
	}

	@Override
	public Short getShort(org.bukkit.inventory.ItemStack is, String key) {
		ItemStack item=getItemStack(is);
		if(item == null)return null;
		NBTTagCompound nbttc = item.getTag();
		if(!nbttc.hasKey(key))return null;
		return nbttc.getShort(key);
	}

	@Override
	public Boolean getBoolean(org.bukkit.inventory.ItemStack is, String key) {
		ItemStack item=getItemStack(is);
		if(item == null)return null;
		NBTTagCompound nbttc = item.getTag();
		if(!nbttc.hasKey(key))return null;
		return nbttc.getBoolean(key);
	}

	@Override
	public Byte getByte(org.bukkit.inventory.ItemStack is, String key) {
		ItemStack item=getItemStack(is);
		if(item == null)return null;
		NBTTagCompound nbttc = item.getTag();
		if(!nbttc.hasKey(key))return null;
		return nbttc.getByte(key);
	}

	@Override
	public byte[] getByteArray(org.bukkit.inventory.ItemStack is, String key) {
		ItemStack item=getItemStack(is);
		if(item == null)return null;
		NBTTagCompound nbttc = item.getTag();
		if(!nbttc.hasKey(key))return null;
		return nbttc.getByteArray(key);
	}

	@Override
	public Float getFloat(org.bukkit.inventory.ItemStack is, String key) {
		ItemStack item=getItemStack(is);
		if(item == null)return null;
		NBTTagCompound nbttc = item.getTag();
		if(!nbttc.hasKey(key))return null;
		return nbttc.getFloat(key);
	}

	@Override
	public Long getLong(org.bukkit.inventory.ItemStack is, String key) {
		ItemStack item=getItemStack(is);
		if(item == null)return null;
		NBTTagCompound nbttc = item.getTag();
		if(!nbttc.hasKey(key))return null;
		return nbttc.getLong(key);
	}

	@Override
	public boolean contantsNBT(org.bukkit.inventory.ItemStack is, String key) {
		ItemStack item=getItemStack(is);
		if(item == null)return false;
		return item.getTag().hasKey(key);
	}
}
