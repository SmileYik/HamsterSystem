package com.github.schooluniform.hamstersystem.mod;

public enum ModRarity {
	Comment(1),
	Uncomment(2),
	Rarity(3);
	
	private int type ;

    private ModRarity(int type) {
       this.type=type;
    }
    
    @Override
    public String toString() {
        return type+"";
    }
    
    public int getLevel(){
    	return type;
    }
}
