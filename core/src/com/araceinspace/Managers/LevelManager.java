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
import com.araceinspace.InputSubSystem.KeyAction;
import com.araceinspace.misc.Background;
import com.badlogic.gdx.Gdx;
import com.araceinspace.misc.Animation;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.util.ArrayList;
import java.util.StringTokenizer;

/**
 * Created by Isaac Assegai on 7/11/17.
 * Loads and keeps track of the current level
 */
public class LevelManager {

    /* Static Variables */
    public static final short CATEGORY_PLAYER = -1;
    public static final short CATEGORY_PLANET = -2;
    public enum CHALLENGES {bronze, silver, gold, first, second, third, custom};

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
    public int playerTime;
    public int ghostTime;
    public boolean didFail; // set to true if the player exploads in contact listener checked by score screen
    String levelStyle = "introtest"; //used by dialog manager to figure out what picture to load

    //Tracks the current level pack that we want to look at
    public int currentLevelPack = 0;
    public static int levelPerPack = 12;
    public boolean nextLevelPackUnlocked;

    /* Constructors */
    public LevelManager(GameWorld p){
        System.out.println("LevelManager Constructor");
        parent = p;
        //setLevel(1);//sets player, planets, background, etc.
       // setupTitleScreen();

    }

    /* Private Methods */

    public void setupGhostFromJson(String ghostJson){
        System.out.println("SETUPGHOST");
        ArrayList<Action>actions;
        ArrayList<SpriteTemplate>levelItems;
        Json json = new Json();

        // String ghostJson = parent.httpManager.readGhostFromServer(currentChallenge, currentLevel);//reads the ghost file from the server backend instead of locally
        levelItems = json.fromJson(ArrayList.class, SpriteTemplate.class, getLevelFile(currentLevel));
        if(ghostJson == null){//ghost json was not found on server, return ghost=null ghost will not be displayed this level
            ghost = null;
            return;
        }
        actions = json.fromJson(ArrayList.class, Action.class, ghostJson);//read an array list of JsonValues

        //go through all the level items, find the player item and initialze him
        for(int i = 0; i < levelItems.size(); i++){
            SpriteTemplate item = levelItems.get(i);
            if(item.getType().equals("player")){
                float xLoc = item.getxLoc();
                float yLoc = item.getyLoc();
                String extra = item.getExtraInfo();
                PlayerState state = PlayerState.STAND_STILL_FORWARD;
                if(extra.equals("flying"))state = PlayerState.FLYING;
                // TextureAtlas atlas = parent.resourceManager.getStandingStillForwardsAtlas();
                TextureAtlas.AtlasRegion region = parent.resourceManager.getHeroAtlas().findRegions("StandingStillForward/StandingStillForwar").first();
                Animation animation = parent.resourceManager.getStandingStillForwardsAnimation();
                ghost = new Ghost(this, state, new Vector2(xLoc, yLoc), parent.world, region, animation, actions);
            }
        }
        ghostTime =  ghost.getInput().getPlayTime();
    }


    private void setupGhost(CHALLENGES currentChallenge){
        System.out.println("SETUPGHOST");
        ArrayList<Action>actions;
        ArrayList<SpriteTemplate>levelItems;
        Json json = new Json();
        if(currentChallenge == CHALLENGES.first || currentChallenge == CHALLENGES.second || currentChallenge == CHALLENGES.third){
            String ghostJson = parent.connectionManager.httpManager.readGhostFromServer(currentChallenge, currentLevel);//reads the ghost file from the server backend instead of locally
            levelItems = json.fromJson(ArrayList.class, SpriteTemplate.class, getLevelFile(currentLevel));
            if(ghostJson == null){//ghost json was not found on server, return ghost=null ghost will not be displayed this level
                ghost = null;
                return;
            }
            actions = json.fromJson(ArrayList.class, Action.class, ghostJson);//read an array list of JsonValues


        }else{

            levelItems = json.fromJson(ArrayList.class, SpriteTemplate.class, getLevelFile(currentLevel));
            String fileName = "levels/"+currentLevelPack+"/level"+currentLevel + "-" + currentChallenge + "-ghost.json";
            boolean exists = Gdx.files.internal(fileName).exists();
            if(!exists){
                System.out.println("File " + fileName + " does not exist, not making ghost.");
                ghost = null;
                return;
            }
            actions = json.fromJson(ArrayList.class, Action.class, Gdx.files.internal(fileName));//read an array list of JsonValues
        }

        //go through all the level items, find the player item and initialze him
        for(int i = 0; i < levelItems.size(); i++){
            SpriteTemplate item = levelItems.get(i);
            if(item.getType().equals("player")){
                float xLoc = item.getxLoc();
                float yLoc = item.getyLoc();
                String extra = item.getExtraInfo();
                PlayerState state = PlayerState.STAND_STILL_FORWARD;
                if(extra.equals("flying"))state = PlayerState.FLYING;
                // TextureAtlas atlas = parent.resourceManager.getStandingStillForwardsAtlas();
                TextureAtlas.AtlasRegion region = parent.resourceManager.getHeroAtlas().findRegions("StandingStillForward/StandingStillForwar").first();
                Animation animation = parent.resourceManager.getStandingStillForwardsAnimation();
                ghost = new Ghost(this, state, new Vector2(xLoc, yLoc), parent.world, region, animation, actions);
            }
        }
       ghostTime =  ghost.getInput().getPlayTime();
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
               // TextureAtlas.AtlasRegion region = parent.resourceManager.getPlanetAtlas().getRegions().first();
                //Animation animations = parent.resourceManager.getPlanetAnimationFromName(item.getAtlas());
               // Planet p = new Planet(new Vector2(item.getxLoc(), item.getyLoc()), region, animations, parent.world, item.getSize(), item.getGravityRadius(), item.getMass(), this);
               // Planet p = new Planet(new Vector2(item.getxLoc()-item.getSize()/2, item.getyLoc()-item.getSize()/2), region, animations, parent.world, item.getSize(), item.getGravityRadius(), item.getMass(), this);
                String atlasName = item.getAtlas();
                TextureAtlas.AtlasRegion region = parent.resourceManager.getHeroAtlas().findRegions("Planets/"+atlasName).first();
                Animation animation = parent.resourceManager.getPlanetAnimationByAtlasName(atlasName);
                Planet p = new Planet(new Vector2(item.getxLoc() - item.getSize()/2, item.getyLoc()-item.getSize()/2), region, animation, parent.world, item.getSize(), item.getGravityRadius(), item.getMass(), this);
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
        //first we need to parse the lvl number to a
        //level pack - level
        int levelPack = (lvl-1) / levelPerPack;//need to get this number from variable
        FileHandle fileHandle = null;
        String fileName = "levels/"+levelPack+"/level"+lvl+".json";
        if(Gdx.files.classpath(fileName).exists()){
            fileHandle = Gdx.files.internal(fileName);
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

    private void setChallengeCompleted(){
        if(currentChallenge == CHALLENGES.bronze || currentChallenge == CHALLENGES.silver || currentChallenge == CHALLENGES.gold){
            int level = getCurrentLevel();
            String levelName = "Level "+level;
            String challengeName = getChallengeName(currentChallenge);
            // parent.setBoolean("com.araceinspace."+level+"bronze", false)
            parent.prefs.putBoolean("com.araceinspace.Saved_Items."+levelName+""+challengeName, true);
            parent.prefs.flush();
        }
    }

    private String getChallengeName(CHALLENGES c){
        String returnVal = "";
        if(c == CHALLENGES.bronze){
            returnVal = "bronze";
        }else if(c == CHALLENGES.silver){
            returnVal = "silver";
        }else if(c == CHALLENGES.gold){
            returnVal = "gold";
        }
        return returnVal;
    }

    private void completeLevel(){

        setGoalCompleted(true);
        System.out.println("play time: " + getPlayer().getPlayTime());
        //int playtime = (int)(getPlayer().getPlayTime()*1000);
        playerTime = (int)(getPlayer().getPlayTime()*1000);
        if(ghost == null){
            //Save Replay if no ghost exists
            getPlayer().getInput().saveInputs("levels/"+currentLevelPack+"/level"+currentLevel + "-" + currentChallenge + "-ghost.json", playerTime, parent.playerName);
        }else{
           ghostTime = ghost.playtime;
            if(playerTime < ghostTime){
                //Save Replay if ghost exists, and playerTime is less than ghost time.
                //getPlayer().getInput().saveInputs("levels/level"+currentLevel + "-" + currentChallenge + "-ghost.json", playerTime);
                setChallengeCompleted();
                parent.setCoins(parent.getCoins() + 5);
                saveBestTime(playerTime);
            }
        }
        parent.gameStateManager.setCurrentState(GameStateManager.GAME_STATE.SCOREBOARD);
    }

    /**
     * Checks the prefs to see if we beat our best time at a bronze, silver, or gold challenge
     * if we did, we save the new time
     * @param playerTime
     */
    private void saveBestTime(int playerTime){
        String challengeString = "";
        switch (currentChallenge){
            case bronze:
                challengeString = "bronze";
                break;
            case silver:
                challengeString = "silver";
                break;
            case gold:
                challengeString = "gold";
                break;
            case first:
            case second:
            case third:
                return;
        }

        int previousBest = parent.prefs.getInteger("com.araceinspace.level"+currentLevel+"."+challengeString+".time", 99999999);
        if(playerTime < previousBest){
            parent.prefs.putInteger("com.araceinspace.level"+currentLevel+"."+challengeString+".time", playerTime);
            parent.prefs.flush();
        }

    }

    public String getGhostReplay(){
        playerTime = (int)(getPlayer().getPlayTime()*1000);
        return getPlayer().getInput().getReplay(playerTime, parent.playerName);
    }

    private void setGoalCompleted(Boolean levelGoalCompleted) {
        this.levelGoalCompleted = levelGoalCompleted;
    }

    /**
     * Checks if we have completed the levels goal.
     * We have completed the goal if we are landed, and the
     * nearest planet is the goal planet.
     */
    public void checkGoal(Planet planet, PlayerPrototype p, float timeElapsed){
        if(!(p instanceof Player)){
            p.setUpdateable(false);
            return;
        }
        //we only do this if the levelGoal has not been completed
        if(!levelGoalCompleted){

            boolean stateGood = p.getPhysics().onPlanet();
            boolean goalGood = (goal.equals(planet));
            boolean playerTypeGood = true; //(!(p instanceof Ghost));
            //we know the goal is completed if the player has landed on the goal planet and he is not a ghost.
            if(stateGood && goalGood && playerTypeGood){
                p.endTime = timeElapsed;
                completeLevel();
            }
        }
    }

    /* Public Methods */
    public void setChallenge(CHALLENGES c){
        currentChallenge = c;
        setupGhost(currentChallenge);
    }

    public CHALLENGES getCurrentChallenge(){
        return currentChallenge;
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
        ghostTime = 0;
        playerTime = 0;
        currentLevel = level;
        parent.setupPhysics();
        setGoalCompleted(false);
        setupPlayer();
       // setupBackground();
        setupPlanets();
        setupGoal();
        currentLevelPack = level / levelPerPack;
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
        /*TextureAtlas atlas = parent.resourceManager.getStandingStillForwardsAtlas();
        Animation animation = parent.resourceManager.getStandingStillForwardsAnimation();
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
               // TextureAtlas atlas = parent.resourceManager.getStandingStillForwardsAtlas();
                TextureAtlas.AtlasRegion region = parent.resourceManager.getHeroAtlas().findRegions("StandingStillForward/StandingStillForwar").first();
                Animation animation = parent.resourceManager.getStandingStillForwardsAnimation();
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

    /**
     * Returns a 3 member array list of booleans
     * 0 - true means we have completed bronze challenge
     * 1 - true means we have completed silver challenge
     * 2 - true means we have completed gold challenge
     * This will be used to decide if we show the corresponding star, or an empty star in the level
     * select button.
     * @param level
     * @return
     */
    public ArrayList<Boolean>getLevelStars(String level){
        ArrayList<Boolean>returnVal = new ArrayList<Boolean>();
        returnVal.add(parent.prefs.getBoolean("com.araceinspace.Saved_Items."+level+"bronze", false));
        returnVal.add(parent.prefs.getBoolean("com.araceinspace.Saved_Items."+level+"silver", false));
        returnVal.add(parent.prefs.getBoolean("com.araceinspace.Saved_Items."+level+"gold", false));
        return returnVal;
    }
    public void playGame(Skin skin, Stage stage, Viewport viewport){
        parent.dialogManager.setupLevelIntroDialog(parent.levelManager.getCurrentLevel(), skin, stage, viewport);
    }

    /**
     * Looks into the level file to get subtitle information
     * This is utilized by level intro dialog to show player
     * level information from the designer
     * @param levelToGet
     * @return
     */
    public ArrayList<String>getLevelsSubtitles(int levelToGet){
        ArrayList<String>subtitles = new ArrayList<String>();
        Json json = new Json();
        ArrayList<SpriteTemplate>levelItems = json.fromJson(ArrayList.class, SpriteTemplate.class, getLevelFile(levelToGet));
        for(int i = 0; i < levelItems.size(); i++){
            if(levelItems.get(i).getType().equals("subtitle")){
                SpriteTemplate item = levelItems.get(i);
                String subtitlesString = item.getExtraInfo();
                String[] result = subtitlesString.split("-");
                for (int j = 0; j < result.length; j++){
                    subtitles.add(result[j]);
                }
            }
        }

        //Initialize all subtitles to something if the level file doesn't include it
        if(subtitles.isEmpty()){
            subtitles.add("The Level Designer");
            subtitles.add("Forgot to include");
            subtitles.add("subtitle information");
            subtitles.add("for this level");
        }
        return subtitles;
    }

    /**
     * Checks the player preferences to see if the given level pack is unlocked
     * @param levelPack
     * @return
     */
    public boolean isLevelPackUnlocked(int levelPack){
        boolean returnVal = true;
        //The first level pack is always unlocked
        if(levelPack == 0){
            returnVal = true;
        }else{
            //check prefs to see if this levelPack is unlocked
            returnVal = parent.prefs.getBoolean("com.araceinspace.levelPackUnlocked."+levelPack);
        }
        return returnVal;
    }
}
