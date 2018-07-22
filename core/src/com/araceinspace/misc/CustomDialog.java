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
    float height;
    String title;
    BitmapFont titleFont;
    private BitmapFont subtitleFont;
    private Label subTitleLabel;
    Skin skin;

    public Label getSubTitleLabel(){
        return subTitleLabel;
    }

    public CustomDialog (String title, Skin skin, float width, float height, BitmapFont font, BitmapFont subtitleFont) {
        super(title, skin, "dialog");
        this.skin = skin;
        this.width = width;
        this.height = height;
        this.title = title;
        this.titleFont = font;
        this.subtitleFont = subtitleFont;
        initialize();
    }

    private void initialize() {
       // padTop(60); // set padding on top of the dialog title
       // getButtonTable().defaults().height(60); // set buttons height
        setModal(true);
        setMovable(false);
        setResizable(false);

        getTitleLabel().getStyle().font = titleFont;
        getTitleLabel().setAlignment(Align.center);
        WindowStyle style =  this.getStyle();

        style.titleFont = titleFont;
        this.setStyle(style);

        subTitleLabel = new Label("test here", skin, "taunt_small");
        subTitleLabel.getStyle().font = subtitleFont;
        getTitleTable().padTop(200);
        getTitleTable().row();

        getTitleTable().add(subTitleLabel).expandX();
        //getTitleTable().row();
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