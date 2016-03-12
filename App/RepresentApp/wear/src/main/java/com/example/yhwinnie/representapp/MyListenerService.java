package com.example.yhwinnie.representapp;

import android.content.Intent;
import android.util.Log;

import com.google.android.gms.wearable.WearableListenerService;
import com.google.android.gms.wearable.MessageEvent;
import java.lang.reflect.Member;

/**
 * Created by yhwinnie on 3/3/16.
 */
public class MyListenerService extends WearableListenerService {

    // In PhoneToWatchService, we passed in a path, either "/FRED" or "/LEXY"
    // These paths serve to differentiate different phone-to-watch messages
    private static final String path_rep = "/phoneToWatch";
    //private static final String PartyPath = "/Party";


    @Override
    public void onMessageReceived(MessageEvent messageEvent) {
        Log.d("T", "in WatchListenerService, got: " + messageEvent.getPath());

        if (messageEvent.getPath().equalsIgnoreCase(path_rep)) {
            final String value = new String(messageEvent.getData());
            Intent startIntent = new Intent(this, MainActivity.class);
            //you need to add this flag since you're starting a new activity from a service
            startIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startIntent.putExtra("Path", value);



            startActivity(startIntent);
        } else {
            super.onMessageReceived(messageEvent);

        }
    }
}
