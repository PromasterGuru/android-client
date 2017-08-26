package com.neeru.client.network;

import okhttp3.Request;
import retrofit2.Response;

/**
 * Created by brajendra on 10/08/17.
 */

public interface ApiListener {
    void onResult(Request request, Response response);


    void onFailure(Request request, Throwable t);

    void onNetworkError(Throwable t);

}
