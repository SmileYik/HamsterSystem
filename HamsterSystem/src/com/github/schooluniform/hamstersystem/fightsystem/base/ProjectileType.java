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
	ARROW("ARROW",Arrow.class,false),
	SNOWBALL("SNOWBALL",Snowball.class,false),
	FIREBALL("FIREBALL",Fireball.class,false),
	EGG("EGG",Egg.class,false),
	SMALL_FIREBALL("SMALL_FIREBALL",SmallFireball.class,false),
	DRAGON_FIRE_BALL("DRAGON_FIRE_BALL",DragonFireball.class,false),
	ENDER_PEARL("ENDER_PEARL",EnderPearl.class,false),
	FISHHOOK("FISHHOOK",FishHook.class,false),
	LARGE_FIRE_BALL("LARGE_FIRE_BALL",LargeFireball.class,false),
	LINGERING_POTION("LINGERING_POTION",LingeringPotion.class,false),
	LLAMA_SPIT("LLAMA_SPIT",LlamaSpit.class,false),
	SKULKER_BULLET("SKULKER_BULLET",ShulkerBullet.class,false),
	SPECTRAL_ARROW("SPECTRAL_ARROW",SpectralArrow.class,false),
	SPLASH_POTION("SPLASH_POTION",SplashPotion.class,false),
	THROWN_EXP_BOTTLE("THROWN_EXP_BOTTLE",ThrownExpBottle.class,false),
	TIPPED_ARROW("TIPPED_ARROW",TippedArrow.class,false),
	WITHER_SKULL("WITHER_SKULL",WitherSkull.class,false),
	
	FIRE_ARROW("FIRE_ARROW",Arrow.class,true),
	FIRE_SNOWBALL("FIRE_SNOWBALL",Snowball.class,true),
	FIRE_FIREBALL("FIRE_FIREBALL",Fireball.class,true),
	FIRE_EGG("FIRE_EGG",Egg.class,true),
	FIRE_SMALL_FIREBALL("FIRE_SMALL_FIREBALL",SmallFireball.class,true),
	FIRE_DRAGON_FIRE_BALL("FIRE_DRAGON_FIRE_BALL",DragonFireball.class,true),
	FIRE_ENDER_PEARL("FIRE_ENDER_PEARL",EnderPearl.class,true),
	FIRE_FISHHOOK("FIRE_FISHHOOK",FishHook.class,true),
	FIRE_LARGE_FIRE_BALL("FIRE_LARGE_FIRE_BALL",LargeFireball.class,true),
	FIRE_LINGERING_POTION("FIRE_LINGERING_POTION",LingeringPotion.class,true),
	FIRE_LLAMA_SPIT("FIRE_LLAMA_SPIT",LlamaSpit.class,true),
	FIRE_SKULKER_BULLET("FIRE_SKULKER_BULLET",ShulkerBullet.class,true),
	FIRE_SPECTRAL_ARROW("FIRE_SPECTRAL_ARROW",SpectralArrow.class,true),
	FIRE_SPLASH_POTION("FIRE_SPLASH_POTION",SplashPotion.class,true),
	FIRE_THROWN_EXP_BOTTLE("FIRE_THROWN_EXP_BOTTLE",ThrownExpBottle.class,true),
	FIRE_TIPPED_ARROW("FIRE_TIPPED_ARROW",TippedArrow.class,true),
	FIRE_WITHER_SKULL("FIRE_WITHER_SKULL",WitherSkull.class,true)
	;
	
	
    private String type ;
    private Class<? extends Projectile> clazz;
    private boolean fire;
    
    private ProjectileType(String type,Class<? extends Projectile> clazz,boolean fire) {
       this.type=type;
       this.clazz=clazz;
       this.fire = fire;
    }
    
    @Override
    public String toString() {
        return type;
    }
    
    public Class<? extends Projectile> getProjectileClass(){
    	return clazz;
    }
    
    public boolean isFire(){
    	return fire;
    }
}
