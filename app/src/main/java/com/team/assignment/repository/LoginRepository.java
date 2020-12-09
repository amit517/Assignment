package com.team.assignment.repository;

import android.app.Application;
import android.util.Log;
import android.widget.Toast;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.gson.JsonObject;
import com.team.assignment.apicom.RetrofitClient;
import com.team.assignment.model.LoginResponse;
import com.team.assignment.view.LoginActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Amit on 09,December,2020
 */
public class LoginRepository {
    private Application application;
    private MutableLiveData<Boolean> mIsUpdating = new MutableLiveData<>();
    private static final String TAG = "LoginRepository";
    public LoginRepository(Application application) {
        this.application = application;
    }

    public MutableLiveData<LoginResponse> makeMyLogin(String email, String password){
        mIsUpdating.setValue(true);
        MutableLiveData<LoginResponse> liveData = new MutableLiveData<>();
        try {
            JsonObject paramObject = new JsonObject();
            paramObject.addProperty("username", email);
            paramObject.addProperty("password", password);
            Call<LoginResponse> call = RetrofitClient
                    .getInstance()
                    .getRetrofitApi()
                    .getUser(paramObject);

            call.enqueue(new Callback<LoginResponse>() {
                @Override
                public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                    try{
                        if(response.code() == 200){
                            LoginResponse loginResponse = response.body();
                            Log.d(TAG, "onResponse: "+loginResponse);
                            liveData.postValue(loginResponse);
                        }else if (response.code() == 400){
                            Toast.makeText(application, "Password is incorrect for provided username.", Toast.LENGTH_SHORT).show();
                        }
                        mIsUpdating.setValue(false);
                    }catch(Exception e){
                        e.printStackTrace();
                        Toast.makeText(application, "Something Went wrong! Please try again later!", Toast.LENGTH_SHORT).show();
                        mIsUpdating.setValue(false);
                    }
                }

                @Override
                public void onFailure(Call<LoginResponse> call, Throwable t) {
                    mIsUpdating.setValue(false);
                    Toast.makeText(application, "Something Went wrong! Please try again later!", Toast.LENGTH_SHORT).show();
                }
            });
        } catch (Exception e) {
            mIsUpdating.setValue(false);
            e.printStackTrace();
        }

        return liveData;
    }

    public MutableLiveData<Boolean> getIsUpdating(){
        return mIsUpdating;
    }
}
