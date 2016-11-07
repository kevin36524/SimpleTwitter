package com.codepath.apps.restclienttemplate.fragments;


import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.codepath.apps.restclienttemplate.R;
import com.codepath.apps.restclienttemplate.models.TweetUser;

import org.parceler.Parcels;

/**
 * A simple {@link Fragment} subclass.
 */
public class UserHeaderFragment extends Fragment {

    TweetUser displayUser;

    ImageView ivPosterImage;
    ImageView ivProfileImage;
    TextView tvUserName;
    TextView tvScreenName;
    TextView tvDescription;
    TextView tvFollowingCount;
    TextView tvFollowersCount;

    public UserHeaderFragment() {
        // Required empty public constructor
    }

    public static UserHeaderFragment newInstance(Parcelable user) {

        Bundle args = new Bundle();

        UserHeaderFragment fragment = new UserHeaderFragment();
        args.putParcelable("user", user);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        displayUser = Parcels.unwrap(getArguments().getParcelable("user"));
        View v = inflater.inflate(R.layout.fragment_user_header, container, false);
        bindViews(v);
        return v;
    }

    public void bindViews(View v) {
        ivPosterImage = (ImageView) v.findViewById(R.id.ivPosterImage);
        ivProfileImage = (ImageView) v.findViewById(R.id.ivProfileImage);
        tvUserName = (TextView) v.findViewById(R.id.tvUserName);
        tvScreenName = (TextView) v.findViewById(R.id.tvScreenName);
        tvDescription = (TextView) v.findViewById(R.id.tvDescription);
        tvFollowingCount = (TextView) v.findViewById(R.id.tvFollowingCount);
        tvFollowersCount = (TextView) v.findViewById(R.id.tvFollowersCount);


        Glide.with(getActivity()).load(displayUser.getProfile_banner_url()).into(ivPosterImage);
        Glide.with(getActivity()).load(displayUser.getProfile_image_url()).into(ivProfileImage);
        tvUserName.setText(displayUser.getName());
        tvScreenName.setText("@" + displayUser.getScreen_name());
        tvDescription.setText(displayUser.getDescription());
        tvFollowersCount.setText("" + displayUser.getFollowers_count());
        tvFollowingCount.setText("" + displayUser.getFriends_count());
    }

}
