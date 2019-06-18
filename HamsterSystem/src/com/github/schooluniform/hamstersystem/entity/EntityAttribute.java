package com.github.schooluniform.hamstersystem.entity;

import com.github.schooluniform.hamstersystem.weapon.Calculation;

public enum EntityAttribute {
	/**护盾*/
	Shield(Calculation.Multiplication),
	/**护盾恢复速率*/
	ShieldRefresh(Calculation.Multiplication),
	/**护甲*/
	Armor(Calculation.Multiplication),
	/**能量*/
	Energy(Calculation.Multiplication),
	/**生命*/
	Health(Calculation.Multiplication),
	/**生命恢复速率*/
	HealthRefresh(Calculation.Multiplication),
	/**Level*/
	Level(Calculation.Addition),
	/**打击护甲, 遭受打击伤害时获得护甲加成*/
	ImpactArmor(Calculation.Multiplication),
	/**切割护甲, 遭受切割伤害时获得护甲加成*/
	SlashArmor(Calculation.Multiplication),
	/**穿刺护甲, 遭受穿刺伤害时获得护甲加成*/
	PunctureArmor(Calculation.Multiplication),
	/**冰冻护甲, 遭受冰冻伤害时获得护甲加成*/
	ColdArmor(Calculation.Multiplication),
	/**电击护甲, 遭受电击伤害时获得护甲加成*/
	ElectricityArmor(Calculation.Multiplication),
	/**火焰护甲, 遭受火焰伤害时获得护甲加成*/
	HeatArmor(Calculation.Multiplication),
	/**毒素护甲, 遭受毒素伤害时获得护甲加成*/
	ToxinArmor(Calculation.Multiplication),
	/**爆炸护甲, 遭受爆炸伤害时获得护甲加成*/
	BlastArmor(Calculation.Multiplication),
	/**腐蚀护甲, 遭受腐蚀伤害时获得护甲加成*/
	CorrosiveArmor(Calculation.Multiplication),
	/**毒气护甲, 遭受毒气伤害时获得护甲加成*/
	GasArmor(Calculation.Multiplication),
	/**磁力护甲, 遭受磁力伤害时获得护甲加成*/
	MagneticArmor(Calculation.Multiplication),
	/**辐射护甲, 遭受辐射伤害时获得护甲加成*/
	RadiationArmor(Calculation.Multiplication),
	/**病毒护甲, 遭受病毒伤害时获得护甲加成*/
	ViralArmor(Calculation.Multiplication),;
	
	//属性护甲类,若值为 -1 则为免疫对应属性效果及伤害
	private Calculation way;
	
	private EntityAttribute(Calculation way){
		this.way = way;
	}
	
	//计算方法
	public Calculation getWay(){
		return way;
	}
	
	public static Calculation getCalculation(){
		Calculation way = null;
		return way;
	}
}
