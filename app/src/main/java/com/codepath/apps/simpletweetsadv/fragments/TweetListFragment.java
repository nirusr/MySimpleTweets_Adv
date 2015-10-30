package com.codepath.apps.simpletweetsadv.fragments;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.activeandroid.query.Delete;
import com.codepath.apps.simpletweetsadv.Helper.DBSaveAsyncTask;
import com.codepath.apps.simpletweetsadv.R;
import com.codepath.apps.simpletweetsadv.adapters.TweetsArrayAdapter;
import com.codepath.apps.simpletweetsadv.application.TwitterApplication;
import com.codepath.apps.simpletweetsadv.client.TwitterClient;
import com.codepath.apps.simpletweetsadv.listeners.EndlessScrollListener;
import com.codepath.apps.simpletweetsadv.models.Tweet;
import com.codepath.apps.simpletweetsadv.models.TweetModel;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sgovind on 10/30/15.
 */
public class TweetListFragment extends Fragment {

    public TweetsArrayAdapter aTweets;
    public RecyclerView lvTweets;
    public ArrayList<Tweet> tweets;
    public SwipeRefreshLayout swipeContainer;
    public TwitterClient client;



    //inflation logic
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //return super.onCreateView(inflater, container, savedInstanceState);
        View v = inflater.inflate(R.layout.fragments_tweet_list, container, false);
        lvTweets = (RecyclerView) v.findViewById(R.id.lvTweets);
        lvTweets.setLayoutManager(new LinearLayoutManager(getContext()));
        return v;
    }

    //lifecycle
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        client = TwitterApplication.getRestClient();//Create singleton client
        tweets = new ArrayList<Tweet>();
        aTweets = new TweetsArrayAdapter(tweets);
        populateTimeline();


    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        lvTweets.setAdapter(aTweets);
        lvTweets.addOnScrollListener(new EndlessScrollListener() {
            @Override
            public boolean onLoadMore(int page, int totalItemsCount) {
                customLoadMoreDataFromClient(page);
                return true;
            }
        });

    }


    private void populateTimeline() {

        if ( isNetworkAvailable()) {
            client.getHomeTimeline(0, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                    aTweets.clear();
                    aTweets.addAll(Tweet.fromJsonArray(response));
                    Log.d("Initial Fetch=", Long.toString(aTweets.getItemCount()));

                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {

                    if (!isNetworkAvailable()) {
                        Toast.makeText(getContext(), "Network Not available - Getting Data from Saved DB", Toast.LENGTH_SHORT).show();
                    } else {
                        Log.v("DEBUG Fail:", errorResponse.toString());
                    }

                }
            });
        } else {
            //TODO get data from DB
            Toast.makeText(getContext(), "Network Not available - Getting Data from Saved DB", Toast.LENGTH_SHORT).show();

        }
    }

    public void customLoadMoreDataFromClient(int page) {

        long maxId = Tweet.maxId - 1;

        if ( isNetworkAvailable()) {
            client.getHomeTimeline(maxId, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                    aTweets.addAll(Tweet.fromJsonArray(response));

                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                    if (! isNetworkAvailable()) {
                        Toast.makeText(getContext(), "Network Not available - Getting Data from Saved DB", Toast.LENGTH_SHORT).show();
                    } else {
                        Log.v("DEBUG Fail:", errorResponse.toString());
                    }

                }
            });
        } else {
            Toast.makeText(getContext(), "Network Not available - Getting Data from Saved DB", Toast.LENGTH_SHORT).show();

        }
    }

    //Network check
    public Boolean isNetworkAvailable() {

        ConnectivityManager connectivityManager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return ( (activeNetworkInfo != null) && activeNetworkInfo.isConnectedOrConnecting());



    }


}
