package com.example.yhwinnie.represent;

import android.app.Activity;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.wearable.view.DotsPageIndicator;
import android.support.wearable.view.WatchViewStub;
import android.view.View;
import android.view.WindowInsets;
import android.widget.TextView;
import android.support.v4.view.GestureDetectorCompat;
import android.support.wearable.view.GridViewPager;
import com.example.yhwinnie.represent.R;
public class MainActivity extends Activity {

    private TextView mTextView;
    private GestureDetectorCompat mDetector;
    private static final String DEBUG_TAG = "Gestures";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final WatchViewStub stub = (WatchViewStub) findViewById(R.id.watch_view_stub);
        stub.setOnLayoutInflatedListener(new WatchViewStub.OnLayoutInflatedListener() {
            @Override
            public void onLayoutInflated(WatchViewStub stub) {
                mTextView = (TextView) stub.findViewById(R.id.text);
                String message = getIntent().getStringExtra("message");
                if (message == null || message.equalsIgnoreCase("")) {
                    message = "Hello World!";

                }
            }});}}