package com.example.yhwinnie.representapp;

/**
 * Created by yhwinnie on 3/2/16.
 */
public class Member implements java.io.Serializable {
    private String name;
    private String party;
    private String email;
    private String link;
    private String tweet;
    private String icon;
    private String termEnd;
    private String bioGuideID;

    public Member(String name, String party, String email, String link, String tweet, String icon, String termEnd, String bioGuideID) {
        this.name = name;
        this.party = party;
        this.email = email;
        this.link = link;
        this.tweet = tweet;
        this.icon = icon;
        this.termEnd = termEnd;
        this.bioGuideID = bioGuideID;

    }

    public void AddIcon(String icon) {
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
    public String getIcon() {
        return icon;
    }
    public String getTermEnd() { return termEnd; }
    public String getBioGuideID() {return bioGuideID;}
}
