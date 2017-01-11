package com.example.bmorales.test1;


import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.frosquivel.magicalcamera.Functionallities.PermissionGranted;
import com.frosquivel.magicalcamera.MagicalCamera;
import com.frosquivel.magicalcamera.Objects.MagicalCameraObject;

/**
 * Created by          Fabián Rosales Esquivel
 * Visit my web page   http://www.frosquivel.com
 * Visit my blog       http://www.frosquivel.com/blog
 * Created Date        on 5/19/16
 * This is an android library to take easy picture
 */
public class FragmentSample extends Fragment {

    private Activity activity;

    private ImageView imageView;
    private Button btntakephoto;
    private Button btnselectedphoto;
    private Button btnGoTo;
    private TextView texttitle;
    private Button btnSeeData;
    private Button btnFacialRecognition;

    private MagicalCamera magicalCamera;
    private PermissionGranted permissionGranted;
    private int RESIZE_PHOTO_PIXELS_PERCENTAGE = 80;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.mainindex, container, false);



        btnGoTo =  (Button) rootView.findViewById(R.id.btnGoTo);
        btnGoTo.setText("Go to Activity");

        btnGoTo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), MainIndex.class));
            }
        });




        return rootView;
    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

    }

}