package com.example.bmorales.test1;



import android.os.Handler;
import android.os.StrictMode;
import android.util.Log;


import org.json.JSONArray;

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

    public JSONArray getJSON() {
        StringBuilder result = new StringBuilder();
        URL url = null;
        try {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

            StrictMode.setThreadPolicy(policy);
            String registrationUrl = String.format( urlServ + "todos");
            url = new URL(registrationUrl);
            URLConnection connection = url.openConnection();
            HttpURLConnection httpConnection = (HttpURLConnection) connection;
            int responseCode = httpConnection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {

                InputStream is = httpConnection.getInputStream();

                BufferedReader reader = new BufferedReader(new InputStreamReader(is));

                String line;
                while ((line = reader.readLine()) != null) {
                    result.append(line);
                }

                JSONArray json = new JSONArray(result.toString());
                return json;

            } else {
                Log.w("MyApp", "failed: " + registrationUrl);
                return null;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }


         return null;
    }



    public void gg(){

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(urlServ)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        IApiMethods api = retrofit.create(IApiMethods.class);

        Call<Usuario> respuesta = api.findUser("67");
        respuesta.enqueue(new Callback<Usuario>() {
            @Override
            public void onResponse(Call<Usuario> call, Response<Usuario> response) {
                String ii ="";
            }

            @Override
            public void onFailure(Call<Usuario> call, Throwable t) {
                String ii ="";
            }
        });
        String AAAA = "";
    }
}




