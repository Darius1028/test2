package com.example.bmorales.test1;

import android.content.ContentValues;
import com.example.bmorales.test1.UsuarioContract.UsuarioEntry;


/**
 * Created by bmorales on 11/22/2016.
 */

public class Usuario {

    private String id;
    private String name;
    private String Lat;
    private String Lon;
    private String Time;

    public Usuario(String id, String name, String lat, String lon, String time){

        this.id = id;
        this.name = name;
        this.Lat = lat;
        this.Lon = lon;
        this.Time = time;
    }

    public String getId() {
        return id;
    }

    public String getName(){
        return name;
    }

    public String getLat(){ return  Lat;}

    public String getLon(){ return  Lon; }

    public String getLocation(){
        return " Lat: " + Lat + " Log: " + Lon;
    }

    public String getTime(){
        return Time;
    }

}
