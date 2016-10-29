package com.codepath.apps.restclienttemplate;

import android.content.Context;
import android.util.Log;

import com.codepath.apps.restclienttemplate.models.Tweet;
import com.codepath.oauth.OAuthBaseClient;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;

import org.scribe.builder.api.Api;
import org.scribe.builder.api.TwitterApi;

import java.util.Arrays;
import java.util.List;

import cz.msebera.android.httpclient.Header;

/*
 * 
 * This is the object responsible for communicating with a REST API. 
 * Specify the constants below to change the API being communicated with.
 * See a full list of supported API classes: 
 *   https://github.com/fernandezpablo85/scribe-java/tree/master/src/main/java/org/scribe/builder/api
 * Key and Secret are provided by the developer site for the given API i.e dev.twitter.com
 * Add methods for each relevant endpoint in the API.
 * 
 * NOTE: You may want to rename this object based on the service i.e TwitterClient or FlickrClient
 * 
 */
public class TwitterClient extends OAuthBaseClient {
	public static final String TAG = TwitterClient.class.toString();

	public static final Class<? extends Api> REST_API_CLASS = TwitterApi.class; // Change this
	public static final String REST_URL = "https://api.twitter.com/1.1/"; // Change this, base API URL
	public static final String REST_CONSUMER_KEY = "VkZthTkGiBqSH01B0EWMC90aX";       // Change this
	public static final String REST_CONSUMER_SECRET = "Uw3nIGLm3HiqB5NK55oAO2AkJX6GJr2jkTgynePGpb5JdS7KIF"; // Change this
	public static final String REST_CALLBACK_URL = "oauth://simpleTwitter"; // Change this (here and in manifest)

	final String DATE_FORMAT = "EEE MMM dd HH:mm:ss Z yyyy";
	final Gson gson = new GsonBuilder().setDateFormat(DATE_FORMAT).create();

	public TwitterClient(Context context) {
		super(context, REST_API_CLASS, REST_URL, REST_CONSUMER_KEY, REST_CONSUMER_SECRET, REST_CALLBACK_URL);
	}

	// CHANGE THIS
	// DEFINE METHODS for different API endpoints here
	public void getInterestingnessList(AsyncHttpResponseHandler handler) {
		String apiUrl = getApiUrl("?nojsoncallback=1&method=flickr.interestingness.getList");
		// Can specify query string params directly or through RequestParams.
		RequestParams params = new RequestParams();
		params.put("format", "json");
		client.get(apiUrl, params, handler);
	}

	public void getTimeline(Integer count, Integer since_id, Long max_id, AsyncHttpResponseHandler handler) {
		String apiUrl = getApiUrl("statuses/home_timeline.json");
		RequestParams params = new RequestParams();
		params.put("count", count);
		params.put("since_id", since_id);
		if (max_id != null) {
			params.put("max_id",max_id);
		}
		client.get(apiUrl, params, handler);
	}

	public interface TweetsResponseInterface {
		public void fetchedTweets(List<Tweet> tweets);
	}

	public void getTimelineTweets(Integer count, Integer since_id, final Long max_id, final TweetsResponseInterface tweetsResponseInterface) {
		getTimeline(count, since_id, max_id, new TextHttpResponseHandler() {
			@Override
			public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.d(TAG, "Some error with fetching");
			}

			@Override
			public void onSuccess(int statusCode, Header[] headers, String responseString) {
				Tweet[] tweets = gson.fromJson(responseString, Tweet[].class);
                if (max_id != null) {
                    tweetsResponseInterface.fetchedTweets(Arrays.asList(tweets));
                    return;
                }
				for (int i = 0; i < tweets.length ; i++) {
					tweets[i].save();
					tweets[i].getUser().save();
				}
				tweetsResponseInterface.fetchedTweets(Arrays.asList(tweets));
			}
		});
	}

	/* 1. Define the endpoint URL with getApiUrl and pass a relative path to the endpoint
	 * 	  i.e getApiUrl("statuses/home_timeline.json");
	 * 2. Define the parameters to pass to the request (query or body)
	 *    i.e RequestParams params = new RequestParams("foo", "bar");
	 * 3. Define the request method and make a call to the client
	 *    i.e client.get(apiUrl, params, handler);
	 *    i.e client.post(apiUrl, params, handler);
	 */
}
