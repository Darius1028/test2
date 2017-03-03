package com.example.bmorales.test1;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;
import java.util.Arrays;
import java.util.logging.Handler;

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
    private Toolbar toolbar;
    private ProgressDialog progressDialog;
    private Handler mHandler;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        serv = new WebserviceActivity();

        FacebookSdk.sdkInitialize(getApplicationContext());

        setContentView(R.layout.loginfb);

        callbackManager = CallbackManager.Factory.create();
        FacebookSdk.setApplicationId(getResources().getString(R.string.facebook_app_id));
        ConnectivityManager cn=(ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo nf=cn.getActiveNetworkInfo();

        info = (TextView)findViewById(R.id.info);
        loginButton = (LoginButton)findViewById(R.id.login_button);
        loginButton.setReadPermissions(Arrays.asList("email", "user_birthday", "user_friends"));
        toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);

        if(nf != null && nf.isConnected()==true ) {
            if( isLoggedIn() ){
                goToIndex(null);
            }
            else{
                new LoadViewTask().execute();
            }
            Toast.makeText(this, "Network Available", Toast.LENGTH_LONG).show();
            info.setText("Network Available");

            loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {

                @Override
                public void onSuccess(LoginResult loginResult) {

                    GraphRequest.newMeRequest(
                            loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
                                @Override
                                public void onCompleted(JSONObject me, GraphResponse response) {
                                    goToIndex(me);
                                }
                            }).executeAsync();

                    new LoadViewTask().execute();
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
    /////////////////////////////////////////////////
    // Subclase /////////////////////////////////////
    // To use the AsyncTask, it must be subclassed
    /////////////////////////////////////////////////
    private class LoadViewTask extends AsyncTask<Void, Integer, Void>
    {
        //Before running code in separate thread
        @Override
        protected void onPreExecute()
        {
            //Create a new progress dialog
            progressDialog = new ProgressDialog(LoginActivity.this);
            //Set the progress dialog to display a horizontal progress bar
            progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            //Set the dialog title to 'Loading...'
            progressDialog.setTitle("Cargando...");
            //This dialog can't be canceled by pressing the back key
            progressDialog.setCancelable(false);
            //This dialog isn't indeterminate
            progressDialog.setIndeterminate(false);
            //The maximum number of items is 100
            progressDialog.setMax(100);
            //Set the current progress to zero
            progressDialog.setProgress(0);
            //Display the progress dialog
            progressDialog.show();
        }

        @Override
        protected Void doInBackground(Void... params)
        {
            /* This is just a code that delays the thread execution 4 times,
             * during 850 milliseconds and updates the current progress. This
             * is where the code that is going to be executed on a background
             * thread must be placed.
             */
            try
            {
                synchronized (this)
                {
                    int counter = 0;
                    while(counter <= 4)
                    {
                        this.wait(850);
                        counter++;
                        publishProgress(counter*25);
                    }
                }
            }
            catch (InterruptedException e)
            {
                e.printStackTrace();
            }
            return null;
        }

        //Update the progress
        @Override
        protected void onProgressUpdate(Integer... values)
        {
            //set the current progress of the progress dialog
            progressDialog.setProgress(values[0]);
        }

        //after executing the code in the thread
        @Override
        protected void onPostExecute(Void result)
        {
            //close the progress dialog
            progressDialog.dismiss();
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

    public void goToIndex(JSONObject object) {

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

        Intent in = new Intent (getApplicationContext(),MainIndex.class);
        in.putExtra("lat", "0.0f");
        in.putExtra("lng", "0.0f");
        startActivity(in);

    }


}



