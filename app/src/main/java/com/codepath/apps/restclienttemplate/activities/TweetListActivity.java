package com.codepath.apps.restclienttemplate.activities;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;

import com.astuetz.PagerSlidingTabStrip;
import com.codepath.apps.restclienttemplate.R;
import com.codepath.apps.restclienttemplate.SimpleTwitterApplication;
import com.codepath.apps.restclienttemplate.TwitterClient;
import com.codepath.apps.restclienttemplate.adapters.TweetsListFragmentsPagerAdapter;
import com.codepath.apps.restclienttemplate.fragments.HomeTimelineFragment;
import com.codepath.apps.restclienttemplate.fragments.MentionsTimelineFragment;
import com.codepath.apps.restclienttemplate.fragments.TweetsListFragment;
import com.codepath.apps.restclienttemplate.models.Tweet;
import com.codepath.apps.restclienttemplate.models.TweetUser;

import org.parceler.Parcels;

public class TweetListActivity extends AppCompatActivity
        implements TweetsListFragment.TweetsListFragmentsListener,
        TweetsListFragmentsPagerAdapter.TweetsListFragmentsPagerDataSource {

    public static final String TAG = TweetListActivity.class.toString();
    static final int NUM_OF_FRAGMENTS = 2;
    static final String fragmentTitles[] = {"Home", "@Mentions"};

    SwipeRefreshLayout swipeRefreshLayout;
    Toolbar toolbar;
    TweetUser currentUser;
    TextView tvToolbarTitle;
    FloatingActionButton fab_compose;
    HomeTimelineFragment homeTimelineFragment;
    MentionsTimelineFragment mentionsTimelineFragment;
    TweetsListFragment currentFragment;
    TwitterClient twitterClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tweet_list);
        bindViews();
        makeInitialNetworkCalls();
    }

    public void setCurrentFragment(TweetsListFragment currentFragment) {
        this.currentFragment = currentFragment;
        currentFragment.fetchTimelineAsync();
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

    }

    private void bindViews() {
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeContainer);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                currentFragment.fetchTimelineAsync();
            }
        });

        // Get the ViewPager and set it's PagerAdapter so that it can display items
        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        viewPager.setAdapter(new TweetsListFragmentsPagerAdapter(getSupportFragmentManager(), this));

        // Give the PagerSlidingTabStrip the ViewPager
        PagerSlidingTabStrip tabsStrip = (PagerSlidingTabStrip) findViewById(R.id.tabs);
        // Attach the view pager to the tab strip
        tabsStrip.setViewPager(viewPager);

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
        currentFragment.insertTweetAtPosition(postedTweet, 0);
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

    // Fragment page Adapter Datasource Methods
    @Override
    public Fragment getItem(int position) {
        if (position == 0) {
            currentFragment = new HomeTimelineFragment();
        } else {
            currentFragment = new MentionsTimelineFragment();
        }
        currentFragment.tweetsListFragmentsListener = this;

        return currentFragment;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return fragmentTitles[position];
    }

    @Override
    public int getCount() {
        return NUM_OF_FRAGMENTS;
    }
}
