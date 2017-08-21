package com.araceinspace.desktop;

import com.araceinspace.ARaceInSpace;
import com.araceinspace.MonetizationSubSystem.DummyController;
import com.araceinspace.TestSubSystem.MonetizationIntegrationTest;
import com.araceinspace.TestSubSystem.StoreLayoutTest;
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
		//config.height = 960;
		//config.width = 640;
		config.width = 1440;
		config.height = 2560;
		//config.useGL30 = true;
		System.out.println("config w/h: " + config.width + "/" + config.height);
		DummyController monetizationController = new DummyController();
		//new LwjglApplication(new StoreLayoutTest(monetizationController, null), config);
		new LwjglApplication(new ARaceInSpace(monetizationController, null), config);
	}
}
