package com.codepath.apps.simpletweetsadv.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.codepath.apps.simpletweetsadv.R;

public class ProfileActivity extends AppCompatActivity {
    public static final int REQUEST_CODE = 200;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
    }
}
