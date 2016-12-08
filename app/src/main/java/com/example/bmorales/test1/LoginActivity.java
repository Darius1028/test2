package com.example.bmorales.test1;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.app.LoaderManager.LoaderCallbacks;

import android.content.Loader;
import android.database.Cursor;

import android.os.Bundle;

import android.widget.TextView;

import java.util.Arrays;

import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;


import org.json.JSONException;
import org.json.JSONObject;

import static android.Manifest.permission.READ_CONTACTS;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity implements LoaderCallbacks<Cursor> {

    /**
     * Id to identity READ_CONTACTS permission request.
     */
    private static final int REQUEST_READ_CONTACTS = 0;

    private TextView info;
    private LoginButton loginButton;

    private CallbackManager callbackManager;

    private WebserviceActivity serv;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        serv = new WebserviceActivity();


        FacebookSdk.sdkInitialize(this.getApplicationContext());
        setContentView(R.layout.loginfb);

        callbackManager = CallbackManager.Factory.create();
        FacebookSdk.setApplicationId(getResources().getString(R.string.facebook_app_id));
        ConnectivityManager cn=(ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo nf=cn.getActiveNetworkInfo();

        info = (TextView)findViewById(R.id.info);
        loginButton = (LoginButton)findViewById(R.id.login_button);
        loginButton.setReadPermissions(Arrays.asList("email", "user_birthday", "user_friends"));

        if(nf != null && nf.isConnected()==true ) {


            if( isLoggedIn() ){
                goToApp(null);
            }

            Toast.makeText(this, "Network Available", Toast.LENGTH_LONG).show();
            info.setText("Network Available");


            //loginButton.setReadPermissions(Arrays.asList("public_profile, email, user_birthday"));


            loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
                @Override
                public void onSuccess(LoginResult loginResult) {

                    GraphRequest.newMeRequest(
                            loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
                                @Override
                                public void onCompleted(JSONObject me, GraphResponse response) {
                                    goToApp(me);
                                }
                            }).executeAsync();


                }

                @Override
                public void onCancel() {
                    Toast.makeText(getApplicationContext(), "Fb Login False", Toast.LENGTH_LONG).show();

                }

                @Override
                public void onError(FacebookException e) {
                    Toast.makeText(getApplicationContext(), "Fb Login Error", Toast.LENGTH_LONG).show();

                }
            });

        }
        else{
            Toast.makeText(this, "Internet no habilitado", Toast.LENGTH_LONG).show();
            info.setText("Network");

        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);

    }


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {

    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {

    }

    public boolean isLoggedIn() {
        AccessToken accessToken = AccessToken.getCurrentAccessToken();

        return accessToken != null;
    }

    public void goToApp(JSONObject object) {

        Radar rar = new Radar();

        rar.onReceive(this.getApplicationContext(), this.getIntent());

        try {
            if(object == null){
                object = new JSONObject();
                    object.put("id",AccessToken.getCurrentAccessToken().getUserId().toString());
                    object.put("name","");
                    serv.login(object, this.getApplicationContext());
            }
            else{
                serv.login(object, this.getApplicationContext());
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        Intent in = new Intent (getApplicationContext(),marco.class);
        startActivityForResult(in, 2);// Act
        finish();
    }


}
