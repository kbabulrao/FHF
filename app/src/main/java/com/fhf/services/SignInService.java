package com.fhf.services;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;

import com.android.volley.VolleyError;
import com.fhf.constants.AppConstants;
import com.fhf.services.webservices.BaseWsImpl;
import com.fhf.services.webservices.WsUrlConstants;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by santosh on 2/12/2017.
 */

public class SignInService extends BaseFHFService implements WebServiceResultListener {

    Intent broadcastIntent;

    public SignInService(Context context) {
        super(context);
    }

    public void validateUser(String email, String password) {
        broadcastIntent = new Intent(AppConstants.LOGIN_SERVICE);
        BaseWsImpl baseWs = new BaseWsImpl(AppConstants.SIGN_IN_SERVICE_REQUEST, this) {
            @Override
            protected void parseResponse(Object response) {
                onServiceCompleted(response, null, getReqId());
            }
        };
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(AppConstants.EMAIL, email);
            jsonObject.put(AppConstants.PASSWORD, password);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        baseWs.getService(WsUrlConstants.LOGIN_SERVICE_ENDPOINT.replace(WsUrlConstants.USERNAME, email).replace(WsUrlConstants.PASSWORD, password),
                null, AppConstants.LOGIN_SERVICE);
    }

    public void registerUser(String userName, String phone, String email, String password) {
        broadcastIntent = new Intent(AppConstants.LOGIN_SERVICE);
        BaseWsImpl baseWs = new BaseWsImpl(AppConstants.SIGN_IN_SERVICE_REQUEST, this) {
            @Override
            protected void parseResponse(Object response) {
                onServiceCompleted(response, null, getReqId());
            }
        };
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(AppConstants.EMAIL, email);
            jsonObject.put(AppConstants.PASSWORD, password);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        baseWs.getService(WsUrlConstants.REGISTER_SERVICE_ENDPOINT.replace(WsUrlConstants.USERNAME, userName).replace(WsUrlConstants.PHONE_NUMBER, phone)
                        .replace(WsUrlConstants.EMAIL, email).replace(WsUrlConstants.PASSWORD, password),
                null, AppConstants.LOGIN_SERVICE);
    }

    @Override
    public void onServiceCompleted(Object response, Object error, int reqId) {
        if (error == null) {
            if (AppConstants.SIGN_IN_SERVICE_REQUEST == reqId) {
                broadcastIntent.putExtra(AppConstants.SUCCESS_TEXT, true);
                try {
                    JSONObject jsonObject = (JSONObject) response;
                    broadcastIntent.putExtra("status", jsonObject.getBoolean("status"));
                    broadcastIntent.putExtra("error_msg", jsonObject.getString("error_msg"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        } else {
            broadcastIntent.putExtra(AppConstants.ERROR_TEXT, error.toString());
        }
        LocalBroadcastManager.getInstance(context).sendBroadcast(broadcastIntent);
    }
}
