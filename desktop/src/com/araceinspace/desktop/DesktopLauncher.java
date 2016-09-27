package com.araceinspace.desktop;

import com.araceinspace.MonetizationSubSystem.DummyController;
import com.araceinspace.TestSubSystem.MonetizationIntegrationTest;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.araceinspace.ARaceInSpace;

public class DesktopLauncher {

	DummyController adsController;
	DesktopLauncher me;


	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = 1280;
		config.height = 800;
		System.out.println("config h/w: " + config.height + "/" + config.width);
		DummyController controller = new DummyController();



		new LwjglApplication(new MonetizationIntegrationTest(controller, null), config);
	}
}
