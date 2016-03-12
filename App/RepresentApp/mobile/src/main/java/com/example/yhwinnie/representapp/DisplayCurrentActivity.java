package com.example.yhwinnie.representapp;

import android.content.Intent;
import android.os.Bundle;
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
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import android.widget.AdapterView;

import javax.net.ssl.HttpsURLConnection;
import android.widget.LinearLayout;

public class DisplayCurrentActivity extends AppCompatActivity {
    private List<Member> members = new ArrayList<Member>();
    private ListView list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_display_current);

        //populateMemberList();

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


                    String info = termEnd + ", " + party + ", " + bioID;

                    Intent requestLink = new Intent(DisplayCurrentActivity.this, DetailActivity.class);
                    requestLink.putExtra("Info", info);
                    //requestLink.putExtra("INDEX", position);

                    startActivity(requestLink);
                }
            });
    }



//    public void detailButton(View view) {
//
//        int index = list.getPositionForView((LinearLayout)view.getParent());
//        String termEnd = members.get(0).getTermEnd();
//        String party =  members.get(0).getParty();
//        String bioID = members.get(0).getBioGuideID();
//
//        Intent requestLink = new Intent(DisplayCurrentActivity.this, DetailActivity.class);
//        requestLink.putExtra("term_end", termEnd);
//        requestLink.putExtra("party", party);
//        requestLink.putExtra("bioID", bioID);
//
//        startActivity(requestLink);
//
//    }

//    public void detailInfo(String lat, String lon) throws IOException {
//
//        //congress.api.sunlightfoundation.com/legislators/locate?latitude=37.7833&longitude=122.4167&apikey=fcf68604828148359ae76a9a23ebfd71
//        String apikey = "fcf68604828148359ae76a9a23ebfd71";
//        String baseURL = "https://congress.api.sunlightfoundation.com";
//        String lonLatAddition = "/legislators/locate?latitude="+lat+"&longitude="+lon+"&apikey="+apikey;
//        String url = baseURL + lonLatAddition;
//        URL apiUrl = new URL(url);
//        HttpsURLConnection urlConnection = (HttpsURLConnection) apiUrl.openConnection();
//        try {
//            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection
//                    .getInputStream()));
//            StringBuilder stringBuilder = new StringBuilder();
//            String line;
//            while ((line = bufferedReader.readLine()) != null) {
//                stringBuilder.append(line).append("\n");
//            }
//            bufferedReader.close();
//            Log.d("T", stringBuilder.toString());
//
//            try {
//
//                JSONObject reader = new JSONObject(stringBuilder.toString());
//                JSONArray result = reader.optJSONArray("results");
//
//                //Iterate the jsonArray and print the info of JSONObjects
//                for(int i = 0; i < result.length(); i++) {
//                    JSONObject jsonObject = result.getJSONObject(i);
//
//                    String firstName = jsonObject.optString("first_name").toString();
//                    String lastName = jsonObject.optString("last_name").toString();
//                    String party = jsonObject.optString("party").toString();
//                    String email = jsonObject.optString("oc_email").toString();
//                    String website = jsonObject.optString("website").toString();
//                    String twitter = jsonObject.optString("twitter_id").toString();
//
//                    String termEnd = jsonObject.optString("term_end").toString();
//                    String bioGuideID = jsonObject.optString("bioguide_id").toString();
//
//                    members.add(new Member(firstName + " " + lastName, party,
//                            email, website, twitter, R.drawable.diannefeinstein, termEnd, bioGuideID));
//
//
//                }
//
//                Intent startDetailActivity = new Intent(this, DetailActivity.class);
//                startActivity(startDetailActivity);
//
//                Intent sendIntent = new Intent(this, DisplayCurrentActivity.class);
//                sendIntent.putExtra("Members_List", members);
//                startActivity(sendIntent);
//                members = new ArrayList<Member>();
//
//            } catch (JSONException e) {
//                // Handle Error
//
//            }
//
//
//        }
//        finally {
//            urlConnection.disconnect();
//        }
//    }

    private class myListAdapter extends ArrayAdapter<Member> {
        public myListAdapter() {
            super(DisplayCurrentActivity.this, R.layout.members_item, members);
        }
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            //Make sure we have a view to work with (may have been given null)
            View itemView = convertView;
            if (itemView == null) {
                itemView = getLayoutInflater().inflate(R.layout.members_item, parent, false);
            }

            // Find the member to work with
            Member currentMember = members.get(position);

            // Fill the view
            ImageView imageView = (ImageView) itemView.findViewById(R.id.imageView);
            imageView.setImageResource(currentMember.getIcon());

            TextView name = (TextView) itemView.findViewById(R.id.textView3);
            name.setText(currentMember.getName());

            TextView party = (TextView) itemView.findViewById(R.id.textView4);
            party.setText("Party: " + currentMember.getParty());

            TextView email = (TextView) itemView.findViewById(R.id.textView5);
            email.setText(currentMember.getEmail());

            TextView link = (TextView) itemView.findViewById(R.id.textView6);
            link.setText(currentMember.getLink());

            TextView tweet = (TextView) itemView.findViewById(R.id.textView7);
            tweet.setText("@" + currentMember.getTweet());

            return itemView;

        }

    }

}




