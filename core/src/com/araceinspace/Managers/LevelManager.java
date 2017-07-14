package com.araceinspace.Managers;

import com.araceinspace.GameObjectSubSystem.Planet;
import com.araceinspace.GameObjectSubSystem.Player;
import com.araceinspace.GameObjectSubSystem.SpriteTemplate;
import com.araceinspace.GameWorld;
import com.araceinspace.misc.Background;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Json;

import java.util.ArrayList;

/**
 * Created by Isaac Assegai on 7/11/17.
 * Loads and keeps track of the current level
 */
public class LevelManager {

    /* Static Variables */
    public static final short CATEGORY_PLAYER = -1;
    public static final short CATEGORY_PLANET = -2;

    /* Field Variables & Objects */
    public GameWorld parent;
    private int currentLevel;
    private Player player;
    private ArrayList<Planet> planets;
    private Background mainBackground;

    /* Constructors */
    public LevelManager(GameWorld p){
        System.out.println("LevelManager Constructor");
        parent = p;
        setLevel(1);//sets player, planets, background, etc.

    }

    /* Private Methods */

    /**
     * Reads the level file and gets the planets to setup.
     */
    private void setupPlanets(){
        planets = new ArrayList<Planet>();
        Json json = new Json();
        ArrayList<SpriteTemplate>levelItems = json.fromJson(ArrayList.class, SpriteTemplate.class, getLevelFile(currentLevel));

        //Read the list of levelItems, create a planet for every planet in the list
        for(int i = 0; i < levelItems.size(); i++){
            SpriteTemplate item = levelItems.get(i);
            if(item.getType().equals("planet")){
                TextureAtlas atlas = parent.animationManager.getPlanetAtlas();
                Animation animations = parent.animationManager.getPlanetAnimationFromName(item.getAtlas());
                Planet p = new Planet(new Vector2(item.getxLoc(), item.getyLoc()), atlas, animations, parent.world, item.getSize(), item.getGravityRadius(), item.getMass(), this);
                planets.add(p);
            }
        }
    }

    /**
     * Returns the level file handle for this level.
     * First checks externally, to check if a mod of the level exists.
     * if not, it looks for the level file internally, in android assets.
     * @param lvl The level we want to find.
     * @return A fileHandler for the  level we want.
     */
    private FileHandle getLevelFile(int lvl){
        FileHandle fileHandle = null;
        String fileName = "levels/level"+lvl+".json";
        if(Gdx.files.classpath(fileName).exists()){
            fileHandle = Gdx.files.local(fileName);
            //System.out.println("using external file.");
        }else{
            fileHandle = Gdx.files.internal(fileName);
            //System.out.println("using internal file.");
        }
        return fileHandle;
    }

    private void setupBackground() {
        System.out.println("SetupBackground");
        Texture background = new Texture(Gdx.files.internal("data/tiledBackground.png"));
        Texture starscape1 = new Texture(Gdx.files.internal("data/stars1.png"));
        Texture starscape2 = new Texture(Gdx.files.internal("data/starscape2.png"));
        Texture starscape3 = new Texture(Gdx.files.internal("data/starscape3.png"));
        background.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat);
        starscape1.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat);
        starscape2.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat);
        starscape3.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat);
        TextureRegion[] backgroundLayers = new TextureRegion[4];
        Texture[] backGroundTextures = new Texture[4];
        backGroundTextures[0] = background;
        backGroundTextures[1] = starscape1;
        backGroundTextures[2] = starscape2;
        backGroundTextures[3] = starscape3;

        backgroundLayers[0] = new TextureRegion(background, 0, 0, Gdx.graphics.getHeight()*2, Gdx.graphics.getHeight()*2);
        backgroundLayers[1] = new TextureRegion(starscape1, 0, 0, 110000, 110000);
        backgroundLayers[2] = new TextureRegion(starscape2, 0, 0, 60000, 60000);
        backgroundLayers[3] = new TextureRegion(starscape3, 0, 0, 20000, 20000);
        mainBackground = new Background(this, backgroundLayers);
        //mainBackground = new Background(this, backGroundTextures);
    }

    /* Public Methods */
    public void setCurrentLevel(int level){
        currentLevel = level;
    }

    public int getCurrentLevel(){
        return currentLevel;
    }

    /**
     * Sets the level to the designated level.
     * Unload all resources used for the previous level.
     * Load all resources used for the set level
     * @param level The Level we are setting to
     */
    public void setLevel(int level){
        currentLevel = level;
        setupPlayer();
        setupBackground();
        setupPlanets();

    }

    /**
     * Returns the current player object
     */
    public Player getPlayer(){
        return player;
    }

    /**
     * Sets the current player object
     * @param p The player to set
     */
    public void setPlayer(Player p){
        player = p;
    }

    /**
     * Sets up the player to be usable
     */
    public void setupPlayer(){
        TextureAtlas atlas = parent.animationManager.getStandingStillForwardsAtlas();
        Animation animation = parent.animationManager.getStandingStillForwardsAnimation();
        player = new Player(this, parent.world, atlas, animation);
       // parent.renderManager.stage.addActor(player);
    }

    public void update(float elaspedTime){
        player.update(elaspedTime);

        //update all planets
        for(int i = 0; i < planets.size(); i++){
            planets.get(i).update(elaspedTime);
        }
    }

    public World getWorld(){
        return parent.world;
    }

    public Background getBackground(){
        return mainBackground;
    }

    public ArrayList<Planet> getPlanets(){
        return planets;
    }
}
