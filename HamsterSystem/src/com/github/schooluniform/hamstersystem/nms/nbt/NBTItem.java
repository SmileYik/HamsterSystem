package com.github.schooluniform.hamstersystem.nms.nbt;

import org.bukkit.inventory.ItemStack;

public interface NBTItem {
	public boolean contantsNBT(ItemStack item, String key);
	public ItemStack addNBT(ItemStack item, String key, Object value);
	public String getString(ItemStack item, String key);
	public Integer getInt(ItemStack item, String key);
	public int[] getIntArray(ItemStack item, String key);
	public Double getDouble(ItemStack item, String key);
	public Short getShort(ItemStack item, String key);
	public Boolean getBoolean(ItemStack item, String key);
	public Byte getByte(ItemStack item, String key);
	public byte[] getByteArray(ItemStack item, String key);
	public Float getFloat(ItemStack item, String key);
	public Long getLong(ItemStack item, String key);
}
