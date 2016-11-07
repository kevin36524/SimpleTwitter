package com.codepath.apps.restclienttemplate.fragments;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.codepath.apps.restclienttemplate.TwitterClient;
import com.codepath.apps.restclienttemplate.models.Tweet;
import com.codepath.apps.restclienttemplate.models.TweetUser;

import org.parceler.Parcels;

import java.util.List;

/**
 * Created by patelkev on 11/6/16.
 */

public class UserFavoriteFragment  extends TweetsListFragment  {
    TweetUser displayUser;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public static UserFavoriteFragment newInstance(Parcelable user) {
        UserFavoriteFragment userTimelineFragment = new UserFavoriteFragment();
        Bundle args = new Bundle();
        args.putParcelable("user", user);
        userTimelineFragment.setArguments(args);
        userTimelineFragment.setArguments(args);
        return userTimelineFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = super.onCreateView(inflater, container, savedInstanceState);
        displayUser = Parcels.unwrap(getArguments().getParcelable("user"));
        fetchNewTweetsWithLastTweetID(null);
        return v;
    }

    @Override
    public void loadMoreTweets(int page, int totalItemsCount) {
        Long lastTweetID = null;
        if (page != 0) {
            lastTweetID = twitterListAdapter.getLastTweetID();
        }
        fetchNewTweetsWithLastTweetID(lastTweetID);
    }

    private void fetchNewTweetsWithLastTweetID(final Long lastTweetID) {
        twitterClient.getUserFavorites(displayUser.getScreen_name(), lastTweetID, new TwitterClient.TweetsResponseInterface() {
            @Override
            public void fetchedTweets(List<Tweet> tweets) {
                if (lastTweetID == null) {
                    resetRecyclerViewWithTweets(tweets);
                    tweetsListFragmentsListener.setRefreshing(false);
                    return;
                }
                appendTweets(tweets);
            }

            @Override
            public void requestFailed(String reason) {
                tweetsListFragmentsListener.showError(reason);
            }
        });
    }
}
