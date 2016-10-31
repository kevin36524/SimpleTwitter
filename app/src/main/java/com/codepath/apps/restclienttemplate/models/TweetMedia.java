package com.codepath.apps.restclienttemplate.models;

import org.parceler.Parcel;

/**
 * Created by patelkev on 10/30/16.
 *
 * "media": [{
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
 */
@Parcel(analyze={TweetMedia.class})
public class TweetMedia {

    String id;

    String media_url_https;

    public TweetMedia() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMedia_url_https() {
        return media_url_https;
    }

    public void setMedia_url_https(String media_url_https) {
        this.media_url_https = media_url_https;
    }
}
