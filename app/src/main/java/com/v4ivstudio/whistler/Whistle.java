package com.v4ivstudio.whistler;

/**
 * Created by Vaibhav Sharma on 5/9/2016.
 **/
public class Whistle {

    public Whistle(String whistler, String statement, String location, String category, boolean nsfw) {
        this.whistler = whistler;
        this.statement = statement;
        this.location = location;
        this.category = category;
        this.nsfw = nsfw;
    }

    public Whistle(int id, String whistler, String statement, String location, String category, boolean nsfw, String published_date) {
        this.id = id;
        this.whistler = whistler;
        this.statement = statement;
        this.location = location;
        this.category = category;
        this.nsfw = nsfw;
        this.published_date = published_date;

    }

    int id;

    public int getId() {
        return id;
    }

    public void setId(int mId) {
        this.id = mId;
    }

    String whistler;

    public String getWhistler() {
        return whistler;
    }

    public void setWhistler(String mWhistler) {
        this.whistler = mWhistler;
    }

    String statement;

    public String getStatement() {
        return statement;
    }

    public void setStatement(String mStatement) {
        this.statement = mStatement;
    }

    String location;

    public String getLocation() {
        return location;
    }

    public void setLocation(String mLocation) {
        this.location = mLocation;
    }

    String category;

    public String getCategory() {
        return category;
    }

    public void setCategory(String mCategory) {
        this.category = mCategory;
    }

    boolean nsfw;

    public boolean isNsfw() {
        return nsfw;
    }

    public void setNsfw(boolean mNSFW) {
        this.nsfw = mNSFW;
    }

    String published_date;

    public String getPublished() {
        return published_date;
    }

    public void setPublished(String mPublished) {
        this.published_date = mPublished;
    }
}
