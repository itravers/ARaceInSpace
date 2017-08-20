package com.araceinspace.misc;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;

/**
 * Created by slack on 8/20/17.
 */
public class FontGenerator {
    public static BitmapFont createFont(FreeTypeFontGenerator ftfg, float dp)
    {
        FreeTypeFontGenerator.FreeTypeFontParameter parameter =  new FreeTypeFontGenerator.FreeTypeFontParameter();
       // parameter.size = (int)(dp * Gdx.graphics.getDensity());
        parameter.size = (int)(dp / 640 * Gdx.graphics.getWidth());
        return ftfg.generateFont(parameter);
    }

}
