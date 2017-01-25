package com.example.bmorales.test1;

import android.content.ContentValues;
import com.example.bmorales.test1.UsuarioContract.UsuarioEntry;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;


/**
 * Created by bmorales on 11/22/2016.
 */

public class Usuario {

    private String id;
    private String name;
    private String email;
    private String Lat;
    private String Lon;
    private String date;
    private String message;


    public Usuario(String id, String name, String email, String lat, String lon, String date, String message){

        this.id = id;
        this.name = name;
        this.email = email;
        this.Lat = lat;
        this.Lon = lon;
        this.date = date;
        this.message = message;
    }

    public String getId() {
        return id;
    }

    public String getName(){
        return name;
    }

    public String getEmail(){
        return email;
    }

    public String getLat(){ return  Lat;}

    public String getLon(){ return  Lon; }

    public String getLocation(){
        return " Lat: " + Lat + " Log: " + Lon;
    }

    public String getTime(){
        return date;
    }

    public String getMessage() { return  message; }


    public String toJSON(){

        Gson gson = new Gson();
        String json = gson.toJson(this);
        return json;

    }

}
