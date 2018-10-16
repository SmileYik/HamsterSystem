package com.github.schooluniform.hamstersystem.weapon;

public enum WeaponAttribute {
	/**暴击倍数*/
	Crit_Damage('*'),
	/**暴击几率*/
	Crit_Chance('*'),
	/**触发几率*/
	Trigger_Adds('*'),
	/**贯穿几率*/
	Impact_Over('+'),
	/**攻击间隔*/
	Attack_Interval('*'),
	/**弹夹*/
	Clip('*'),
	/**X轴精准*/
	Precise_X('*'),
	/**Y轴精准*/
	Precise_Y('*'),
	/**Z轴精准*/
	Precise_Z('*'),
	/**射速*/
	Rate_of_Fire('*'),
	/**后坐力*/
	Recoil('*'),
	/**换弹时长*/
	Reload_Speed('*'),
	/**近战充能时每次攻击所消耗的能量*/
	Charging_Efficiency('*'),
	/**近战武器充能时所增加的伤害倍率*/
	Charging_Damage('*'),;
	
	private char way;
	
	WeaponAttribute(char way){
		this.way = way;
	}
	
	public char getWay(){
		return way;
	}
}
