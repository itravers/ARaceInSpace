package com.araceinspace.desktop;

import com.araceinspace.MonetizationSubSystem.DummyController;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.araceinspace.ARaceInSpace;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		DummyController controller = new DummyController();
		new LwjglApplication(new ARaceInSpace(controller), config);
	}
}
