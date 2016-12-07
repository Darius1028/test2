package com.example.bmorales.test1;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.app.LoaderManager.LoaderCallbacks;

import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;

import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.text.format.Time;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;


import android.util.Log;

import static android.Manifest.permission.READ_CONTACTS;

import com.example.bmorales.test1.UsuarioDbHelper;
import com.example.bmorales.test1.Usuario;
import com.example.bmorales.test1.Geo;
import com.example.bmorales.test1.WebserviceActivity;

/**
 * Created by bmorales on 11/18/2016.
 */
/////extends AppCompatActivity implements LoaderCallbacks<Cursor> {
public class marco extends Activity implements LoaderCallbacks<Cursor> {

    private TextView mTextView;
    private UsuarioDbHelper out;
    private Geo infoGeo;
    private ListView lv;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.demo);
        //mTextView = (TextView) findViewById(R.id.out);
        //mTextView.setText("My Awesome Text alw ewd ew ew ew fe ewf ef efewe ewf ewfew dddd");

        out = new UsuarioDbHelper(getBaseContext());
        infoGeo = new Geo(getBaseContext());
        lv = (ListView) findViewById(R.id.LISTA);



    }



    public void fillData(ArrayList<String> arrayInfo){

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, arrayInfo);
        // Set The Adapter
        lv.setAdapter(arrayAdapter);

    }


    private AutoCompleteTextView mEmailView;

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    public void buttonBulk(View v){
        //out.bulkData();
    }

    public void buttonPressDrop(View v){
        out.dropALL();
    }


    public void getRandom(View v){
        mTextView = (TextView) findViewById(R.id.out);
        mTextView.setText(out.getNameRandom());
        fillData(out.getLista());
    }

    public void getGeo(View v){
        dialogBox();
    };


    public void dialogBox() {
        infoGeo.getLocation(getBaseContext());
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage(" Lat: " + infoGeo.getLatitude() + " Log: " + infoGeo.getLongitude());
        alertDialogBuilder.setPositiveButton("Ok",
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {

                        Calendar now = Calendar.getInstance();
                        Usuario user = new Usuario(null, "Dario", "", ""+infoGeo.getLatitude()+"", ""+infoGeo.getLongitude()+"", now.getTime().toString() );
                        Log.d("INFO", " " + out.saveUsuario(user) );
                    }
                });

        alertDialogBuilder.setNegativeButton("cancel",
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {

                    }
                });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }
}
