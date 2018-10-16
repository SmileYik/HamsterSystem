package com.github.schooluniform.hamstersystem.weapon;

public enum WeaponTag {
	/**
	 * 指向武器数据标签:<p>
	 *   key: HSWLT; value: String
	 */
	HSWLT,
	/**
	 * 武器弹夹标签:<p>
	 *   key: HSWClip; value: int
	 */
	HSWClip,
	/**
	 * 武器等级经验标签:<p>
	 *   key: HSWEL; value: int[]; 0: Exp, 1: Level
	 */
	HSWEL,
	/**
	 * 武器基本信息标签:<p>
	 *   key: HSWInfo; value: int[]; 0: 是否双倍(0否1是), 1~9: 极性
	 */
	HSWInfo,
	/**
	 * 武器MOD等级:<p>
	 *   key: HSWML; value: int[]
	 */
	HSWML,
	/**
	 * 武器MOD经验:<p>
	 *   key: HSWME; value: int[]
	 */
	HSWME,
	/**
	 * 武器MOD标签:<p>
	 *   key: HSWMOD; value: int[]
	 */
	HSWMOD,
	/**
	 * 弹夹内子弹数量
	 */
	HSCSize,
}
