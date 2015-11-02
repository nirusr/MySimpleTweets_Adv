package com.codepath.apps.simpletweetsadv.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.codepath.apps.simpletweetsadv.R;
import com.codepath.apps.simpletweetsadv.models.Tweet;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sgovind on 10/23/15.
 */
public class TweetsArrayAdapter extends RecyclerView.Adapter<TweetsArrayAdapter.ViewHolder> {

    public static interface TweetOnItemClickListner {
        void onItemClick(View itemView, int position);
    }

    private static TweetOnItemClickListner tweetOnItemClickListner;

    public void setTweetOnItemClickListner(TweetOnItemClickListner listner) {
        this.tweetOnItemClickListner = listner;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        //Define screen fields
        TextView tvUsername ;
        TextView tvBody;
        ImageView ivProfileImage;
        TextView tvTimestamp;


        public ViewHolder(final View itemView) {
            super(itemView);
            //set the fields in the view holder
            tvUsername = (TextView) itemView.findViewById(R.id.tvUsername);
            tvBody = (TextView) itemView.findViewById(R.id.tvBody);
            ivProfileImage = (ImageView) itemView.findViewById(R.id.ivProfileImage);
            tvTimestamp = (TextView) itemView.findViewById(R.id.tvTimestamp);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (tweetOnItemClickListner != null) {
                        tweetOnItemClickListner.onItemClick(itemView, getLayoutPosition() );
                    }
                }
            });

        }


    }

    private ArrayList<Tweet> mTweets;

    public TweetsArrayAdapter(ArrayList<Tweet> tweets) {
        this.mTweets = tweets;

        //Log.d("#of tweets:", Integer.toString(getItemCount()));
    }


    @Override
    public TweetsArrayAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View itemTweetView  = inflater.inflate(R.layout.item_tweet, parent, false);
        ViewHolder viewHolder = new ViewHolder(itemTweetView);
        return viewHolder;

    }

    @Override
    public void onBindViewHolder(TweetsArrayAdapter.ViewHolder holder, int position) {
        Tweet tweet = mTweets.get(position);
        holder.tvUsername.setText(tweet.getUser().getName());
        holder.tvBody.setText((tweet.getBody()));
        holder.tvTimestamp.setText(tweet.getCreatedAt());
        Picasso.with(holder.ivProfileImage.getContext()).
                load(tweet.getUser().getProfileImageUrl()).fit().centerCrop().into(holder.ivProfileImage);

        //Set the tag with screen name to retrieve later using onClick
        holder.ivProfileImage.setTag(tweet.getUser().getScreenName());


    }

    @Override
    public int getItemCount() {
        return mTweets.size();
    }

    public void clear() {
        mTweets.clear();
        notifyDataSetChanged();
    }

    public void addAll(List<Tweet> list) {
        mTweets.addAll(list);
        notifyDataSetChanged();

    }

    public void insertAt(int position, Tweet tweet) {
        mTweets.add(position, tweet);
        notifyDataSetChanged();
    }



}
