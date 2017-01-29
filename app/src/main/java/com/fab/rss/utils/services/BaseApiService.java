package com.fab.rss.utils.services;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Author:       Fab
 * Email:        ffontaine@thingsaremoving.com
 * Created:      1/28/17
 */

@SuppressWarnings("UnusedDeclaration")
public class BaseApiService {

    private Retrofit retrofit;

    public BaseApiService() {

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();

        retrofit = new Retrofit.Builder()
                .baseUrl(IApiService.BASE_URL_API)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public IApiService create() {
        return retrofit.create(IApiService.class);
    }

}
