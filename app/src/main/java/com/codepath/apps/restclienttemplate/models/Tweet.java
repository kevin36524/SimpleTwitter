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

{
    "created_at": "Mon Oct 31 02:29:12 +0000 2016",
    "id": 792916302327349200,
    "id_str": "792916302327349248",
    "text": "ZEEEEEEKE!\n\n#DallasCowboys #PHIvsDAL https://t.co/Iaue4sAIYI",
    "truncated": false,
    "entities": {entities},
    "extended_entities": {
        "media": [{
            "id": 792916187323564000,
            "id_str": "792916187323564032",
            "indices": [
                37,
                60
            ],
            "media_url": "http://pbs.twimg.com/media/CwEBEkIUAAAERsC.jpg",
            "media_url_https": "https://pbs.twimg.com/media/CwEBEkIUAAAERsC.jpg",
            "url": "https://t.co/Iaue4sAIYI",
            "display_url": "pic.twitter.com/Iaue4sAIYI",
            "expanded_url": "https://twitter.com/NFL/status/792916302327349248/photo/1",
            "type": "photo",
            "sizes": {
                "small": {
                    "w": 680,
                    "h": 680,
                    "resize": "fit"
                },
                "large": {
                    "w": 1200,
                    "h": 1200,
                    "resize": "fit"
                },
                "medium": {
                    "w": 1200,
                    "h": 1200,
                    "resize": "fit"
                },
                "thumb": {
                    "w": 150,
                    "h": 150,
                    "resize": "crop"
                }
            }
        }]
    },
    "source": "<a href="
    http: //twitter.com" rel="nofollow">Twitter Web Client</a>",
        "in_reply_to_status_id": null,
    "in_reply_to_status_id_str": null,
    "in_reply_to_user_id": null,
    "in_reply_to_user_id_str": null,
    "in_reply_to_screen_name": null,
    "user": {
        "id": 19426551,
        "id_str": "19426551",
        "name": "NFL",
        "screen_name": "NFL",
        "location": "",
        "description": "Official Twitter account of the National Football League. Our Social Media Policy: https://t.co/EyuKeW3SvK",
        "url": "https://t.co/vIikzemJNp",
        "entities": {
            "url": {
                "urls": [{
                    "url": "https://t.co/vIikzemJNp",
                    "expanded_url": "http://www.nfl.com",
                    "display_url": "nfl.com",
                    "indices": [
                        0,
                        23
                    ]
                }]
            },
            "description": {
                "urls": [{
                    "url": "https://t.co/EyuKeW3SvK",
                    "expanded_url": "http://on.nfl.com/ZNKG9n",
                    "display_url": "on.nfl.com/ZNKG9n",
                    "indices": [
                        83,
                        106
                    ]
                }]
            }
        },
        "protected": false,
        "followers_count": 20431892,
        "friends_count": 2137,
        "listed_count": 45553,
        "created_at": "Sat Jan 24 01:28:06 +0000 2009",
        "favourites_count": 1197,
        "utc_offset": -14400,
        "time_zone": "Eastern Time (US & Canada)",
        "geo_enabled": true,
        "verified": true,
        "statuses_count": 128036,
        "lang": "en",
        "contributors_enabled": false,
        "is_translator": false,
        "is_translation_enabled": false,
        "profile_background_color": "FFFFFF",
        "profile_background_image_url": "http://pbs.twimg.com/profile_background_images/783952246/66e217ec60a9c3c977295fb8b7ae1601.jpeg",
        "profile_background_image_url_https": "https://pbs.twimg.com/profile_background_images/783952246/66e217ec60a9c3c977295fb8b7ae1601.jpeg",
        "profile_background_tile": false,
        "profile_image_url": "http://pbs.twimg.com/profile_images/783338002093264896/l5PkU6mn_normal.jpg",
        "profile_image_url_https": "https://pbs.twimg.com/profile_images/783338002093264896/l5PkU6mn_normal.jpg",
        "profile_banner_url": "https://pbs.twimg.com/profile_banners/19426551/1477275857",
        "profile_link_color": "0084B4",
        "profile_sidebar_border_color": "FFFFFF",
        "profile_sidebar_fill_color": "DDEEF6",
        "profile_text_color": "333333",
        "profile_use_background_image": true,
        "has_extended_profile": false,
        "default_profile": false,
        "default_profile_image": false,
        "following": true,
        "follow_request_sent": false,
        "notifications": false,
        "translator_type": "regular"
    },
    "geo": null,
    "coordinates": null,
    "place": null,
    "contributors": null,
    "is_quote_status": false,
    "retweet_count": 209,
    "favorite_count": 505,
    "favorited": false,
    "retweeted": false,
    "possibly_sensitive": false,
    "possibly_sensitive_appealable": false,
    "lang": "eu"
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
    String mediaUrl;

    TweetEntities entities;

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

    public TweetEntities getEntities() {
        return entities;
    }

    public void setEntities(TweetEntities entities) {
        this.entities = entities;
    }

    public String getMediaUrl() {
        if (this.getEntities() == null || this.getEntities().getMedia() == null) {
            return null;
        }
        return this.getEntities().getMedia().get(0).getMedia_url_https();
    }

    public void setMediaUrl(String mediaUrl) {
        this.mediaUrl = mediaUrl;
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
