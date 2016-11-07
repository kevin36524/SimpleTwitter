package com.codepath.apps.restclienttemplate.activities;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;

import com.astuetz.PagerSlidingTabStrip;
import com.codepath.apps.restclienttemplate.R;
import com.codepath.apps.restclienttemplate.adapters.TweetsListFragmentsPagerAdapter;
import com.codepath.apps.restclienttemplate.fragments.TweetsListFragment;
import com.codepath.apps.restclienttemplate.fragments.UserFavoriteFragment;
import com.codepath.apps.restclienttemplate.fragments.UserHeaderFragment;
import com.codepath.apps.restclienttemplate.fragments.UserTimelineFragment;

public class UserProfileActivity extends AppCompatActivity implements
        TweetsListFragment.TweetsListFragmentsListener,
        TweetsListFragmentsPagerAdapter.TweetsListFragmentsPagerDataSource{

    Parcelable currentUser;
    static final String pagerTitles[] = {"TWEETS", "FAVORITES"};
    UserTimelineFragment userTimelineFragment;
    UserFavoriteFragment favoritesTimelineFragment;
    TweetsListFragment currentFragment;
    SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        currentUser = getIntent().getParcelableExtra("currentUser");
        if (savedInstanceState == null) {
            createAndAppendFragments();
        }
        bindViews();
        addViewPager();
    }

    private void bindViews() {
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeContainer);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                currentFragment.loadMoreTweets(0, 25);
            }
        });
    }

    private void addViewPager() {
        // Get the ViewPager and set it's PagerAdapter so that it can display items
        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        viewPager.setAdapter(new TweetsListFragmentsPagerAdapter(getSupportFragmentManager(), this));

        // Give the PagerSlidingTabStrip the ViewPager
        PagerSlidingTabStrip tabsStrip = (PagerSlidingTabStrip) findViewById(R.id.tabs);
        // Attach the view pager to the tab strip
        tabsStrip.setViewPager(viewPager);
    }

    private void createAndAppendFragments() {
        userTimelineFragment = UserTimelineFragment.newInstance(currentUser);
        userTimelineFragment.tweetsListFragmentsListener = this;

        favoritesTimelineFragment = UserFavoriteFragment.newInstance(currentUser);
        favoritesTimelineFragment.tweetsListFragmentsListener = this;

        currentFragment = userTimelineFragment;

        UserHeaderFragment userHeaderFragment = UserHeaderFragment.newInstance(currentUser);

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.fragment_header_container, userHeaderFragment);
        ft.commit();
    }

    @Override
    public void setRefreshing(Boolean refreshing) {
        swipeRefreshLayout.setRefreshing(refreshing);
    }

    @Override
    public void showError(String errorString) {
        Snackbar.make(swipeRefreshLayout, errorString, Snackbar.LENGTH_SHORT)
                .show();
    }

    // TweetsListFragmentsPagerDataSource
    @Override
    public Fragment getItem(int position) {
        if (position == 0) {
            return userTimelineFragment;
        }
        return favoritesTimelineFragment;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return pagerTitles[position];
    }

    @Override
    public int getCount() {
        return pagerTitles.length;
    }
}
