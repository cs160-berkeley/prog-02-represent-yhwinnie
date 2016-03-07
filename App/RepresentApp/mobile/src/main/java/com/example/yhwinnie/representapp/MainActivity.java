package com.example.yhwinnie.representapp;

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
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wearable.MessageApi;
import com.google.android.gms.wearable.NodeApi;
import com.google.android.gms.wearable.Wearable;
import android.widget.EditText;
import com.google.android.gms.wearable.Node;
import android.util.Log;

public class MainActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
    GoogleApiClient googleApiClient = null;
    public static final String TAG = "MyDataMAP.....";
    public static final String WEARABLE_DATA_PATH = "/wearable/data/path";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        GoogleApiClient.Builder builder = new GoogleApiClient.Builder(this);
        builder.addApi(Wearable.API);
        builder.addConnectionCallbacks(this);
        builder.addOnConnectionFailedListener(this);
        googleApiClient = builder.build();
    }

    @Override
    protected void onStart() {
        super.onStart();
        googleApiClient.connect();
    }

    protected void onStop() {
        if (googleApiClient != null && googleApiClient.isConnected()) {
            googleApiClient.disconnect();
        }
        super.onStop();
    }

    @Override
    public void onConnected(Bundle bundle) {
        sendMessage();
    }

    public void sendMessage() {
        if (googleApiClient.isConnected()) {
            String message = ((EditText) findViewById(R.id.editText)).getText().toString();
            if (message == null || message.equalsIgnoreCase("")) {
                message = "Hello World!";
            }
            new SendMessageToDataLayer(WEARABLE_DATA_PATH, message).start();

        }
        else {


        }
    }

    public class SendMessageToDataLayer extends Thread {
        String path;
        String message;

        public SendMessageToDataLayer(String path, String message) {
            this.path = path;
            this.message = message;
        }


    @Override
    public void run() {
        NodeApi.GetConnectedNodesResult nodesList = Wearable.NodeApi.getConnectedNodes(googleApiClient).await();
        for (Node node: nodesList.getNodes()) {
            MessageApi.SendMessageResult messageResult = Wearable.MessageApi.sendMessage(googleApiClient, node.getId(), path, message.getBytes()).await();
            if (messageResult.getStatus().isSuccess()) {
                // print success log
                Log.v(TAG, "Message: successfully sent to" + node.getDisplayName());
                Log.v(TAG, "Message: Node Id is" + node.getId());
                Log.v(TAG, "Message: Node size is" + nodesList.getNodes().size());
            }
            else {
                Log.v(TAG, "Error while seinding Message");
            }


            }
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    public void goButton(View view) {
        Intent startNewActivity = new Intent(this, DisplayMessageActivity.class);
        startActivity(startNewActivity);

        sendMessage();
    }

    public void currentLocationButton(View view) {
        Intent startCurrentLocationActivity = new Intent(this, DisplayCurrentActivity.class);
        startActivity(startCurrentLocationActivity);

        sendMessage();
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
}
