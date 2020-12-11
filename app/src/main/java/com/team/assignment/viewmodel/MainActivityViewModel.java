package com.team.assignment.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.gson.JsonObject;
import com.team.assignment.model.CvData;
import com.team.assignment.model.LoginResponse;
import com.team.assignment.repository.LoginRepository;
import com.team.assignment.repository.UpdateInfoRepository;

/**
 * Created by Amit on 10,December,2020
 */
public class MainActivityViewModel  extends AndroidViewModel {

    private UpdateInfoRepository mRepo;

    public MainActivityViewModel (@NonNull Application application){
        super(application);
        mRepo = new UpdateInfoRepository(application);
    }

    public LiveData<Boolean> getIsUpdating(){
        return mRepo.getIsUpdating();
    }

    public MutableLiveData<CvData> sendPersonalData(JsonObject paramObject){
        return mRepo.sendPersonalData(paramObject);
    }
}
