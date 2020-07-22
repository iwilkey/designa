package dev.iwilkey.devisea.gfx;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import dev.iwilkey.devisea.assets.Assets;

public class Text {
	
	private static String sym = "abcdefghijklmnopqrstuvwxyz ";
	private static char[] chars = sym.toCharArray();
	
	public static List<String> runtimeMessages = new ArrayList<String>();
	public static List<BufferedImage[]> allProcessedMessages = new ArrayList<BufferedImage[]>();
	
	// TODO: \n makes a new line. Lowers the rendering forloop y by size * 1.12
	
	private static BufferedImage[] process(String mes) {
		mes = mes.toLowerCase();
		int[] letterIndex = new int[mes.length()];
		char[] message = mes.toCharArray();
		
		int in = 0;
		for(int i = 0; i < message.length; i++) {
			char c = message[i];
			for(int l = 0; l < chars.length; l++) {
				if(c == chars[l]) {
					letterIndex[in] = l;
					in++;
				}
			}
		}
		
		BufferedImage[] preconcat = new BufferedImage[mes.length()];
		for(int i = 0; i < letterIndex.length; i++) {
			BufferedImage letter = new BufferedImage(8, 8, BufferedImage.TYPE_INT_ARGB);
			letter = Assets.letters[letterIndex[i]];
			preconcat[i] = letter;
		}
		
		return preconcat;
	}
	
	public static void draw(Graphics g, String message, int x, int y) {
		
		if(runtimeMessages.contains(message)) {
			int index = runtimeMessages.indexOf(message);
			for(int i = 0; i < message.length(); i++) {
				g.drawImage(allProcessedMessages.get(index)[i], x + (i * 9), y, null);
			}
			return;
		}
		
		BufferedImage[] phrase = process(message);
		
		recordMessage(message, phrase);
		
		for(int i = 0; i < message.length(); i++) {
			g.drawImage(phrase[i], x + (i * 9), y, null);
		}
	}
	
	// Override for above draw, just with size
	public static void draw(Graphics g, String message, int x, int y, int size) {
		
		if(runtimeMessages.contains(message)) {
			int index = runtimeMessages.indexOf(message);
			for(int i = 0; i < message.length(); i++) {
				g.drawImage(allProcessedMessages.get(index)[i], x + (i * (int)(size + (1 * 1.12f))), y, size, size, null);
			}
			return;
		}
		
		BufferedImage[] phrase = process(message);
		
		recordMessage(message, phrase);
		
		for(int i = 0; i < message.length(); i++) {
			g.drawImage(phrase[i], x + (i * (int)(size + (1 * 1.12f))), y, size, size, null);
		}
	}
	
	private static void recordMessage(String mes, BufferedImage[] pm) {
		runtimeMessages.add(mes);
		allProcessedMessages.add(pm);
	}
}
