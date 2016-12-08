package com.example.bmorales.test1;


import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;


/**
 * Created by bmorales on 11/24/2016.
 */

public class Radar extends BroadcastReceiver {

    private static final String SMS_RECEIVED = "android.provider.Telephony.SMS_RECEIVED";
    private static final String TAG = "SMSBroadcastReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i(TAG, "Intent recieved: " + intent.getAction());

        if (intent.getAction().equals(SMS_RECEIVED)) {
            Bundle bundle = intent.getExtras();
            if (bundle != null) {
                Object[] pdus = (Object[])bundle.get("pdus");

                final SmsMessage[] messages = new SmsMessage[pdus.length];
                for (int i = 0; i < pdus.length; i++) {
                    messages[i] = SmsMessage.createFromPdu((byte[])pdus[i]);
                }
                if (messages.length > -1) {

                    Toast.makeText(context, "Message recieved: " + messages[0].getMessageBody(), Toast.LENGTH_LONG).show();

                    if(  messages[0].getMessageBody().indexOf("CUENTA2") > 0 ){
                        Geo geo = new Geo();

                        geo.getLocation(context);
                        Toast.makeText(context, " Lat: " + geo.getLatitude() + " Log: " + geo.getLongitude() , Toast.LENGTH_LONG).show();


                    }
                }
            }
        }
    }
}