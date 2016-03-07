package com.example.yhwinnie.represent;

/**
 * Created by yhwinnie on 3/2/16.
 */
public class Member {
    private String name;
    private String party;
    private String email;
    private String link;
    private String tweet;
    private int icon;

    public Member(String name, String party, String email, String link, String tweet, int icon) {
        this.name = name;
        this.party = party;
        this.email = email;
        this.link = link;
        this.tweet = tweet;
        this.icon = icon;
    }

    public String getName() {
        return name;
    }

    public String getParty() {
        return party;
    }
    public String getEmail() {
        return email;
    }
    public String getLink() {
        return link;
    }
    public String getTweet() {
        return tweet;
    }
    public int getIcon() {
        return icon;
    }
}
