package com.araceinspace.Managers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Net;
import com.badlogic.gdx.net.HttpStatus;
import com.badlogic.gdx.utils.Json;

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
    public  void sendRequest(String url, Object requestObject, String method) {

        final Json json = new Json();

        String requestJson = json.toJson(requestObject); // this is just an example

        Net.HttpRequest request = new Net.HttpRequest(method);
        request.setUrl(url);

        request.setContent(requestJson);

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
}
