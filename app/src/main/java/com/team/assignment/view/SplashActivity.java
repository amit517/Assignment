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
        //binding = DataBindingUtil.setContentView(this,R.layout.activity_splash);
        sessionManager = new SessionManager(this);
        Bundle anim = ActivityOptions.makeCustomAnimation(SplashActivity.this,
                R.anim.fade_in, R.anim.nothing).toBundle();
        if (sessionManager.getIsLogedIn()) {

            startActivity(new Intent(SplashActivity.this, MainActivity.class)
                    .setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY)
                    .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP),anim);
        }
        else {
            startActivity(new Intent(SplashActivity.this,LoginActivity.class)
                    .setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY)
                    .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP),anim);
        }
        finish();
    }
}