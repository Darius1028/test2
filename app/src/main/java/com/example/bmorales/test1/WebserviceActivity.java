package com.example.bmorales.test1;


import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;
import org.apache.commons.io.FileUtils;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.File;
import java.util.Calendar;
import id.zelory.compressor.Compressor;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.Call;

import retrofit2.Callback;
import retrofit2.converter.gson.GsonConverterFactory;

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


    public void uploadFile(Uri fileUri, String userId, Geo loc, Context context) {
        // create upload service client

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(urlServ)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        IApiMethods api = retrofit.create(IApiMethods.class);

        // https://github.com/iPaulPro/aFileChooser/blob/master/aFileChooser/src/com/ipaulpro/afilechooser/utils/FileUtils.java
        // use the FileUtils to get the actual file by uri

        File file = FileUtils.getFile(fileUri.getPath());
        File compressedImageFile = Compressor.getDefault(context).compressToFile(file);



        // create RequestBody instance from file
        RequestBody requestFile =
                RequestBody.create(MediaType.parse("image/jpg"), compressedImageFile);

        //RequestBody.create(MediaType.parse("multipart/form-data"), file);


        ///////////////////////////////////////////////////////////////
        // MultipartBody.Part is used to send also the actual file name
        ///////////////////////////////////////////////////////////////
        MultipartBody.Part body =
                MultipartBody.Part.createFormData("picture", file.getName(), requestFile);

        /////////////////////////////////////////////////
        // add another part within the multipart request
        /////////////////////////////////////////////////
        String descriptionString = "jpg";
        RequestBody description =
                RequestBody.create(
                        MediaType.parse("multipart/form-data"), descriptionString);

        ////////////////////////////////////////////////
        // add another part within the multipart request
        ///////////////////////////////////////////////

        Calendar now = Calendar.getInstance();
        Usuario iu =  new Usuario(userId, "", "", String.valueOf(loc.getLatitude()), String.valueOf(loc.getLongitude()), now.getTime().toString(), "Envio imagen");

        RequestBody user =
                RequestBody.create(
                        MediaType.parse("multipart/form-data"),  iu.toJSON());


        // finally, execute the request
        Call<ResponseBody> call = api.uploadFile(description, user, body);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call,
                                   Response<ResponseBody> response) {
                ResponseBody temp = response.body();
                try {
                    JSONObject jsonObj = new JSONObject(temp.string());
                    Log.v("Upload", "success " + jsonObj.getInt("msg"));
                }
                catch (Throwable t){
                    Log.v("Upload error", "error Json error " + t.toString() );
                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e("Upload error:", t.getMessage());
            }
        });
    }
}




