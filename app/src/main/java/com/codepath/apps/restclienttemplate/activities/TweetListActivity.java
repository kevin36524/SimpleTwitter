package com.codepath.apps.restclienttemplate.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.codepath.apps.restclienttemplate.EndlessRecyclerViewScrollListener;
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
    Toolbar toolbar;
    LinearLayoutManager linearLayoutManager;
    TextView tvToolbarTitle;

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
        linearLayoutManager = new LinearLayoutManager(this);
        rvTweetsList.setLayoutManager(linearLayoutManager);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        tvToolbarTitle = (TextView) findViewById(R.id.tvToolbarTitle);
        tvToolbarTitle.setText("Simple Tweets");

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        EndlessRecyclerViewScrollListener scrollListener = new EndlessRecyclerViewScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                twitterClient.getTimelineTweets(25, 1, twitterListAdapter.getLastTweetID(), new TwitterClient.TweetsResponseInterface() {
                    @Override
                    public void fetchedTweets(List<Tweet> tweets) {
                        twitterListAdapter.appendTweets(tweets);
                    }
                });
            }
        };

        // Adds the scroll listener to RecyclerView
        rvTweetsList.addOnScrollListener(scrollListener);

        rvTweetsList.setAdapter(twitterListAdapter);
        twitterClient = SimpleTwitterApplication.getTwitterClient(); // singleton instance

        twitterClient.getTimelineTweets(25, 1, null, new TwitterClient.TweetsResponseInterface() {
            @Override
            public void fetchedTweets(List<Tweet> tweets) {
                twitterListAdapter.resetTweets(tweets);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.tweet_activity_menu, menu);

        MenuItem compose = menu.findItem(R.id.miCompose);
        compose.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Log.d(TAG, "Start a new compose Activity");
                Intent it = new Intent(TweetListActivity.this, TweetComposeActivity.class);
                startActivity(it);
                return true;
            }
        });

        return true;
    }



    private void fetchTimelineAsync(int i) {
        twitterClient.getTimelineTweets(25, 1, null, new TwitterClient.TweetsResponseInterface() {
            @Override
            public void fetchedTweets(List<Tweet> tweets) {
                // TODO update adapter
                twitterListAdapter.resetTweets(tweets);
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }
}
