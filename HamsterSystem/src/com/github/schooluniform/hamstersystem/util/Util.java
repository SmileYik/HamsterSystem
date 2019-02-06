package com.github.schooluniform.hamstersystem.util;

import java.util.HashMap;

import com.github.schooluniform.hamstersystem.entity.EntityAttribute;

public class Util {
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

		if(modDatas[0].split(",").length==3){
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
	
	//TODO 
	public static HashMap<EntityAttribute,Double> getDefaultEntityAttributes(){
		HashMap<EntityAttribute,Double> attributes = new HashMap<>();
		attributes.put(EntityAttribute.Health, 100D);
		attributes.put(EntityAttribute.Armor, 1D);
		attributes.put(EntityAttribute.Shield, 100D);
		return attributes;
	}
}
