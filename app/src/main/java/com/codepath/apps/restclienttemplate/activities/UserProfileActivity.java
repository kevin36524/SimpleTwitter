package com.codepath.apps.restclienttemplate.activities;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.codepath.apps.restclienttemplate.R;
import com.codepath.apps.restclienttemplate.fragments.TweetsListFragment;
import com.codepath.apps.restclienttemplate.fragments.UserHeaderFragment;
import com.codepath.apps.restclienttemplate.fragments.UserTimelineFragment;

public class UserProfileActivity extends AppCompatActivity implements TweetsListFragment.TweetsListFragmentsListener{

    Parcelable currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        currentUser = getIntent().getParcelableExtra("currentUser");
        if (savedInstanceState == null) {
            createAndAppendFragments();
        }
    }

    private void createAndAppendFragments() {
        UserTimelineFragment userTimelineFragment = UserTimelineFragment.newInstance(currentUser);
        userTimelineFragment.tweetsListFragmentsListener = this;

        UserHeaderFragment userHeaderFragment = UserHeaderFragment.newInstance(currentUser);

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.fragment_container, userTimelineFragment);
        ft.replace(R.id.fragment_header_container, userHeaderFragment);
        ft.commit();
    }

    @Override
    public void setRefreshing(Boolean refreshing) {

    }

    @Override
    public void showError(String errorString) {

    }
}
