package com.example.bmorales.test1;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;

import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import android.view.ViewGroup;

import android.support.v4.app.Fragment;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.FileDescriptor;
import java.io.IOException;
import java.util.ArrayList;

import id.zelory.compressor.Compressor;

public class MapFrag extends Fragment {

    MapView mMapView;
    Location tempLoc;
    private GoogleMap googleMap;
    private View _rootView;
    private ItemDbHelper out;
    private MarkerOptions mMarkerOptions;
    Marker myMarker;


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (_rootView == null) {
            _rootView = inflater.inflate(R.layout.fragment_map, container, false);
            Bundle b = getArguments();
            final double lat = b.getDouble("lat");
            final double lng = b.getDouble("lng");
            mMapView = (MapView) _rootView.findViewById(R.id.mapView);
            mMapView.onCreate(savedInstanceState);
            out = new ItemDbHelper(getContext());
            mMapView.onResume(); // needed to get the map to display immediately
            try {
                MapsInitializer.initialize(getActivity().getApplicationContext());
            } catch (Exception e) {
                e.printStackTrace();
            }
            mMapView.getMapAsync(new OnMapReadyCallback() {
                @Override
                public void onMapReady(GoogleMap mMap) {

                    googleMap = mMap;
                    // For showing a move to my location button
                    googleMap.setMyLocationEnabled(true);
                    // For dropping a marker at a point on the Map
                    LatLng current  = new LatLng(lat, lng);
                    mMarkerOptions = new MarkerOptions().position(current).title(""+lat+", "+lng ).draggable(true).icon(BitmapDescriptorFactory.fromResource(R.drawable.spin)).alpha(0.5f);

                    myMarker = googleMap.addMarker(mMarkerOptions);
                    Cursor tempUserData = out.getList();
                    for (tempUserData.moveToFirst(); !tempUserData.isAfterLast(); tempUserData.moveToNext()) {
                        //listItems.add(tempUserData.getString(1) +" Lat:"+tempUserData.getString(2)+" Lon:"+tempUserData.getString(3) + " Time:" + tempUserData.getString(4));
                        Double doubleLat = Double.parseDouble(tempUserData.getString(3));
                        Double doubleLng = Double.parseDouble(tempUserData.getString(4));
                        LatLng tempInfo = new LatLng(doubleLat, doubleLng);
                        MarkerOptions tempMarker = new MarkerOptions().position(tempInfo).title("SPIN").snippet("").alpha(0.5f);
                        String imagePath = tempUserData.getString(6);

                        File imgFile = new  File(imagePath);
                       // if(!imgFile.exists()){
                           // String temp = getRealPathFromURI(getContext(), Uri.parse(imagePath) );
                            //imgFile = new File(temp);
                       // }

                        if(imgFile.exists()){
                        Bitmap compressedImage = new Compressor.Builder(getContext())
                                .setMaxWidth(80)
                                .setQuality(75)
                                .setCompressFormat(Bitmap.CompressFormat.WEBP)
                                .setDestinationDirectoryPath(Environment.getExternalStoragePublicDirectory(
                                        Environment.DIRECTORY_PICTURES).getAbsolutePath())
                                .build()
                                .compressToBitmap(imgFile);


                            tempMarker.icon(BitmapDescriptorFactory.fromBitmap(compressedImage));
                        }

                        googleMap.addMarker(tempMarker);
                        // .icon(BitmapDescriptorFactory.fromResource("/storage/emulated/0/DCIM/Camera/IMG.jpg"));
                    }
                    // For zooming automatically to the location of the marker
                    CameraPosition cameraPosition = new CameraPosition.Builder().target(current).zoom(15).build();
                    googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

                    googleMap.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {

                        @Override
                        public void onMarkerDragStart(Marker marker) {
                        }

                        @Override
                        public void onMarkerDragEnd(Marker marker) {
                            if(myMarker != null){
                                myMarker.setTitle("" + googleMap.getMyLocation().getLatitude() + ", "  + googleMap.getMyLocation().getLongitude());

                                getActivity().getIntent().removeExtra("lat");
                                getActivity().getIntent().removeExtra("lng");

                                getActivity().getIntent().putExtra("lat", googleMap.getMyLocation().getLatitude());
                                getActivity().getIntent().putExtra("lng", googleMap.getMyLocation().getLongitude());

                            }
                        }

                        @Override
                        public void onMarkerDrag(Marker marker) {
                        }

                    });

                    googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                        @Override
                        public void onMapClick(LatLng latLng) {
                            if(myMarker != null){
                                myMarker.setTitle("" + latLng.latitude + ", "  + latLng.longitude);
                                myMarker.setPosition(latLng);



                                Intent intent = getActivity().getIntent();
                                String lat1 =  intent.getExtras().getString("lat");

                                intent.removeExtra("lat");
                                intent.removeExtra("lng");



                                intent.putExtra("lat", "" + latLng.latitude);
                                intent.putExtra("lng",  "" + latLng.longitude);

                                String lat14 =  intent.getExtras().getString("lat");
                                String lat15 =  intent.getExtras().getString("lat");


                            }
                        }
                    });

                }

            });



        }
        else{

        }
        return _rootView;
    }





    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }




    private Bitmap getBitmapFromUri(Uri uri) throws IOException {
        ParcelFileDescriptor parcelFileDescriptor =
                getContext().getContentResolver().openFileDescriptor(uri, "r");

        FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();
        Bitmap image = BitmapFactory.decodeFileDescriptor(fileDescriptor);
        parcelFileDescriptor.close();
        return image;
    }

    public String getRealPathFromURI(Context context, Uri contentUri) {
        Cursor cursor = null;
        try {
            String[] proj = { MediaStore.Images.Media.DATA };
            cursor = context.getContentResolver().query(contentUri,  proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }


}