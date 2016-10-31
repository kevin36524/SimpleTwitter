package com.codepath.apps.restclienttemplate.activities;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.View;
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
    FloatingActionButton fab_compose;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tweet_list);
        bindViews();
        initializeTweetsListWithCachedData();
        bindEndlessScrollViewListener();
        makeInitialNetworkCalls();
    }

    private void initializeTweetsListWithCachedData() {
        tweets = SQLite.select().from(Tweet.class).queryList();
        twitterListAdapter = new TwitterListAdapter(tweets, this, this);
        linearLayoutManager = new LinearLayoutManager(this);
        rvTweetsList.setLayoutManager(linearLayoutManager);
        rvTweetsList.setAdapter(twitterListAdapter);
    }

    private void bindEndlessScrollViewListener() {
        EndlessRecyclerViewScrollListener scrollListener = new EndlessRecyclerViewScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                if (!isNetworkAvailable()) {
                    showError("No network connectivity");
                    return;
                }
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
    }

    private void bindViews() {
        rvTweetsList = (RecyclerView) findViewById(R.id.rvTweetsList);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeContainer);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                fetchTimelineAsync();
            }
        });

        // Toolbar
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        tvToolbarTitle = (TextView) findViewById(R.id.tvToolbarTitle);
        tvToolbarTitle.setText("Simple Tweets");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        // Floating Compose
        fab_compose = (FloatingActionButton) findViewById(R.id.fab_compose);
        fab_compose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "Start a new compose Activity");
                Intent it = new Intent(TweetListActivity.this, TweetComposeActivity.class);
                it.putExtra("currentUser", Parcels.wrap(currentUser));
                startActivityForResult(it, 200);
            }
        });
    }

    private void makeInitialNetworkCalls() {

        twitterClient = SimpleTwitterApplication.getTwitterClient(); // singleton instance

        if (!isNetworkAvailable()) {
            showError("No network connectivity");
            return;
        }

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

    private void fetchTimelineAsync() {
        if (!isNetworkAvailable()) {
            showError("No network connectivity");
            swipeRefreshLayout.setRefreshing(false);
        } else {
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

    @Override
    public void onTweetClicked(Tweet tweet) {
        Intent intent = new Intent(this, TweetDetailActivity.class);
        intent.putExtra("selectedTweet", Parcels.wrap(tweet));
        startActivity(intent);
    }

    private void showError(String errorString) {
        //Toast.makeText(this, errorString, Toast.LENGTH_SHORT).show();
        Snackbar.make(rvTweetsList, errorString, Snackbar.LENGTH_SHORT)
                .show();
    }

    private Boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting();
    }
}
