package com.neeru.client.prefs;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.Nullable;

import com.neeru.client.models.User;

/**
 * Created by brajendra on 29/07/17.
 */

public class AuthPreference {

    private final String NAME = "prefs_user";
    private final String KEY_TOKEN = "key_token";
    private final String KEY_MOBILE = "key_mobile";
    private final String KEY_FNAME = "key_fname";
    private final String KEY_LNAME = "key_lname";
    private final String KEY_EMAIL = "key_email";
    private final String KEY_AVATAR = "key_avatar";
    private final String KEY_DESC = "key_desc";
    private final String KEY_ROLE = "key_role";


    private final SharedPreferences prefs;


    public AuthPreference(final Context context) {
        prefs = context.getApplicationContext().getSharedPreferences(NAME, Context.MODE_PRIVATE);
    }


    public void setUser(@Nullable User user) {
        if (user == null) {

        }

        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(KEY_TOKEN, "Bearer "+user.accessToken);
        editor.putString(KEY_MOBILE, user.contact);
        editor.putString(KEY_EMAIL, user.email);
        editor.putString(KEY_FNAME, user.firstName);
        editor.putString(KEY_LNAME, user.lastName);
        editor.putString(KEY_AVATAR, user.avatar);
        editor.putString(KEY_DESC, user.description);
        editor.putString(KEY_ROLE, user.roles);


        editor.apply();

    }


    public String getAccessTocken() {

        return prefs.getString(KEY_TOKEN, null);

    }

    public void setAccessTocken(String token) {
        prefs.edit()
                .putString(KEY_TOKEN, token)
                .apply();
    }

}
