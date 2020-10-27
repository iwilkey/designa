package dev.iwilkey.designa.desktop;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

import dev.iwilkey.designa.Game;

public class DesktopLauncher {

	public static void main (String[] arg) {

		int width = 2000, height = 1125, fullscreen = 0;
		if(arg.length > 0) {
			width = Integer.parseInt(arg[0]);
			height = Integer.parseInt(arg[1]);
			fullscreen = Integer.parseInt(arg[2]);
		}

		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();

		config.title = "Designa";
		config.width = width;
		config.height = height;
		config.resizable = true;
		config.fullscreen = fullscreen != 0;
		config.addIcon("icon.png", Files.FileType.Internal);

		new LwjglApplication(new Game(), config);

	}

}
