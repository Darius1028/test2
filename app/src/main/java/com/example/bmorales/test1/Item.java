package com.example.bmorales.test1;

import com.google.gson.Gson;


/**
 * Created by bmorales on 11/22/2016.
 */

public class Item {

    private String id;
    private String titulo;
    private String descripcion;
    private String Lat;
    private String Lon;
    private String date;
    private String path;


    public Item(String id, String titulo, String descripcion, String lat, String lon, String date, String path){
        this.id = id;
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.Lat = lat;
        this.Lon = lon;
        this.date = date;
        this.path = path;
    }


    public String getId() {
        return id;
    }

    public String getTitle(){
        return titulo;
    }

    public String getDescripcion(){
        return descripcion;
    }

    public String getLat(){ return  Lat;}

    public String getLon(){ return  Lon; }

    public String getLocation(){
        return " Lat: " + Lat + " Log: " + Lon;
    }

    public String getTime(){
        return date;
    }

    public String getPath() { return  path; }


    public String toJSON(){

        Gson gson = new Gson();
        String json = gson.toJson(this);
        return json;

    }

}
