package com.example.yhwinnie.representapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import java.util.ArrayList;
import java.util.List;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.ImageView;
import android.util.Log;

public class DisplayMessageActivity extends AppCompatActivity {
    private ArrayList<Member> members = new ArrayList<Member>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_message);

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

                Intent requestLink = new Intent(DisplayMessageActivity.this, DetailActivity.class);
                requestLink.putExtra("Info", info);

                startActivity(requestLink);
            }
        });
    }







    public void populateListView() {
        ArrayAdapter<Member> adapter = new myListAdapter();
        ListView list = (ListView) findViewById(R.id.listView);
        list.setAdapter(adapter);
    }
//
//    public void detailButton(View view) {
//        Intent startDetailActivity = new Intent(this, DetailActivity.class);
//        startActivity(startDetailActivity);
//    }

    private class myListAdapter extends ArrayAdapter<Member> {
        public myListAdapter() {
            super(DisplayMessageActivity.this, R.layout.members_item, members);
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
            email.setText("Email: " + currentMember.getEmail());

            TextView link = (TextView) itemView.findViewById(R.id.textView6);
            link.setText(currentMember.getLink());

            TextView tweet = (TextView) itemView.findViewById(R.id.textView7);
            tweet.setText("@" + currentMember.getTweet());

            return itemView;

        }
    }
}
