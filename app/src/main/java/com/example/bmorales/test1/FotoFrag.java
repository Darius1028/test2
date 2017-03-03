package com.example.bmorales.test1;


import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.FileDescriptor;
import java.io.IOException;


import static android.app.Activity.RESULT_OK;

public class FotoFrag extends Fragment  {

    private static final int MY_REQUEST_CODE_CAMERA = 1;
    private static final int MY_REQUEST_CODE_EXTERNAL_STORAGE = 2;
    private static final int MY_REQUEST_CODE_GPS = 3;
    private WebserviceActivity serv;
    private String Tok;
    private ImageView mImageView;

    private Button buttonPICTURE;
    private Button buttonGUARDAR;


    private EditText textTitulo;
    private EditText textDescripcion;


    private View _rootView;
    private ItemDbHelper out;



    private Uri Path;

    private Intent intent;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        if (_rootView == null) {


            _rootView = inflater.inflate(R.layout.fragment_foto, container, false);

            Bundle b = getArguments();


            Tok = b.getString("tok");

            out = new ItemDbHelper(getContext());

            mImageView = (ImageView) _rootView.findViewById(R.id.imageView1);
            ////  inicializacion nodejs Neurona
            serv = new WebserviceActivity();

            buttonPICTURE = (Button) _rootView.findViewById(R.id.PICTURE);
            buttonPICTURE.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    dialogBoxPicture();
                }
            });

            buttonGUARDAR = (Button) _rootView.findViewById(R.id.GUARDAR);
            buttonGUARDAR.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    savePic();
                }
            });

            textTitulo =(EditText) _rootView.findViewById(R.id.TITULO);
            textDescripcion =(EditText) _rootView.findViewById(R.id.DESCRIPCION);

            intent = getActivity().getIntent();


        }


        return _rootView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);


    }
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
                    String result = MediaStore.Images.Media.insertImage(getContext().getContentResolver(), bitmap, "", "");
                    Path = Uri.parse(result);
                    mImageView.setImageBitmap(bitmap);
                }
                break;
            }
            case MY_REQUEST_CODE_EXTERNAL_STORAGE:
                if (resultCode == RESULT_OK && null != data) {
                    Uri selectedImage = data.getData();
                    Path = selectedImage;
                    String[] filePathColumn = { MediaStore.Images.Media.DATA};

                    Cursor cursor = getContext().getContentResolver().query(selectedImage, filePathColumn, null, null, null);
                    cursor.moveToFirst();

                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                    String picturePath = cursor.getString(columnIndex);
                    cursor.close();


                    Bitmap bmp = null;

                    try{
                        bmp = getBitmapFromUri(selectedImage);

                        mImageView.setImageBitmap(bmp);


                    }catch (IOException e){
                        e.printStackTrace();
                    }
                }
                break;
        }
    }




    ////////////////////////////////
    //////////Guardar///////////////
    ////////////////////////////////


    public boolean savePic(){


        String titulo = textTitulo.getText().toString();
        String descripcion = textDescripcion.getText().toString();



        if(titulo.length() == 0){
            textTitulo.setError("Dato requerido");
            return false;
        }

        if(descripcion.length() == 0){
            textDescripcion.setError("Dato requerido");
            return false;
        }

        if(Path == null){
            Toast.makeText(getContext(), "Carge una imagen ", Toast.LENGTH_LONG).show();
            return false;
        }
       // Bitmap image = ((BitmapDrawable)mImageView.getDrawable()).getBitmap();

        String lat = intent.getExtras().getString("lat");

        String lng = intent.getExtras().getString("lng");

        serv.uploadFile(Path, Tok, lat, lng, getContext());

        return false;
    }










}