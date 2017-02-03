package com.example.bmorales.test1;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.google.gson.internal.bind.ArrayTypeAdapter;

import java.util.ArrayList;
import java.util.Calendar;


public class DataFrag extends Fragment  {

    private static final int MY_REQUEST_CODE_CAMERA = 1;
    private static final int MY_REQUEST_CODE_EXTERNAL_STORAGE = 2;
    private static final int MY_REQUEST_CODE_GPS = 3;
    private Geo infoGeo;
    private ListView lv;
    private UsuarioDbHelper out;


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View viewData = inflater.inflate(R.layout.fragment_data, container, false);

        Bundle b = getArguments();



        out = new UsuarioDbHelper(getContext());
        lv = (ListView)  viewData.findViewById(R.id.LISTA);
        ////  inicializacion nodejs Neurona


        Button Drop = (Button) viewData.findViewById(R.id.DROP);
        Drop.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                buttonPressDrop();
            }
        });

        Button Get = (Button) viewData.findViewById(R.id.GET);
        Get.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                getGeo();
            }
        });

        Button Geo = (Button) viewData.findViewById(R.id.GEO);
        Geo.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                dialogBox();
            }
        });

        return viewData;
    }


    public void buttonPressDrop(){
        out.dropALL();
    }

    public void getRandom(){
        fillData(out.getLista());
    }

    public void fillData(ArrayList<String> arrayInfo){

       ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, arrayInfo);
        // Set The Adapter
        lv.setAdapter(arrayAdapter);
    }

    public void getGeo(){
        fillData(out.getLista());
    };

    public void dialogBox() {


        if(Build.VERSION.SDK_INT >= 23){
            if(ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED ){
                Log.d("GPS", "00" );

                if( ActivityCompat.shouldShowRequestPermissionRationale((Activity) getContext(),Manifest.permission.ACCESS_COARSE_LOCATION ) ){
                    Log.d("GPS", "OK ACCESS_COARSE_LOCATION" );

                }
                else if( ActivityCompat.shouldShowRequestPermissionRationale((Activity) getContext(),Manifest.permission.ACCESS_FINE_LOCATION ) ){
                    Log.d("GPS", "OK ACCESS_FINE_LOCATION" );

                }

                ActivityCompat.requestPermissions((Activity) getContext(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},3);
            }
        }


        infoGeo = new Geo(getContext());
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getContext());
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



}