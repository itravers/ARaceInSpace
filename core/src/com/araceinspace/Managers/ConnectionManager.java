package com.araceinspace.Managers;

import com.araceinspace.GameWorld;
import com.araceinspace.misc.CallBack;
import jdk.nashorn.internal.codegen.CompilerConstants;

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

    /* Boolean Field Variables */
    private boolean levelPackDownloaded = false;
    private boolean isDownloadingLevelPack = false;

    /**
     * Constructor
     * @param p
     */
    public ConnectionManager(GameWorld p){
        parent = p;
        httpManager = new HttpManager(this);
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
}
