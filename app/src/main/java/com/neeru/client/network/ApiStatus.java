package com.neeru.client.network;

/**
 * Created by brajendra on 25/08/17.
 */

public enum ApiStatus {

    GET,
    POST,
    PUT,
    IMAGE_UPLOAD,
    DELETE;


    public static ApiStatus type(String s) {
        for (ApiStatus type : values()) {
            if (type.name().equals(s)) {
                return type;
            }
        }
        return null;
    }
}
