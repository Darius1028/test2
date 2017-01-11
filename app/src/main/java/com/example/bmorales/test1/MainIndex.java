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
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Environment;
import android.provider.MediaStore;
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

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
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

/**
 * Created by bmorales on 11/18/2016.
 */
/////extends AppCompatActivity implements LoaderCallbacks<Cursor> {
public class MainIndex extends AppCompatActivity implements LoaderCallbacks<Cursor> {

    private static final String BITMAP_STORAGE_KEY = "viewbitmap";
    private static final String IMAGEVIEW_VISIBILITY_STORAGE_KEY = "imageviewvisibility";
    private ImageView mImageView;
    private Bitmap mImageBitmap;


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
    private Button btnPicture;
    private Button btnSeeData;
    private Button btnFacialRecognition;
    private TextView texttitle;

    private PermissionGranted permissionGranted;
    private int RESIZE_PHOTO_PIXELS_PERCENTAGE = 3000;

    private String mCurrentPhotoPath;

    private static final String JPEG_FILE_PREFIX = "IMG_";
    private static final String JPEG_FILE_SUFFIX = ".jpg";

    private AlbumStorageDirFactory mAlbumStorageDirFactory = null;

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

        startCamera();

        //btnPicture = (Button) findViewById(R.id.PICTURE);

        //btnPicture.setOnClickListener(takePictureHandler);

        mImageView = (ImageView) findViewById(R.id.imageView1);
        mImageBitmap = null;


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.FROYO) {
            mAlbumStorageDirFactory = new FroyoAlbumDirFactory();
        } else {
            mAlbumStorageDirFactory = new BaseAlbumDirFactory();
        }

    }


    View.OnClickListener takePictureHandler = new View.OnClickListener(){
        public void onClick(View v){
            dispatchTakePictureIntent();
        };
    };

    public void startCamera(){


        btnGoTo =  (Button) findViewById(R.id.btnGoTo);
        btnGoTo.setText("Go to Fragment");


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
        fillData(out.getLista());
    }

    public void getGeo(View v){
        dialogBox();
    };

    public void getImage(View v){
        dialogBoxPicture();
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

    public void dialogBoxPicture() {

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

        alertDialogBuilder.setPositiveButton("Foto",
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        dispatchTakePictureIntent();
                    }
                });
        alertDialogBuilder.setNegativeButton("File",
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {

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

    private void setPic() {

		/* There isn't enough memory to open up more than a couple camera photos */
		/* So pre-scale the target bitmap into which the file is decoded */

		/* Get the size of the ImageView */
        int targetW = mImageView.getWidth();
        int targetH = mImageView.getHeight();

		/* Get the size of the image */
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;

		/* Figure out which way needs to be reduced less */
        int scaleFactor = 1;
        if ((targetW > 0) || (targetH > 0)) {
            scaleFactor = Math.min(photoW/targetW, photoH/targetH);
        }

		/* Set bitmap options to scale the image decode target */
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;
        bmOptions.inPurgeable = true;

		/* Decode the JPEG file into a Bitmap */
        Bitmap bitmap = BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);

		/* Associate the Bitmap to the ImageView */
        mImageView.setImageBitmap(bitmap);
        mImageView.setVisibility(View.VISIBLE);

    }

    private void galleryAddPic(){
        Intent mediaScanIntent = new Intent("android.intent.action.MEDIA_SCANER_SCAN_FILE");
        File f = new File(mCurrentPhotoPath);
        Uri contentUri = Uri.fromFile(f);
        this.sendBroadcast(mediaScanIntent);

    };


    /* Photo album for this application */
    private String getAlbumName() {
        return getString(R.string.album_name);
    }

    private File getAlbumDir() {
        File storageDir = null;

        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {

            storageDir = mAlbumStorageDirFactory.getAlbumStorageDir(getAlbumName());

            if (storageDir != null) {
                if (! storageDir.mkdirs()) {
                    if (! storageDir.exists()){
                        Log.d("CameraSample", "failed to create directory");
                        return null;
                    }
                }
            }

        } else {
            Log.v(getString(R.string.app_name), "External storage is not mounted READ/WRITE.");
        }

        return storageDir;
    }


    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = JPEG_FILE_PREFIX + timeStamp + "_";
        File albumF = getAlbumDir();
        File imageF = File.createTempFile(imageFileName, JPEG_FILE_SUFFIX, albumF);
        return imageF;
    }

    private File setUpPhotoFile() throws IOException {

        File f = createImageFile();
        mCurrentPhotoPath = f.getAbsolutePath();

        return f;
    }
    private void dispatchTakePictureIntent() {

        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                File f = null;

                try {
                    f = setUpPhotoFile();
                    mCurrentPhotoPath = f.getAbsolutePath();
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
                } catch (IOException e) {
                    e.printStackTrace();
                    f = null;
                    mCurrentPhotoPath = null;
                }


        startActivityForResult(takePictureIntent,121);
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
            case 121: {
                if (resultCode == RESULT_OK) {
                    handleBigCameraPhoto();
                }
                break;
            }
        }
    }

    private void handleBigCameraPhoto(){

        if(mCurrentPhotoPath != null ){
            setPic();
            galleryAddPic();
            mCurrentPhotoPath = null;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        magicalCamera.permissionGrant(requestCode, permissions, grantResults);
    }
}
