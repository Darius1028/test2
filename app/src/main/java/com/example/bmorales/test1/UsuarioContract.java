package com.example.bmorales.test1;

import android.provider.BaseColumns;

/**
 * Created by bmorales on 11/22/2016.
 */

public class UsuarioContract {

    public static abstract class UsuarioEntry implements BaseColumns{
        public static final String TABLE_NAME ="usuario";
        public static final String ID = "id";
        public static final String NAME = "name";
        public static final String LAT = "lat";
        public static final String LON = "lon";
        public static final String TIME = "time";
    }
}
