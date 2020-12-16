package com.team.assignment.view;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.team.assignment.R;
import com.team.assignment.utils.SessionManager;


public class SplashActivity extends AppCompatActivity {
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sessionManager = new SessionManager(this);
        /**
         * Check if the user is logged in or not
         */
        if (sessionManager.getIsLogedIn()) {
            startActivity(new Intent(SplashActivity.this, MainActivity.class)
                    .setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY)
                    .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
        } else {
            startActivity(new Intent(SplashActivity.this, LoginActivity.class)
                    .setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY)
                    .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
        }
        finish();
    }
}