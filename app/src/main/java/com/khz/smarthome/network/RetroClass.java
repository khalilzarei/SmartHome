package com.khz.smarthome.network;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetroClass {

    private static final String   BASE_URL = "https://gruneslicht.de/Installer/public/";
    private static       Retrofit retrofit = null;

    private static Retrofit getRetrofitInstance() {
        Gson gson = new GsonBuilder().setLenient().create();
        OkHttpClient okHttpClient = new OkHttpClient()
                .newBuilder()
                .connectTimeout(60, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .build();
        return new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

    }

    //    public static APIService getAPIService() {
    //        return getRetrofitInstance().create(APIService.class);
    //    }

    public static APIService getAPIService() {
        OkHttpClient client = new OkHttpClient.Builder().readTimeout(2, TimeUnit.MINUTES)
                                                        .writeTimeout(2, TimeUnit.MINUTES)
                                                        .addInterceptor(chain -> {
                                                            Request original = chain.request();
                                                            Request.Builder requestBuilder = original
                                                                    .newBuilder()
                                                                    .method(original
                                                                            .method(), original
                                                                            .body());
                                                            Request request = requestBuilder
                                                                    .build();
                                                            return chain.proceed(request);
                                                        })
                                                        .build();
        if (retrofit == null) {
            retrofit = new Retrofit.Builder().baseUrl(BASE_URL)
                                             .client(client)
                                             .addConverterFactory(GsonConverterFactory.create())
                                             .build();
        }
        return retrofit.create(APIService.class);
    }
}