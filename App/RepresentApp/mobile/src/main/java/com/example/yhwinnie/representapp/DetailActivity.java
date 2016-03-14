package com.example.yhwinnie.representapp;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;
import android.widget.ImageView;

public class DetailActivity extends AppCompatActivity {

    private TextView partyView;
    private TextView endTermView;
    private TextView committeeView;
    private TextView billsView;
    private String committee;
    private String bills;
    private ImageView image;

    private Toolbar mActionBarToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        setContentView(R.layout.activity_detail);


        partyView = (TextView) findViewById(R.id.textView12);
        endTermView = (TextView) findViewById(R.id.textView14);
        committeeView = (TextView) findViewById(R.id.textView11);
        billsView = (TextView) findViewById(R.id.textView15);
        image = (ImageView) findViewById(R.id.imageView2);

        committee = "";
        bills = "";

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        String[] splitted = extras.getString("Info").split(", ");
        String party = splitted[1];
        String termEnd = splitted[0];
        final String bioGuideID = splitted[2];
        String name = splitted[3];

//        mActionBarToolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(mActionBarToolbar);
//        getSupportActionBar().setTitle(name);

        ////https://theunitedstates.io/images/congress/[size]/[bioguide].jpg
        String url = "https://theunitedstates.io/images/congress/450x550/"+bioGuideID+".jpg";

        try {
            URL imageURL = new URL(url);
            Bitmap bitmap = BitmapFactory.decodeStream((InputStream) new URL(url).getContent());
            image.setImageBitmap(bitmap);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }



        if (party.equalsIgnoreCase("D")) {
            partyView.setText("Democrat");
        }else if (party.equalsIgnoreCase("R")) {
            partyView.setText("Republican");
        } else {
            partyView.setText("Independent");
        }
        endTermView.setText(termEnd + "\n");

        Thread thread = new Thread(new Runnable(){
            @Override
            public void run() {
                try {
                    printCommittee(bioGuideID);
                    //printBills(bioGuideID);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        thread.start();
    }




    public void printCommittee(String bioGuideID) throws IOException {
        //congress.api.sunlightfoundation.com/committees?member_ids=P000197&apikey=fcf68604828148359ae76a9a23ebfd71

        String apikey = "fcf68604828148359ae76a9a23ebfd71";
        String baseURL = "https://congress.api.sunlightfoundation.com";
        String zipCodeAddition = "/committees?member_ids="+bioGuideID+"&apikey="+apikey;
        String url = baseURL + zipCodeAddition;
        URL apiUrl = new URL(url);
        HttpsURLConnection urlConnection = (HttpsURLConnection) apiUrl.openConnection();
        try {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection
                    .getInputStream()));
            StringBuilder stringBuilder = new StringBuilder();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line).append("\n");
            }
            bufferedReader.close();
            Log.d ("T", stringBuilder.toString());

            try {

                JSONObject reader = new JSONObject(stringBuilder.toString());
                JSONArray result = reader.optJSONArray("results");

                //Iterate the jsonArray and print the info of JSONObjects
                for(int i = 0; i < result.length(); i++) {
                    JSONObject jsonObject = result.getJSONObject(i);

                    //int firstName = Integer.parseInt(jsonObject.optString("first_name").toString());
                    //String committee = jsonObject.optString("name").toString();
                    committee += jsonObject.optString("name").toString() + "\n";
                    Log.d("T", committee);

                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (!committee.equalsIgnoreCase("")) {
                            committeeView.setText(committee);
                            committee = "";
                        }
                    }
                });

            } catch (JSONException e) {
                // Handle Error
            }

        }
        finally {
            urlConnection.disconnect();
        }

        //congress.api.sunlightfoundation.com/bills?sponsor_id=S001175&apikey=fcf68604828148359ae76a9a23ebfd71

        apikey = "fcf68604828148359ae76a9a23ebfd71";
        baseURL = "https://congress.api.sunlightfoundation.com";
        zipCodeAddition = "/bills?sponsor_id="+bioGuideID+"&apikey="+apikey;
        url = baseURL + zipCodeAddition;
        apiUrl = new URL(url);
        urlConnection = (HttpsURLConnection) apiUrl.openConnection();
        try {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection
                    .getInputStream()));
            StringBuilder stringBuilder = new StringBuilder();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line).append("\n");
            }
            bufferedReader.close();
            Log.d("T", stringBuilder.toString());

            try {

                JSONObject reader = new JSONObject(stringBuilder.toString());
                JSONArray result = reader.optJSONArray("results");

                //Iterate the jsonArray and print the info of JSONObjects
                for(int i = 0; i < result.length(); i++) {
                    JSONObject jsonObject = result.getJSONObject(i);

                    //int firstName = Integer.parseInt(jsonObject.optString("first_name").toString());
                    //String committee = jsonObject.optString("name").toString();
                    bills += jsonObject.optString("introduced_on").toString() + ": " +
                            jsonObject.optString("official_title").toString() + "\n";

                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        billsView.setText(bills);
                    }
                });
                //bills = "";
            } catch (JSONException e) {
                // Handle Error
            }

        }
        finally {
            urlConnection.disconnect();
        }
    }
}





