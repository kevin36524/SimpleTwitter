package com.codepath.apps.restclienttemplate.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.codepath.apps.restclienttemplate.R;
import com.codepath.apps.restclienttemplate.SimpleTwitterApplication;
import com.codepath.apps.restclienttemplate.TwitterClient;
import com.codepath.apps.restclienttemplate.models.Tweet;
import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.AnimatorInflater;
import com.squareup.picasso.Picasso;

import org.parceler.Parcels;

import java.util.List;

public class TweetDetailActivity extends AppCompatActivity {

    ImageView ivProfileImage;
    TextView tvName;
    TextView tvHandle;
    TextView tvContent;
    ImageView ivMediaImage;
    TextView tvRetweetCount;
    TextView tvRetweetLabel;
    TextView tvFavoriteCount;
    TextView tvFavoriteLabel;
    LinearLayout actionContainer;
    ImageView ivReply;
    ImageView ivRetweet;
    ImageView ivFavorite;
    ImageView ivShare;

    Tweet selectedTweet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tweet_detail);
        selectedTweet = Parcels.unwrap(getIntent().getParcelableExtra("selectedTweet"));
        bindViews();
        addListeners();
    }

    private void bindViews() {
        ivProfileImage = (ImageView) findViewById(R.id.ivProfileImage);
        tvName = (TextView) findViewById(R.id.tvName);
        tvHandle = (TextView) findViewById(R.id.tvHandle);
        tvContent = (TextView) findViewById(R.id.tvContent);
        ivMediaImage = (ImageView) findViewById(R.id.ivMediaImage);
        tvRetweetCount = (TextView) findViewById(R.id.tvRetweetCount);
        tvRetweetLabel = (TextView) findViewById(R.id.tvRetweetLabel);
        tvFavoriteCount = (TextView) findViewById(R.id.tvFavoriteCount);
        tvFavoriteLabel = (TextView) findViewById(R.id.tvFavoriteLabel);
        actionContainer = (LinearLayout) findViewById(R.id.actionContainer);
        ivReply = (ImageView) findViewById(R.id.ivReply);
        ivRetweet = (ImageView) findViewById(R.id.ivRetweet);
        ivFavorite = (ImageView) findViewById(R.id.ivFavorite);
        ivShare = (ImageView) findViewById(R.id.ivShare);

        tvContent.setText(selectedTweet.getText());
        Picasso.with(this).load(selectedTweet.getUser().getProfile_image_url_https()).into(ivProfileImage);
        tvName.setText(selectedTweet.getUser().getName());
        tvHandle.setText("@" + selectedTweet.getUser().getScreen_name());
        tvRetweetCount.setText("" + selectedTweet.getRetweet_count());
        tvFavoriteCount.setText("" + selectedTweet.getFavorite_count());
        ivMediaImage.setImageResource(0);
        if (selectedTweet.getMediaUrl() != null) {
            Picasso.with(this).load(selectedTweet.getMediaUrl()).into(ivMediaImage);
        }
    }

    private void addListeners() {

        final Animator anim = AnimatorInflater.loadAnimator(TweetDetailActivity.this, R.animator.rotation_animator);
        anim.setTarget(ivRetweet);
        anim.setDuration(300);

        ivRetweet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("TweetDetailActivity", "Test for click");
                SimpleTwitterApplication.getTwitterClient().postRetweet("" + selectedTweet.getId(), new TwitterClient.TweetsResponseInterface() {
                    @Override
                    public void fetchedTweets(List<Tweet> tweets) {
                        Tweet newTweet = tweets.get(0);
                        selectedTweet.setRetweet_count(newTweet.getRetweet_count());
                        tvRetweetCount.setText("" + newTweet.getRetweet_count());
                    }
                });
                anim.start();
            }
        });
    }


}
