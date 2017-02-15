package com.fhf.data;

import android.content.Context;
import android.content.SharedPreferences;

import com.fhf.FHFApplication;

/**
 * Created by Santosh on 11/6/2015.
 */
public class AppSessionData {

    public static String FHF_APP = "FHFApp";
    public String USER_PREFS = "user_prefs";
    public String CURRENT_USER_VALUES = "current_user_value";
    public static String SHPREF_KEY_ACCESS_TOKEN = "SHPREF_KEY_ACCESS_TOKEN";

    private static AppSessionData sessionData;

    public static boolean isCompanyFeedEnabled() {
        boolean isCompanyFeedEnabled = FHFApplication.getAppContext().getSharedPreferences(FHF_APP, Context.MODE_PRIVATE).getBoolean(SHPREF_KEY_ACCESS_TOKEN, false);
        return isCompanyFeedEnabled;
    }

    public static void setCompanyFeedEnabled(boolean companyFeedEnabled) {
        SharedPreferences.Editor prefsEditor = FHFApplication.getAppContext().getSharedPreferences(FHF_APP, Context.MODE_PRIVATE).edit();
        prefsEditor.putBoolean(SHPREF_KEY_ACCESS_TOKEN, companyFeedEnabled);
        prefsEditor.commit();
    }

    public void setCurrentUser(User currentUser, Context ctx) {
        ComplexPreferences complexPreferences = ComplexPreferences.getComplexPreferences(ctx, USER_PREFS, 0);
        complexPreferences.putObject(CURRENT_USER_VALUES, currentUser);
        complexPreferences.commit();
    }

    public User getCurrentUser(Context ctx) {
        ComplexPreferences complexPreferences = ComplexPreferences.getComplexPreferences(ctx, USER_PREFS, 0);
        User currentUser = complexPreferences.getObject(CURRENT_USER_VALUES, User.class);
        return currentUser;
    }

    public void clearCurrentUser(Context ctx) {
        ComplexPreferences complexPreferences = ComplexPreferences.getComplexPreferences(ctx, USER_PREFS, 0);
        complexPreferences.clearObject();
        complexPreferences.commit();
    }

    /**
     * Create private constructor
     */
    private AppSessionData() {
    }

    /**
     * Create a static method to get instance.
     */
    public static AppSessionData getSessionDataInstance() {
        if (sessionData == null) {
            sessionData = new AppSessionData();
        }
        return sessionData;
    }

}
