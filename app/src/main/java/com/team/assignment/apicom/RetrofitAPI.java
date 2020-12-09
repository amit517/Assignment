package com.team.assignment.apicom;

import com.google.gson.JsonObject;
import com.team.assignment.model.LoginResponse;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface RetrofitAPI {

    @Headers("Content-Type: application/json")
    @POST("login")
    Call<LoginResponse> getUser(@Body JsonObject jsonObject);

}
