package com.codepath.apps.restclienttemplate.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.codepath.apps.restclienttemplate.R;
import com.codepath.apps.restclienttemplate.models.Tweet;

import org.parceler.Parcels;

public class TweetDetailActivity extends AppCompatActivity {

    TextView tvTweetContent;

    Tweet selectedTweet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tweet_detail);
        selectedTweet = Parcels.unwrap(getIntent().getParcelableExtra("selectedTweet"));
        bindViews();

    }

    private void bindViews() {
        tvTweetContent = (TextView) findViewById(R.id.tvTweetContent);
        tvTweetContent.setText(selectedTweet.getText());
    }


}
