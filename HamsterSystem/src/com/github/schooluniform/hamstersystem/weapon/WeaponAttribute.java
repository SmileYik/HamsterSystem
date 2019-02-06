package com.github.schooluniform.hamstersystem.weapon;

public enum WeaponAttribute {
	/**暴击倍数*/
	Crit_Damage(Calculation.Multiplication),
	/**暴击几率*/
	Crit_Chance(Calculation.Multiplication),
	/**触发几率*/
	Trigger_Adds(Calculation.Multiplication),
	/**贯穿几率*/
	Impact_Over(Calculation.Addition),
	/**攻击间隔*/
	Attack_Interval(Calculation.Multiplication),
	/**弹夹*/
	Clip(Calculation.Multiplication),
	/**X轴精准*/
	Precise_X(Calculation.Multiplication),
	/**Y轴精准*/
	Precise_Y(Calculation.Multiplication),
	/**Z轴精准*/
	Precise_Z(Calculation.Multiplication),
	/**射速*/
	Rate_of_Fire(Calculation.Multiplication),
	/**后坐力*/
	Recoil(Calculation.Multiplication),
	/**换弹时长*/
	Reload_Speed(Calculation.Multiplication),
	/**近战充能时每次攻击所消耗的能量*/
	Charging_Efficiency(Calculation.Multiplication),
	/**近战武器充能时所增加的伤害倍率*/
	Charging_Damage(Calculation.Multiplication),;
	
	private Calculation way;
	
	WeaponAttribute(Calculation way){
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

