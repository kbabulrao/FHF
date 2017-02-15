package com.fhf.services;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;

import com.fhf.constants.AppConstants;
import com.fhf.services.webservices.BaseWsImpl;
import com.fhf.services.webservices.WsUrlConstants;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by santosh on 2/12/2017.
 */

public class LoginService extends BaseFHFService implements WebServiceResultListener {

    Intent broadcastIntent;

    public LoginService(Context context) {
        super(context);
    }

    public void validateUser(String email, String password) {
        broadcastIntent = new Intent(AppConstants.LOGIN_SERVICE);
        BaseWsImpl baseWs = new BaseWsImpl(AppConstants.CAMERA_CAPTURE_IMAGE_REQUEST_CODE, this) {
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
        baseWs.postService(WsUrlConstants.LOGIN_SERVICE_ENDPOINT, jsonObject, AppConstants.LOGIN_SERVICE);
    }

    @Override
    public void onServiceCompleted(Object response, Object error, int reqId) {
        LocalBroadcastManager.getInstance(context).sendBroadcast(broadcastIntent);
    }
}
