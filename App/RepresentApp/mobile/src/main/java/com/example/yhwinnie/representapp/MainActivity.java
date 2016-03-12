package com.example.yhwinnie.representapp;

import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.content.Intent;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.app.NotificationCompat;
import android.app.Notification;
import android.widget.Button;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wearable.MessageApi;
import com.google.android.gms.wearable.NodeApi;
import com.google.android.gms.wearable.Wearable;
import android.widget.EditText;
import com.google.android.gms.wearable.Node;
import android.util.Log;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import android.app.AlertDialog;

import android.support.v4.content.ContextCompat;
import android.support.v4.app.ActivityCompat;

import android.util.Base64;

import java.io.IOException;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import android.location.Location;
import android.widget.TextView;
import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.app.Activity;
import android.location.Geocoder;
import android.location.Address;
import android.widget.Toast;
import java.net.URL;
import javax.net.ssl.HttpsURLConnection;
import java.io.InputStream;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONException;
import java.net.URLEncoder;
import android.os.AsyncTask;

public class MainActivity extends Activity implements
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    private Button mGoButton;
    private Button mCurrentLocationButton;
    private Location mLastLocation;

    private String mLatitudeText, mLongitudeText;
    private EditText zipCode;
    private String zipCodeText;

    public String twitter;

    public String time_line;

    public int index;

    public String county;
    public String countyCheck;


    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;
    private ArrayList<Member> members = new ArrayList<Member>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mGoButton = (Button) findViewById(R.id.goButton);
        mCurrentLocationButton = (Button) findViewById(R.id.currentButton);

        zipCode = (EditText) findViewById(R.id.editText);
        index = 0;


        if (client == null) {
            client = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }

        mGoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                zipCodeText = zipCode.getText().toString();

                final Geocoder geoCoder = new Geocoder(MainActivity.this);
                // 1 means only get a single address
                try {
                    List<Address> addresses = geoCoder.getFromLocationName(zipCodeText, 1);
                    Address address = addresses.get(0);

                    String cityName = address.getLocality();

                    Toast.makeText(MainActivity.this, cityName, Toast.LENGTH_LONG).show();
                    String message = String.format("Latitude: %f, Longitude: %f",
                            address.getLatitude(), address.getLongitude());

                    String lat = Double.toString(address.getLatitude());
                    String lon = Double.toString(address.getLongitude());
                    Log.d("T", message);

                    //https://maps.googleapis.com/maps/api/geocode/json?address=94704

                    new Thread(new Runnable(){
                        @Override
                        public void run() {
                            try {
                                String url = "https://maps.googleapis.com/maps/api/geocode/json?address="+zipCodeText;

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

                                            JSONArray addressComp = jsonObject.optJSONArray("address_components");
                                            //JSONArray types = addressComp.getJSONArray("types");


                                            for (int j = 0; j < addressComp.length(); j++) {
                                                JSONObject obj = addressComp.getJSONObject(j);

                                                JSONArray types = obj.getJSONArray("types");

                                                for (int k = 0; k < types.length(); k++ ) {
                                                    //JSONObject countyObject = types.getJSONObject(k);

                                                    countyCheck = types.getString(k);
                                                    Log.d("T", "OOOO" + countyCheck);

                                                    if (countyCheck.equals("administrative_area_level_2")) {
                                                        county = obj.optString("long_name").toString();
                                                        Log.d("T", county);
                                                        break;
                                                    }
                                                }

                                            }
                                        }

                                    } catch (JSONException e) {
                                        // Handle Error

                                    }


                                }
                                finally {
                                    urlConnection.disconnect();
                                }
                                //Your implementation goes here
                            } catch (Exception ex) {
                                ex.printStackTrace();
                            }
                            try {
                                Log.d("T", "gggg" + county);
                                printCongressmenFromZip(zipCodeText, county);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        }
                    }).start();




                    //Log.d("T", "gggg" + county);


//                    Thread thread = new Thread(new Runnable(){
//                        @Override
//                        public void run() {
//                            try {
//                                Log.d("T", "gggg" + county);
//                                printCongressmenFromZip(zipCodeText, county);
//                            } catch (Exception e) {
//                                e.printStackTrace();
//                            }
//                        }
//                    });
//
//                    thread.start();
//
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });


        mCurrentLocationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                Thread thread = new Thread(new Runnable(){
                    @Override
                    public void run() {
                        try {
                            printCongressmenCurrentLocation(mLatitudeText, mLongitudeText);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });

                thread.start();
            }
        });
    }

    public void printCongressmenFromZip(String zipCode, String county) throws IOException {
        String apikey = "fcf68604828148359ae76a9a23ebfd71";
        String baseURL = "https://congress.api.sunlightfoundation.com";
        String zipCodeAddition = "/legislators/locate?apikey="+apikey + "&zip=" + zipCode;
        String url = baseURL + zipCodeAddition;
        StringBuilder stringBuilder;
        String twitter = "";
        URL apiUrl = new URL(url);
        HttpsURLConnection urlConnection = (HttpsURLConnection) apiUrl.openConnection();
        try {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection
                    .getInputStream()));
            stringBuilder = new StringBuilder();
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
                    String firstName = jsonObject.optString("first_name").toString();
                    String lastName = jsonObject.optString("last_name").toString();
                    String party = jsonObject.optString("party").toString();
                    String email = jsonObject.optString("oc_email").toString();
                    String website = jsonObject.optString("website").toString();
                    twitter = jsonObject.optString("twitter_id").toString();

                    String termEnd = jsonObject.optString("term_end").toString();
                    String bioGuideID = jsonObject.optString("bioguide_id").toString();

                    members.add(new Member(firstName + " " + lastName, party,
                            email, website, twitter, R.drawable.diannefeinstein, termEnd, bioGuideID));
                }


            } catch (JSONException e) {
                // Handle Error
            }
        }
        finally {
            urlConnection.disconnect();
        }


        // https://api.twitter.com/1.1/statuses/user_timeline.json?screen_name=twitterapi&count=2

        final String CONSUMER_KEY = "bpfuB6Os7cJv3Un2MMNDEQ2ON";
        final String CONSUMER_SECRET = "9oN4NT6NXV3wD6BE1HsDRUVHUBHa7WCIdxoKQTU9RB8ab31dpi";

        final String TwitterTokenURL = "https://api.twitter.com/oauth2/token";
        final String TwitterStreamURL = "https://api.twitter.com/1.1/statuses/user_timeline.json?screen_name=";


        String send = stringBuilder.toString() + "|" + county;
        Intent sendIntentToWatch = new Intent(getBaseContext(), PhoneToWatchService.class);
        sendIntentToWatch.putExtra("Path", "phoneToWatch");
        sendIntentToWatch.putExtra("JSON", send);

        startService(sendIntentToWatch);

        Intent sendIntent = new Intent(this, DisplayMessageActivity.class);
        sendIntent.putExtra("Members_List", members);
        startActivity(sendIntent);

        members = new ArrayList<Member>();




    }

    public void printCongressmenCurrentLocation(String lat, String lon) throws IOException {

        //congress.api.sunlightfoundation.com/legislators/locate?latitude=37.7833&longitude=122.4167&apikey=fcf68604828148359ae76a9a23ebfd71
        String apikey = "fcf68604828148359ae76a9a23ebfd71";
        String baseURL = "https://congress.api.sunlightfoundation.com";
        String lonLatAddition = "/legislators/locate?latitude="+lat+"&longitude="+lon+"&apikey="+apikey;
        String url = baseURL + lonLatAddition;
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

                    String firstName = jsonObject.optString("first_name").toString();
                    String lastName = jsonObject.optString("last_name").toString();
                    String party = jsonObject.optString("party").toString();
                    String email = jsonObject.optString("oc_email").toString();
                    String website = jsonObject.optString("website").toString();
                    String twitter = jsonObject.optString("twitter_id").toString();

                    String termEnd = jsonObject.optString("term_end").toString();
                    String bioGuideID = jsonObject.optString("bioguide_id").toString();

                    members.add(new Member(firstName + " " + lastName, party,
                            email, website, twitter, R.drawable.diannefeinstein, termEnd, bioGuideID));


                }

            } catch (JSONException e) {
                // Handle Error

            }

            Intent sendIntentToWatch = new Intent(getBaseContext(), PhoneToWatchService.class);
            sendIntentToWatch.putExtra("Path", "phoneToWatch");
            sendIntentToWatch.putExtra("JSON", stringBuilder.toString());

            startService(sendIntentToWatch);

            Intent sendIntent = new Intent(this, DisplayCurrentActivity.class);
            sendIntent.putExtra("Members_List", members);
            startActivity(sendIntent);
            members = new ArrayList<Member>();


        }
        finally {
            urlConnection.disconnect();
        }
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onStart() {
        super.onStart();

        client.connect();

    }

    @Override
    public void onStop() {
        super.onStop();

        client.disconnect();
    }

    @Override
    public void onConnected(Bundle bundle) {

        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(client);

        if (mLastLocation != null) {
            mLatitudeText = (String.valueOf(mLastLocation.getLatitude()));
            mLongitudeText = (String.valueOf(mLastLocation.getLongitude()));
            }
        }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }


}