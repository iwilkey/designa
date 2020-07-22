package dev.iwilkey.devisea.display;

import java.awt.Canvas;
import java.awt.Dimension;

import javax.swing.JFrame;

public class Display {
		
	private JFrame frame;
	private Canvas canvas;
	
	private String name;
	private int width, height;
	
	public Display(String name, int w, int h) {
		this.name = name;
		this.width = w;
		this.height = h;
		
		createDisplay();
	}
	
	private void createDisplay() {
		
		frame = new JFrame(name);
		frame.setSize(width, height);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		
		canvas = new Canvas();
		Dimension d = new Dimension(width, height);
		canvas.setPreferredSize(d);
		canvas.setMaximumSize(d);
		canvas.setMinimumSize(d);
		canvas.setFocusable(false);
		
		frame.add(canvas);
		frame.pack();
		
	}
	
	public JFrame getFrame() {
		return frame;
	}
	
	public Canvas getCanvas() {
		return canvas;
	}
	
	
	
}
