package com.example.bmorales.test1;


import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationListener;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;


import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import java.io.FileDescriptor;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import android.util.Log;
import android.widget.Toast;
import com.facebook.AccessToken;
import com.facebook.login.LoginManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;


/**
 * Created by bmorales on 11/18/2016.
 */
public class MainIndex extends AppCompatActivity implements
        LoaderCallbacks<Cursor>,
        ActivityCompat.OnRequestPermissionsResultCallback
{

    private ImageView mImageView;
    private UsuarioDbHelper out;
    private Geo infoGeo;
    private ListView lv;
    private Toolbar toolbar;
    private WebserviceActivity serv;
    private static final int MY_REQUEST_CODE_CAMERA = 1;
    private static final int MY_REQUEST_CODE_EXTERNAL_STORAGE = 2;
    private static final int MY_REQUEST_CODE_GPS = 3;
    private Location tempLoc;
    private Context context;
    private ViewPager mViewPager;
    private SectionsPagerAdapter mSectionsPagerAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState){

        super.onCreate(savedInstanceState);
        setContentView(R.layout.mainindex);

        /// inicial Context
        this.context = this.getApplicationContext();

        ////  inicializacion nodejs Neurona
        serv = new WebserviceActivity();

        out = new UsuarioDbHelper(getBaseContext());
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        mImageView = (ImageView) findViewById(R.id.imageView1);
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

        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);
    }


    public void fillData(ArrayList<String> arrayInfo){

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, arrayInfo);
        // Set The Adapter
        lv.setAdapter(arrayAdapter);
    }

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

                ActivityCompat.requestPermissions((Activity) this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},3);
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
        super.onActivityResult(requestCode,resultCode,data);
    }



    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch(requestCode){

            case MY_REQUEST_CODE_CAMERA:
                Log.v("", "CAMARA Permiso");
                break;
            case MY_REQUEST_CODE_EXTERNAL_STORAGE:
                Log.v("", "STORAGE Permiso");
                break;
            case MY_REQUEST_CODE_GPS:
                Log.v("", "GPS Permiso");

                break;

        }
    }



    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        private List<Fragment> fragments;

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);

            this.fragments = new ArrayList<Fragment>();

            Bundle args = new Bundle();

            infoGeo = new Geo(getBaseContext());

            args.putDouble("lat",infoGeo.getLatitude());
            args.putDouble("lng",infoGeo.getLongitude());
            args.putString("tok",AccessToken.getCurrentAccessToken().getUserId().toString());



            MapFrag temFrag = new MapFrag();
            temFrag.setArguments(args);

            FotoFrag temFoto = new FotoFrag();
            temFoto.setArguments(args);

            MapFrag temFrag2 = new MapFrag();
            temFrag2.setArguments(args);

            fragments.add(temFrag);
            fragments.add(temFoto);


        }

        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }

    }


}
