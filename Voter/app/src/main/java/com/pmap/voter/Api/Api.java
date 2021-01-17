package com.pmap.voter.Api;

import android.content.Context;
import android.content.SharedPreferences;

import com.pmap.voter.logic.P;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;




    public class Api {
        public static Retrofit getRetrofitBuilder(Context context) {
            SharedPreferences sharedpreferences = context.getSharedPreferences(IPActivity.MyPREFERENCES, Context.MODE_PRIVATE);
            String url=sharedpreferences.getString("url", P.URL);

            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);


            OkHttpClient okHttpClient = new OkHttpClient.Builder()
                    .addInterceptor(loggingInterceptor)
                    .readTimeout(30, TimeUnit.SECONDS)
                    .connectTimeout(30, TimeUnit.SECONDS)
                    .retryOnConnectionFailure(true)
                    .build();

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(url)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(okHttpClient)
                    .build();


            return retrofit;

        }

    }

