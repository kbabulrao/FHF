package com.fhf.services;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;

import com.fhf.constants.AppConstants;
import com.fhf.data.AppSessionData;
import com.fhf.data.User;
import com.fhf.services.webservices.BaseWsImpl;
import com.fhf.services.webservices.WsUrlConstants;
import com.google.gson.stream.JsonReader;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.InputStreamReader;

/**
 * Created by santosh on 2/12/2017.
 */

public class SignInService extends BaseFHFService implements WebServiceResultListener {

    Intent broadcastIntent;
    User currentUser;

    public SignInService(Context context) {
        super(context);
        this.context = context;
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
        baseWs.getService(WsUrlConstants.LOGIN_SERVICE_ENDPOINT.replace(WsUrlConstants.EMAIL, email).replace(WsUrlConstants.PASSWORD, password),
                null, AppConstants.LOGIN_SERVICE);
    }

    public void registerUser(String userName, String phone, String email, String password) {
        currentUser = new User();
        currentUser.setEmail(email);
        currentUser.setPhone_number(phone);
        currentUser.setUsername(userName);

        broadcastIntent = new Intent(AppConstants.LOGIN_SERVICE);
        BaseWsImpl baseWs = new BaseWsImpl(AppConstants.SIGN_UP_SERVICE_REQUEST, this) {
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

    public void recoveryPassword(String email) {
        broadcastIntent = new Intent(AppConstants.LOGIN_SERVICE);
        BaseWsImpl baseWs = new BaseWsImpl(AppConstants.FORGOT_PWD_SERVICE_REQUEST, this) {
            @Override
            protected void parseResponse(Object response) {
                onServiceCompleted(response, null, getReqId());
            }
        };
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(AppConstants.EMAIL, email);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        baseWs.getService(WsUrlConstants.FORGOT_PWD_SERVICE_ENDPOINT.replace(WsUrlConstants.EMAIL, email),
                null, AppConstants.LOGIN_SERVICE);
    }


    @Override
    public void onServiceCompleted(Object response, Object error, int reqId) {
        if (error == null) {
            broadcastIntent.putExtra(AppConstants.SUCCESS_TEXT, true);
            if (AppConstants.SIGN_IN_SERVICE_REQUEST == reqId) {
//                try {
                JsonReader jsonReader = new JsonReader(new InputStreamReader(new ByteArrayInputStream(response.toString().getBytes())));
                User user = gson.fromJson(jsonReader, User.class);
                if (user.getMessage() != null) {
                    if (user.getMessage().equalsIgnoreCase(AppConstants.SUCCESS_TEXT)) {
                        AppSessionData.getSessionDataInstance().setCurrentUser(user);
                    } else {
                        broadcastIntent.putExtra("error_msg", user.getMessage());
                    }
                }
//                    JSONObject jsonObject = (JSONObject) response;
//                    broadcastIntent.putExtra("status", jsonObject.getBoolean("status"));
//                    broadcastIntent.putExtra("error_msg", jsonObject.getString("error_msg"));
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
            } else if (AppConstants.SIGN_UP_SERVICE_REQUEST == reqId) {
                JsonReader jsonReader = new JsonReader(new InputStreamReader(new ByteArrayInputStream(response.toString().getBytes())));
                User user = gson.fromJson(jsonReader, User.class);
                if (user.getMessage() != null) {
                    if (user.getMessage().equalsIgnoreCase(AppConstants.SUCCESS_TEXT) && currentUser != null) {
                        currentUser.setUserid(user.getUserid());
                        AppSessionData.getSessionDataInstance().setCurrentUser(currentUser);
                    } else {
                        broadcastIntent.putExtra("error_msg", user.getMessage());
                    }
                }
            } else if (AppConstants.FORGOT_PWD_SERVICE_REQUEST == reqId) {
                try {
                    JSONObject jsonObject = (JSONObject) response;
                    broadcastIntent.putExtra("msg", jsonObject.getString("message"));
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