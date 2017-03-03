package com.example.bmorales.test1;

import android.content.ClipData;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.text.format.Time;

import com.example.bmorales.test1.ItemContract.ItemEntry;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by bmorales on 11/22/2016.
 */

public class ItemDbHelper extends SQLiteOpenHelper{

    public static final int DATABASE_VERSION = 1;

    public static final String DATABASE_NAME = "items.db";

    public ItemDbHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
      //UsuarioEntry.SQL_DELETE_ENTRIES
        db.execSQL(" CREATE TABLE "  + ItemEntry.TABLE_NAME + " ("
                + ItemEntry.ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + ItemEntry.TITULO + " TEXT NOT NULL,"
                + ItemEntry.DESCRIPCION + " TEXT NOT NULL,"
                + ItemEntry.LAT + " TEXT NOT NULL,"
                + ItemEntry.LON + " TEXT NOT NULL,"
                + ItemEntry.TIME + " TEXT NOT NULL,"
                + ItemEntry.PATH + " TEXT NOT NULL )"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public ContentValues toContentValues(Item item) {

        ContentValues values = new ContentValues();

        values.put(ItemEntry.TITULO, item.getTitle());
        values.put(ItemEntry.DESCRIPCION, item.getDescripcion());
        values.put(ItemEntry.LAT, item.getLat());
        values.put(ItemEntry.LON, item.getLon());
        values.put(ItemEntry.TIME, item.getTime());
        values.put(ItemEntry.PATH, item.getPath());
        return values;
    }

    public long saveItem(Item item) {
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();

        return sqLiteDatabase.insert(
                ItemEntry.TABLE_NAME,
                null,
                toContentValues(item));

    }

    public void dropALL(){

       String SQL_DELETE_ENTRIES =  "DELETE FROM " + ItemEntry.TABLE_NAME;
       SQLiteDatabase sqLiteDatabase = getWritableDatabase();
       sqLiteDatabase.execSQL(SQL_DELETE_ENTRIES);

    }





    public ArrayList<String> getListaString(){
        SQLiteDatabase db = getWritableDatabase();

        String columns[] = new String[]{ItemEntry.ID, ItemEntry.TITULO, ItemEntry.DESCRIPCION, ItemEntry.LAT, ItemEntry.LON, ItemEntry.TIME, ItemEntry.PATH};

        Cursor c = db.query(
                ItemEntry.TABLE_NAME,
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

        String columns[] = new String[]{ItemEntry.ID, ItemEntry.TITULO, ItemEntry.DESCRIPCION, ItemEntry.LAT, ItemEntry.LON, ItemEntry.TIME, ItemEntry.PATH};

        Cursor c = db.query(
                ItemEntry.TABLE_NAME,
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
