package com.araceinspace.misc;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.Viewport;
import java.util.ArrayList;

/**
 * Created by Isaac Assegai on 8/20/17.
 */
public class CustomDialog extends Dialog {
    float width;
    float height;
    private BitmapFont titleFont;
    private BitmapFont subtitleFont;
    private ArrayList<Label> subTitleLabels;
    Skin skin;

    public CustomDialog (ArrayList<String> subtitles, Skin skin, Viewport viewport) {
        super("", skin, "dialog");
        this.skin = skin;
        initialize(subtitles, viewport);
    }

    private void initialize(ArrayList<String> subtitles, Viewport viewport) {
        //set fonts
        titleFont = FreetypeFontLoader.createFont(new FreeTypeFontGenerator(Gdx.files.internal("Font_Destroy.ttf")), 25);
        subtitleFont = FreetypeFontLoader.createFont(new FreeTypeFontGenerator(Gdx.files.internal("Font_Destroy.ttf")), 14);
        this.width = viewport.getScreenWidth();
        this.height = viewport.getScreenHeight()*.75f;
        subTitleLabels = new ArrayList<Label>();

        setModal(true);
        setMovable(false);
        setResizable(false);

        getTitleLabel().setAlignment(Align.center);

        //Loop through subtitles, create a label for each, add to subtitleLabels and add to table
        for(int i = 0; i < subtitles.size(); i++){
            Label subTitleLabel = new Label(subtitles.get(i), skin, "taunt_small");
            subTitleLabel.getStyle().font = subtitleFont;
            subTitleLabels.add(subTitleLabel);
            getTitleTable().row();
            getTitleTable().add(subTitleLabel).expandX();
        }
        getTitleTable().padTop(200);
    }

    /**
     * Adds a subtitle to the dialog
     * @param newSub
     */
    public void addSubtitle(String newSub){
        Label subTitleLabel = new Label(newSub, skin, "taunt_small");
        subTitleLabel.getStyle().font = subtitleFont;
        subTitleLabels.add(subTitleLabel);
        getTitleTable().row();
        getTitleTable().add(subTitleLabel).expandX();
    }

    @Override
    public CustomDialog text(String text) {
        super.text(new Label(text, this.getSkin(), "default"));
        return this;
    }

    /**
     * Adds a text button to the button table.
     * @param listener the input listener that will be attached to the button.
     */
    public CustomDialog button(String buttonText, InputListener listener) {
        TextButton button = new TextButton(buttonText, this.getSkin());
        button.addListener(listener);
        button(button);
        return this;
    }

    @Override
    public float getPrefWidth() {
        // force dialog width

        return width;
    }

    @Override
    public float getPrefHeight() {
        // force dialog height
        return height;
    }
}