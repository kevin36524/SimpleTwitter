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
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;

import com.codepath.apps.restclienttemplate.R;
import com.codepath.apps.restclienttemplate.SimpleTwitterApplication;
import com.codepath.apps.restclienttemplate.TwitterClient;
import com.codepath.apps.restclienttemplate.fragments.HomeTimelineFragment;
import com.codepath.apps.restclienttemplate.models.Tweet;
import com.codepath.apps.restclienttemplate.models.TweetUser;

import org.parceler.Parcels;

public class TweetListActivity extends AppCompatActivity implements HomeTimelineFragment.TweetsListFragmentsListener {

    public static final String TAG = TweetListActivity.class.toString();

    SwipeRefreshLayout swipeRefreshLayout;
    Toolbar toolbar;
    TweetUser currentUser;
    TextView tvToolbarTitle;
    FloatingActionButton fab_compose;
    HomeTimelineFragment homeTimelineFragment;
    TwitterClient twitterClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tweet_list);

        if (savedInstanceState == null) {
            homeTimelineFragment = (HomeTimelineFragment) getSupportFragmentManager().findFragmentById(R.id.tweetsListFragment);
        }

        bindViews();
        makeInitialNetworkCalls();
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

        homeTimelineFragment.fetchTimelineAsync();
    }

    private void bindViews() {
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeContainer);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                homeTimelineFragment.fetchTimelineAsync();
            }
        });

        homeTimelineFragment.tweetsListFragmentsListener = this;

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
        homeTimelineFragment.insertTweetAtPosition(postedTweet, 0);
    }

    @Override
    public void setRefreshing(Boolean refreshing) {
        swipeRefreshLayout.setRefreshing(refreshing);
    }

    public Boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting();
    }

    public void showError(String errorString) {
        //Toast.makeText(this, errorString, Toast.LENGTH_SHORT).show();
        Snackbar.make(swipeRefreshLayout, errorString, Snackbar.LENGTH_SHORT)
                .show();
    }
}
