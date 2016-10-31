package com.codepath.apps.restclienttemplate.models;

import org.parceler.Parcel;

import java.util.List;

/**
 * Created by patelkev on 10/30/16.

 Sample Payload
 "entities": {
 "hashtags": [{
 "text": "DallasCowboys",
 "indices": [
 12,
 26
 ]
 }, {
 "text": "PHIvsDAL",
 "indices": [
 27,
 36
 ]
 }],
 "symbols": [],
 "user_mentions": [],
 "urls": [],
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
 }

 */


@Parcel(analyze={TweetEntities.class})
public class TweetEntities {
    List<TweetMedia> media;

    public TweetEntities() {
    }

    public List<TweetMedia> getMedia() {
        return media;
    }

    public void setMedia(List<TweetMedia> media) {
        this.media = media;
    }
}
