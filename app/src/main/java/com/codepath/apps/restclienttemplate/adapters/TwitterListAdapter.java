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

    public TwitterListAdapter(List<Tweet> tweets, Context context) {
        this.tweets = new ArrayList<Tweet>();
        this.tweets.addAll(tweets);
        this.mContext = context;
    }

    @Override
    public TweetViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View tweetCell = inflater.inflate(R.layout.cell_tweet, parent, false);

        TweetViewHolder tweetViewHolder = new TweetViewHolder(tweetCell);

        return tweetViewHolder;
    }

    public void resetTweets(List<Tweet> tweets) {
        this.tweets = new ArrayList<Tweet>();
        this.tweets.addAll(tweets);
        this.notifyDataSetChanged();
    }

    public void appendTweets(List<Tweet> addedTweets) {
        this.tweets.addAll(addedTweets);
        this.notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(TweetViewHolder holder, int position) {
        Tweet tweet = tweets.get(position);
        holder.tvTilte.setText(tweet.getText());
        holder.tvUserName.setText(tweet.getUser().getName());
        holder.tvUserHandle.setText("@" + tweet.getUser().getScreen_name());
        holder.tvRelativeTime.setText(tweet.relativeTime());
        Picasso.with(mContext).load(tweet.getUser().getProfile_image_url_https()).into(holder.ivProfile);
    }

    @Override
    public int getItemCount() {
        if (tweets == null) {
            return 0;
        }
        return tweets.size();
    }

    public class TweetViewHolder extends RecyclerView.ViewHolder {

        TextView tvTilte;
        ImageView ivProfile;
        TextView tvUserName;
        TextView tvUserHandle;
        TextView tvRelativeTime;

        public TweetViewHolder(View itemView) {
            super(itemView);
            tvTilte = (TextView) itemView.findViewById(R.id.tvTitle);
            ivProfile = (ImageView) itemView.findViewById(R.id.ivProfile);
            tvUserName = (TextView) itemView.findViewById(R.id.tvUserName);
            tvUserHandle = (TextView) itemView.findViewById(R.id.tvUserHandle);
            tvRelativeTime = (TextView) itemView.findViewById(R.id.tvRelativeTime);
        }
    }

    public long getLastTweetID() {
        return tweets.get(tweets.size() - 1).getId();
    }
}
