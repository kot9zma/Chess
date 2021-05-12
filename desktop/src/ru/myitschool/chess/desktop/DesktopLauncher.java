package ru.myitschool.chess.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

import ru.myitschool.chess.ChessMain;


public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = ChessMain.SCR_WIDTH;
		config.height = ChessMain.SCR_HEIGHT;
		new LwjglApplication(new ChessMain(), config);
	}
}
