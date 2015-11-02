package com.codepath.apps.simpletweetsadv.fragments;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.codepath.apps.simpletweetsadv.adapters.TweetsArrayAdapter;
import com.codepath.apps.simpletweetsadv.application.TwitterApplication;
import com.codepath.apps.simpletweetsadv.client.TwitterClient;
import com.codepath.apps.simpletweetsadv.listeners.EndlessScrollListener;
import com.codepath.apps.simpletweetsadv.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by sgovind on 10/30/15.
 */
public class UserTimeLineFragment extends TweetListFragment {



    public TwitterClient client;


    public static UserTimeLineFragment newInstance(String screenName) {
        UserTimeLineFragment userTimeLineFragment = new UserTimeLineFragment();
        Bundle args = new Bundle();
        args.putString("screen_name", screenName);
        userTimeLineFragment.setArguments(args);
        return userTimeLineFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        client = TwitterApplication.getRestClient();//Create singleton client
        tweets = new ArrayList<Tweet>();
        aTweets = new TweetsArrayAdapter(tweets);
        populateTimeline();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        lvTweets.addOnScrollListener(new EndlessScrollListener() {
            @Override
            public boolean onLoadMore(int page, int totalItemsCount) {
                customLoadMoreDataFromClient(page);
                return true;
            }
        });


        //swipecontainer

        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //populateTimeline();
                populateTimeline();
                swipeContainer.setRefreshing(false);

            }
        });
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);



    }

    private void populateTimeline() {
        String screenName = getArguments().getString("screen_name");

        if ( isNetworkAvailable()) {
            client.getUsersTimeline(screenName, 0, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                    aTweets.clear();
                    aTweets.addAll(Tweet.fromJsonArray(response));
                    Log.d("Response=", response.toString());
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
        String screenName = getArguments().getString("screen_name");

        long maxId = Tweet.maxId - 1;

        if ( isNetworkAvailable()) {
            client.getUsersTimeline(screenName, maxId, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                    aTweets.addAll(Tweet.fromJsonArray(response));
                    Log.d("Response=", response.toString());

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
