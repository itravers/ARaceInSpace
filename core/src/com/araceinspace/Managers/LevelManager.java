package com.araceinspace.Managers;

import com.araceinspace.GameObjectSubSystem.Components.PlayerState;
import com.araceinspace.GameObjectSubSystem.GameObject;
import com.araceinspace.GameObjectSubSystem.Ghost;
import com.araceinspace.GameObjectSubSystem.Planet;
import com.araceinspace.GameObjectSubSystem.Player;
import com.araceinspace.GameObjectSubSystem.PlayerPrototype;
import com.araceinspace.GameObjectSubSystem.SpriteTemplate;
import com.araceinspace.GameWorld;
import com.araceinspace.InputSubSystem.Action;
import com.araceinspace.misc.Background;
import com.badlogic.gdx.Gdx;
import com.araceinspace.misc.Animation;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;

import java.util.ArrayList;

/**
 * Created by Isaac Assegai on 7/11/17.
 * Loads and keeps track of the current level
 */
public class LevelManager {

    /* Static Variables */
    public static final short CATEGORY_PLAYER = -1;
    public static final short CATEGORY_PLANET = -2;
    public enum CHALLENGES {bronze, silver, gold};

    /* Field Variables & Objects */
    public GameWorld parent;
    private int currentLevel;
    private Player player;
    private Ghost ghost;
    private ArrayList<Planet> planets;
    public Background mainBackground;
    private Boolean levelGoalCompleted;
    private GameObject goal; /* The goal planet to land on */
    public CHALLENGES currentChallenge;

    /* Constructors */
    public LevelManager(GameWorld p){
        System.out.println("LevelManager Constructor");
        parent = p;
        //setLevel(1);//sets player, planets, background, etc.
       // setupTitleScreen();

    }

    /* Private Methods */

    private void setupGhost(CHALLENGES currentChallenge){
        Json json = new Json();
        ArrayList<SpriteTemplate>levelItems = json.fromJson(ArrayList.class, SpriteTemplate.class, getLevelFile(currentLevel));
        String fileName = "ghosts/level"+currentLevel + "-" + currentChallenge + "-ghost.json";
        boolean exists = Gdx.files.local(fileName).exists();
        if(!exists){
            System.out.println("File " + fileName + " does not exist, not making ghost.");
            return;
        }
        ArrayList<Action>actions = json.fromJson(ArrayList.class, Action.class, Gdx.files.local(fileName));//read an array list of JsonValues



        //go through all the level items, find the player item and initialze him
        for(int i = 0; i < levelItems.size(); i++){
            SpriteTemplate item = levelItems.get(i);
            if(item.getType().equals("player")){
                float xLoc = item.getxLoc();
                float yLoc = item.getyLoc();
                String extra = item.getExtraInfo();
                PlayerState state = PlayerState.STAND_STILL_FORWARD;
                if(extra.equals("flying"))state = PlayerState.FLYING;
                // TextureAtlas atlas = parent.animationManager.getStandingStillForwardsAtlas();
                TextureAtlas.AtlasRegion region = parent.animationManager.getHeroAtlas().findRegions("StandingStillForward/StandingStillForwar").first();
                Animation animation = parent.animationManager.getStandingStillForwardsAnimation();
                ghost = new Ghost(this, state, new Vector2(xLoc, yLoc), parent.world, region, animation, actions);
            }
        }
    }

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
                TextureAtlas.AtlasRegion region = parent.animationManager.getPlanetAtlas().getRegions().first();
                Animation animations = parent.animationManager.getPlanetAnimationFromName(item.getAtlas());
                Planet p = new Planet(new Vector2(item.getxLoc(), item.getyLoc()), region, animations, parent.world, item.getSize(), item.getGravityRadius(), item.getMass(), this);
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



    private void updateInGame(float elapsedTime){
        player.update(elapsedTime);
        if(ghost != null)ghost.update(elapsedTime);

        //update all planets
        for(int i = 0; i < planets.size(); i++){
            planets.get(i).update(elapsedTime);
        }
    }

    private void updateTitleScreen(float elapsedTime){
        updateInGame(elapsedTime);
    }

    /**
     * REads the levelx.json file for current level and designates the appropriate planet as the goal.
     */
    private void setupGoal(){
        int level = getCurrentLevel();
        if(level != 0){ //level 0 doesn't have a goal as it is the menu
            levelGoalCompleted = false;
            Json json = new Json();
            ArrayList<SpriteTemplate> levelItems = json.fromJson(ArrayList.class, SpriteTemplate.class,
                    getLevelFile(level));
            //read list of levelItems, creating a planet for every planet in the list.
            for(int i = 0; i < levelItems.size(); i++){
                SpriteTemplate item = levelItems.get(i);
                if(item.getType().equals("goal")){
                    int index = Integer.valueOf(item.getExtraInfo()); //The index of the goal planet defined in the levelx.json file
                    Planet p = planets.get(index);
                    goal = p;
                }
            }
        }
    }

    private void completeLevel(){
        setGoalCompleted(true);
        getPlayer().getInput().saveInputs("ghosts/level"+currentLevel + "-" + currentChallenge + "-ghost.json");
        parent.gameStateManager.setCurrentState(GameStateManager.GAME_STATE.SCOREBOARD);

    }

    private void setGoalCompleted(Boolean levelGoalCompleted) {
        this.levelGoalCompleted = levelGoalCompleted;
    }

    /**
     * Checks if we have completed the levels goal.
     * We have completed the goal if we are landed, and the
     * nearest planet is the goal planet.
     */
    public void checkGoal(Planet planet, PlayerPrototype p){
        if(!(p instanceof Player))return;
        //we only do this if the levelGoal has not been completed
        if(!levelGoalCompleted){

            boolean stateGood = p.getPhysics().onPlanet();
            boolean goalGood = (goal.equals(planet));
            boolean playerTypeGood = true; //(!(p instanceof Ghost));
            //we know the goal is completed if the player has landed on the goal planet and he is not a ghost.
            if(stateGood && goalGood && playerTypeGood){

                completeLevel();
            }
        }
    }

    /* Public Methods */
    public void setChallenge(CHALLENGES c){
        currentChallenge = c;
        setupGhost(currentChallenge);
    }

    public void setCurrentLevel(int level){
        currentLevel = level;
    }

    public int getCurrentLevel(){
        return currentLevel;
    }

    public GameObject getGoal(){
        return goal;
    }

    /**
     * Sets the level to the designated level.
     * Unload all resources used for the previous level.
     * Load all resources used for the set level
     * @param level The Level we are setting to
     */
    public void setLevel(int level){
        currentLevel = level;
        parent.setupPhysics();
        setGoalCompleted(false);
        setupPlayer();
       // setupBackground();
        setupPlanets();
        setupGoal();
    }

    /**
     * Returns the current player object
     */
    public Player getPlayer(){
        return player;
    }

    public Ghost getGhost(){
        return ghost;
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
        /*TextureAtlas atlas = parent.animationManager.getStandingStillForwardsAtlas();
        Animation animation = parent.animationManager.getStandingStillForwardsAnimation();
        player = new Player(this, parent.world, atlas, animation);
       // parent.renderManager.stage.addActor(player);
       */

        Json json = new Json();
        ArrayList<SpriteTemplate>levelItems = json.fromJson(ArrayList.class, SpriteTemplate.class, getLevelFile(currentLevel));
        //go through all the level items, find the player item and initialze him
        for(int i = 0; i < levelItems.size(); i++){
            SpriteTemplate item = levelItems.get(i);
            if(item.getType().equals("player")){
                float xLoc = item.getxLoc();
                float yLoc = item.getyLoc();
                String extra = item.getExtraInfo();
                PlayerState state = PlayerState.STAND_STILL_FORWARD;
                if(extra.equals("flying"))state = PlayerState.FLYING;
               // TextureAtlas atlas = parent.animationManager.getStandingStillForwardsAtlas();
                TextureAtlas.AtlasRegion region = parent.animationManager.getHeroAtlas().findRegions("StandingStillForward/StandingStillForwar").first();
                Animation animation = parent.animationManager.getStandingStillForwardsAnimation();
                player = new Player(this, state, new Vector2(xLoc, yLoc), parent.world, region, animation);
            }
        }
    }

    public void update(float elapsedTime){
        /* Update ingame, if we are actually INGAME */
        if(parent.gameStateManager.getCurrentState() == GameStateManager.GAME_STATE.INGAME){
           updateInGame(elapsedTime);
        }else if(parent.gameStateManager.getCurrentState() == GameStateManager.GAME_STATE.TITLE_SCREEN){
            updateTitleScreen(elapsedTime);
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
