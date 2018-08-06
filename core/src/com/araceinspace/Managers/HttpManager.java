package com.araceinspace.Managers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Net;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.net.HttpStatus;
import com.badlogic.gdx.utils.ByteArray;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonWriter;

import java.io.*;
import java.util.ArrayList;
import java.util.StringTokenizer;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * Created by Isaac Assegai on 7/31/17.
 * Class is to be used to make http post and get requests to scoreboard and ghost backend.
 */
public class HttpManager {
    boolean responseReady;
    boolean requestFailed;
    String responseJson;
    byte [] responseBytes;

    public HttpManager(){
        responseReady = false;
        requestFailed = false;
    }

    public void sendRequest(String url, String requestJson, String method){
        sendRequest(url, requestJson, method, false);
    }

    public  void sendRequest(String url, String requestJson, String method, final boolean getBytes) {
        //System.out.println("httpManger: send request to: " + url);

      //  final Json json = new Json();

       // String requestJson = json.toJson(requestObject); // this is just an example

        Net.HttpRequest request = new Net.HttpRequest(method);
        request.setUrl(url);

        if(requestJson != null){
            request.setContent(requestJson);
        }


        request.setHeader("Content-Type", "application/json");
        request.setHeader("Accept", "application/json");

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

    public String readGhostFromServer(LevelManager.CHALLENGES currentChallenge, int currentLevel){
        String returnval = "";
        //Gdx.net.openURI("192.168.1.197");
        //String url = "http://192.168.1.197/html/araceinspace/";
       // url += "level"+currentLevel + "-" + currentChallenge + "-ghost.json";
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

    public String readCustomGhostFromServer(String ghostID, int level){
        String returnval = "";
        String url = "http://192.168.1.197:3001/leaderboards/customghost/"+ghostID+"/"+level;
        sendRequest(url, null, "GET");
        returnval = waitForResponse();
        return returnval;
    }

    public String readLeaderBoardFromServer(int levelPack){
        String returnval = "";
        //Gdx.net.openURI("192.168.1.197");
        String url = "http://192.168.1.197:3001/leaderboards/json/"+levelPack;

        sendRequest(url, null, "GET");
        returnval = waitForResponse();
        return returnval;
    }

    public String submitScore(int level, int place, String name, int time){
        String returnval = "";
        //Gdx.net.openURI("192.168.1.197");
        String url = "http://192.168.1.197:3001/leaderboards/update/"+level+"/"+place+"/"+name+"/"+time;

        sendRequest(url, null, "GET");
        returnval = waitForResponse();
        return returnval;
    }

    public String submitGhostReplay(String replay, String id, int level, int place, String name, int time){
        System.out.println("Submit Ghost");
        String returnval = "";
        //Gdx.net.openURI("192.168.1.197");
        String url = "http://192.168.1.197:3001/leaderboards/submitGhost/"+id+"/"+level+"/"+place+"/"+time+"/"+name;
        //Json json = new Json(JsonWriter.OutputType.json);
        //replay = json.toJson("test");

        sendRequest(url, replay, "POST");
        returnval = waitForResponse();
        return returnval;
    }

    public ArrayList<String> getLevelLeaders(int levelPack){
        ArrayList<String>returnVal = new ArrayList<String>();
        String url = "http://192.168.1.197:3001/leaderboards/levelleaders/"+levelPack;
        sendRequest(url, null, "GET");
        String leadersString = waitForResponse();
        if(leadersString == null || leadersString.contains("MongoError") || leadersString.contains("TypeError")){
            for(int i = 0; i < 12; i++)returnVal.add("N/A");
            return returnVal;
        }

        System.out.println("getLevelLeaders(): " + leadersString);
        StringTokenizer tokenizer = new StringTokenizer(leadersString, ":");

        while(tokenizer.hasMoreTokens()){
            returnVal.add(tokenizer.nextToken());
        }
        //test Array
       // for(int i = 0; i < 12; i++)returnVal.add("Slack");
        return returnVal;
    }

    /**
     * Sends a request to server for a certain
     * level pack. Downloads that level pack
     * to a useable place for the player
     */
    public boolean dlLevelPackFromServer(int levelPackToBuy){
        String url = "http://192.168.1.197:3001/levelpacks/"+levelPackToBuy;
        sendRequest(url, null, "GET", true);
        //String s_levelPack = waitForResponse();
        byte[] levelPack = waitForResponse_b();
        if(false){
           // for(int i = 0; i < 12; i++)returnVal.add("N/A");
            //return returnVal;
            //error here
        }else{
            //byte[] levelPack = s_levelPack.getBytes();
            byte[] buffer = new byte[1024];
            if(levelPack == null)return false;//didn't work something is wrong
            ZipInputStream zis = new ZipInputStream(new ByteArrayInputStream(levelPack));
            ZipEntry zipEntry;

            try {
                zipEntry = zis.getNextEntry();
                while(zipEntry != null){
                    String fileName = zipEntry.getName();
                    System.out.println(System.getProperty("user.dir"));
                    //File newFile = new File("unzipTest/" + fileName);
                   // FileOutputStream fos = new FileOutputStream(newFile);
                    FileHandle file = Gdx.files.local("levels/"+levelPackToBuy+"/"+fileName);

                   // file.writeString("My god, it's full of stars", false);
                    int len;
                    while ((len = zis.read(buffer)) > 0) {
                       // fos.write(buffer, 0, len);
                        //file.writeBytes(buffer, true);
                        file.writeBytes(buffer, 0, len, true);
                    }
                    //fos.close();

                    zipEntry = zis.getNextEntry();
                }
                return true;
            } catch (IOException e) {
                e.printStackTrace();
            }






        }
        return true;
    }
}
