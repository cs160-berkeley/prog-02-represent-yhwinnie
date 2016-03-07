package com.example.yhwinnie.representapp;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.wearable.view.DotsPageIndicator;
import android.support.wearable.view.WatchViewStub;
import android.view.View;
import android.view.WindowInsets;
import android.widget.TextView;
import android.support.v4.view.GestureDetectorCompat;
import android.support.wearable.view.GridViewPager;
import android.view.MotionEvent;
import android.widget.Toast;
import android.view.View.OnTouchListener;
import android.util.Log;
import android.widget.ImageView;
import android.view.ViewGroup;

public class MainActivity extends Activity {
    private float x1, y1;
    static final int MIN_DISTANCE = 150;
    private TextView mTextView;
    private GestureDetectorCompat mDetector;
    private static final String DEBUG_TAG = "Gestures";
    private String TAG = MainActivity.class.getSimpleName();


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

                    ImageView imageView = (ImageView) findViewById(R.id.imageView2);

                    imageView.setOnTouchListener(new View.OnTouchListener() {
                        @Override
                        public boolean onTouch(View view, MotionEvent event) {
                            String s = "";
                            s += "action=" + event.getAction();
                            s += ", X=" + event.getX();
                            s += ", Y=" + event.getY();

                            int i = 0;
                            TextView Name = (TextView) findViewById(R.id.Name);
                            TextView Party = (TextView) findViewById(R.id.Party);
                            ImageView image = (ImageView) findViewById(R.id.imageView2);

                            if (i == 0) {
                                Name.setText("Dianne Feinstein");
                                Party.setText("Democrat");
                                image.setImageResource(R.drawable.diannefeinstein);
                                i += 1;
                            }

                            if (i == 1) {
                                Name.setText("Barbara Boxer");
                                Party.setText("Democrat");
                                image.setImageResource(R.drawable.barbaraboxer);
                                i += 1;
                            }

                            if (i == 2) {
                                Name.setText("Nancy Pelosi");
                                Party.setText("Democrat");
                                image.setImageResource(R.drawable.pelosi);
                                i = 0;
                            }

                            return false;
                        }
                    });
                    imageView.setOnGenericMotionListener(new View.OnGenericMotionListener() {
                        @Override
                        public boolean onGenericMotion(View view, MotionEvent event) {
                            String s = "";
                            s += "action=" + event.getAction();
                            s += ", X=" + event.getX();
                            s += ", Y=" + event.getY();
                            Log.d(TAG, s);
                            return false;
                        }
                    });
                }
            }
        });
    }
}

















