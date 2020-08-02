package com.iwilkey.designa.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

import com.iwilkey.designa.Game;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();

		config.title = "Desgina pa1.0.11";
		config.width = 600;
		config.height = 600;
		config.resizable = false;

		new LwjglApplication(new Game(), config);
	}
}
