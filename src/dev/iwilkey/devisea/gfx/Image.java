package dev.iwilkey.devisea.gfx;

import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Image {
	
	public static BufferedImage loadImage(String path) {
		
		try {
			return ImageIO.read(Image.class.getResource(path));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return null;
		
	}
	
	
}
