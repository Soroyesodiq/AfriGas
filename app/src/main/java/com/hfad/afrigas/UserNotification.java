package com.hfad.afrigas;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class UserNotification extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_notification);
        getSupportActionBar().setHomeButtonEnabled(true);
        setTitle("Notifications");
    }
}
