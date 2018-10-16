package com.github.schooluniform.hamstersystem.fightsystem.base;

import org.bukkit.entity.Arrow;
import org.bukkit.entity.DragonFireball;
import org.bukkit.entity.Egg;
import org.bukkit.entity.EnderPearl;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.FishHook;
import org.bukkit.entity.LargeFireball;
import org.bukkit.entity.LingeringPotion;
import org.bukkit.entity.LlamaSpit;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.ShulkerBullet;
import org.bukkit.entity.SmallFireball;
import org.bukkit.entity.Snowball;
import org.bukkit.entity.SpectralArrow;
import org.bukkit.entity.SplashPotion;
import org.bukkit.entity.ThrownExpBottle;
import org.bukkit.entity.TippedArrow;
import org.bukkit.entity.WitherSkull;

public enum ProjectileType {
	ARROW("ARROW",Arrow.class),
	SNOWBALL("SNOWBALL",Snowball.class),
	FIREBALL("FIREBALL",Fireball.class),
	EGG("EGG",Egg.class),
	SMALL_FIREBALL("SMALL_FIREBALL",SmallFireball.class),
	DRAGON_FIRE_BALL("DRAGON_FIRE_BALL",DragonFireball.class),
	ENDER_PEARL("ENDER_PEARL",EnderPearl.class),
	FISHHOOK("FISHHOOK",FishHook.class),
	LARGE_FIRE_BALL("LARGE_FIRE_BALL",LargeFireball.class),
	LINGERING_POTION("LINGERING_POTION",LingeringPotion.class),
	LLAMA_SPIT("LLAMA_SPIT",LlamaSpit.class),
	SKULKER_BULLET("SKULKER_BULLET",ShulkerBullet.class),
	SPECTRAL_ARROW("SPECTRAL_ARROW",SpectralArrow.class),
	SPLASH_POTION("SPLASH_POTION",SplashPotion.class),
	THROWN_EXP_BOTTLE("THROWN_EXP_BOTTLE",ThrownExpBottle.class),
	TIPPED_ARROW("TIPPED_ARROW",TippedArrow.class),
	WITHER_SKULL("WITHER_SKULL",WitherSkull.class)
	;
	
	
    private String type ;
    private Class<? extends Projectile> clazz;
    
    private ProjectileType(String type,Class<? extends Projectile> clazz) {
       this.type=type;
       this.clazz=clazz;
    }
    
    @Override
    public String toString() {
        return type;
    }
    
    public Class<? extends Projectile> getProjectileClass(){
    	return clazz;
    }
}
