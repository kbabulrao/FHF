package com.fhf.services;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.Date;

/**
 * Created by Santosh on 11/9/2015.
 */
public class BaseFHFService {

    protected Context context;
    public static final String SUCCESS = "SUCCESS";
    public static final String ERROR = "ERROR";
    public static final String SIGN_IN = "SIGN_IN";
    private WebServiceResultListener onServiceResultListener;
    protected Gson gson;

    /**
     * Context
     */
    public BaseFHFService(Context context) {
        this.context = context;
        GsonBuilder gsonBuilder = new GsonBuilder();
        gson = gsonBuilder.create();
    }

    public WebServiceResultListener getOnServiceResultListener() {
        return onServiceResultListener;
    }

    public void setOnServiceResultListener(WebServiceResultListener onServiceResultListener) {
        this.onServiceResultListener = onServiceResultListener;
    }
}
