package com.example.yhwinnie.representapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.RelativeLayout;
import android.util.Log;
import android.view.View.OnTouchListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Member;
import java.util.ArrayList;

public class MainActivity extends Activity {

    private TextView mRepName;
    private TextView mParty;
    private RelativeLayout mainLayout;
    private float x1, x2, y1, y2;

    private ArrayList<Member> members = new ArrayList<Member>();

    private int index = 0;

    private String value;
    private String state;
    private String stateInitial;
    private String county;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mainLayout = (RelativeLayout) findViewById(R.id.mainLayout);

        mRepName = (TextView) findViewById(R.id.textView9);

        mParty = (TextView) findViewById(R.id.textView10);



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

                state = jsonObject.optString("state_name").toString();
                stateInitial = jsonObject.optString("state").toString();

                mRepName.setText(firstName + " " + lastName);
                mParty.setText(party);

                index = 1;


            } catch (JSONException e) {
                e.printStackTrace();
            }

        }


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

                                JSONObject jsonObject = result.getJSONObject(index);

                                String firstName = jsonObject.optString("first_name").toString();
                                String lastName = jsonObject.optString("last_name").toString();
                                String party = jsonObject.optString("party").toString();

                                mRepName.setText(firstName + " " + lastName);

                                mParty.setText(party);

                                index += 1;


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

                    String info = termEnd + ", " + party + ", " + bioGuideID;

                    Intent sendIntent = new Intent(getBaseContext(), WatchToPhoneService.class);
                    sendIntent.putExtra("Info", info);
                    startService(sendIntent);




                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
    }
}




















