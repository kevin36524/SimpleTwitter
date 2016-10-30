package com.codepath.apps.restclienttemplate.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.codepath.apps.restclienttemplate.R;
import com.codepath.apps.restclienttemplate.SimpleTwitterApplication;
import com.codepath.apps.restclienttemplate.TwitterClient;
import com.codepath.apps.restclienttemplate.models.Tweet;
import com.codepath.apps.restclienttemplate.models.TweetUser;
import com.squareup.picasso.Picasso;

import org.parceler.Parcels;

import java.util.List;

public class TweetComposeActivity extends AppCompatActivity {

    ImageView ivProfile;
    TextView tvScreenName;
    TextView tvHandle;
    ImageView ivBack;
    EditText etTweetContent;
    TextView tvTweetLength;
    Button tweetButton;
    Context mContext;
    TweetUser currentUser;

    public static final int maxTweetLength = 180;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tweet_compose);
        mContext = this;
        currentUser = Parcels.unwrap(getIntent().getParcelableExtra("currentUser"));
        bindViewReferences();
    }

    private void bindViewReferences() {
        ivProfile = (ImageView) findViewById(R.id.ivProfile);
        tvScreenName = (TextView) findViewById(R.id.tvScreenName);
        tvHandle = (TextView) findViewById(R.id.tvHandle);
        ivBack = (ImageView) findViewById(R.id.ivBack);
        etTweetContent = (EditText) findViewById(R.id.etTweetContent);
        tvTweetLength = (TextView) findViewById(R.id.tvTweetLength);
        tweetButton = (Button) findViewById(R.id.tweetButton);

        tvScreenName.setText(currentUser.getName());
        tvHandle.setText("@"+currentUser.getScreen_name());
        Picasso.with(this).load(currentUser.getProfile_image_url_https()).into(ivProfile);

        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(204); finish();
            }
        });
        etTweetContent.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                int pendingCharCount = (maxTweetLength - s.length());
                tvTweetLength.setText("" + pendingCharCount);
                tvTweetLength.setTextColor(pendingCharCount > 0 ? Color.GREEN : Color.RED);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        tweetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String tweetContent = etTweetContent.getText().toString();
                if (tweetContent.length() > maxTweetLength) {
                    Toast.makeText(mContext, "tweet is too long", Toast.LENGTH_SHORT).show();
                } else {
                    SimpleTwitterApplication.getTwitterClient().postTweet(tweetContent, new TwitterClient.TweetsResponseInterface() {
                        @Override
                        public void fetchedTweets(List<Tweet> tweets) {
                            Intent rtIntent = new Intent();
                            rtIntent.putExtra("code", 200);
                            rtIntent.putExtra("tweet", Parcels.wrap(tweets.get(0)));
                            setResult(200, rtIntent);
                            finish();
                        }
                    });
                }
            }
        });

    }

}
