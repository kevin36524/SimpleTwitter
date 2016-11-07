package com.codepath.apps.restclienttemplate.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
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

    private Boolean didLoadEarliestTweet;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        didLoadEarliestTweet = false;
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
    public void loadMoreTweets(int page, int totalItemsCount) {
        Long lastTweetID = null;
        if (didLoadEarliestTweet && page!=0) {
            tweetsListFragmentsListener.setRefreshing(false);
            return;
        }
        if (page != 0) {
            lastTweetID = twitterListAdapter.getLastTweetID();
        }
        fetchNewTweetsWithLastTweetID(lastTweetID);
    }

    private void fetchNewTweetsWithLastTweetID(final Long lastTweetID) {
        twitterClient.getMentionsTimelineTweets(25, lastTweetID, new TwitterClient.TweetsResponseInterface() {
            @Override
            public void fetchedTweets(List<Tweet> tweets) {
                if (lastTweetID == null) {
                    resetRecyclerViewWithTweets(tweets);
                    tweetsListFragmentsListener.setRefreshing(false);
                    return;
                }
                if (tweets.size() < 25) {
                    didLoadEarliestTweet = true;
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
