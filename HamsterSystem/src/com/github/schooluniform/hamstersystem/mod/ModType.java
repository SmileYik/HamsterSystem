package com.github.schooluniform.hamstersystem.mod;

public enum ModType {
	Melee(1),
	Launcher(2),
	Display(3),
	Player(4);
	
	private int ID ;

    private ModType(int ID) {
       this.ID=ID;
    }
    
    public int getID() {
    	return ID;
    }
    
}
