package com.codepath.apps.simpletweetsadv.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.codepath.apps.simpletweetsadv.R;
import com.codepath.apps.simpletweetsadv.adapters.TweetsArrayAdapter;
import com.codepath.apps.simpletweetsadv.models.Tweet;

import java.util.ArrayList;

/**
 * Created by sgovind on 10/30/15.
 */
public class TweetListFragment extends Fragment {

    public TweetsArrayAdapter aTweets;
    public RecyclerView lvTweets;
    public ArrayList<Tweet> tweets;
    public SwipeRefreshLayout swipeContainer;


   //inflation logic
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //return super.onCreateView(inflater, container, savedInstanceState);
        View v = inflater.inflate(R.layout.fragment_tweet_list, container, false);
        lvTweets = (RecyclerView) v.findViewById(R.id.lvTweets);
        lvTweets.setLayoutManager(new LinearLayoutManager(getContext()));
        swipeContainer = (SwipeRefreshLayout) v.findViewById(R.id.swipeContainer);
        return v;
    }

    //lifecycle
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tweets = new ArrayList<Tweet>();
        aTweets = new TweetsArrayAdapter(tweets);

    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        lvTweets.setAdapter(aTweets);


    }

    //Make sure activity has implemented the interface
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        Activity activity = context instanceof  Activity ? (Activity) context : null;


    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

    }
}
