package com.codepath.apps.simpletweetsadv.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.codepath.apps.simpletweetsadv.activities.ComposeTweetActivity;
import com.codepath.apps.simpletweetsadv.activities.ProfileActivity;
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
public class HomeTimeLineFragment extends TweetListFragment {


    public TwitterClient client;

    public static HomeTimeLineFragment newInstance() {
        HomeTimeLineFragment homeTimeLineFragment = new HomeTimeLineFragment();
        return homeTimeLineFragment;
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
        //this listener comes from adapter
        aTweets.setTweetOnItemClickListner(new TweetsArrayAdapter.TweetOnItemClickListner() {
            @Override
            public void onItemClick(View itemView, int position) {
                String name = tweets.get(position).getUser().getScreenName().toString();
                Toast.makeText(getContext(), name, Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(getActivity(), ProfileActivity.class);
                intent.putExtra(ProfileActivity.SCREEN_NAME, name);
                intent.putExtra(ProfileActivity.USER, tweets.get(position).getUser());
                startActivityForResult(intent, ProfileActivity.REQUEST_CODE);

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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Tweet tweet = (Tweet) data.getParcelableExtra(ComposeTweetActivity.TWEET);
        aTweets.insertAt(0, tweet);
    }
}
