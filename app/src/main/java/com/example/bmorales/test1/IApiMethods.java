package com.example.bmorales.test1;

/**
 * Created by Dario on 12/2/2016.
 */

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface IApiMethods {


    //Direccion del request en mi caso la ip de mi computador personal
    String URL = "https://neurona-turk128.c9.io/api/";

    @Headers({
            "Accept: application/json",
            "Content-type: application/json"
    })

    @GET("todos")
    Call<Usuario> todos();

    @FormUrlEncoded
      @POST("todos")
      Call<Usuario> findUser(@Field("id") String id, @Field("name") String name);

}
