package com.example.bmorales.test1;


import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.provider.Settings;

import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.app.LoaderManager.LoaderCallbacks;


import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;


import android.os.Build;
import android.os.Bundle;

import android.support.v7.widget.Toolbar;

import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;

import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.io.File;
import java.io.FileDescriptor;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import android.util.Log;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.login.LoginManager;





/**
 * Created by bmorales on 11/18/2016.
 */
/////extends AppCompatActivity implements LoaderCallbacks<Cursor> {
public class MainIndex extends AppCompatActivity implements LoaderCallbacks<Cursor> {

    private ImageView mImageView;
    private Bitmap mImageBitmap;

    private TextView mTextView;
    private UsuarioDbHelper out;
    private Geo infoGeo;
    private ListView lv;
    private Toolbar toolbar;
    private Context context;
    private WebserviceActivity serv;

    private Button btnGoTo;

    private String mCurrentPhotoPath;

    private static final String JPEG_FILE_PREFIX = "IMG_";
    private static final String JPEG_FILE_SUFFIX = ".jpg";

    private AlbumStorageDirFactory mAlbumStorageDirFactory = null;

    private int MY_REQUEST_CODE_CAMERA = 121;
    private int MY_REQUEST_CODE_EXTERNAL_STORAGE = 130;
    private int MY_REQUEST_CODE_CAMERA_REQUEST = 160;
    private int MY_REQUEST_CODE_GPS = 140;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mainindex);


        ////  inicializacion nodejs Neurona
        serv = new WebserviceActivity();

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



        lv = (ListView) findViewById(R.id.LISTA);

        mImageView = (ImageView) findViewById(R.id.imageView1);
        mImageBitmap = null;

    }


    View.OnClickListener takePictureHandler = new View.OnClickListener(){
        public void onClick(View v){

        };
    };


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
        fillData(out.getLista());
    }

    public void getGeo(View v){
        dialogBox();
    };

    public void getImage(View v){
        dialogBoxPicture();
    };
    public void dialogBox() {


        if(Build.VERSION.SDK_INT >= 23){
            if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED ){
                Log.d("GPS", "00" );

                if( ActivityCompat.shouldShowRequestPermissionRationale((Activity) this,Manifest.permission.ACCESS_COARSE_LOCATION ) ){
                    Log.d("GPS", "OK ACCESS_COARSE_LOCATION" );

                }
                else if( ActivityCompat.shouldShowRequestPermissionRationale((Activity) this,Manifest.permission.ACCESS_FINE_LOCATION ) ){
                    Log.d("GPS", "OK ACCESS_FINE_LOCATION" );

                }

                ActivityCompat.requestPermissions((Activity) this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},1);


            }
        }


        infoGeo = new Geo(getBaseContext());
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage(" Lat: " + infoGeo.getLatitude() + " Log: " + infoGeo.getLongitude());
        alertDialogBuilder.setPositiveButton("Ok",
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {

                        Calendar now = Calendar.getInstance();
                        Usuario user = new Usuario(null, "Dario", "", ""+infoGeo.getLatitude()+"", ""+infoGeo.getLongitude()+"", now.getTime().toString(), "OK" );

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
    public void dialogBoxPicture() {

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this, R.style.MyAlertDialogStyle);
        alertDialogBuilder.setTitle("Imagen");

        alertDialogBuilder.setItems(new CharSequence[]{"Tomar Foto","Seleccionar Foto","Otro"}, new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case 0:
                        Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                        startActivityForResult(cameraIntent, MY_REQUEST_CODE_CAMERA_REQUEST);
                        break;
                    case 1:
                        getImageFile();
                        break;
                    case 2:
                        break;

                }
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

    private void getImageFile(){
        Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(i,122);
    };
    private Bitmap getBitmapFromUri(Uri uri) throws IOException {
        ParcelFileDescriptor parcelFileDescriptor =
                getContentResolver().openFileDescriptor(uri, "r");

        FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();
        Bitmap image = BitmapFactory.decodeFileDescriptor(fileDescriptor);
        parcelFileDescriptor.close();
        return image;
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
        switch (requestCode) {
            case 160: {
                if (resultCode == RESULT_OK) {
                    Bitmap bitmap = (Bitmap) data.getExtras().get("data");

                    mImageView.setImageBitmap(bitmap);
                }
                break;
            }
            case 122:
                if (resultCode == RESULT_OK && null != data) {
                    Uri selectedImage = data.getData();
                    String[] filePathColumn = { MediaStore.Images.Media.DATA};

                    Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
                    cursor.moveToFirst();

                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                    String picturePath = cursor.getString(columnIndex);
                    cursor.close();

                    Bitmap bmp = null;

                    try{
                        bmp = getBitmapFromUri(selectedImage);

                        //Uri imageUri = Uri.parse("/storage/emulated/0/DCIM/Camera/IMG_20170104_112852363_HDR.jpg");
                        if(Build.VERSION.SDK_INT >= 23){
                            if(ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ){
                                Log.d("STORAGE", "Check" );

                                if( ActivityCompat.shouldShowRequestPermissionRationale((Activity) this,Manifest.permission.READ_EXTERNAL_STORAGE ) ){
                                    Log.d("STORAGE", "OK ACCESS_COARSE_LOCATION" );
                                }


                                ActivityCompat.requestPermissions((Activity) this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},1);


                            }

                                if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                                        && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED ){
                                    Log.d("GPS", "00" );

                                    if( ActivityCompat.shouldShowRequestPermissionRationale((Activity) this,Manifest.permission.ACCESS_COARSE_LOCATION ) ){
                                        Log.d("GPS", "OK ACCESS_COARSE_LOCATION" );

                                    }
                                    else if( ActivityCompat.shouldShowRequestPermissionRationale((Activity) this,Manifest.permission.ACCESS_FINE_LOCATION ) ){
                                        Log.d("GPS", "OK ACCESS_FINE_LOCATION" );

                                    }

                                    ActivityCompat.requestPermissions((Activity) this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},1);


                                }


                        }
                        infoGeo = new Geo(getBaseContext());
                        serv.uploadFile(Uri.parse(picturePath), AccessToken.getCurrentAccessToken().getUserId().toString(), infoGeo, getBaseContext());
                    }catch (IOException e){
                        e.printStackTrace();
                    }

                    mImageView.setImageBitmap(bmp);

                }
                break;
        }
    }




    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch(requestCode){

            case 121:
                Log.v("", "CAMARA Permiso");
                break;
            case 130:
                Log.v("", "STORAGE Permiso");
                break;
            case 140:
                Log.v("", "GPS Permiso");
                break;

        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    public boolean CheckStoragePermission() {
        int permissionCheckRead = ActivityCompat.checkSelfPermission(context,
                Manifest.permission.READ_EXTERNAL_STORAGE);
        if (permissionCheckRead != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) context,
                    Manifest.permission.READ_EXTERNAL_STORAGE)) {
                // Show an expanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

                ActivityCompat.requestPermissions((Activity) context,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        MY_REQUEST_CODE_EXTERNAL_STORAGE);
            } else {
                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions((Activity) context,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        MY_REQUEST_CODE_EXTERNAL_STORAGE);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
            return false;
        } else
            return true;
    }

}
