package com.github.schooluniform.hamstersystem.fightsystem.base;

public enum ElementalDamageType{
	
	Cold(1),
	Electricity(10),
	Heat(100),
	Toxin(1000);
	
	int id;
	
	ElementalDamageType(int id){
		this.id=id;
	}
	
	public int getID(){
		return id;
	}
	
	public DamageType getDamageType(){
		if(id == 1)return DamageType.Cold;
		else if(id == 10)return DamageType.Electricity;
		else if(id == 100)return DamageType.Heat;
		else return DamageType.Toxin;
	}
}