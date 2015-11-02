package com.codepath.apps.simpletweetsadv.activities;

import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.astuetz.PagerSlidingTabStrip;
import com.codepath.apps.simpletweetsadv.R;
import com.codepath.apps.simpletweetsadv.adapters.TweetPagerAdapter;
import com.codepath.apps.simpletweetsadv.fragments.HomeTimeLineFragment;
import com.codepath.apps.simpletweetsadv.fragments.TweetListFragment;

//branch
public class TimelineActivity extends AppCompatActivity {

    ViewPager vpPager;
    PagerSlidingTabStrip tabStrip;
    TweetPagerAdapter tweetPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);

        vpPager = (ViewPager) findViewById(R.id.viewpager);
        tweetPagerAdapter = new TweetPagerAdapter(getSupportFragmentManager());
       // vpPager.setAdapter(new TweetPagerAdapter(getSupportFragmentManager()));
        vpPager.setAdapter(tweetPagerAdapter);
        tabStrip = (PagerSlidingTabStrip) findViewById(R.id.tabs);
        tabStrip.setViewPager(vpPager);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.timeline, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.compose: {
                //Toast.makeText(this, "Post clicked", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(this, ComposeTweetActivity.class);
                startActivityForResult(intent, ComposeTweetActivity.REQUEST_CODE);
                return true;

            }
            case R.id.miProfile: {
                //TODO add intent
                Intent intent = new Intent(this, ProfileActivity.class);
                intent.putExtra(ProfileActivity.SCREEN_NAME, ""); //TODO how to get screen name from user
                startActivityForResult(intent, ProfileActivity.REQUEST_CODE);
                return true;

            }
            default:
                return super.onOptionsItemSelected(item);


        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ComposeTweetActivity.REQUEST_CODE && resultCode == RESULT_OK) {
            vpPager.getCurrentItem();
            Log.d("Current Page=", Integer.toString(vpPager.getCurrentItem()));

        }

    }

    public Boolean isNetworkAvailable() {

        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return ((activeNetworkInfo != null) && activeNetworkInfo.isConnectedOrConnecting());


    }

}
