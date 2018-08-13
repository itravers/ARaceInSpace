package com.araceinspace.Managers;

import com.araceinspace.GameWorld;
import com.araceinspace.misc.CallBack;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Array;
import jdk.nashorn.internal.codegen.CompilerConstants;

import java.util.*;

/**
 *  Created by Isaac Assegai on 8/11/18
 *  Connection Manager is the interface (not literally)
 *  used to manage the connection to the backend server.
 *  It asynchrously downloads, stores, and makes available
 *  data from the backend
 */
public class ConnectionManager {
    public GameWorld parent; //The GameWorld that Contains this Manager
    //Make this private before you are done
    public HttpManager httpManager; //Used to pull the data down from the server

    /*Field Variables */

    //update time
    float lastUpdateTime = 0;

    //Level Packs
    private boolean levelPackDownloaded = false;
    private boolean isDownloadingLevelPack = false;

    //level packs available
    private String levelPacksAvailable = ""; //on boot we get a space delimited string of the level packs available

    //level leaders
    HashMap<Integer, HashMap<String, Object>> levelLeadersMap;
    public static final float levelLeadersRefreshAge = 5; //5 minutes 300 secs

    /**
     * Constructor
     * @param p
     */
    public ConnectionManager(GameWorld p){
        System.out.println("ConnectionManager Constructed");
        parent = p;
        httpManager = new HttpManager(this);
        levelLeadersMap = new HashMap<Integer, HashMap<String, Object>>();

        //dl level packs available
        getLevelPacksAvailable(new CallBack() {
            @Override
            public void onCallBack(boolean success) {
                System.out.println("getLevelPacksAvailable CALLBACK");
                parent.prefs.putString("com.araceinspace.levelPacksAvailable", levelPacksAvailable);
            }
        });
        //dl level leaders for pack 0
        refreshLevelLeaders(0, new CallBack() {
            @Override
            public void onCallBack(boolean success) {
                levelLeadersCallBack(success);
            }
        });
    }

    private void levelLeadersCallBack(boolean success){
        System.out.println("getLevelLeaders() CALLBACK, success: " + success);
    }

    public ArrayList<String>getLevelLeaders(int levelPack){
        if(levelLeadersMap.containsKey(levelPack)){
            Float age = (Float)levelLeadersMap.get(levelPack).get("age");
            if(age > levelLeadersRefreshAge){
                refreshLevelLeaders(levelPack, null);
            }
            return (ArrayList<String>)levelLeadersMap.get(levelPack).get("data");
        }else{
            refreshLevelLeaders(levelPack, null);
            ArrayList<String> temp = new ArrayList<String>();
            for(int i = 0; i < 12; i++)temp.add("N/A");
            return temp;
        }
    }

    public void refreshLevelLeaders(int levelPack, CallBack callBack){
        //check the local data to see if it exists
        if(levelLeadersMap.containsKey(levelPack)){
            //it does exist, lets check and make sure it's not too old
            Float age = (Float)levelLeadersMap.get(levelPack).get("age");
            if(age > levelLeadersRefreshAge){
               getLevelLeadersHelper(levelPack, callBack);
            }else{
                //we do not do anything, no need to refresh the data
                //data = (ArrayList<String>)levelLeadersMap.get(levelPack).get("data");
            }
        }else{
            //we do not do anything, no need to refresh the data
            //data = (ArrayList<String>)levelLeadersMap.get(levelPack).get("data");
            getLevelLeadersHelper(levelPack, callBack);
        }
    }

    private void getLevelLeadersHelper(final int levelPack, final CallBack callBack){
        //start new thread here
        (new Thread() {
            public void run() {
                ArrayList<String> data = httpManager.downloadLevelLeaders(levelPack);
                HashMap<String, Object> levelLeaderInnerMap = new HashMap<String, Object>();
                levelLeaderInnerMap.put("age", new Float(0));
                levelLeaderInnerMap.put("data", data);
                levelLeadersMap.put(levelPack, levelLeaderInnerMap);
                if(callBack != null)callBack.onCallBack(true);
            }
        }).start();


    }

    /**
     * On Startup read all the level packs available into levelPacksAvailable
     * now we will run this list and return true if any element matches
     * levelPack
     * @param levelPack
     * @return
     */
    public boolean isLevelPackAvailable(int levelPack){
        if(levelPacksAvailable.contains(Integer.toString(levelPack))){
            return true;
        }else{
            return false;
        }
    }

    /**
     * First Called: Async queries the server for the level
     * packs available. Saves to local prefs
     * Second+ Call: Gets value from local prefs
     */
    public void getLevelPacksAvailable(final CallBack callBack){
        setLevelPacksAvailable(parent.prefs.getString("com.araceinspace.levelPacksAvailable"));
        (new Thread() {
            public void run() {
                boolean success = httpManager.downloadLevelPacksAvailable();
                if(success){
                    callBack.onCallBack(true);
                    System.out.println("downloaded levelPacksAvailable: " + levelPacksAvailable);
                }else{
                    callBack.onCallBack(false);
                    System.out.println("error downloading level packs available");
                }
            }
        }).start();
    }

    public synchronized void setLevelPacksAvailable(String s){
        levelPacksAvailable = s;
    }



    /**
     * Async Downloads a given level pack from the server
     * Level pack will be saved in android/assets/levels/packNum
     * Marks
     * @param levelPackToBuy
     * @return
     */
    public void downloadLevelPack(final int levelPackToBuy, final CallBack callBack){
        isDownloadingLevelPack = true;
        //Create a new thread to download the level pack
        (new Thread() {
            public void run() {
                boolean success = httpManager.dlLevelPackFromServer(levelPackToBuy);
                if(success){
                    setLevelPackDownloaded(true);

                    callBack.onCallBack(true);

                    System.out.println("downloaded level pack: " + levelPackToBuy);
                }else{
                    setLevelPackDownloaded(false);

                    callBack.onCallBack(false);
                    //setDownloadingLevelPack(false);//done download
                    System.out.println("error downloading level pack: " + levelPackToBuy);
                }
            }
        }).start();
    }

    /**
     * Sets the level back parameter to downloaded
     * @param dld
     */
    private synchronized void setLevelPackDownloaded(boolean dld){
        levelPackDownloaded = dld;
    }

    /**
     * Checks to see if the level pack has been downloaded
     * @return
     */
    public boolean isLevelPackDownloaded(){
        return levelPackDownloaded;
    }

    public synchronized void setDownloadingLevelPack(boolean b){
        isDownloadingLevelPack = b;
    }

    /**
     * Checks to see if the level pack is currently downloading
     * @return
     */
    public boolean isDownloadingLevelPack() { return isDownloadingLevelPack; }

    public void update(float gameTime){
        //System.out.println("gametime: " + gameTime);
        float timeElapsed = gameTime - lastUpdateTime;
        //only update once a second
        if(timeElapsed > 2){
            if(timeElapsed > 100)timeElapsed = 2;
            //update the times in all the data that time's out

            //level leaders times
            Iterator it = levelLeadersMap.entrySet().iterator();
            while(it.hasNext()){
                Map.Entry<Integer, HashMap<String, Object>> entry = (Map.Entry<Integer, HashMap<String, Object>>) it.next();
                int lvlPack = Integer.valueOf(entry.getKey());
                entry.getKey();
                Float age = (Float)entry.getValue().get("age");
                age += timeElapsed;
                entry.getValue().replace("age", age);
                if(age > levelLeadersRefreshAge){
                    System.out.println("refreshing levelLeaders: " + lvlPack);
                    refreshLevelLeaders(lvlPack, new CallBack() {
                        @Override
                        public void onCallBack(boolean success) {
                            levelLeadersCallBack(success);
                        }
                    });
                }
            }

            lastUpdateTime = gameTime;
        }
    }
}
