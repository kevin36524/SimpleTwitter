package com.codepath.apps.restclienttemplate.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.codepath.apps.restclienttemplate.SimpleTwitterApplication;
import com.codepath.apps.restclienttemplate.TwitterClient;
import com.codepath.apps.restclienttemplate.models.Tweet;
import com.raizlabs.android.dbflow.sql.language.SQLite;

import java.util.List;

/**
 * Created by patelkev on 11/5/16.
 */

public class HomeTimelineFragment extends TweetsListFragment {
    TwitterClient twitterClient;
    HomeTimeLineFragmentListener homeTimeLineFragmentListener;

    public interface HomeTimeLineFragmentListener {
        void setRefreshing(Boolean refreshing);
        void showError(String errorString);
        Boolean isNetworkAvailable();
    }

    public void setHomeTimeLineFragmentListener(HomeTimeLineFragmentListener homeTimeLineFragmentListener) {
        this.homeTimeLineFragmentListener = homeTimeLineFragmentListener;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        twitterClient = SimpleTwitterApplication.getTwitterClient();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = super.onCreateView(inflater, container, savedInstanceState);
        List<Tweet> tweets = SQLite.select().from(Tweet.class).queryList();
        resetRecyclerViewWithTweets(tweets);
        return v;
    }

    public void fetchTimelineAsync() {
        if (!homeTimeLineFragmentListener.isNetworkAvailable()) {
            homeTimeLineFragmentListener.showError("No network connectivity");
            homeTimeLineFragmentListener.setRefreshing(false);
        } else {
            twitterClient.getTimelineTweets(25, 1, null, new TwitterClient.TweetsResponseInterface() {
                @Override
                public void fetchedTweets(List<Tweet> tweets) {
                    resetRecyclerViewWithTweets(tweets);
                    homeTimeLineFragmentListener.setRefreshing(false);
                }
            });
        }
    }

    @Override
    protected void loadMoreTweets(int page, int totalItemsCount, RecyclerView view) {
        fetchNewTweetsWithLastTweetID(twitterListAdapter.getLastTweetID());
    }

    private void fetchNewTweetsWithLastTweetID(long lastTweetID) {
        twitterClient.getTimelineTweets(25, 1, lastTweetID, new TwitterClient.TweetsResponseInterface() {
            @Override
            public void fetchedTweets(List<Tweet> tweets) {
                appendTweets(tweets);
            }
        });
    }
}
