package com.example.bmorales.test1;



import android.content.Context;
import android.os.Handler;
import android.os.StrictMode;
import android.util.Log;
import android.widget.Toast;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;

import java.io.InputStream;
import java.io.InputStreamReader;

import java.net.HttpURLConnection;

import java.net.URL;
import java.net.URLConnection;

import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.Call;
import retrofit2.Callback;
/**
 * Created by bmorales on 11/28/2016.
 */

//identify
public class WebserviceActivity {

    private String urlServ = "https://neurona-turk128.c9.io/api/";
    boolean success = false;
    Usuario user = null;

    public boolean login(JSONObject object, final Context context){

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(urlServ)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        IApiMethods api = retrofit.create(IApiMethods.class);


        Call<Usuario> respuesta = null;
        try {

            respuesta = api.findUser(object.getString("id").toString(),object.getString("name").toString());

        } catch (JSONException e) {
            e.printStackTrace();
        }
        respuesta.enqueue(new Callback<Usuario>() {
            @Override
            public void onResponse(Call<Usuario> call, Response<Usuario> response) {
                success = response.isSuccessful();
                if(success) {
                    user = response.body();
                    Toast.makeText(context, user.getMessage(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<Usuario> call, Throwable t) {
                Log.e("REST", t.getMessage());
            }
        });

        return success;
    }
}




