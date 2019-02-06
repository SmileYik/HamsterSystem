package com.github.schooluniform.hamstersystem.entity;

public enum EntityAttribute {
	/**护盾*/
	Shield,
	/**护盾恢复速率*/
	ShieldRefresh,
	/**护甲*/
	Armor,
	/**能量*/
	Energy,
	/**生命*/
	Health,
	/**生命恢复速率*/
	HealthRefresh,
	/**Level*/
	Level,
	/**打击护甲, 遭受打击伤害时获得护甲加成*/
	ImpactArmor,
	/**切割护甲, 遭受切割伤害时获得护甲加成*/
	SlashArmor,
	/**穿刺护甲, 遭受穿刺伤害时获得护甲加成*/
	PunctureArmor,
	/**冰冻护甲, 遭受冰冻伤害时获得护甲加成*/
	ColdArmor,
	/**电击护甲, 遭受电击伤害时获得护甲加成*/
	ElectricityArmor,
	/**火焰护甲, 遭受火焰伤害时获得护甲加成*/
	HeatArmor,
	/**毒素护甲, 遭受毒素伤害时获得护甲加成*/
	ToxinArmor,
	/**爆炸护甲, 遭受爆炸伤害时获得护甲加成*/
	BlastArmor,
	/**腐蚀护甲, 遭受腐蚀伤害时获得护甲加成*/
	CorrosiveArmor,
	/**毒气护甲, 遭受毒气伤害时获得护甲加成*/
	GasArmor,
	/**磁力护甲, 遭受磁力伤害时获得护甲加成*/
	MagneticArmor,
	/**辐射护甲, 遭受辐射伤害时获得护甲加成*/
	RadiationArmor,
	/**病毒护甲, 遭受病毒伤害时获得护甲加成*/
	ViralArmor,
	
	//属性护甲类,若值为 -1 则为免疫对应属性效果及伤害
	
}
