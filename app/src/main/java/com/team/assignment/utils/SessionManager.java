package com.team.assignment.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Amit on 13,August,2020
 */
public class SessionManager {
    private final Context context;
    private static final String Pref_Name = "demoCvPoject";
    private static final String token = "token";
    private static final String isLogedIn = "isLogedIn";
    private final SharedPreferences sharedPreferences;
    private final SharedPreferences.Editor editor;

    public SessionManager(Context context) {
        this.context = context;
        sharedPreferences = context.getSharedPreferences(Pref_Name, MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public void setToken(String value) {
        editor.putString(token, value).apply();
    }

    public String getToken() {
        return sharedPreferences.getString(token," ");
    }

    public void setIsLogedIn(boolean code) {
        editor.putBoolean(isLogedIn, code).apply();
    }

    public boolean getIsLogedIn() {
        return sharedPreferences.getBoolean(isLogedIn, false);
    }

}
