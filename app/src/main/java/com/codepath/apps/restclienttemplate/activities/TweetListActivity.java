package com.codepath.apps.restclienttemplate.activities;

import android.content.Intent;
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
import android.view.MenuItem;
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
        currentFragment.loadMoreTweets(0, 25);
    }

    private void makeInitialNetworkCalls() {

        twitterClient = SimpleTwitterApplication.getTwitterClient(); // singleton instance

        twitterClient.getCurrentUserDetails(new TwitterClient.TweetUserResponseInterface() {
            @Override
            public void fetchedUserInfo(TweetUser user) {
                currentUser = user;
            }

            @Override
            public void requestFailed(String reason) {
                showError(reason);
            }
        });

    }

    private void bindViews() {
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeContainer);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                currentFragment.loadMoreTweets(0, 25);
            }
        });

        // Get the ViewPager and set it's PagerAdapter so that it can display items
        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        viewPager.setAdapter(new TweetsListFragmentsPagerAdapter(getSupportFragmentManager(), this));
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                currentFragment = (position == 0) ? homeTimelineFragment : mentionsTimelineFragment;
            }

            @Override
            public void onPageSelected(int position) {
                currentFragment = (position == 0) ? homeTimelineFragment : mentionsTimelineFragment;
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

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

    public void showError(String errorString) {
        //Toast.makeText(this, errorString, Toast.LENGTH_SHORT).show();
        Snackbar.make(swipeRefreshLayout, errorString, Snackbar.LENGTH_SHORT)
                .show();
    }

    // Fragment page Adapter Datasource Methods
    @Override
    public Fragment getItem(int position) {
        if (position == 0) {
            homeTimelineFragment = new HomeTimelineFragment();
            currentFragment = homeTimelineFragment;
        } else {
            mentionsTimelineFragment = new MentionsTimelineFragment();
            currentFragment = mentionsTimelineFragment;
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

    public void launchProfile(MenuItem item) {
        Intent it = new Intent(this, UserProfileActivity.class);
        it.putExtra("currentUser", Parcels.wrap(currentUser));
        startActivity(it);
    }
}
