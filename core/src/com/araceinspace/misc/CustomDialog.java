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

/**
 * Created by Isaac Assegai on 8/20/17.
 */
public class CustomDialog extends Dialog {
    float width;
    String title;
    BitmapFont titleFont;

    public CustomDialog (String title, Skin skin, float width, BitmapFont font) {
        super(title, skin, "dialog");
        this.width = width;
        this.title = title;
        this.titleFont = font;
        initialize();
    }

    private void initialize() {
        padTop(60); // set padding on top of the dialog title
        getButtonTable().defaults().height(60); // set buttons height
        setModal(true);
        setMovable(false);
        setResizable(false);

        getTitleLabel().getStyle().font = titleFont;
        WindowStyle style =  this.getStyle();
        style.titleFont = titleFont;
        this.setStyle(style);
        //getTitleTable().removeActor(getTitleLabel());
        //Label titleLabel = new Label(title, this.getSkin());
        //titleLabel.getStyle().font = titleFont;
        //getTitleTable().add(titleLabel).align(Align.center);


        //title
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
        return width/2;
    }
}