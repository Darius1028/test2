package com.example.bmorales.test1;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.os.Bundle;

/**
 * Created by          Fabián Rosales Esquivel
 * Visit my web page   http://www.frosquivel.com
 * Visit my blog       http://www.frosquivel.com/blog
 * Created Date        on 5/19/16
 * This is an android library to take easy picture
 */
public class ActivityForFragment extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activity_for_fragment);

        FragmentTransaction ft = this.getFragmentManager().beginTransaction();
        ft.replace(R.id.fragment_pace, new FragmentSample()).addToBackStack("tag").commit();
    }

}