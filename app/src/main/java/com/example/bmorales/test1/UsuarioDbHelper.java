package com.example.bmorales.test1;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import android.text.format.Time;
import android.util.Log;

import com.example.bmorales.test1.UsuarioContract.UsuarioEntry;
import com.example.bmorales.test1.Usuario;


import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Random;

/**
 * Created by bmorales on 11/22/2016.
 */

public class UsuarioDbHelper extends SQLiteOpenHelper{

    public static final int DATABASE_VERSION = 1;

    public static final String DATABASE_NAME = "usuarios.db";

    public UsuarioDbHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
      //UsuarioEntry.SQL_DELETE_ENTRIES
        db.execSQL(" CREATE TABLE "  + UsuarioEntry.TABLE_NAME + " ("
                + UsuarioEntry.ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + UsuarioEntry.NAME + " TEXT NOT NULL,"
                + UsuarioEntry.EMAIL + " TEXT NOT NULL,"
                + UsuarioEntry.LAT + " TEXT NOT NULL,"
                + UsuarioEntry.LON + " TEXT NOT NULL,"
                + UsuarioEntry.TIME + " TEXT NOT NULL,"
                + UsuarioEntry.MESSAGE + " TEXT NOT NULL )"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public ContentValues toContentValues(Usuario user) {

        ContentValues values = new ContentValues();

        values.put(UsuarioEntry.NAME, user.getName());
        values.put(UsuarioEntry.EMAIL, user.getEmail());
        values.put(UsuarioEntry.LAT, user.getLat());
        values.put(UsuarioEntry.LON, user.getLon());
        values.put(UsuarioEntry.TIME, user.getTime());
        values.put(UsuarioEntry.MESSAGE, user.getMessage());
        return values;
    }

    public long saveUsuario(Usuario user) {
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();

        return sqLiteDatabase.insert(
                UsuarioEntry.TABLE_NAME,
                null,
                toContentValues(user));

    }

    public void dropALL(){

       String SQL_DELETE_ENTRIES =  "DELETE FROM " + UsuarioEntry.TABLE_NAME;
       SQLiteDatabase sqLiteDatabase = getWritableDatabase();
       sqLiteDatabase.execSQL(SQL_DELETE_ENTRIES);

    }


    public void bulkData() {
        Time now = new Time();
        now.setToNow();

        saveUsuario(new Usuario(null , "Carlos Perez", "saf@.dasf.com", "1111", "1111", now.toString(), "OK"));
        saveUsuario( new Usuario(null, "Daniel Samper", "saf@.dasf.com","1111", "1111", now.toString(), "OK"));
        saveUsuario( new Usuario(null, "Lucia Aristizabal", "saf@.dasf.com","1111", "1111", now.toString(), "OK"));
        saveUsuario( new Usuario(null, "Marina Acosta", "saf@.dasf.com","1111", "1111", now.toString(), "OK"));
        saveUsuario( new Usuario(null, "Olga Ortiz","saf@.dasf.com", "1111", "1111", now.toString(), "OK"));
        saveUsuario( new Usuario(null, "Pamela Briger", "saf@.dasf.com","1111", "1111", now.toString(), "OK"));
        saveUsuario( new Usuario(null, "Rodrigo Benavidez", "saf@.dasf.com","1111", "1111", now.toString(), "OK"));
        saveUsuario( new Usuario(null, "Tom Bonz", "saf@.dasf.com","1111", "1111", now.toString(), "OK"));
    }

    public String getNameRandom(){
        SQLiteDatabase db = getWritableDatabase();

        String columns[] = new String[]{UsuarioEntry.ID, UsuarioEntry.NAME};

        Cursor c = db.query(
                UsuarioEntry.TABLE_NAME,
                columns,
                null,
                null,
                null,
                null,
                null,
                null
        );

        Random ram = new Random();
        ArrayList<String> listItems = new ArrayList<String>();
        int tempInt = c.getCount();

        for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
            listItems.add(c.getString(1));
        }

        if(listItems.size() > 0){
           return  listItems.get(ram.nextInt(listItems.size()));
        }



        return "NO DATA";
    }


    public ArrayList<String> getListaString(){
        SQLiteDatabase db = getWritableDatabase();

        String columns[] = new String[]{UsuarioEntry.ID, UsuarioEntry.NAME, UsuarioEntry.EMAIL, UsuarioEntry.LAT, UsuarioEntry.LON, UsuarioEntry.TIME, UsuarioEntry.MESSAGE};

        Cursor c = db.query(
                UsuarioEntry.TABLE_NAME,
                columns,
                null,
                null,
                null,
                null,
                null
        );

        ArrayList<String> listItems = new ArrayList<String>();
        int tempInt = c.getCount();

        for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
            listItems.add(c.getString(1) +" :"+c.getString(2)+" :"+c.getString(3) + " :" + c.getString(4)  + " :" + c.getString(5)  + " :" + c.getString(6) );
        }



        if(listItems.size() == 0){
            listItems.add("NO DATA");
        }


        return listItems;
    }


    public Cursor getList(){
        SQLiteDatabase db = getWritableDatabase();

        String columns[] = new String[]{UsuarioEntry.ID, UsuarioEntry.NAME, UsuarioEntry.EMAIL, UsuarioEntry.LAT, UsuarioEntry.LON, UsuarioEntry.TIME, UsuarioEntry.MESSAGE};

        Cursor c = db.query(
                UsuarioEntry.TABLE_NAME,
                columns,
                null,
                null,
                null,
                null,
                null
        );


        return c;
    }

}
