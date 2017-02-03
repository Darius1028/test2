package com.example.bmorales.test1;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.facebook.AccessToken;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.FileDescriptor;
import java.io.IOException;

import static android.app.Activity.RESULT_OK;

public class FotoFrag extends Fragment {

    private static final int MY_REQUEST_CODE_CAMERA = 1;
    private static final int MY_REQUEST_CODE_EXTERNAL_STORAGE = 2;
    private static final int MY_REQUEST_CODE_GPS = 3;
    private WebserviceActivity serv;
    private Geo infoGeo;
    private String Tok;
    private ImageView mImageView;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_foto, container, false);

        Bundle b = getArguments();

        final double lat = b.getDouble("lat");
        final double lng = b.getDouble("lng");

        Tok = b.getString("tok");

        mImageView = (ImageView) view.findViewById(R.id.imageView1);
        ////  inicializacion nodejs Neurona
        serv = new WebserviceActivity();

        Button button = (Button) view.findViewById(R.id.PICTURE);
        button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                dialogBoxPicture();
            }
        });


        return view;
    }

    public void getImage(View v){
        dialogBoxPicture();
    };
    public void dialogBoxPicture() {

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getContext(), R.style.MyAlertDialogStyle);
        alertDialogBuilder.setTitle("Imagen");

        alertDialogBuilder.setItems(new CharSequence[]{"Tomar Foto","Seleccionar Foto","Otro"}, new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case 0:
                        Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                        startActivityForResult(cameraIntent, MY_REQUEST_CODE_CAMERA);
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

        startActivityForResult(i,MY_REQUEST_CODE_EXTERNAL_STORAGE);
    };
    private Bitmap getBitmapFromUri(Uri uri) throws IOException {
        ParcelFileDescriptor parcelFileDescriptor =
               getContext().getContentResolver().openFileDescriptor(uri, "r");

        FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();
        Bitmap image = BitmapFactory.decodeFileDescriptor(fileDescriptor);
        parcelFileDescriptor.close();
        return image;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case MY_REQUEST_CODE_CAMERA: {
                if (resultCode == RESULT_OK) {
                    Bitmap bitmap = (Bitmap) data.getExtras().get("data");
                    mImageView.setImageBitmap(bitmap);
                }
                break;
            }
            case MY_REQUEST_CODE_EXTERNAL_STORAGE:
                if (resultCode == RESULT_OK && null != data) {
                    Uri selectedImage = data.getData();
                    String[] filePathColumn = { MediaStore.Images.Media.DATA};

                    Cursor cursor = getContext().getContentResolver().query(selectedImage, filePathColumn, null, null, null);
                    cursor.moveToFirst();

                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                    String picturePath = cursor.getString(columnIndex);
                    cursor.close();

                    Bitmap bmp = null;

                    try{
                        bmp = getBitmapFromUri(selectedImage);

                        //Uri imageUri = Uri.parse("/storage/emulated/0/DCIM/Camera/IMG_20170104_112852363_HDR.jpg");
                        if(Build.VERSION.SDK_INT >= 23){
                            if(ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||
                                    ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ||
                                    ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED ){

                                if( ActivityCompat.shouldShowRequestPermissionRationale((Activity) getContext(),Manifest.permission.READ_EXTERNAL_STORAGE ) ||
                                        ActivityCompat.shouldShowRequestPermissionRationale((Activity) getContext(),Manifest.permission.ACCESS_COARSE_LOCATION ) ||
                                        ActivityCompat.shouldShowRequestPermissionRationale((Activity) getContext(),Manifest.permission.ACCESS_FINE_LOCATION ) ){
                                }else{
                                    ActivityCompat.requestPermissions((Activity) getContext(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.READ_EXTERNAL_STORAGE },MY_REQUEST_CODE_GPS);
                                }
                            }
                        }

                        infoGeo = new Geo(getContext());
                        serv.uploadFile(Uri.parse(picturePath), Tok, infoGeo, getContext());
                        mImageView.setImageBitmap(bmp);


                    }catch (IOException e){
                        e.printStackTrace();
                    }
                }
                break;
        }
    }


}