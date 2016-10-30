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
import com.codepath.apps.restclienttemplate.models.TweetUser;
import com.raizlabs.android.dbflow.sql.language.SQLite;

import org.parceler.Parcels;

import java.util.List;

public class TweetListActivity extends AppCompatActivity implements TwitterListAdapter.ClickDelegate {

    public static final String TAG = TweetListActivity.class.toString();
    TwitterClient twitterClient;
    RecyclerView rvTweetsList;
    List<Tweet> tweets;
    TwitterListAdapter twitterListAdapter;
    SwipeRefreshLayout swipeRefreshLayout;
    Toolbar toolbar;
    TweetUser currentUser;
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
        twitterListAdapter = new TwitterListAdapter(tweets, this, this);
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

        twitterClient.getCurrentUserDetails(new TwitterClient.TweetUserResponseInterface() {
            @Override
            public void fetchedUserInfo(TweetUser user) {
                currentUser = user;
            }
        });

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
                it.putExtra("currentUser", Parcels.wrap(currentUser));
                startActivityForResult(it, 200);
                return true;
            }
        });

        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data == null) {
            return;
        }
        Tweet postedTweet = Parcels.unwrap(data.getParcelableExtra("tweet"));
        twitterListAdapter.appendTweetAtPosition(postedTweet, 0);
        rvTweetsList.smoothScrollToPosition(0);
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

    @Override
    public void onTweetClicked(Tweet tweet) {
        Intent intent = new Intent(this, TweetDetailActivity.class);
        intent.putExtra("selectedTweet", Parcels.wrap(tweet));
        startActivity(intent);


    }
}
