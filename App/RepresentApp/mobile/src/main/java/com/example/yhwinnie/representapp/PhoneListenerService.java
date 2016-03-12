package com.example.yhwinnie.representapp;

import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.WearableListenerService;
import android.util.Log;
import android.widget.Toast;
import android.content.Context;
import android.content.Intent;
/**
 * Created by yhwinnie on 3/9/16.
 */
public class PhoneListenerService extends WearableListenerService {

    private static final String TOAST = "/send_toast";

    @Override
    public void onMessageReceived(MessageEvent messageEvent) {
        Log.d("T", "inPhoneListenerService, got: " + messageEvent.getPath());
        if (messageEvent.getPath().equalsIgnoreCase(TOAST)) {
            final String value = new String(messageEvent.getData());

            Intent startIntent = new Intent(this, DetailActivity.class);
            //you need to add this flag since you're starting a new activity from a service
            startIntent.putExtra("Info", value);
            startIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            //startIntent.putExtra("Index", value);

            startActivity(startIntent);

            } else {
                super.onMessageReceived(messageEvent);

        }
    }
}
