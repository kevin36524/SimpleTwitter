package com.codepath.apps.restclienttemplate.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.codepath.apps.restclienttemplate.TwitterClient;
import com.codepath.apps.restclienttemplate.models.Tweet;
import com.raizlabs.android.dbflow.sql.language.SQLite;

import java.util.List;

/**
 * Created by patelkev on 11/5/16.
 */

public class MentionsTimelineFragment extends TweetsListFragment {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = super.onCreateView(inflater, container, savedInstanceState);
        List<Tweet> tweets = SQLite.select().from(Tweet.class).queryList();
        resetRecyclerViewWithTweets(tweets);
        fetchNewTweetsWithLastTweetID(null);
        return v;
    }

    @Override
    protected void loadMoreTweets(int page, int totalItemsCount, RecyclerView view) {
        tweetsListFragmentsListener.setRefreshing(false);
    }

    private void fetchNewTweetsWithLastTweetID(final Long lastTweetID) {
        twitterClient.getMentionsTimelineTweets(25, new TwitterClient.TweetsResponseInterface() {
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
