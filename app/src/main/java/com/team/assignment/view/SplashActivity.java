package com.team.assignment.view;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.team.assignment.R;
import com.team.assignment.databinding.ActivitySplashBinding;
import com.team.assignment.utils.SessionManager;


public class SplashActivity extends AppCompatActivity {
    private ActivitySplashBinding binding;
    private  static final int SPLASH_SCREEN =500;
    private SessionManager sessionManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_splash);
        sessionManager = new SessionManager(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        new Handler().postDelayed(new Runnable()
        {
            @Override
            public void run() {
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
        },SPLASH_SCREEN);
    }
}