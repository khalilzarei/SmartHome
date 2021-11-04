package com.khz.smarthome.network;

import com.khz.smarthome.ui.login.LoginResponse;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface APIService {

    //region login

    @FormUrlEncoded
    @POST("api/v1.0/owner/login")
    Call<LoginResponse> login(
            @Field("email") String email,
            @Field("password") String password);
    //endregion

}
