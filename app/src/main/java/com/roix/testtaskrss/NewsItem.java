package com.roix.testtaskrss;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import java.io.Serializable;

/**
 * Created by u5 on 9/4/16.
 */
public class NewsItem implements Parcelable,Serializable{
    private String title;
    private String link;
    private String comment;
    private String description;
    private String pubDate;

    public NewsItem (String title,String comment,String link,String description,String pubDate){
        this.title=title;
        this.comment=comment;
        this.link=link;
        this.description=description;
        this.pubDate=pubDate;
    }

    public NewsItem (Parcel parcel){
        String [] data=new String[5];
        parcel.readStringArray(data);
        title=data[0];
        link=data[1];
        comment=data[2];
        description=data[3];
        pubDate=data[4];
    }

    public String getTitle() {
        return title;
    }

    public String getLink() {
        return link;
    }

    public String getComment() {
        return comment;
    }

    public String getDescription() {
        return description;
    }

    public String getPubDate() {
        return pubDate;
    }

    @Override
    public int describeContents() {
        return 0;
    }


    public static final Parcelable.Creator<NewsItem> CREATOR = new Parcelable.Creator<NewsItem>() {
        public NewsItem createFromParcel(Parcel in) {
            return new NewsItem(in);
        }

        @Override
        public NewsItem[] newArray(int i) {
            return new NewsItem[i];
        }
    };

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeStringArray(new String[]{title, link, comment, description, pubDate});

    }
}