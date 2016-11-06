package com.codepath.apps.restclienttemplate.activities;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.codepath.apps.restclienttemplate.R;
import com.codepath.apps.restclienttemplate.fragments.TweetsListFragment;
import com.codepath.apps.restclienttemplate.fragments.UserTimelineFragment;
import com.codepath.apps.restclienttemplate.models.TweetUser;

import org.parceler.Parcels;

public class UserProfileActivity extends AppCompatActivity implements TweetsListFragment.TweetsListFragmentsListener{

    TweetUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        currentUser = Parcels.unwrap(getIntent().getParcelableExtra("currentUser"));
        if (savedInstanceState == null) {
            createAndAppendFragment();
        }
    }

    private void createAndAppendFragment() {
        UserTimelineFragment userTimelineFragment = UserTimelineFragment.newInstance(currentUser);
        userTimelineFragment.tweetsListFragmentsListener = this;
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.fragment_container, userTimelineFragment);
        ft.commit();
    }

    @Override
    public void setRefreshing(Boolean refreshing) {

    }

    @Override
    public void showError(String errorString) {

    }

    @Override
    public Boolean isNetworkAvailable() {
        return true;
    }
}
