package com.codepath.apps.restclienttemplate.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by patelkev on 11/5/16.
 */

public class TweetsListFragmentsPagerAdapter extends FragmentPagerAdapter {

    public interface TweetsListFragmentsPagerDataSource {
        public Fragment getItem(int position);
        public CharSequence getPageTitle(int position);
        public int getCount();
    }

    TweetsListFragmentsPagerDataSource dataSource;

    public TweetsListFragmentsPagerAdapter(FragmentManager fm, TweetsListFragmentsPagerDataSource dataSource) {
        super(fm);
        this.dataSource = dataSource;
    }

    @Override
    public Fragment getItem(int position) {
        return dataSource.getItem(position);
    }

    @Override
    public int getCount() {
        return dataSource.getCount();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return dataSource.getPageTitle(position);
    }
}
