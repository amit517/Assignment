package com.team.assignment.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.team.assignment.repository.LoginRepository;

/**
 * Created by Amit on 10,December,2020
 */
public class MainActivityViewModel  extends AndroidViewModel {

    public MainActivityViewModel (@NonNull Application application){
        super(application);
        //mRepo = new LoginRepository(application);
    }
}
