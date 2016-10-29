package com.codepath.apps.restclienttemplate.models;

import android.text.format.DateUtils;

import com.codepath.apps.restclienttemplate.MyDatabase;
import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.ForeignKey;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;

import org.parceler.Parcel;

import java.util.Date;

/**
 * Created by patelkev on 10/27/16.
 */

// Sample response
/*
{
    "created_at": "Fri Oct 28 05:17:05 +0000 2016",
    "id": 791871388227428400,
    "id_str": "791871388227428352",
    "text": "LinkedIn Q3 sales up 23% to $960M in its last earnings ahead of MSFT acquisition https://t.co/QYEOmFFhwO",
    "truncated": false,
    "entities": {
        "hashtags": [],
        "symbols": [],
        "user_mentions": [],
        "urls": [{
            "url": "https://t.co/QYEOmFFhwO",
            "expanded_url": "http://tcrn.ch/2eRGdR1",
            "display_url": "tcrn.ch/2eRGdR1",
            "indices": [81, 104]
        }]
    },
    "source": "<a href=\"http://www.socialflow.com\" rel=\"nofollow\">SocialFlow</a>",
    "in_reply_to_status_id": null,
    "in_reply_to_status_id_str": null,
    "in_reply_to_user_id": null,
    "in_reply_to_user_id_str": null,
    "in_reply_to_screen_name": null,
    "user": USER,
    "geo": null,
    "coordinates": null,
    "place": null,
    "contributors": null,
    "is_quote_status": false,
    "retweet_count": 6,
    "favorite_count": 11,
    "favorited": false,
    "retweeted": false,
    "possibly_sensitive": false,
    "possibly_sensitive_appealable": false,
    "lang": "en"
}
*/

@Table(database = MyDatabase.class)
@Parcel(analyze={Tweet.class})
public class Tweet extends BaseModel {

    @PrimaryKey
    @Column
    long id;

    @Column
    Date created_at;

    @Column
    String text;

    @Column
    @ForeignKey(saveForeignKeyModel = false)
    TweetUser user;

    @Column
    int retweet_count;

    @Column
    int favorite_count;

    public Tweet() {
    }

    public long getId() {
        return id;
    }

    public String relativeTime() {
        if (created_at == null) {
            return "";
        }
        long dateMillis = created_at.getTime();
        return DateUtils.getRelativeTimeSpanString(dateMillis,
                System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS).toString();
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public TweetUser getUser() {
        return user;
    }

    public void setUser(TweetUser user) {
        this.user = user;
    }

    public int getRetweet_count() {
        return retweet_count;
    }

    public void setRetweet_count(int retweet_count) {
        this.retweet_count = retweet_count;
    }

    public int getFavorite_count() {
        return favorite_count;
    }

    public void setFavorite_count(int favorite_count) {
        this.favorite_count = favorite_count;
    }
}
