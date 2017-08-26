package com.neeru.client.network;

import okhttp3.Request;
import retrofit2.Response;

/**
 * Created by brajendra on 21/08/17.
 */

public interface PostListener {

    void onResult(Integer position,Request request, Response response, boolean isError, Throwable throwable,ApiStatus status);
}
