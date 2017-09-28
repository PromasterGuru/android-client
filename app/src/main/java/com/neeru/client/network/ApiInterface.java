package com.neeru.client.network;


import com.neeru.client.models.Address;
import com.neeru.client.models.User;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.HeaderMap;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

/**
 * Created by brajendra on 10/08/17.
 */

public interface ApiInterface {

    String HEADER_AUTHORIZATION = "Authorization";
    String HEADER_DEVICE_ID = "device-id";


    @GET("/auth/v1/addresses")
    Call<List<Address>> getAddress(@HeaderMap Map<String, String> headers);

    @POST("auth/v1/address")
    Call<Address> createAddress(@HeaderMap Map<String, String> headers, @Body Address address);

    @PUT("auth/v1/address/{id}")
    Call<Address> updateAddress(@HeaderMap Map<String, String> headers, @Path("id") int id, @Body Address address);

    @DELETE("auth/v1/address/{id}")
    Call<Address> deleteAddress(@HeaderMap Map<String, String> headers, @Path("id") int id);




    @PUT("auth/v1/user")
    Call<List<User>> updateUser(@HeaderMap Map<String, String> headers, @Body User user);

    @DELETE("/auth/v1/signout")
    Call<Type> signOut();
}
