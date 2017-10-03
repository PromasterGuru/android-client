package com.neeru.client.network;

import android.content.Context;


import com.neeru.client.App;
import com.neeru.client.models.Address;
import com.neeru.client.models.Review;
import com.neeru.client.models.User;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.neeru.client.network.ApiInterface.HEADER_AUTHORIZATION;
import static com.neeru.client.network.ApiInterface.HEADER_DEVICE_ID;


/**
 * Created by brajendra on 10/08/17.
 */

public class NetworkHelper {


    private ApiListener listener;
    private PostListener postListener;

    public NetworkHelper(ApiListener listener) {
        this.listener = listener;
    }

    public NetworkHelper(PostListener postListener) {
        this.postListener = postListener;
    }

    public NetworkHelper(ApiListener listener, PostListener postListener) {
        this.listener = listener;
        this.postListener = postListener;
    }


    public void getAddress(String token, Context context) {
        executeCallback(RetrofitApiHelper.initApiClient().getAddress(getHeaders(token, context)));
    }


    public void createAddress(String token, Context context, Address addreess) {
        executePost(RetrofitApiHelper.initApiClient().createAddress(getHeaders(token, context), addreess), ApiStatus.POST, null);
    }

    public void updateAddress(String token, Context context, Address addreess, int id) {
        executePost(RetrofitApiHelper.initApiClient().updateAddress(getHeaders(token, context), id, addreess), ApiStatus.PUT, null);
    }

    public void deleteAddress(Integer position, String token, Context context, int id) {
        executePost(RetrofitApiHelper.initApiClient().deleteAddress(getHeaders(token, context), id), ApiStatus.DELETE, position);
    }


    public void updateUser(String token,Context context,User user) {
        executePost(RetrofitApiHelper.initApiClient().updateUser(getHeaders(token, context), user), ApiStatus.DELETE, -1);
    }

    public void updateAvater(String token,Context context,User user) {
        executePost(RetrofitApiHelper.initApiClient().updateUser(getHeaders(token, context), user), ApiStatus.IMAGE_UPLOAD, -1);
    }


    public void createReview(String token,Review review) {
        executePost(RetrofitApiHelper.initApiClient().createReview(token, review), ApiStatus.POST, -1);
    }


    Map getHeaders(String token, Context context) {
        Map<String, String> map = new HashMap<>();
        map.put(HEADER_AUTHORIZATION, token);
        map.put(HEADER_DEVICE_ID, App.getDeviseID(context));

        return map;
    }


    void executeCallback(Call call) {
        call.enqueue(new Callback<Type>() {
            @Override
            public void onResponse(Call<Type> call, Response<Type> response) {
                listener.onResult(call.request(), response);
            }

            @Override
            public void onFailure(Call<Type> call, Throwable t) {
                if (t instanceof IOException) {
                    listener.onNetworkError(t);
                } else {
                    listener.onFailure(call.request(), t);
                }

            }
        });

    }

    void executePost(Call call, final ApiStatus status, final Integer position) {
        call.enqueue(new Callback<Type>() {
            @Override
            public void onResponse(Call<Type> call, Response<Type> response) {

                if (postListener != null) {
                    postListener.onResult(position, call.request(), response, false, null, status);
                }


            }

            @Override
            public void onFailure(Call<Type> call, Throwable t) {
                if (postListener != null) {
                    postListener.onResult(position, call.request(), null, true, t, status);
                }


            }
        });

    }


}
