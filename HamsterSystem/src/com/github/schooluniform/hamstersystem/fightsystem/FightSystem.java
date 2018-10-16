package com.github.schooluniform.hamstersystem.fightsystem;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

import com.github.schooluniform.hamstersystem.data.FightData;

public class FightSystem implements Runnable{

	private static HashMap<Integer, Integer> onFightTimer = new HashMap<>();
	private static HashMap<Integer, FightData> onFightData = new HashMap<>();
	private Stack<Integer> onFightIO = new Stack<>();
	
	@Override
	public void run() {
		for(Map.Entry<Integer, Integer> entry : onFightTimer.entrySet()){
			entry.setValue(entry.getValue());
			if(entry.getValue()<=0)
				onFightIO.push(entry.getKey()*10+0);
		}
		
		while(!onFightIO.isEmpty()){
			int id = onFightIO.pop();
			if(id%10 == 0){
				onFightTimer.remove(id/10);
				onFightData.remove(id/10);
			}else{
				onFightTimer.remove(id/10);
				onFightData.remove(id/10);
				//Bukkit.geten
			}
		}
	}
	
}
