package com.araceinspace.Managers;

import com.araceinspace.GameWorld;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Net;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.net.HttpStatus;
import com.badlogic.gdx.utils.ByteArray;

import java.io.*;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.StringTokenizer;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * Created by Isaac Assegai on 7/31/17.
 * Class is to be used to make http post and get requests to
 * the leaderboard, ghost and level pack backend.
 */
public class HttpManager {
    GameWorld parent;

    boolean responseReady; //Used by waitforresponse
    boolean requestFailed; //Used to judge if the last request failed
    String responseJson; //Used when we want to get JSON from the backend
    byte [] responseBytes; //Used when we want to get pure data from the backend
    String levelPacksAvailable; //on boot we get a space delimited string of the level packs available


    public HttpManager(GameWorld parent){
        this.parent = parent;
        responseReady = false;
        requestFailed = false;
        setup();
    }

    private void setup(){
        getLevelPacksAvailable();
    }

    /**
     * Sending a request to the backend, expecting a string/json reponse
     * @param url
     * @param requestJson
     * @param method
     */
    public void sendRequest(String url, String requestJson, String method){
        sendRequest(url, requestJson, method, false);
    }

    /**
     * Sending a request to the backend
     * @param url
     * @param requestJson
     * @param method
     * @param getBytes //should this return data in responseBytes(true) or responseJSON(false)
     */
    public  void sendRequest(String url, String requestJson, String method, final boolean getBytes) {
        Net.HttpRequest request = new Net.HttpRequest(method);
        request.setUrl(url);

        if(requestJson != null){
            request.setContent(requestJson);
        }

        request.setHeader("Content-Type", "application/json");
        request.setHeader("Accept", "application/json");

        //Send the http request
        Gdx.net.sendHttpRequest(request, new Net.HttpResponseListener() {
            public void handleHttpResponse(Net.HttpResponse httpResponse) {
                int statusCode = httpResponse.getStatus().getStatusCode();
                if(statusCode != HttpStatus.SC_OK) {
                    System.out.println("Request Failed");
                    requestFailed = true;
                    return;
                }

                if(getBytes){
                    responseBytes = httpResponse.getResult();
                }else{
                    responseJson = httpResponse.getResultAsString();
                }

                responseReady = true;
                try {
                    //DO some stuff with the response string
                }
                catch(Exception exception) {
                    exception.printStackTrace();
                }
            }

            public void failed(Throwable t) {
                System.out.println("Request Failed Completely");
                requestFailed = true;
            }

            @Override
            public void cancelled() {
                System.out.println("request cancelled");
                requestFailed = true;
            }
        });
    }

    /**
     * Makes thread sleep until a reponse has come back from server
     * @return
     */
    public String waitForResponse(){
        while(!responseReady){
            if(requestFailed){
                requestFailed = false;
                return null;
            }
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        responseReady = false; //reset responseready boolean
        return responseJson;
    }

    /**
     * Makes thread sleep until a response has come back from server
     * and we expecting pure data
     * @return
     */
    public byte[] waitForResponse_b(){
        while(!responseReady){
            if(requestFailed){
                requestFailed = false;
                return null;
            }
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        responseReady = false; //reset responseready boolean
        return responseBytes;
    }

    /**
     * Gets the fastest ghost from a specific challenge and level
     * @param currentChallenge
     * @param currentLevel
     * @return
     */
    public String readGhostFromServer(LevelManager.CHALLENGES currentChallenge, int currentLevel){
        String returnval = "";
        int place = 0;
        switch(currentChallenge){
            case first:
                    place = 1;
                break;
            case second:
                place = 2;
                break;
            case third:
                place = 3;
                break;
        }
        String url = "http://192.168.1.197:3001/leaderboards/getghost/"+currentLevel+"/"+place;
        sendRequest(url, null, "GET");
        returnval = waitForResponse();
        if( returnval == null  || returnval.startsWith("no ghost found")){
            //ghost was not found on server for some reason, we want to read default ghost from local file system
            String fileName = "ghosts/level"+currentLevel + "-default-ghost.json";
            returnval = Gdx.files.internal(fileName).readString();
        }
        return returnval;
    }

    /**
     * Sets up the levelPacksAvailable String
     * First it checks the local record of
     * level packs that are available. This is to allow
     * the client to display previously dl'd level packs when offline.
     * Second it asks the server what record packs are available.
     * if the server responds it overwrites the local records
     */
    private void getLevelPacksAvailable(){

        levelPacksAvailable = parent.prefs.getString("com.araceinspace.levelPacksAvailable");

        String url = "http://192.168.1.197:3001/levelpacks/packsAvailable";
        sendRequest(url, null, "GET");
        String response = waitForResponse();
        if(response != null){
            levelPacksAvailable = response;
            parent.prefs.putString("com.araceinspace.levelPacksAvailable", levelPacksAvailable);
        }

        System.out.println("levelPacksAvailable: " + levelPacksAvailable);
    }

    /**
     * Gets a specific ghost from the server based on it's ghostID
     * @param ghostID
     * @param level
     * @return
     */
    public String readCustomGhostFromServer(String ghostID, int level){
        String returnval = "";
        String url = "http://192.168.1.197:3001/leaderboards/customghost/"+ghostID+"/"+level;
        sendRequest(url, null, "GET");
        returnval = waitForResponse();
        return returnval;
    }

    /**
     * Gets the leaderboards for a specific levelPack from the server
     * @param levelPack
     * @return
     */
    public String readLeaderBoardFromServer(int levelPack){
        String returnval = "";
        String url = "http://192.168.1.197:3001/leaderboards/json/"+levelPack;
        sendRequest(url, null, "GET");
        returnval = waitForResponse();
        return returnval;
    }

    /**
     * Submits a score to the server for a given level, place, with a name, and a time
     * @param level
     * @param place
     * @param name
     * @param time
     * @return
     */
    public String submitScore(int level, int place, String name, int time){
        String returnval = "";
        String url = "http://192.168.1.197:3001/leaderboards/update/"+level+"/"+place+"/"+name+"/"+time;
        sendRequest(url, null, "GET");
        returnval = waitForResponse();
        return returnval;
    }

    /**
     * Submits a ghost replay to the server
     * @param replay
     * @param id
     * @param level
     * @param place
     * @param name
     * @param time
     * @return
     */
    public String submitGhostReplay(String replay, String id, int level, int place, String name, int time){
        System.out.println("Submit Ghost");
        String returnval = "";
        String url = "http://192.168.1.197:3001/leaderboards/submitGhost/"+id+"/"+level+"/"+place+"/"+time+"/"+name;
        sendRequest(url, replay, "POST");
        returnval = waitForResponse();
        return returnval;
    }

    /**
     * Returns the level leaders for a specific level pack
     * @param levelPack
     * @return
     */
    public ArrayList<String> getLevelLeaders(int levelPack){
        ArrayList<String>returnVal = new ArrayList<String>();
        String url = "http://192.168.1.197:3001/leaderboards/levelleaders/"+levelPack;
        sendRequest(url, null, "GET");
        String leadersString = waitForResponse();

        //check for errors, and return a correct data structure filled with error data
        if(leadersString == null || leadersString.contains("MongoError") || leadersString.contains("TypeError") || leadersString.contains("error no Level Pack")){
            for(int i = 0; i < 12; i++)returnVal.add("N/A");
            return returnVal;
        }

        StringTokenizer tokenizer = new StringTokenizer(leadersString, ":");

        while(tokenizer.hasMoreTokens()){
            returnVal.add(tokenizer.nextToken());
        }
        return returnVal;
    }

    /**
     * Sends a request to server for a certain
     * level pack. Downloads that level pack
     * to a useable place for the player
     */
    public boolean dlLevelPackFromServer(int levelPackToBuy){
        String url = "http://192.168.1.197:3001/levelpacks/get/"+levelPackToBuy;
        sendRequest(url, null, "GET", true);
        byte[] levelPack = waitForResponse_b();
        if(false){
        }else{
            if(levelPack == null)return false;//didn't work something is wrong
            ZipInputStream zis = new ZipInputStream(new ByteArrayInputStream(levelPack));
            ZipEntry zipEntry;

            try {
                zipEntry = zis.getNextEntry();
                while(zipEntry != null){
                    byte[] buffer = new byte[1024];
                    String fileName = zipEntry.getName();
                    FileHandle file;

                    if(fileName.contains(".swp")){
                        continue; //don't do anything with a swap file
                    }else if(fileName.contains("ghost")){
                        //this is a ghost file and should be saved under "ghosts/levelpack/filename
                        file = Gdx.files.local("ghosts/"+levelPackToBuy+"/"+fileName);
                    }else{
                        //this is a level file and should be saved under "levels/levelpack/filename
                       file = Gdx.files.local("levels/"+levelPackToBuy+"/"+fileName);
                    }

                    int len;
                    file.writeString("", false); //causes the  file to be blanked out

                    while ((len = zis.read(buffer)) > 0) {
                        file.writeBytes(buffer, 0, len, true); //actually writing the file
                    }

                    zipEntry = zis.getNextEntry();
                }
                return true;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return true;
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
}