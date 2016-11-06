package com.codepath.apps.restclienttemplate;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.codepath.apps.restclienttemplate.models.Tweet;
import com.codepath.apps.restclienttemplate.models.TweetUser;
import com.codepath.oauth.OAuthBaseClient;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;

import org.scribe.builder.api.Api;
import org.scribe.builder.api.TwitterApi;

import java.util.ArrayList;
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
    public static final String NO_NETWORK_ERROR = "Network is not reachable";

	public TwitterClient(Context context) {
		super(context, REST_API_CLASS, REST_URL, REST_CONSUMER_KEY, REST_CONSUMER_SECRET, REST_CALLBACK_URL);
	}

	public interface TweetsResponseInterface {
		public void fetchedTweets(List<Tweet> tweets);
        public void requestFailed(String reason);
	}

    public interface TweetUserResponseInterface {
        public void fetchedUserInfo(TweetUser user);
        public void requestFailed(String reason);
    }

    private  TextHttpResponseHandler responseHandlerFor(final String errorStringPrefix, final TweetsResponseInterface responseInterface) {
        return new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                responseInterface.requestFailed(errorStringPrefix + responseString);
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                Tweet[] tweets = gson.fromJson(responseString, Tweet[].class);
                responseInterface.fetchedTweets(Arrays.asList(tweets));
            }
        };
    }


    private  TextHttpResponseHandler responseHandlerFor(final String errorStringPrefix, final TweetUserResponseInterface responseInterface) {
        return new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                responseInterface.requestFailed(errorStringPrefix + responseString);
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                TweetUser user = gson.fromJson(responseString, TweetUser.class);
                responseInterface.fetchedUserInfo(user);
            }
        };
    }

    public void getMentionsTimelineTweets(Integer count, final TweetsResponseInterface tweetsResponseInterface) {
        if (!isNetworkReachable()) {
            tweetsResponseInterface.requestFailed(NO_NETWORK_ERROR);
            return;
        }
        String apiUrl = getApiUrl("statuses/mentions_timeline.json");
        RequestParams params = new RequestParams();
        params.put("count", count);
        client.get(apiUrl, params,responseHandlerFor("Fetching mentions_timeline failed ERR: ", tweetsResponseInterface));
    }

	public void getHomeTimelineTweets(Integer count, Integer since_id, final Long max_id, final TweetsResponseInterface tweetsResponseInterface) {
        if (!isNetworkReachable()) {
            tweetsResponseInterface.requestFailed(NO_NETWORK_ERROR);
            return;
        }
        String apiUrl = getApiUrl("statuses/home_timeline.json");
        RequestParams params = new RequestParams();
        params.put("count", count);
        params.put("since_id", since_id);
        if (max_id != null) {
            params.put("max_id",max_id);
        }
        client.get(apiUrl, params, new TextHttpResponseHandler() {
			@Override
			public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                tweetsResponseInterface.requestFailed("Fetching home_timeline failed ERR: " + responseString);
			}

			@Override
			public void onSuccess(int statusCode, Header[] headers, String responseString) {
				Tweet[] tweets = gson.fromJson(responseString, Tweet[].class);
                if (max_id != null) {
                    tweetsResponseInterface.fetchedTweets(Arrays.asList(tweets));
                    return;
                }
				for (int i = 0; i < tweets.length ; i++) {
                    tweets[i].setMediaUrl(tweets[i].getMediaUrl());
					tweets[i].save();
					tweets[i].getUser().save();
				}
				tweetsResponseInterface.fetchedTweets(Arrays.asList(tweets));
			}
		});
	}

    public void postTweet(String post, final TweetsResponseInterface tweetsResponseInterface) {
        if (!isNetworkReachable()) {
            tweetsResponseInterface.requestFailed(NO_NETWORK_ERROR);
            return;
        }
        String apiUrl = getApiUrl("statuses/update.json");
        RequestParams params = new RequestParams();
        params.put("status", post);
        client.post(apiUrl, params, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                tweetsResponseInterface.requestFailed("postTweet failed ERR: " + responseString);
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                ArrayList<Tweet> tweets = new ArrayList<Tweet>();
                tweets.add(gson.fromJson(responseString, Tweet.class));
                tweetsResponseInterface.fetchedTweets(tweets);
            }
        });
    }

    public void postRetweet(String id, final TweetsResponseInterface tweetsResponseInterface) {
        if (!isNetworkReachable()) {
            tweetsResponseInterface.requestFailed(NO_NETWORK_ERROR);
            return;
        }
        String apiUrl = getApiUrl("statuses/retweet/" + id + ".json");
        client.post(apiUrl, null, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                tweetsResponseInterface.requestFailed("postRetweet failed ERR: " + responseString);
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                ArrayList<Tweet> tweets = new ArrayList<Tweet>();
                tweets.add(gson.fromJson(responseString, Tweet.class));
                tweetsResponseInterface.fetchedTweets(tweets);
            }
        });
    }

    public void getUserTimeLine(String screenName, final TweetsResponseInterface tweetsResponseInterface) {
        if (!isNetworkReachable()) {
            tweetsResponseInterface.requestFailed(NO_NETWORK_ERROR);
            return;
        }
        String apiUrl = getApiUrl("statuses/user_timeline.json");
        RequestParams params = new RequestParams();
        params.put("count", 25);
        params.put("screen_name",screenName);
        client.get(apiUrl, params, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                tweetsResponseInterface.requestFailed("getUserTimeLine failed ERR: " + responseString);
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                Tweet[] tweets = gson.fromJson(responseString, Tweet[].class);
                tweetsResponseInterface.fetchedTweets(Arrays.asList(tweets));
            }
        });
    }

    public void getCurrentUserDetails(final TweetUserResponseInterface tweetUserResponseInterface) {
        if (!isNetworkReachable()) {
            tweetUserResponseInterface.requestFailed(NO_NETWORK_ERROR);
            return;
        }
        String apiUrl = getApiUrl("account/verify_credentials.json");
        client.get(apiUrl, null, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                tweetUserResponseInterface.requestFailed("getCurrentUserDetails failed ERR: " + responseString);
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                TweetUser user = gson.fromJson(responseString, TweetUser.class);
                tweetUserResponseInterface.fetchedUserInfo(user);
            }
        });
    }

    public Boolean isNetworkReachable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        // return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting();
        if ((activeNetworkInfo != null)&&(activeNetworkInfo.isConnected())){
            return true;
        }else{
            return false;
        }
    }
}
