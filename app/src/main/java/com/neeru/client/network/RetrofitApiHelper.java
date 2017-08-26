package com.neeru.client.network;



import com.neeru.client.BuildConfig;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by brajendra on 10/08/17.
 */

public class RetrofitApiHelper {

    private static ApiInterface mApi;

    public static ApiInterface initApiClient() {
        if (mApi == null) {
            OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

            httpClient.readTimeout(1, TimeUnit.MINUTES);
            httpClient.connectTimeout(1, TimeUnit.MINUTES);
            if (BuildConfig.DEBUG) {
                HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
                logging.setLevel(HttpLoggingInterceptor.Level.BODY);

                httpClient.addInterceptor(logging);
            }


            final Retrofit retrofit = new Retrofit.Builder()
                    .client(httpClient.build())
                    .baseUrl(BuildConfig.KEY_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            return retrofit.create(ApiInterface.class);
        } else {
            return mApi;
        }

    }


    private static ApiInterface mStringApi;

    public static ApiInterface initStringApiClient() {
        if (mStringApi == null) {
            OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

            httpClient.readTimeout(1, TimeUnit.MINUTES);
            httpClient.connectTimeout(1, TimeUnit.MINUTES);
            if (BuildConfig.DEBUG) {
                HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
                logging.setLevel(HttpLoggingInterceptor.Level.BODY);

                httpClient.addInterceptor(logging);
            }
            Retrofit retrofit = new Retrofit.Builder()
                    .client(httpClient.build())
                    .baseUrl(BuildConfig.KEY_URL)
                    .addConverterFactory(new ToStringConverterFactory())
                    .build();

            return retrofit.create(ApiInterface.class);
        } else {
            return mStringApi;
        }
    }


}
