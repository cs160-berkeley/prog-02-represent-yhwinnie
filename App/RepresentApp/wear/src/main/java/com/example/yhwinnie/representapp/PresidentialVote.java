package com.example.yhwinnie.representapp;

import android.app.Activity;
import android.os.Bundle;
import android.support.wearable.view.WatchViewStub;
import android.widget.TextView;
import android.content.Intent;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import android.content.Context;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONException;
import java.util.ArrayList;
import android.util.Log;

public class PresidentialVote extends Activity {

    private TextView mTextView;
    private TextView mCounty;

    private TextView mObama;
    private TextView mRomney;
    private TextView mObamaPercentage;
    private TextView mRomneyPercentage;

    private String stateInitial;
    private String county;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_presidential_vote);
        final WatchViewStub stub = (WatchViewStub) findViewById(R.id.watch_view_stub);
        stub.setOnLayoutInflatedListener(new WatchViewStub.OnLayoutInflatedListener() {
            @Override
            public void onLayoutInflated(WatchViewStub stub) {
                mTextView = (TextView) stub.findViewById(R.id.textView3);
                mCounty = (TextView) stub.findViewById(R.id.textView4);

                mObama = (TextView) stub.findViewById(R.id.textView5);
                mRomney = (TextView) stub.findViewById(R.id.textView6);
                mObamaPercentage = (TextView) stub.findViewById(R.id.textView7);
                mRomneyPercentage = (TextView) stub.findViewById(R.id.textView8);

                Intent intent = getIntent();
                Bundle extras = intent.getExtras();
                String state = extras.getString("State");
                stateInitial = extras.getString("StateInitial").toUpperCase();
                county = extras.getString("County").replaceAll(" County", "");


                mTextView.setText(state.toUpperCase());
                mCounty.setText(county.toUpperCase() + " COUNTY");

                ArrayList jsonObjectArray = new ArrayList();

                StringBuffer sb = new StringBuffer();
                BufferedReader br = null;

                try {
                    br = new BufferedReader(new InputStreamReader(getAssets().open(
                            "election-county-2012.json")));
                    String temp;
                    while ((temp = br.readLine()) != null)
                        sb.append(temp);
                    } catch (IOException e) {
                        e.printStackTrace();
                    } finally {
                        try {
                            br.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                String myJsonString = sb.toString();


                try {
                    String countyCheck;
                    String state_postal;
                    JSONArray result = new JSONArray(myJsonString);
                    for(int i = 0; i < result.length(); i++) {
                        JSONObject obj = result.getJSONObject(i);

                        countyCheck = obj.optString("county-name").toString();
                        state_postal = obj.optString("state-postal").toString();

                        if (countyCheck.equalsIgnoreCase(county) &&
                                state_postal.equalsIgnoreCase(stateInitial)) {
                            //mObama.setText(obj.optString("obama-vote").toString().toUpperCase());
                            //mRomney.setText(obj.optString("romney-vote").toString().toUpperCase());
                            mObamaPercentage.setText(obj.optString("obama-percentage")+ "%"
                                    .toString().toUpperCase());
                            mRomneyPercentage.setText(obj.optString("romney-percentage")+ "%"
                                    .toString().toUpperCase());

                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
