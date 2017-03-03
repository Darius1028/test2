package com.example.bmorales.test1;

import android.provider.BaseColumns;

/**
 * Created by bmorales on 11/22/2016.
 */

public class ItemContract {

    public static abstract class ItemEntry implements BaseColumns{
        public static final String TABLE_NAME ="item";
        public static final String ID = "id";
        public static final String TITULO = "titulo";
        public static final String DESCRIPCION = "descripcion";
        public static final String LAT = "lat";
        public static final String LON = "lon";
        public static final String TIME = "time";
        public static final String PATH = "path";
    }
}
