package com.araceinspace.client;

import com.araceinspace.MonetizationSubSystem.DummyController;
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.backends.gwt.GwtApplication;
import com.badlogic.gdx.backends.gwt.GwtApplicationConfiguration;
import com.araceinspace.ARaceInSpace;

public class HtmlLauncher extends GwtApplication {

        @Override
        public GwtApplicationConfiguration getConfig () {
                return new GwtApplicationConfiguration(960, 640);
        }

        @Override
        public ApplicationListener createApplicationListener () {
                DummyController controller = new DummyController();
                return new ARaceInSpace(controller, null);
        }
}