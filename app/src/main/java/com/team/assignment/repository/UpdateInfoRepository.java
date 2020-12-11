package com.team.assignment.repository;

import android.app.Application;
import android.util.Log;
import android.widget.Toast;

import androidx.lifecycle.MutableLiveData;

import com.google.gson.JsonObject;
import com.team.assignment.apicom.RetrofitClient;
import com.team.assignment.model.CvData;
import com.team.assignment.model.PdfUploadResponse;
import com.team.assignment.utils.SessionManager;
import com.team.assignment.view.MainActivity;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Amit on 11,December,2020
 */
public class UpdateInfoRepository {
    private Application application;
    private MutableLiveData<Boolean> mIsUpdating = new MutableLiveData<>();
    private SessionManager sessionManager;

    public UpdateInfoRepository(Application application) {
        this.application = application;
        sessionManager = new SessionManager(application);
    }

    public MutableLiveData<Boolean> getIsUpdating() {
        return mIsUpdating;
    }

    public MutableLiveData<CvData> sendPersonalData(JsonObject paramObject) {
        mIsUpdating.setValue(true);
        MutableLiveData<CvData> liveData = new MutableLiveData<>();
        try {

            Call<CvData> call = RetrofitClient
                    .getInstance()
                    .getRetrofitApi()
                    .sendBasicInfo("Token " + sessionManager.getToken(), paramObject);

            Log.d("TAG", "sendPersonalData: " + call.toString());

            call.enqueue(new Callback<CvData>() {
                @Override
                public void onResponse(Call<CvData> call, Response<CvData> response) {
                    try {
                        if (response.code() == 201) {
                            CvData cvData = response.body();
                            liveData.postValue(cvData);
                        } else {
                            mIsUpdating.setValue(false);
                            Toast.makeText(application, "Please check all the fields", Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(application, "Something Went wrong! Please try again later!", Toast.LENGTH_SHORT).show();
                        mIsUpdating.setValue(false);
                    }
                }

                @Override
                public void onFailure(Call<CvData> call, Throwable t) {
                    mIsUpdating.setValue(false);
                    Log.d("TAG", "onFailure: " + t.getMessage());
                    Toast.makeText(application, "Something Went wrong! Please try again later!", Toast.LENGTH_SHORT).show();
                }
            });
        } catch (Exception e) {
            mIsUpdating.setValue(false);
            e.printStackTrace();
        }

        return liveData;
    }

    public MutableLiveData<PdfUploadResponse> uploadPDF(File file, int fileToken){

        mIsUpdating.setValue(true);
        MutableLiveData<PdfUploadResponse> liveData = new MutableLiveData<>();

        try {
            RequestBody requestFile = RequestBody.create(MediaType.parse("application/pdf"),
                    file);
            MultipartBody.Part body = MultipartBody.Part.createFormData("file", file.getName(),
                    requestFile);


            Call<PdfUploadResponse> call = RetrofitClient
                    .getInstance()
                    .getRetrofitApi()
                    .uploadPdf("Token " + sessionManager.getToken(),fileToken, body);

            Log.d("TAG", "sendPersonalData: " + call.toString());

            call.enqueue(new Callback<PdfUploadResponse>() {
                @Override
                public void onResponse(Call<PdfUploadResponse> call, Response<PdfUploadResponse> response) {
                    try {
                        if (response.code() == 200) {
                            PdfUploadResponse uploadResponse = response.body();
                            liveData.postValue(uploadResponse);
                        } else {
                            Toast.makeText(application, "Something Went wrong! Please try again later!", Toast.LENGTH_SHORT).show();
                        }
                        mIsUpdating.setValue(false);
                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(application, "Something Went wrong! Please try again later!", Toast.LENGTH_SHORT).show();
                        mIsUpdating.setValue(false);
                    }
                }

                @Override
                public void onFailure(Call<PdfUploadResponse> call, Throwable t) {
                    mIsUpdating.setValue(false);
                    Log.d("TAG", "onFailure: " + t.getMessage());
                    Toast.makeText(application, "Something Went wrong! Please try again later!", Toast.LENGTH_SHORT).show();
                }
            });
        } catch (Exception e) {
            mIsUpdating.setValue(false);
            e.printStackTrace();
        }
        return liveData;
    }

}
