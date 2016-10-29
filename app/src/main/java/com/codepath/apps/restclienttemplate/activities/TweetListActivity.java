package com.codepath.apps.restclienttemplate.activities;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.codepath.apps.restclienttemplate.R;
import com.codepath.apps.restclienttemplate.SimpleTwitterApplication;
import com.codepath.apps.restclienttemplate.TwitterClient;
import com.codepath.apps.restclienttemplate.adapters.TwitterListAdapter;
import com.codepath.apps.restclienttemplate.models.Tweet;
import com.raizlabs.android.dbflow.sql.language.SQLite;

import java.util.List;

public class TweetListActivity extends AppCompatActivity {

    public static final String TAG = TweetListActivity.class.toString();
    TwitterClient twitterClient;
    RecyclerView rvTweetsList;
    List<Tweet> tweets;
    TwitterListAdapter twitterListAdapter;
    SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tweet_list);
        rvTweetsList = (RecyclerView) findViewById(R.id.rvTweetsList);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeContainer);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                fetchTimelineAsync(0);
            }
        });

        tweets = SQLite.select().from(Tweet.class).queryList();
        twitterListAdapter = new TwitterListAdapter(tweets, this);
        rvTweetsList.setLayoutManager(new LinearLayoutManager(this));

        rvTweetsList.setAdapter(twitterListAdapter);
        twitterClient = SimpleTwitterApplication.getTwitterClient(); // singleton instance

//        twitterClient.getTimelineTweets(25, 1, 25, new TwitterClient.TweetsResponseInterface() {
//            @Override
//            public void fetchedTweets(List<Tweet> tweets) {
//                twitterListAdapter.resetTweets(tweets);
//            }
//        });
    }

    private void fetchTimelineAsync(int i) {
        twitterClient.getTimelineTweets(25, 1, 25, new TwitterClient.TweetsResponseInterface() {
            @Override
            public void fetchedTweets(List<Tweet> tweets) {
                // TODO update adapter
                twitterListAdapter.resetTweets(tweets);
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }
}
