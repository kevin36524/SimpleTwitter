package com.codepath.apps.restclienttemplate.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.apps.restclienttemplate.R;
import com.codepath.apps.restclienttemplate.models.Tweet;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by patelkev on 10/27/16.
 */

public class TwitterListAdapter extends RecyclerView.Adapter<TwitterListAdapter.TweetViewHolder> {

    public ArrayList<Tweet> tweets;
    public Context mContext;
    public ClickDelegate clickDelegate;

    public interface ClickDelegate {
        public void onTweetClicked(Tweet tweet, TweetsAction tweetsActions);
    }

    public enum TweetsAction {
        TWEETS_ACTION_DETAIL, TWEETS_ACTION_PROFILE
    }

    public TwitterListAdapter(List<Tweet> tweets, Context context, ClickDelegate clickDelegate) {
        this.tweets = new ArrayList<Tweet>();
        this.tweets.addAll(tweets);
        this.mContext = context;
        this.clickDelegate = clickDelegate;
    }

    @Override
    public TweetViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View tweetCell = inflater.inflate(R.layout.cell_tweet, parent, false);

        TweetViewHolder tweetViewHolder = new TweetViewHolder(tweetCell, clickDelegate);

        return tweetViewHolder;
    }

    public void resetTweets(List<Tweet> tweets) {
        this.tweets = new ArrayList<Tweet>();
        this.tweets.addAll(tweets);
        this.notifyDataSetChanged();
    }

    public void appendTweets(List<Tweet> addedTweets) {
        int currentSize = tweets.size();
        this.tweets.addAll(tweets.size(),addedTweets);
        int newSize = tweets.size();
        this.notifyItemRangeInserted(currentSize, newSize);
    }

    public void appendTweetAtPosition(Tweet tweet, int position) {
        tweets.add(position, tweet);
        this.notifyItemInserted(position);
    }

    @Override
    public void onBindViewHolder(TweetViewHolder holder, int position) {
        Tweet tweet = tweets.get(position);
        holder.tvTilte.setText(tweet.getText());
        holder.tvUserName.setText(tweet.getUser().getName());
        holder.tvUserHandle.setText("@" + tweet.getUser().getScreen_name());
        holder.tvRelativeTime.setText(tweet.relativeTime());
        holder.ivProfile.setImageResource(0);
        holder.ivMediaImage.setImageResource(0);
        Picasso.with(mContext).load(tweet.getUser().getProfile_image_url_https()).into(holder.ivProfile);
        if (tweet.getMediaUrl() != null) {
            Picasso.with(mContext).load(tweet.getMediaUrl()).into(holder.ivMediaImage);
        }

    }

    @Override
    public int getItemCount() {
        if (tweets == null) {
            return 0;
        }
        return tweets.size();
    }

    public class TweetViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView tvTilte;
        ImageView ivProfile;
        TextView tvUserName;
        TextView tvUserHandle;
        TextView tvRelativeTime;
        ClickDelegate clickDelegate;
        ImageView ivMediaImage;

        public TweetViewHolder(View itemView, ClickDelegate clickDelegate) {
            super(itemView);
            tvTilte = (TextView) itemView.findViewById(R.id.tvTitle);
            ivProfile = (ImageView) itemView.findViewById(R.id.ivProfile);
            tvUserName = (TextView) itemView.findViewById(R.id.tvUserName);
            tvUserHandle = (TextView) itemView.findViewById(R.id.tvUserHandle);
            tvRelativeTime = (TextView) itemView.findViewById(R.id.tvRelativeTime);
            ivMediaImage = (ImageView) itemView.findViewById(R.id.ivMediaImage);
            this.clickDelegate = clickDelegate;
            tvTilte.setOnClickListener(this);
            ivProfile.setOnClickListener(this);
            tvUserName.setOnClickListener(this);
            tvUserHandle.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            Tweet activeTweet = tweets.get(position);
            TweetsAction action = TweetsAction.TWEETS_ACTION_DETAIL;
            switch (v.getId()) {
                case R.id.tvUserHandle:
                case R.id.tvUserName:
                case R.id.ivProfile:
                    action = TweetsAction.TWEETS_ACTION_PROFILE;
                    break;
            }
            clickDelegate.onTweetClicked(activeTweet,action);
        }
    }

    public Long getLastTweetID() {
        if (tweets.size() == 0) {
            return null;
        }
        return tweets.get(tweets.size() - 1).getId();
    }
}
