package com.fhf.data;

import android.content.Context;
import android.content.SharedPreferences;

import com.fhf.FHFApplication;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * Created by santosh on 3/7/2017.
 */

public class AppSessionData {

    private static AppSessionData appSessionData;
    SharedPreferences pref;

    //app level names
    public static final String SHARED_PREFERENCE_NAME = "FHF";
    public static final String SHARED_PREFERENCE_NAME_LOCATION = "FHFLOCATION";

    public static final String SHRPRF_KEY_USER = "USER";

    /**
     * Create private constructor
     */
    private AppSessionData() {
    }

    /**
     * Create a static method to get instance.
     */
    public static AppSessionData getSessionDataInstance() {
        if (appSessionData == null) {
            appSessionData = new AppSessionData();
        }
        return appSessionData;
    }

    public void setCurrentUser(User user) {

        if (pref == null) {
            pref = FHFApplication.getAppContext().getSharedPreferences(SHARED_PREFERENCE_NAME, Context.MODE_PRIVATE);
        }
        GsonBuilder gsonBuilder = new GsonBuilder();
        Gson gson = gsonBuilder.create();
        String userJsonString = gson.toJson(user);

        SharedPreferences.Editor editor = pref.edit();
        editor.putString(SHRPRF_KEY_USER, userJsonString);
        editor.commit();
    }

    public static String getValueFromSharedPreferences(String key) {
        Gson gson = new Gson();
        SharedPreferences pref = FHFApplication.getAppContext().getSharedPreferences(SHARED_PREFERENCE_NAME, Context.MODE_PRIVATE);

        String value = pref.getString(key, "");
        User userDataModel = gson.fromJson(value, User.class);
        return value;

    }
}
