package com.github.schooluniform.hamstersystem;

import java.util.HashMap;

import org.junit.Test;

import com.github.schooluniform.hamstersystem.entity.EntityAttribute;

public class Test1 {
	
	@Test
	public void test(){
		HashMap<EntityAttribute, Double> attributes = new HashMap<>();
		attributes.put(EntityAttribute.Armor, 10D);
		System.out.println(attributes.get(EntityAttribute.Armor));
		double a = attributes.get(EntityAttribute.Armor);
		a = 11;
		System.out.println(attributes.get(EntityAttribute.Armor));
	}
}
