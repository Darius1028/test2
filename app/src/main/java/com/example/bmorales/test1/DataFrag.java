package com.example.bmorales.test1;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
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

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;


public class DataFrag extends Fragment  {

    private static final int MY_REQUEST_CODE_CAMERA = 1;
    private static final int MY_REQUEST_CODE_EXTERNAL_STORAGE = 2;
    private static final int MY_REQUEST_CODE_GPS = 3;
    private Geo infoGeo;
    private ListView lv;
    private ItemDbHelper out;
    private View _rootView;


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        if (_rootView == null) {
            _rootView = inflater.inflate(R.layout.fragment_data, container, false);

            Bundle b = getArguments();

            out = new ItemDbHelper(getContext());
            lv = (ListView)  _rootView.findViewById(R.id.LISTA);
            ////  inicializacion nodejs Neurona

            Button Drop = (Button) _rootView.findViewById(R.id.DROP);
            Drop.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    buttonPressDrop();
                }
            });

            Button Get = (Button) _rootView.findViewById(R.id.GET);
            Get.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    fillData(out.getListaString());
                }
            });

            Button Geo = (Button) _rootView.findViewById(R.id.GEO);
            Geo.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    dialogBox();
                }
            });
            //final double lat =  getActivity().getIntent().getExtras().getDouble("lat");
            //final double lng =  getActivity().getIntent().getExtras().getDouble("lng");

            //Intent intent = getActivity().getIntent();
            //String sass = "";
        }
        return _rootView;
    }


    public void buttonPressDrop(){
        out.dropALL();
    }



    public void fillData(ArrayList<String> arrayInfo){

       ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getContext(),android.R.layout.simple_list_item_1, arrayInfo);
        // Set The Adapter
        lv.setAdapter(arrayAdapter);
    }


    public void dialogBox() {


        infoGeo = new Geo(getContext());
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getContext());
        alertDialogBuilder.setMessage(" Lat: " + infoGeo.getLatitude() + " Log: " + infoGeo.getLongitude());
        alertDialogBuilder.setPositiveButton("Ok",
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {

                        DateFormat df = new SimpleDateFormat("dd MM yyyy, HH:mm");
                        String date = df.format(Calendar.getInstance().getTime());
                        Item item = new Item( "ddds 34234543 355445", "info ",  "Desk ", "" + infoGeo.getLatitude() , "" + infoGeo.getLongitude(), date , "LoremIpsum" );
                        out.saveItem(item);

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