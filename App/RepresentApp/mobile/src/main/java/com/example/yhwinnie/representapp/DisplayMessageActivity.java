package com.example.yhwinnie.representapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.ImageView;
import android.util.Log;
import com.twitter.sdk.android.Twitter;
import io.fabric.sdk.android.Fabric;
import java.io.IOException;
import java.net.MalformedURLException;
import java.io.InputStream;
import java.util.Arrays;


import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.identity.TwitterLoginButton;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterAuthException;
import com.twitter.sdk.android.core.models.User;

import com.twitter.sdk.android.core.TwitterException;
import android.graphics.BitmapFactory;
import android.graphics.Bitmap;
import java.net.URL;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.TwitterApiClient;
import com.twitter.sdk.android.core.services.StatusesService;
import com.twitter.sdk.android.core.models.Tweet;
import com.twitter.sdk.android.core.TwitterAuthToken;
import android.os.StrictMode;
import com.twitter.sdk.android.tweetui.UserTimeline;


public class DisplayMessageActivity extends AppCompatActivity {
    private ArrayList<Member> members = new ArrayList<Member>();
    Toolbar mActionBarToolbar;
    private TwitterLoginButton loginButton;
    private static final String TWITTER_KEY = "bpfuB6Os7cJv3Un2MMNDEQ2ON";
    private static final String TWITTER_SECRET = "9oN4NT6NXV3wD6BE1HsDRUVHUBHa7WCIdxoKQTU9RB8ab31dpi";
    private String twitterImage;
    private ImageView twitterImageView;
    private TwitterSession session;
    //private View itemView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        TwitterAuthConfig authConfig = new TwitterAuthConfig(TWITTER_KEY, TWITTER_SECRET );
        Fabric.with(DisplayMessageActivity.this, new Twitter(authConfig));
        setContentView(R.layout.activity_display_message);

        mActionBarToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mActionBarToolbar);


        getSupportActionBar().setTitle("ZIP CODE: " + getIntent().getExtras().getString("ZIP CODE"));
//        Thread thread = new Thread(new Runnable(){
//            @Override
//            public void run() {
//                try {


        loginButton = (TwitterLoginButton)findViewById(R.id.twitter_login_button);
        loginButton.setCallback(new LoginHandler());

        session = Twitter.getSessionManager().getActiveSession();
        TwitterAuthToken authToken = session.getAuthToken();
        String token = authToken.token;
        String secret = authToken.secret;


        TwitterApiClient twitterApiClient = TwitterCore.getInstance().getApiClient();
        // Can also use Twitter directly: Twitter.getApiClient()
        StatusesService statusesService = twitterApiClient.getStatusesService();
        statusesService.show(524971209851543553L, null, null, null, new Callback<Tweet>() {
            @Override
            public void success(Result<Tweet> result) {
                //Do something with result, which provides a Tweet inside of result.data
                members = (ArrayList<Member>) getIntent().getSerializableExtra("Members_List");
                ArrayAdapter<Member> adapter = new myListAdapter();
                ListView list = (ListView) findViewById(R.id.listView);

                list.setAdapter(adapter);

                list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(final AdapterView<?> parent, View view, int position, long id) {
                        Member currentMember = members.get(position);
                        String termEnd = currentMember.getTermEnd();
                        String party = currentMember.getParty();
                        String bioID = currentMember.getBioGuideID();
                        String name = currentMember.getName();

                        String info = termEnd + ", " + party + ", " + bioID + ", " + name;

                        Intent requestLink = new Intent(DisplayMessageActivity.this, DetailActivity.class);
                        requestLink.putExtra("Info", info);

                        startActivity(requestLink);
                    }
                });
            }

            public void failure(TwitterException exception) {
                //Do something on failure
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        loginButton.onActivityResult(requestCode, resultCode, data);
    }

    private class LoginHandler extends Callback<TwitterSession> {
        @Override
        public void success(Result<TwitterSession> twitterSessionResult) {
            String output = "Status: " +
                    "Your login was successful " +
                    twitterSessionResult.data.getUserName() +
                    "\nAuth Token Received: " +
                    twitterSessionResult.data.getAuthToken().token;
            Log.d("T", output);



        }

        @Override
        public void failure(TwitterException e) {

        }
    }

    public void populateListView() {
        ArrayAdapter<Member> adapter = new myListAdapter();
        ListView list = (ListView) findViewById(R.id.listView);
        list.setAdapter(adapter);
    }


    private class myListAdapter extends ArrayAdapter<Member> {
        private String text;
        public myListAdapter() {
            super(DisplayMessageActivity.this, R.layout.members_item, members);
        }
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            //Make sure we have a view to work with (may have been given null)
            //final View itemView = convertView;
            //if (itemView == null) {
                final View itemView = getLayoutInflater().inflate(R.layout.members_item, parent, false);
            //}

            // Find the member to work with
            Member currentMember = members.get(position);

            // Fill the view
            ImageView imageView = (ImageView) itemView.findViewById(R.id.imageView);
            imageView.setImageResource(currentMember.getIcon());

            TextView name = (TextView) itemView.findViewById(R.id.textView3);
            name.setText(currentMember.getName().toUpperCase());

            TextView party = (TextView) itemView.findViewById(R.id.textView4);
            if (currentMember.getParty().equalsIgnoreCase("D"))  {
                party.setText("Democrat");
            }else{
                party.setText("Republican");
            }

            TextView email = (TextView) itemView.findViewById(R.id.textView5);
            email.setText(currentMember.getEmail());

            TextView link = (TextView) itemView.findViewById(R.id.textView6);
            link.setText(currentMember.getLink());



            ImageView bioPicture = (ImageView) itemView.findViewById(R.id.imageView);

            ////https://theunitedstates.io/images/congress/[size]/[bioguide].jpg
            String id = currentMember.getBioGuideID();
            String url = "https://theunitedstates.io/images/congress/225x275/"+id+".jpg";

            try {
                URL imageURL = new URL(url);
                Bitmap bitmap = BitmapFactory.decodeStream((InputStream)new URL(url).getContent());
                bioPicture.setImageBitmap(bitmap);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            UserTimeline userTimeline = new UserTimeline.Builder()
                    .screenName(currentMember.getTweet()).build();


            TwitterCore.getInstance().getApiClient(session).getStatusesService()
                    .userTimeline(null,
                            currentMember.getTweet(),
                            1,
                            null,
                            null,
                            null,
                            null,
                            null,
                            null,
                            new Callback<List<Tweet>>() {
                                @Override
                                public void success(Result<List<Tweet>> result) {
                                    text = "@@";
                                    for (Tweet t : result.data) {
                                        android.util.Log.d("twittercommunity", "tweet is " + t.text);
                                        TextView tweet = (TextView) itemView.findViewById(R.id.textView7);
                                        tweet.setText("Latest tweet: " + t.text);
                                    }
                                }

                                @Override
                                public void failure(TwitterException exception) {
                                    android.util.Log.d("twittercommunity", "exception " + exception);
                                }
                            });



            return itemView;

        }
    }


    }

