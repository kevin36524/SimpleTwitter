package com.codepath.apps.restclienttemplate.models;

import com.codepath.apps.restclienttemplate.MyDatabase;
import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;

import org.parceler.Parcel;

/**
 * Created by patelkev on 10/27/16.
 */

/* Sample
 {
        "id": 816653,
        "id_str": "816653",
        "name": "TechCrunch",
        "screen_name": "TechCrunch",
        "location": "San Francisco, CA",
        "description": "Breaking technology news, analysis, and opinions from TechCrunch. The number one guide for all things tech.\nGot a tip? Let us know tips@techcrunch.com",
        "url": "http://t.co/b5Oyx12qGG",
        "entities": {
            "url": {
                "urls": [{
                    "url": "http://t.co/b5Oyx12qGG",
                    "expanded_url": "http://techcrunch.com",
                    "display_url": "techcrunch.com",
                    "indices": [0, 22]
                }]
            },
            "description": {
                "urls": []
            }
        },
        "protected": false,
        "followers_count": 7727796,
        "friends_count": 858,
        "listed_count": 107441,
        "created_at": "Wed Mar 07 01:27:09 +0000 2007",
        "favourites_count": 1706,
        "utc_offset": -25200,
        "time_zone": "Pacific Time (US & Canada)",
        "geo_enabled": true,
        "verified": true,
        "statuses_count": 141145,
        "lang": "en",
        "contributors_enabled": false,
        "is_translator": false,
        "is_translation_enabled": true,
        "profile_background_color": "149500",
        "profile_background_image_url": "http://pbs.twimg.com/profile_background_images/621096023751004161/BAKy7hCT.png",
        "profile_background_image_url_https": "https://pbs.twimg.com/profile_background_images/621096023751004161/BAKy7hCT.png",
        "profile_background_tile": false,
        "profile_image_url": "http://pbs.twimg.com/profile_images/615392662233808896/EtxjSSKk_normal.jpg",
        "profile_image_url_https": "https://pbs.twimg.com/profile_images/615392662233808896/EtxjSSKk_normal.jpg",
        "profile_banner_url": "https://pbs.twimg.com/profile_banners/816653/1476894216",
        "profile_link_color": "097000",
        "profile_sidebar_border_color": "FFFFFF",
        "profile_sidebar_fill_color": "DDFFCC",
        "profile_text_color": "222222",
        "profile_use_background_image": true,
        "has_extended_profile": false,
        "default_profile": false,
        "default_profile_image": false,
        "following": true,
        "follow_request_sent": false,
        "notifications": false,
        "translator_type": "none"
    }
  */

@Table(database = MyDatabase.class)
@Parcel(analyze={TweetUser.class})
public class TweetUser extends BaseModel {

    @PrimaryKey
    @Column
    long id;

    @Column
    String name;

    @Column
    String screen_name;

    @Column
    String description;

    @Column
    String url;

    @Column
    String profile_image_url;

    @Column
    String profile_image_url_https;

    @Column
    String profile_banner_url;

    public TweetUser() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getScreen_name() {
        return screen_name;
    }

    public void setScreen_name(String screen_name) {
        this.screen_name = screen_name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getProfile_image_url() {
        return profile_image_url;
    }

    public void setProfile_image_url(String profile_image_url) {
        this.profile_image_url = profile_image_url;
    }

    public String getProfile_image_url_https() {
        return profile_image_url_https;
    }

    public void setProfile_image_url_https(String profile_image_url_https) {
        this.profile_image_url_https = profile_image_url_https;
    }

    public String getProfile_banner_url() {
        return profile_banner_url;
    }

    public void setProfile_banner_url(String profile_banner_url) {
        this.profile_banner_url = profile_banner_url;
    }
}
