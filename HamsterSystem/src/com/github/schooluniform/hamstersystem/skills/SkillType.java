package com.github.schooluniform.hamstersystem.skills;

import com.github.schooluniform.hamstersystem.skills.skills.*;

public enum SkillType {
	IceBomb(IceBomb.class)
	;
	
	private Class<? extends AbstractSkill> clazz;
	SkillType(Class<? extends AbstractSkill> clazz){
		this.clazz = clazz;
	}
	
	public Class<? extends AbstractSkill> getClazz(){
		return clazz;
	}
}
