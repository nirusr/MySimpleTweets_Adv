package com.codepath.apps.simpletweetsadv.activities;

import android.media.Image;
import android.support.v4.app.FragmentTransaction;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.apps.simpletweetsadv.R;
import com.codepath.apps.simpletweetsadv.application.TwitterApplication;
import com.codepath.apps.simpletweetsadv.client.TwitterClient;
import com.codepath.apps.simpletweetsadv.fragments.UserTimeLineFragment;
import com.codepath.apps.simpletweetsadv.models.User;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.squareup.picasso.Picasso;

import org.apache.http.Header;
import org.json.JSONObject;
import org.w3c.dom.Text;

public class ProfileActivity extends AppCompatActivity {
    public static final int REQUEST_CODE = 200;
    public static final String SCREEN_NAME = "screen_name";
    public static final String USER = "USER";
    public String screenName;
    public UserTimeLineFragment fragmentUserTime;
    public User user;
    TwitterClient client;
    ImageView ivProfileImage;
    TextView tvTagLine;
    TextView tvUsername;
    TextView tvFollowersCount;
    TextView tvFriendsCount;
    User clickedUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        ivProfileImage = (ImageView) findViewById(R.id.ivProfileImage);
        tvUsername = (TextView) findViewById(R.id.tvUsername);
        tvFollowersCount = (TextView) findViewById(R.id.tvFollowers);
        tvFriendsCount = (TextView) findViewById(R.id.tvFollowing);
        tvTagLine = (TextView) findViewById(R.id.tvTagline);

        client = TwitterApplication.getRestClient();
        screenName = getIntent().getStringExtra("screen_name");
        Log.d("Screen Name(Intent)=", screenName);


        client.getVerifyCredentials(new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                //super.onSuccess(statusCode, headers, response);
                user = User.fromJson(response);
                getSupportActionBar().setTitle(user.getScreenName());
                populateUserInfo(user);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                //super.onFailure(statusCode, headers, throwable, errorResponse);
            }
        });

        //instantiate the UsersTimeline Fragment
        if (savedInstanceState == null) {

            fragmentUserTime = new UserTimeLineFragment().newInstance(screenName);
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.flContainer, fragmentUserTime);
            ft.commit();
        }


    }

    public void populateUserInfo(User user) {
        Log.d("TEST", "TEST");
        clickedUser = new User();
        clickedUser = getIntent().getParcelableExtra(ProfileActivity.USER);
        if (clickedUser != null) {
            Picasso.with(this).load(clickedUser.getProfileImageUrl()).fit().centerCrop().into(ivProfileImage);
            tvUsername.setText(clickedUser.getName());
            tvTagLine.setText(clickedUser.getTagLine());
            tvFollowersCount.setText(clickedUser.getFollowersCount());
            tvFriendsCount.setText(clickedUser.getFriendsCount());
        } else {
            //Log.d("User.Tag:", user.getTagLine());
            Picasso.with(this).load(user.getProfileImageUrl()).fit().centerCrop().into(ivProfileImage);
            tvUsername.setText(user.getName());
            tvTagLine.setText(user.getTagLine());
            tvFollowersCount.setText(user.getFollowersCount());
            tvFriendsCount.setText(user.getFriendsCount());

        }

    }

}
