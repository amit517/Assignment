package com.team.assignment.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.team.assignment.model.LoginResponse;
import com.team.assignment.repository.LoginRepository;

/**
 * Created by Amit on 09,December,2020
 */
public class LoginActivityViewModel extends AndroidViewModel {

    private LoginRepository mRepo;

    public LoginActivityViewModel(@NonNull Application application) {
        super(application);
        mRepo = new LoginRepository(application);
    }

    public LiveData<Boolean> getIsUpdating() {
        return mRepo.getIsUpdating();
    }

    public MutableLiveData<LoginResponse> makeMyLogin(String email, String password) {
        return mRepo.makeMyLogin(email, password);
    }
}
