package com.github.schooluniform.hamstersystem.fightsystem.base;

public enum DamageType {
	/** 打击: 使目标暂停所有行动一定时间*/
	Impact,
	/** 穿刺: 降低目标的伤害*/
	Puncture,
	/** 切割: 使目标在一定时间内肉体每秒都受到伤害*/
	Slash,
	/** 冰冻: 降低目标速度*/
	Cold,
	/** 电击: 小范围连锁伤害, 当直接对肉体造成伤害且伤害过高会使目标暂停所有行动一定时间*/
	Electricity,
	/** 火焰: 使目标着火*/
	Heat,
	/** 毒素: 使目标中毒,且在一定时间内肉体每秒都受到伤害*/
	Toxin,
	/**
	 * 爆炸: 范围内的目标都暂停所有行动一定时间<p>
	 * Cold (冰冻 + Heat (火焰
	 */
	Blast,
	/**
	 * 腐蚀: 永久降低目标护甲 <p>
	 * Toxin(毒素 + Electricity(电击
	 */
	Corrosive,
	/**
	 * 毒气: 范围内的目标获得毒素效果<p>
	 * Toxin(毒素 + Heat (火焰
	 */
	Gas,
	/**
	 * 磁力: 永久降低护盾上限 <p>
	 * Cold (冰冻 +  Electricity(电击
	 */
	Magnetic,
	/**
	 * 辐射: 致盲目标 <p>
	 * Heat (火焰 + Electricity(电击
	 */
	Radiation,
	/**
	 * 病毒: 永久减少生命上限 <p>
	 * Cold (冰冻+ Toxin(毒素
	 */
	Viral;
	
	public static ElementalDamageType[] getElementalDamage(DamageType type){
		switch (type) {
		case Blast:
			return new ElementalDamageType[]{ElementalDamageType.Cold,ElementalDamageType.Heat};
		case Corrosive:
			return new ElementalDamageType[]{ElementalDamageType.Toxin,ElementalDamageType.Electricity};
		case Gas:
			return new ElementalDamageType[]{ElementalDamageType.Toxin,ElementalDamageType.Heat};
		case Magnetic:
			return new ElementalDamageType[]{ElementalDamageType.Cold,ElementalDamageType.Electricity};
		case Radiation:
			return new ElementalDamageType[]{ElementalDamageType.Electricity,ElementalDamageType.Heat};
		case Viral:
			return new ElementalDamageType[]{ElementalDamageType.Cold,ElementalDamageType.Toxin};
		default:
			return new ElementalDamageType[]{};

		}
	}
}
