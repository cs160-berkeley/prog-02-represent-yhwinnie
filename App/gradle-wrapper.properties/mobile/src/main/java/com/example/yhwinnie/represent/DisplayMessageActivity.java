package com.example.yhwinnie.represent;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class DisplayMessageActivity extends AppCompatActivity {
    private List<Member> members = new ArrayList<Member>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_message);

        populateMemberList();
        populateListView();
    }

    public void populateMemberList() {
        members.add(new Member("DIANNE FEINSTEIN", "Democrat", "dfeinstein@gmail.com", "www.feinsteind.com", "@lastMessage", R.drawable.diannefeinstein));
        members.add(new Member("BARBARA BOXER", "Democrat", "barb@gmail.com", "www.barbboxer.com", "@lastMessage", R.drawable.barbaraboxer));
        members.add(new Member("NANCY PELOSI", "Democrat", "nancyp@gmail.com", "www.pelosinancy.com", "@lastMessage", R.drawable.pelosi));
        members.add(new Member("JACKIE SPEIER", "Democrat", "jacksp@gmail.com", "www.speierjack.com", "@lastMessage", R.drawable.speier));
    }

    public void populateListView() {
        ArrayAdapter<Member> adapter = new myListAdapter();
        ListView list = (ListView) findViewById(R.id.listView);
        list.setAdapter(adapter);
    }

    public void detailButton(View view) {
        Intent startDetailActivity = new Intent(this, DetailActivity.class);
        startActivity(startDetailActivity);
    }

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
            party.setText(currentMember.getParty());

            TextView email = (TextView) itemView.findViewById(R.id.textView5);
            email.setText(currentMember.getEmail());

            TextView link = (TextView) itemView.findViewById(R.id.textView6);
            link.setText(currentMember.getLink());

            TextView tweet = (TextView) itemView.findViewById(R.id.textView7);
            tweet.setText(currentMember.getTweet());

            return itemView;

        }
    }
}
