package com.github.schooluniform.hamstersystem.particle;

import org.bukkit.Color;

public class ParticleColor {
	private float red,green,blue;

	private ParticleColor(float red, float green, float blue) {
		super();
		this.red = red/255F;
		this.green = green/255F;
		this.blue = blue/255F;
		if(this.red<Float.MIN_NORMAL)
			this.red=Float.MIN_NORMAL;
	}
	
	public static ParticleColor getRGB(float red, float green, float blue){
		return new ParticleColor(red, green, blue);
	}

	public static ParticleColor getRGB(int red, int green, int blue){
		return getRGB(new Float(red), new Float(green), new Float(blue));
	}
	
	public static ParticleColor getRGB(Color color){
		return getRGB(color.getRed(),color.getGreen(),color.getBlue());
	}
	
	public static ParticleColor getRGB(java.awt.Color color){
		return getRGB(color.getRed(),color.getGreen(),color.getBlue());
	}
	
	public static ParticleColor getRGB(com.sun.prism.paint.Color color){
		return getRGB(color.getRed(),color.getGreen(),color.getBlue());
	}
	
	public static ParticleColor getRGB(String colorString){
		String[] colorData = colorString.replace(" ", "").split(",");
		return getRGB(Integer.parseInt(colorData[0]),
				Integer.parseInt(colorData[1]),
				Integer.parseInt(colorData[2]));
	}
	
	public float getRed() {
		return red;
	}

	public float getGreen() {
		return green;
	}

	public float getBlue() {
		return blue;
	}
}
