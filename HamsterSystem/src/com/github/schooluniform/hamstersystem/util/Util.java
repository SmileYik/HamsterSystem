package com.github.schooluniform.hamstersystem.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.GameMode;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.util.BlockIterator;

import com.github.schooluniform.hamstersystem.entity.EntityAttribute;

public class Util {
	public static String modDataToString(int[] modId,int[] modLevel,int[] modExp,int[] modP) {
		StringBuilder sb = new StringBuilder();
		for(int i = 0;i<modId.length;i++) {
			sb.append(modId[i]+","+modLevel[i]+","+modExp[i]+","+modP[i]+";");
		}
		sb.deleteCharAt(sb.length()-1);
		return sb.toString();
	}
	
	public static String modDataToString(int[] modid,int[] modLevel, int[] modExp){
		StringBuilder sb = new StringBuilder();
		for(int i = 0;i<modid.length;i++){
			sb.append(modid[i]+","+modLevel[i]+","+modExp[i]+";");
		}
		sb.deleteCharAt(sb.length()-1);
		return sb.toString();
	}
	
	public static String modDataToString(int[] modid,int[] modLevel){
		StringBuilder sb = new StringBuilder();
		for(int i = 0;i<modid.length;i++){
			sb.append(modid[i]+","+modLevel[i]+";");
		}
		sb.deleteCharAt(sb.length()-1);
		return sb.toString();
	}
	
	/**
	 * 
	 * @param modData
	 * @return The first array is mod id,the second array is mod level.  if returned array length is 3, the third is mod exp.
	 */
	public static int[][] modDataToArray(String modData){
		String[] modDatas = modData.split(";");
		int length = modDatas[0].split(",").length;
		if(length == 4) {
			int[] modId = new int[modDatas.length],
					modLevel=new int[modDatas.length],
					modExp=new int[modDatas.length],
					modP = new int[modDatas.length+1];
			for(int i = 0;i<modDatas.length;i++){
				String[] datas = modDatas[i].split(",");
				modId[i]=Integer.parseInt(datas[0]);
				modLevel[i]=Integer.parseInt(datas[1]);
				modExp[i]=Integer.parseInt(datas[2]);
				modP[i+1] = Integer.parseInt(datas[3]);
			}
			return new int[][]{
				modId,modLevel,modExp,modP
			};
		}else if(length==3){
			int[] modId = new int[modDatas.length],
					modLevel=new int[modDatas.length],
					modExp=new int[modDatas.length];
			for(int i = 0;i<modDatas.length;i++){
				String[] datas = modDatas[i].split(",");
				modId[i]=Integer.parseInt(datas[0]);
				modLevel[i]=Integer.parseInt(datas[1]);
				modExp[i]=Integer.parseInt(datas[2]);
			}
			return new int[][]{
				modId,modLevel,modExp
			};
		}else{
			int[] modId = new int[modDatas.length],
					modLevel=new int[modDatas.length];
			for(int i = 0;i<modDatas.length;i++){
				String[] datas = modDatas[i].split(",");
				modId[i]=Integer.parseInt(datas[0]);
				modLevel[i]=Integer.parseInt(datas[1]);
			}
			return new int[][]{
				modId,modLevel
			};
		}
	}
	
	public static String formatNum(double num,int l){
		return String.format("%."+l+"f", num);
	}
	
	//TODO 
	public static HashMap<EntityAttribute,Double> getDefaultEntityAttributes(){
		HashMap<EntityAttribute,Double> attributes = new HashMap<>();
		attributes.put(EntityAttribute.Health, 20D);
		attributes.put(EntityAttribute.Armor, 1D);
		attributes.put(EntityAttribute.Shield, 20D);
		attributes.put(EntityAttribute.ShieldRefresh, 10D);
		return attributes;
	}
	
	/**
	 * 这一段参考的MythicMob
	 * 未启用
	 * @param entity
	 * @return
	 */
	private static LivingEntity getTargetEntity(LivingEntity entity) {
        final int range = 32;
        List<LivingEntity> entities = new ArrayList<LivingEntity>();
        for(Entity e : entity.getNearbyEntities(range, range, range)) {
        	if(e instanceof LivingEntity) {
        		entities.add((LivingEntity)e);
        	}
        }
        
        LivingEntity target = null;
        BlockIterator bi;
        try {
            bi = new BlockIterator(entity, range);
	        while (bi.hasNext()) {
	            Block b = bi.next();
	            if (!b.getType().isTransparent()) {
	            	break;
	            }
	            int bx = b.getX();
	            int by = b.getY();
	            int bz = b.getZ();
	            for (LivingEntity e : entities) {
	                double ex = e.getLocation().getX();
	                double ey = e.getLocation().getY();
	                double ez = e.getLocation().getZ();
	                //                     最大体长  = 根8-根1/8 = 2.47 = 2.5 
	                if (bx - 0.75 <= ex && ex <= bx + 1.75 && 
	                		bz - 0.75 <= ez && ez <= bz + 1.75 &&
	                		by - 1 <= ey && ey <= by + 2.5) {//目标身高判断 凋零最高3.5
	                    if (target == null || !(target instanceof Player) || ((Player)target).getGameMode() != GameMode.CREATIVE) {
	                        return e;
	                    }
	                }
	            }
	        }
	        return null;
	    }catch (IllegalStateException e2) {
            return null;
        }
    }
}
