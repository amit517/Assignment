package com.team.assignment.apicom;

import com.google.gson.JsonObject;
import com.team.assignment.model.CvData;
import com.team.assignment.model.LoginResponse;
import com.team.assignment.model.PdfUploadResponse;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;

public interface RetrofitAPI {

    @Headers("Content-Type: application/json")
    @POST("login")
    Call<LoginResponse> getUser(@Body JsonObject jsonObject);

    @Headers("Content-Type: application/json")
    @POST("v0/recruiting-entities/")
    Call<CvData> sendBasicInfo(@Header("Authorization") String header,
                               @Body JsonObject jsonObject);

    //Content Type not specified

    @Multipart
    @PUT("file-object/{file_token}/")
    Call<PdfUploadResponse> uploadPdf(@Header("Authorization") String header,
                                      @Path("file_token") int fileToken,
                                      @Part MultipartBody.Part file
                                      );
}
