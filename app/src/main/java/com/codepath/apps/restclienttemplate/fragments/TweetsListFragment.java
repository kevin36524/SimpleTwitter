package com.codepath.apps.restclienttemplate.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.codepath.apps.restclienttemplate.EndlessRecyclerViewScrollListener;
import com.codepath.apps.restclienttemplate.R;
import com.codepath.apps.restclienttemplate.activities.TweetDetailActivity;
import com.codepath.apps.restclienttemplate.adapters.TwitterListAdapter;
import com.codepath.apps.restclienttemplate.models.Tweet;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by patelkev on 11/4/16.
 */

public class TweetsListFragment extends Fragment implements TwitterListAdapter.ClickDelegate {

    public interface TweetsListFragmentsListener {
        void fetchNewTweetsWithLastTweetID(long lastTweetID);
    }


    //inflation logic
    RecyclerView rvTweetsList;
    LinearLayoutManager linearLayoutManager;
    TwitterListAdapter twitterListAdapter;
    List<Tweet> tweets = new ArrayList<Tweet>();
    public TweetsListFragmentsListener tweetsListFragmentsListener;
    EndlessRecyclerViewScrollListener endlessRecyclerViewScrollListener;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_tweets_list, container, false);
        rvTweetsList = (RecyclerView) v.findViewById(R.id.rvTweetsList);
        initializeTweetsListWithCachedData();
        bindEndlessScrollViewListener();
        return v;
    }

    //creation logic
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private void initializeTweetsListWithCachedData() {
        twitterListAdapter = new TwitterListAdapter(tweets, getActivity(), this);
        linearLayoutManager = new LinearLayoutManager(getActivity());
        rvTweetsList.setLayoutManager(linearLayoutManager);
        rvTweetsList.setAdapter(twitterListAdapter);
    }

    private void bindEndlessScrollViewListener() {
        endlessRecyclerViewScrollListener = new EndlessRecyclerViewScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                tweetsListFragmentsListener.fetchNewTweetsWithLastTweetID(twitterListAdapter.getLastTweetID());
            }
        };
        rvTweetsList.addOnScrollListener(endlessRecyclerViewScrollListener);
    }

    @Override
    public void onTweetClicked(Tweet tweet) {
        Intent intent = new Intent(getActivity(), TweetDetailActivity.class);
        intent.putExtra("selectedTweet", Parcels.wrap(tweet));
        startActivity(intent);
    }

    // Adapter Interface
    public void appendTweets(List<Tweet> newTweets) {
        twitterListAdapter.appendTweets(newTweets);
    }

    public void resetRecyclerViewWithTweets(List<Tweet> newTweets) {
        twitterListAdapter.resetTweets(newTweets);
        endlessRecyclerViewScrollListener.resetState();
    }

    public void insertTweetAtPosition(Tweet newTweet, int position) {
        twitterListAdapter.appendTweetAtPosition(newTweet, position);
        rvTweetsList.smoothScrollToPosition(position);
    }
}
