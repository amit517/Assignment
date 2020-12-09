package com.team.assignment.view;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.snackbar.Snackbar;
import com.team.assignment.R;
import com.team.assignment.databinding.ActivityLoginBinding;
import com.team.assignment.model.LoginResponse;
import com.team.assignment.utils.MyApplication;
import com.team.assignment.utils.SessionManager;
import com.team.assignment.viewmodel.LoginActivityViewModel;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = LoginActivity.class.getSimpleName();
    private ActivityLoginBinding binding;
    private ProgressDialog progressDialog;
    private SessionManager sessionManager;
    private LoginActivityViewModel loginActivityViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login);
        init();
        if (sessionManager.getIsLogedIn()){
            gotoMainActivity();
        }
        binding.btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(view.getApplicationWindowToken(), 0);
                if (MyApplication.hasNetwork()) {
                    String email = binding.etUserId.getText().toString();
                    String password = binding.etPassword.getText().toString();
                    if (checkField(email, password)) {
                        signInWithEmailPassword(email, password);
                    } else {
                        if (email.isEmpty()) {
                            binding.etUserId.setError("Email Can't Be Empty");
                        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                            binding.etUserId.setError("Invalid email address");
                        }
                        if (password.isEmpty()) {
                            binding.etPassword.setError("Password Can't Be empty");
                        } else if (password.length() <= 5) {
                            binding.etPassword.setError("Minimum Password Length 6 digit");
                        }
                    }
                } else {
                    Toast.makeText(LoginActivity.this, "Please check you internet!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        loginActivityViewModel.getIsUpdating().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(@Nullable Boolean aBoolean) {
                if (aBoolean) {
                    showProgressBar();
                } else {
                    hideProgressBar();
                }
            }
        });
    }

    private void init() {
        progressDialog = new ProgressDialog(this, R.style.ProgressDialogColour);
        progressDialog.setMessage("Please wait...");
        progressDialog.setCancelable(false);
        sessionManager = new SessionManager(this);
        loginActivityViewModel = new ViewModelProvider(this).get(LoginActivityViewModel.class);
    }

    private void signInWithEmailPassword(String email, String password) {
        loginActivityViewModel.makeMyLogin(email, password).observe(LoginActivity.this, new Observer<LoginResponse>() {
            @Override
            public void onChanged(LoginResponse loginResponse) {
                if (loginResponse.getSuccess()) {
                    Snackbar.make(binding.getRoot(), "Successfully logged in", Snackbar.LENGTH_SHORT)
                            .setAction("Action", null).show();
                    sessionManager.setToken(loginResponse.getToken());
                    sessionManager.setIsLogedIn(true);
                    gotoMainActivity();
                } else {
                    Snackbar.make(binding.getRoot(), "Credentials didn't match", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
            }
        });
    }

    private void gotoMainActivity() {
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    private boolean checkField(String email, String password) {
        return !email.isEmpty() && !password.isEmpty() && Patterns.EMAIL_ADDRESS.matcher(email).matches() && password.length() > 6;
    }

    private void showProgressBar() {
        progressDialog.show();
    }

    private void hideProgressBar() {
        progressDialog.dismiss();
    }
}
