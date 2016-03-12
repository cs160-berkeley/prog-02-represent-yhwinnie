package com.example.yhwinnie.representapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

public class DetailActivity extends AppCompatActivity {

    private TextView partyView;
    private TextView endTermView;
    private TextView committeeView;
    private TextView billsView;
    private String committee;
    private String bills;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        partyView = (TextView) findViewById(R.id.textView12);
        endTermView = (TextView) findViewById(R.id.textView14);
        committeeView = (TextView) findViewById(R.id.textView13);
        billsView = (TextView) findViewById(R.id.textView15);

        committee = "";
        bills = "";

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        String[] splitted = extras.getString("Info").split(", ");
        String party = splitted[1];
        String termEnd = splitted[0];
        final String bioGuideID = splitted[2];


        partyView.setText(party);
        endTermView.setText(termEnd);

        Thread thread = new Thread(new Runnable(){
            @Override
            public void run() {
                try {
                    printCommittee(bioGuideID);
                    printBills(bioGuideID);
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
                    committee += jsonObject.optString("name").toString() + ", ";

                }
                committeeView.setText(committee);
                committee = "";
            } catch (JSONException e) {
                // Handle Error
            }

        }
        finally {
            urlConnection.disconnect();
        }
    }

    public void printBills(String bioGuideID) throws IOException {
        //congress.api.sunlightfoundation.com/bills?sponsor_id=S001175&apikey=fcf68604828148359ae76a9a23ebfd71

        String apikey = "fcf68604828148359ae76a9a23ebfd71";
        String baseURL = "https://congress.api.sunlightfoundation.com";
        String zipCodeAddition = "/bills?sponsor_id="+bioGuideID+"&apikey="+apikey;
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
                    bills += jsonObject.optString("introduced_on").toString() + ": " +
                            jsonObject.optString("official_title").toString() + ", ";

                }
                billsView.setText(bills);
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
