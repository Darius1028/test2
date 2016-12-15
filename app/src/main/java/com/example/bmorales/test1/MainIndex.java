package com.example.bmorales.test1;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.provider.Settings;
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
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.text.format.Time;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


import android.util.Log;
import android.widget.Toast;

import static android.Manifest.permission.READ_CONTACTS;

import com.example.bmorales.test1.UsuarioDbHelper;
import com.example.bmorales.test1.Usuario;
import com.example.bmorales.test1.Geo;
import com.example.bmorales.test1.WebserviceActivity;
import com.facebook.login.LoginManager;
import com.frosquivel.magicalcamera.Functionallities.PermissionGranted;
import com.frosquivel.magicalcamera.MagicalCamera;
import com.frosquivel.magicalcamera.Objects.MagicalCameraObject;
import com.frosquivel.magicalcamera.Utilities.ConvertSimpleImage;
import com.google.android.gms.vision.face.Landmark;
import com.afollestad.materialdialogs.MaterialDialog;

/**
 * Created by bmorales on 11/18/2016.
 */
/////extends AppCompatActivity implements LoaderCallbacks<Cursor> {
public class MainIndex extends AppCompatActivity implements LoaderCallbacks<Cursor> {

    private TextView mTextView;
    private UsuarioDbHelper out;
    private Geo infoGeo;
    private ListView lv;
    private Toolbar toolbar;
    private Context context;
    private MagicalCamera magicalCamera;

    private ImageView imageView;
    private Button btntakephoto;
    private Button btnselectedphoto;
    private Button btnGoTo;
    private Button btnSeeData;
    private Button btnFacialRecognition;
    private TextView texttitle;

    private PermissionGranted permissionGranted;
    private int RESIZE_PHOTO_PIXELS_PERCENTAGE = 3000;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mainindex);

        out = new UsuarioDbHelper(getBaseContext());
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        try
        {

            String provider = Settings.Secure.getString(getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);


            if(!provider.contains("gps")){ //if gps is disabled
                final Intent poke = new Intent();
                poke.setClassName("com.android.settings", "com.android.settings.widget.SettingsAppWidgetProvider");
                poke.addCategory(Intent.CATEGORY_ALTERNATIVE);
                poke.setData(Uri.parse("3"));
                sendBroadcast(poke);
            }
        }
        catch (Exception e) {
               Log.e("Error !!!", e.getMessage().toString());
        }

        infoGeo = new Geo(getBaseContext());

        lv = (ListView) findViewById(R.id.LISTA);



    }


    public void startCamera(){

        permissionGranted = new PermissionGranted(this);
        //permission for take photo, it is false if the user check deny
        permissionGranted.checkCameraPermission();
        //for search and write photoss in device internal memory
        //normal or SD memory
        permissionGranted.checkReadExternalPermission();
        permissionGranted.checkWriteExternalPermission();
        //permission for location for use the `photo information device.
        permissionGranted.checkLocationPermission();

        magicalCamera = new MagicalCamera(this, RESIZE_PHOTO_PIXELS_PERCENTAGE, permissionGranted);

        imageView =  (ImageView) findViewById(R.id.imageView);
        btntakephoto =  (Button) findViewById(R.id.btntakephoto);
        btnselectedphoto =  (Button) findViewById(R.id.btnselectedphoto);
        btnGoTo =  (Button) findViewById(R.id.btnGoTo);
        texttitle =  (TextView) findViewById(R.id.texttitle);
        texttitle.setText("Activity Example");
        btnGoTo.setText("Go to Fragment");
        btnSeeData = (Button) findViewById(R.id.btnSeeData);
        btnFacialRecognition = (Button) findViewById(R.id.btnFacialRecognition);

        btntakephoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                magicalCamera.takePhoto();
            }
        });

        btnselectedphoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                magicalCamera.selectedPicture("my_header_name");
            }
        });

        btnGoTo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainIndex.this, ActivityForFragment.class));
            }
        });





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
                        Usuario user = new Usuario(null, "Dario", "", ""+infoGeo.getLatitude()+"", ""+infoGeo.getLongitude()+"", now.getTime().toString(), "OK" );
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

    /////////////////////////////////////////////////////
    /////////////////tooll bar///////////////////////////
    /////////////////////////////////////////////////////

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_activity_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_add:
                actionMessage(R.string.action_add);
                return true;
            case R.id.action_edit:
                actionMessage(R.string.action_edit);
                return true;
            case R.id.action_settings:
                actionMessage(R.string.action_settings);
                return true;
            case R.id.action_help:
                actionMessage(R.string.action_help);
                return true;
            case R.id.action_about:
                actionMessage(R.string.action_about);
                return true;
            case R.id.action_logout:
                callFacebookLogout();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void actionMessage(int resid) {
        Toast.makeText(this, getText(resid), Toast.LENGTH_SHORT).show();
    }

    /**
     * Logout From Facebook
     */
    public void callFacebookLogout( ) {

        LoginManager.getInstance().logOut();
        Intent intent = new Intent(context, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //this is for rotate picture in this method
        //magicalCamera.resultPhoto(requestCode, resultCode, data, MagicalCamera.ORIENTATION_ROTATE_180);
        magicalCamera.resultPhoto(requestCode, resultCode, data);

        if(magicalCamera.getPhoto()!=null) {
            //another form to rotate image

           // magicalCamera.rotatePicture(magicalCamera.getPhoto(), MagicalCamera.ORIENTATION_ROTATE_90);

            //set the photo in image view
            imageView.setImageBitmap(magicalCamera.getPhoto());

            String path = magicalCamera.savePhotoInMemoryDevice(magicalCamera.getPhoto(), "myTestPhoto", true);

            //CONVERT BITMAP EXAMPLE COMMENT
            //convert the bitmap to bytes
            /*byte[] bytesArray =  ConvertSimpleImage.bitmapToBytes(magicalCamera.getPhoto(), MagicalCamera.PNG);
            //convert the bytes to string 64, with this form is easly to send by web service or store data in DB
            String imageBase64 = ConvertSimpleImage.bytesToStringBase64(bytesArray);

            //if you need to revert the process
            byte[] anotherArrayBytes = ConvertSimpleImage.stringBase64ToBytes(imageBase64);

            //again deserialize the image
            Bitmap myImageAgain = ConvertSimpleImage.bytesToBitmap(anotherArrayBytes);
            */

            if (path != null) {
                Toast.makeText(MainIndex.this,
                        "The photo is save in device, please check this path: " + path,
                        Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(MainIndex.this,
                        "Sorry your photo dont write in devide, please contact with fabian7593@gmail and say this error",
                        Toast.LENGTH_SHORT).show();
            }
        }else{
            Toast.makeText(MainIndex.this,
                    "Your image is null, please debug, or test with another device, or maybe contact with fabian7593@gmail.com for try to fix the bug, thanks and sorry",
                    Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        magicalCamera.permissionGrant(requestCode, permissions, grantResults);
    }
}
