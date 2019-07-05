package com.github.schooluniform.hamstersystem.mod;

public enum ModPolarity {
	Attack(1),
	Defensive(2),
	Tactical(3),
	Auxiliary(4),
	Null(0);
	
	private int type ;

    private ModPolarity(int type) {
       this.type=type;
    }
    
    /**
     * 获取极性类型
     * @return
     */
    public int getType(){
    	return type;
    }
    
    public static ModPolarity getPolarity(int type){
    	for(ModPolarity p:ModPolarity.values())
    		if(p.type == type)return p;
    	return ModPolarity.Null;
    }
}
