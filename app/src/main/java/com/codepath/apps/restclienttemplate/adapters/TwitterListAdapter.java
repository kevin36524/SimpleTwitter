package com.codepath.apps.restclienttemplate.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.codepath.apps.restclienttemplate.R;
import com.codepath.apps.restclienttemplate.models.Tweet;

import java.util.List;

/**
 * Created by patelkev on 10/27/16.
 */

public class TwitterListAdapter extends RecyclerView.Adapter<TwitterListAdapter.TweetViewHolder> {

    public List<Tweet> tweets;

    public TwitterListAdapter(List<Tweet> tweets) {
        this.tweets = tweets;
    }

    @Override
    public TweetViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View tweetCell = inflater.inflate(R.layout.cell_tweet, parent, false);

        TweetViewHolder tweetViewHolder = new TweetViewHolder(tweetCell);

        return tweetViewHolder;
    }

    @Override
    public void onBindViewHolder(TweetViewHolder holder, int position) {
        Tweet tweet = tweets.get(position);
        holder.tvTilte.setText(tweet.getText());
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

        public TweetViewHolder(View itemView) {
            super(itemView);
            tvTilte = (TextView) itemView.findViewById(R.id.tvTitle);
        }
    }
}
