package com.araceinspace.misc;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetLoaderParameters;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.AsynchronousAssetLoader;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.BitmapFont.BitmapFontData;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeBitmapFontData;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.badlogic.gdx.utils.Array;

/**
 * Creates {@link BitmapFont} instances from FreeType font files. Requires a {@link FreeTypeFontLoaderParameter} to be
 * passed to {@link AssetManager#load(String, Class, AssetLoaderParameters)} which specifies the name of the TTF
 * file as well the parameters used to generate the BitmapFont (size, characters, etc.)
 */
public class FreetypeFontLoader extends AsynchronousAssetLoader<BitmapFont, FreetypeFontLoader.FreeTypeFontLoaderParameter>{
    public FreetypeFontLoader (FileHandleResolver resolver) {
        super(resolver);
    }

    public static class FreeTypeFontLoaderParameter extends AssetLoaderParameters<BitmapFont>{
        /** the name of the TTF file to be used to load the font **/
        public String fontFileName;
        /** the parameters used to generate the font, e.g. size, characters, etc. **/
        public FreeTypeFontParameter fontParameters = new FreeTypeFontParameter();
    }

    @Override
    public void loadAsync (AssetManager manager, String fileName, FileHandle file, FreeTypeFontLoaderParameter parameter) {
        if(parameter == null) throw new RuntimeException("FreetypeFontParameter must be set in AssetManager#load to point at a TTF file!");
    }

    @Override
    public BitmapFont loadSync (AssetManager manager, String fileName, FileHandle file, FreeTypeFontLoaderParameter parameter) {
        if(parameter == null) throw new RuntimeException("FreetypeFontParameter must be set in AssetManager#load to point at a TTF file!");
        FreeTypeFontGenerator generator = manager.get(parameter.fontFileName + ".gen", FreeTypeFontGenerator.class);
       // BitmapFont font = generator.generateFont(parameter.fontParameters);
        BitmapFont font = createFont(generator, parameter.fontParameters.size, parameter.fontParameters);
        return font;
    }

    @Override
    public Array<AssetDescriptor> getDependencies (String fileName, FileHandle file, FreeTypeFontLoaderParameter parameter) {
        Array<AssetDescriptor> deps = new Array<AssetDescriptor>();
        deps.add(new AssetDescriptor<FreeTypeFontGenerator>(parameter.fontFileName + ".gen", FreeTypeFontGenerator.class));
        return deps;
    }

    public static BitmapFont createFont(FreeTypeFontGenerator ftfg, float dp){
        FreeTypeFontGenerator.FreeTypeFontParameter parameter =  new FreeTypeFontGenerator.FreeTypeFontParameter();
        return createFont(ftfg, dp, parameter);
    }

    public static BitmapFont createFont(FreeTypeFontGenerator ftfg, float dp, FreeTypeFontGenerator.FreeTypeFontParameter parameter)
    {
        float developmentScreenWidth = 640f;
       // parameter.size = (int)(dp * Gdx.graphics.getDensity());
        parameter.size = (int)(dp / developmentScreenWidth * Gdx.graphics.getWidth());
        return ftfg.generateFont(parameter);
    }

}

