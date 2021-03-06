package com.alesegdia.demux.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.alesegdia.demux.GameConfig;
import com.alesegdia.demux.GdxGame;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = GameConfig.WINDOW_WIDTH;
		config.height = GameConfig.WINDOW_HEIGHT;
		config.title = "Demux";
		new LwjglApplication(new GdxGame(), config);
	}
}
