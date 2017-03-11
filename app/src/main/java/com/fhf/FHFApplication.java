package com.fhf;

import android.app.Application;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.support.multidex.MultiDex;
import android.util.Base64;
import android.util.Log;

import com.facebook.FacebookSdk;
import com.fhf.data.EnvironmentSettings;
import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by santosh on 2/11/2017.
 */

public class FHFApplication extends Application {

    private static Context context;
    public static final int PROD = 0;
    public static final int DEV = 1;
    public static final int LOCAL = 2;
    public static int env = DEV;
    private static EnvironmentSettings envSettings;
    public static String environment;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(newBase);
        MultiDex.install(this);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        loadEnvironmentValues();

        FacebookSdk.sdkInitialize(getApplicationContext());
        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    "com.fhf",  // replace with your unique package username
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {

        } catch (NoSuchAlgorithmException e) {

        }
    }

    public static Context getAppContext() {
        return context;
    }

    private void loadEnvironmentValues() {
        Log.v("loadEnvironmentValues", "loadEnvironmentValues=");
        try {
            String settingsFile = null;
            switch (env) {
                case PROD:
                    settingsFile = "app_settings_prod.json";
                    environment = "prod";
                    break;
                case DEV:
                    settingsFile = "app_settings_dev.json";
                    environment = "dev";
                    break;
                case LOCAL:
                    settingsFile = "app_settings_local.json";
                    environment = "local";
                    break;
            }
            InputStream inputStream = this.getAssets().open(settingsFile);
            JsonReader jsonReader = new JsonReader(new InputStreamReader(inputStream));
            Gson gson = new Gson();
            envSettings = gson.fromJson(jsonReader, EnvironmentSettings.class);

            Log.d("URL", getEnvSettings().getBaseUrl());

        } catch (IOException e) {
            Log.d("EnvironmentSettings", "Error loading env values", e);
        }
    }

    public static EnvironmentSettings getEnvSettings() {
        return envSettings;
    }

}
