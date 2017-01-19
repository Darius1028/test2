package com.example.bmorales.test1;

/**
 * Created by bmorales on 1/17/2017.
 */

public class postImagen {
    private String id;
    private String idUser;
    private String Lat;
    private String Lon;
    private String date;
    private String message;


    public postImagen(String id, String idUser, String name, String email, String lat, String lon, String date, String message){

        this.id = id;
        this.idUser = idUser;
        this.Lat = lat;
        this.Lon = lon;
        this.date = date;
        this.message = message;
    }

    public String getId() {
        return id;
    }

    public String getIdUser(){ return idUser; }

    public String getLat(){ return  Lat;}

    public String getLon(){ return  Lon; }

    public String getLocation(){
        return " Lat: " + Lat + " Log: " + Lon;
    }

    public String getTime(){
        return date;
    }

    public String getMessage() { return  message; }


}
