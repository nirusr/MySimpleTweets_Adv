package com.codepath.apps.simpletweetsadv.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.codepath.apps.simpletweetsadv.fragments.HomeTimeLineFragment;
import com.codepath.apps.simpletweetsadv.fragments.MentionsTimeLineFragment;

/**
 * Created by sgovind on 10/30/15.
 */
public class TweetPagerAdapter extends FragmentPagerAdapter {

    public String tabTitles[] = {"Home Timeline", "Mentions Timeline"};

    public TweetPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {

        if ( position == 0) {
            return HomeTimeLineFragment.newInstance();

        } else if ( position == 1) {
            return MentionsTimeLineFragment.newInstance();
        } else {
            return null;
        }


    }

    @Override
    public int getCount() {
        return tabTitles.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tabTitles[position];
    }
}
