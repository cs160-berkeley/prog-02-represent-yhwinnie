package com.example.yhwinnie.representapp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.ImageView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import android.widget.AdapterView;

import javax.net.ssl.HttpsURLConnection;
import android.widget.LinearLayout;
import android.support.v7.widget.Toolbar;

import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterApiClient;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterAuthToken;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterLoginButton;
import com.twitter.sdk.android.core.models.Tweet;
import com.twitter.sdk.android.core.services.StatusesService;
import com.twitter.sdk.android.tweetui.UserTimeline;

import io.fabric.sdk.android.Fabric;

public class DisplayCurrentActivity extends AppCompatActivity {
    private List<Member> members = new ArrayList<Member>();
    private ListView list;
    private Toolbar mActionBarToolbar;
    private TwitterLoginButton loginButton;
    private TwitterSession session;
    private static final String TWITTER_KEY = "bpfuB6Os7cJv3Un2MMNDEQ2ON";
    private static final String TWITTER_SECRET = "9oN4NT6NXV3wD6BE1HsDRUVHUBHa7WCIdxoKQTU9RB8ab31dpi";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

//        mActionBarToolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(mActionBarToolbar);
//        getSupportActionBar().setDisplayShowHomeEnabled(true);


        getSupportActionBar().setTitle("CURRENT LOCATION");

        TwitterAuthConfig authConfig = new TwitterAuthConfig(TWITTER_KEY, TWITTER_SECRET );
        Fabric.with(this, new Twitter(authConfig));
        setContentView(R.layout.content_display_current);

        loginButton = (TwitterLoginButton)findViewById(R.id.twitter_login_button);
        loginButton.setCallback(new LoginHandler());

        assert getSupportActionBar() != null;

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

                        Intent requestLink = new Intent(DisplayCurrentActivity.this, DetailActivity.class);
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
            TwitterSession session = Twitter.getSessionManager().getActiveSession();
            TwitterAuthToken authToken = session.getAuthToken();
            String token = authToken.token;
            String secret = authToken.secret;

            //https://api.twitter.com/1.1/statuses/user_timeline.json?screen_name=twitterapi&count=2

        }

        @Override
        public void failure(TwitterException e) {

        }
    }


    private class myListAdapter extends ArrayAdapter<Member> {
        public myListAdapter() {
            super(DisplayCurrentActivity.this, R.layout.members_item, members);
        }
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            //Make sure we have a view to work with (may have been given null)

            final View itemView = getLayoutInflater().inflate(R.layout.members_item, parent, false);


            // Find the member to work with
            Member currentMember = members.get(position);

            // Fill the view
            ImageView imageView = (ImageView) itemView.findViewById(R.id.imageView);
            //imageView.setImageResource(currentMember.getIcon());

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

            TextView tweet = (TextView) itemView.findViewById(R.id.textView7);
            tweet.setText("@" + currentMember.getTweet());

            ImageView bioPicture = (ImageView) itemView.findViewById(R.id.imageView);

            ////https://theunitedstates.io/images/congress/[size]/[bioguide].jpg
            String id = currentMember.getBioGuideID();
            String url = "https://theunitedstates.io/images/congress/225x275/"+id+".jpg";

            try {
                URL imageURL = new URL(url);
                Bitmap bitmap = BitmapFactory.decodeStream((InputStream) new URL(url).getContent());
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




