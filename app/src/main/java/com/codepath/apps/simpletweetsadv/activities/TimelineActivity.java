package com.codepath.apps.simpletweetsadv.activities;

import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.codepath.apps.simpletweetsadv.R;
import com.codepath.apps.simpletweetsadv.fragments.TweetListFragment;

//branch
public class TimelineActivity extends AppCompatActivity {

    TweetListFragment tweetListFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.timeline, menu);
        MenuItem addTweetItem = menu.findItem(R.id.action_compose);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_compose: {
                //Toast.makeText(this, "Post clicked", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(this, ComposeTweetActivity.class);
                startActivityForResult(intent, ComposeTweetActivity.REQUEST_CODE);

            }
            default:
                return super.onOptionsItemSelected(item);


        }
    }

   /* @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ComposeTweetActivity.REQUEST_CODE && resultCode == RESULT_OK) {
            Tweet postedTweet = (Tweet) data.getParcelableExtra(ComposeTweetActivity.TWEET);
            aTweets.insertAt(0, postedTweet);
            // populateTimeline();
        }

    }*/

    public Boolean isNetworkAvailable() {

        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return ((activeNetworkInfo != null) && activeNetworkInfo.isConnectedOrConnecting());


    }

}
