package com.github.schooluniform.hamstersystem.mod;

public enum ModType {
	Melee("Melee"),
	Launcher("Launcher"),
	Display("Display"),
	Player("Player");
	
	private String type ;

    private ModType(String type) {
       this.type=type;
    }
    
    @Override
    public String toString() {
        return type;
    }
}
