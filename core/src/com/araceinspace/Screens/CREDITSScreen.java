package com.araceinspace.Screens;

import com.araceinspace.Managers.GameStateManager;
import com.araceinspace.Managers.RenderManager;
import com.araceinspace.misc.OrthCamera;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageTextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.util.ArrayList;

/**
 * Created by Isaac Assegai on 3/23/18.
 * An Option in the “menu screen”. The background of the credits
 * will be the “vector art of my face”. My face is the best face.
 * It’s a great face. There’s never been a face as good as my face.
 * Since I think I’m probably going to be the only person working on this game,
 * I want to make it REALLY REALLY OBVIOUS. I’d like to go overboard a
 * little bit and list every possible job I’ve done for the game Similar
 * to how a triple A game would be done. It should be 1-2 minutes long,
 * with 50+ credits all attributed to MEEEEE :)

 */
public class CREDITSScreen extends Screen
{
    float elapsedTime;

    private Skin skin;
    private Table mainTable;
    private SpriteBatch batch;
    private Texture backgroundImg;
    private ImageTextButton continueButton;
    private ClickListener continueButtonListener;
    private OrthographicCamera camera;
   // private Viewport viewport;

    private ArrayList<String> title;
    private ArrayList<String> name;

    private ArrayList<Label>titleLabels;
    private ArrayList<Label>nameLabels;

    private float yVel = 50;

    int screenHeight;
    int screenWidth;

    private Table table;

    private boolean firstTime = true;



    public CREDITSScreen(RenderManager p) {
        super(p);

       // parent.parent.levelManager.setLevel(1);
        // parent.parent.elapsedTime = 0;
    }
    @Override
    public void setup() {
        System.out.println("Setting up Credits Screen");
        elapsedTime = 0;
        screenHeight = Gdx.graphics.getHeight();
        screenWidth = Gdx.graphics.getWidth();
        skin = parent.parent.resourceManager.getSkin();
        batch = new SpriteBatch();
        backgroundImg = new Texture("isaac.png");
       // camera = new OrthographicCamera();
        //viewport = new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), camera);
        stage = new Stage(viewport, batch);
        table = new Table();
        table.setDebug(parent.parent.devMode);
        table.setWidth(screenWidth);
        table.align(Align.center|Align.top);
        table.setPosition(0, screenHeight);
        continueButton = new ImageTextButton("Continue", skin);

        continueButtonListener = new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y){
                System.out.println("button Clicked");
                parent.parent.gameStateManager.setCurrentState(GameStateManager.GAME_STATE.LEVEL_SELECT);
            }

        };
        continueButton.addListener(continueButtonListener);
        table.add(continueButton).size(screenWidth*1.25f, screenHeight/12);
        Gdx.input.setInputProcessor(stage);
        stage.addActor(table);

        setupStrings();
        setupLabels();
        parent.parent.soundManager.playSong(parent.parent.resourceManager.risingSun);

    }

    @Override
    public void render(float elapsedTime) {
        elapsedTime += Gdx.graphics.getDeltaTime();
        updateCredits(elapsedTime);
        //System.out.println("elapsedTime: " + elapsedTime);
        //Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClearColor(.447f, .2784f, .3843f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.begin();

        batch.draw(backgroundImg, (screenWidth/2)-backgroundImg.getWidth()/2, (screenHeight/2)-backgroundImg.getHeight()/2);


        batch.end();
        stage.act(Gdx.graphics.getDeltaTime());
        stage.draw();
    }

    @Override
    public void dispose() {
        backgroundImg.dispose();
    }

    @Override
    public OrthCamera getBackgroundCamera() {
        return null;
    }

    /**
     * Updates the positions of the credits
     */
    private void updateCredits(float elapsedTime){
        if(titleLabels.get(titleLabels.size()-1).getY() > screenHeight+120){
            if(firstTime){
               // long id = parent.parent.soundManager.playSound(parent.parent.resourceManager.everybodyDies);
                parent.parent.resourceManager.everybodyDies.setOnCompletionListener(new Music.OnCompletionListener() {
                    @Override
                    public void onCompletion(Music music) {
                        System.out.println("Sound Finished");
                        parent.parent.gameStateManager.setCurrentState(GameStateManager.GAME_STATE.LEVEL_SELECT);
                        parent.parent.soundManager.playSong(parent.parent.resourceManager.beethovens7th, true, false);
                    }
                });
                parent.parent.soundManager.playSong(parent.parent.resourceManager.everybodyDies, false, false);
                firstTime = false;
            }


            //parent.parent.resourceManager.everybodyDies.setLooping(id, false);
           /* try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }*/
            //parent.parent.gameStateManager.setCurrentState(GameStateManager.GAME_STATE.LEVEL_SELECT);
        }

        //update the first credit
        float mainY = titleLabels.get(0).getY();
        mainY = mainY + (yVel*elapsedTime);
        titleLabels.get(0).setY(mainY);
        nameLabels.get(0).setY(mainY+ nameLabels.get(0).getHeight());

        //update all other credits
        for(int i = 1; i < titleLabels.size(); i++){
            float y = titleLabels.get(i-1).getY() - 250;
            titleLabels.get(i).setY(y);
            nameLabels.get(i).setY(y + nameLabels.get(i).getHeight());
        }
    }

    /**
     * Sets up the strings to be used as titles and names in their own arrays.
     */
    private void setupStrings(){
        //first setup titles
        title = new ArrayList<String>();
        title.add("Lead Designer");
        title.add("Director");
        title.add("Chief Publisher");
        title.add("Human Relations");
        title.add("Designer");
        title.add("System Architect");
        title.add("Art Director");
        title.add("Concept Artist");
        title.add("Artist");
        title.add("Animator");
        title.add("Research & Dev.");
        title.add("Writer");
        title.add("Marketing/PR");
        title.add("Programmer");
        title.add("Lead Gopher");
        title.add("Quartermaster");
        title.add("Sound Design");
        title.add("Composer/Arranger");
        title.add("QA/Testing");
        title.add("Accountant");
        title.add("Legal Services");
        title.add("Voice Actor");
        title.add("IT Director");
        title.add("Systems Administrator");
        title.add("Monitization Manger");

        //then setup names
        name = new ArrayList<String>();
        for( int i = 0; i < title.size(); i++){
            name.add("Isaac Assegai");
        }
    }

    private void setupLabels(){
        //first setup titleLabels
        titleLabels = new ArrayList<Label>();
        for(int i = 0; i < title.size(); i++){
            Label l = new Label(title.get(i)+"",skin);
            //Label l = new Label(title.get(i)+"",skin, "taunt_small");

            l.setPosition((screenWidth/2)-(l.getWidth()/2), -20);
            titleLabels.add(l);
            stage.addActor(l);

        }

        //then setup nameLabels
        nameLabels = new ArrayList<Label>();
        for(int i = 0; i < name.size(); i++){
            Label l = new Label(name.get(i)+"",skin, "taunt_small");

            l.setPosition((screenWidth/2)-(l.getWidth()/2), titleLabels.get(i).getY() + l.getHeight());
            //titleLabels.add(l);
            stage.addActor(l);
            nameLabels.add(l);
        }
    }
}
