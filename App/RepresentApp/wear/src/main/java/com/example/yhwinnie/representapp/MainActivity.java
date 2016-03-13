package com.example.yhwinnie.representapp;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.RelativeLayout;
import android.util.Log;
import android.view.View.OnTouchListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;

import java.lang.reflect.Member;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.net.URL;
import javax.net.ssl.HttpsURLConnection;
import java.io.InputStream;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import android.graphics.BitmapFactory;
import android.graphics.Bitmap;
import android.widget.ImageView;

public class MainActivity extends Activity {

    private TextView mRepName;
    private TextView mParty;
    private ImageView bioPic;
    private RelativeLayout mainLayout;
    private float x1, x2, y1, y2;
    private Bitmap bitmap;
    private String id;

    private ArrayList<Member> members = new ArrayList<Member>();

    private int index = 0;

    private String value;
    private String state;
    private String stateInitial;
    private String county;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        setContentView(R.layout.activity_main);

        mainLayout = (RelativeLayout) findViewById(R.id.mainLayout);

        mRepName = (TextView) findViewById(R.id.textView9);

        mParty = (TextView) findViewById(R.id.textView10);

        bioPic = (ImageView) findViewById(R.id.imageView3);



        Intent intent = getIntent();
        Bundle extras = intent.getExtras();

        if (extras != null) {
            String values = extras.getString("Path");
            Log.d("T", values);

            String split[] = values.split("\\|");

            value = split[0];
            county = split[1];

            Log.d("T", value);
            Log.d("T", "GGGG" + county);


            JSONObject reader = null;
            try {
                reader = new JSONObject(value.toString());
                JSONArray result = reader.optJSONArray("results");

                JSONObject jsonObject = result.getJSONObject(index);

                String firstName = jsonObject.optString("first_name").toString();
                String lastName = jsonObject.optString("last_name").toString();
                String party = jsonObject.optString("party").toString();
                id = jsonObject.optString("bioguide_id").toString();

                Log.d("T", id);

                state = jsonObject.optString("state_name").toString();
                stateInitial = jsonObject.optString("state").toString();



                //String url = "https://theunitedstates.io/images/congress/225x275/"+id+".jpg";
                new DownloadImageTask((ImageView) findViewById(R.id.imageView3)).execute("https://theunitedstates.io/images/congress/225x275/S001175.jpg");


                mRepName.setText(firstName + " " + lastName);
                if (party.equalsIgnoreCase("D")) {
                    mParty.setText("Democrat");
                }else {
                    mParty.setText("Republican");
                }



            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

        mainLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                JSONObject reader = null;
                try {
                    reader = new JSONObject(value.toString());
                    JSONArray result = reader.optJSONArray("results");

                    JSONObject jsonObject = result.getJSONObject(index);

                    String termEnd = jsonObject.optString("term_end").toString();
                    String party = jsonObject.optString("party").toString();
                    String bioGuideID = jsonObject.optString("bioguide_id").toString();
                    String name = jsonObject.optString("first_name").toString() + " " +
                            jsonObject.optString("last_name").toString();;

                    String info = termEnd + ", " + party + ", " + bioGuideID + ", " + name;

                    Intent sendIntent = new Intent(getBaseContext(), WatchToPhoneService.class);
                    sendIntent.putExtra("Info", info);
                    startService(sendIntent);




                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });


        mainLayout.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                switch (event.getAction()) {

                    //Get x and y coordinate
                    case MotionEvent.ACTION_DOWN: {
                        x1 = event.getX();
                        y1 = event.getY();
                        break;
                    }
                    case MotionEvent.ACTION_UP: {
                        x2 = event.getX();
                        y2 = event.getY();
                    }

                    if (x1 > x2) {
                        if (index < value.length() - 1) {

                            JSONObject reader = null;
                            try {
                                reader = new JSONObject(value.toString());
                                JSONArray result = reader.optJSONArray("results");

                                index += 1;

                                bioPic = (ImageView) findViewById(R.id.imageView3);


                                JSONObject jsonObject = result.getJSONObject(index);

                                String firstName = jsonObject.optString("first_name").toString();
                                String lastName = jsonObject.optString("last_name").toString();
                                String party = jsonObject.optString("party").toString();
                                id = jsonObject.optString("bioguide_id").toString();

                                String url = "https://theunitedstates.io/images/congress/225x275/"+id+".jpg";

                                new DownloadImageTask((ImageView) findViewById(R.id.imageView3)).execute(url);

                                mRepName.setText(firstName + " " + lastName);

                                if (party.equalsIgnoreCase("D")) {
                                    mParty.setText("Democrat");
                                }else {
                                    mParty.setText("Republican");
                                }

                                //index += 1;


                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        } else {
                            break;
                        }



                        //}
                    } else if (y1 > y2) {
                        //TO-DO: Slide from bottom to up animation

                        Intent startIntent = new Intent(MainActivity.this, PresidentialVote.class);
                        startIntent.putExtra("State", state);
                        startIntent.putExtra("StateInitial", stateInitial);
                        startIntent.putExtra("County", county);
                        startActivity(startIntent);
                    }
                }
                return false;
            }

        });



    }

    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }
        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }
        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }


}




















