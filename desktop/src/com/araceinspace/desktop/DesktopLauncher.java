package com.araceinspace.desktop;

import com.araceinspace.MonetizationSubSystem.DummyController;
import com.araceinspace.TestSubSystem.MonetizationIntegrationTest;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

/**
 * Created by Isaac Assegai on 9/17/16.
 * The Desktop Launcher intializes all the
 * systems to get the game working on the desktop.
 */
public class DesktopLauncher {

	DummyController monetizationController;
	DesktopLauncher me;

	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = 1280;
		config.height = 800;
		//config.useGL30 = true;
		System.out.println("config h/w: " + config.height + "/" + config.width);
		DummyController monetizationController = new DummyController();
		new LwjglApplication(new MonetizationIntegrationTest(monetizationController, null), config);
	}
}
