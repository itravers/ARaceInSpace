package com.araceinspace.Managers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Net;
import com.badlogic.gdx.net.HttpStatus;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonWriter;

/**
 * Created by Isaac Assegai on 7/31/17.
 * Class is to be used to make http post and get requests to scoreboard and ghost backend.
 */
public class HttpManager {
    boolean responseReady;
    boolean requestFailed;
    String responseJson;

    public HttpManager(){
        responseReady = false;
        requestFailed = false;
    }
    public  void sendRequest(String url, String requestJson, String method) {

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

                responseJson = httpResponse.getResultAsString();
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
            returnval = Gdx.files.local(fileName).readString();
        }
        return returnval;
    }

    public String readLeaderBoardFromServer(){
        String returnval = "";
        //Gdx.net.openURI("192.168.1.197");
        String url = "http://192.168.1.197:3001/leaderboards/json/";

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
}
