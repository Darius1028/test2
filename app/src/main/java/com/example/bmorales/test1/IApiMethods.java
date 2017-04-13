package com.example.bmorales.test1;

/**
 * Created by Dario on 12/2/2016.
 */

import android.content.Intent;
import android.content.pm.LauncherApps;
import android.util.Log;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;



public interface IApiMethods {


    //Direccion del request en mi caso la ip de mi computador personal
    String URL = "http://ec2-34-208-163-216.us-west-2.compute.amazonaws.com/api/";

    @Headers({
            "Accept: application/json",
            "Content-type: application/json"
    })

    @GET("todos")
    Call<Usuario> todos();

    @FormUrlEncoded
      @POST("todos")
      Call<Usuario> findUser(@Field("id") String id, @Field("name") String name);

    @Multipart
    @POST("file_upload")
    Call<ResponseBody> uploadFile(
            @Part("description") RequestBody description,
            @Part("item") RequestBody item,
            @Part MultipartBody.Part file);

}
