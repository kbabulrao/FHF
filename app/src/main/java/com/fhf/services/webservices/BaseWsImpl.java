package com.fhf.services.webservices;

import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.fhf.FHFApplication;
import com.fhf.services.WebServiceResultListener;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Santosh on 11/9/2015.
 */
public abstract class BaseWsImpl {

    protected static RequestQueue queue = null;
    protected WsUrlConstants wsUrlConstants;

    private int reqId;
    private List<WebServiceResultListener> requestListeners;
    public static int statusCode;

    public BaseWsImpl(int reqId, WebServiceResultListener listener) {
        if (queue == null) {
            queue = Volley.newRequestQueue(FHFApplication.getAppContext());
        }
        requestListeners = new ArrayList<WebServiceResultListener>();
        this.reqId = reqId;
        this.addWsRequestListner(listener);
        if (wsUrlConstants == null) {
            wsUrlConstants = new WsUrlConstants();
        }
    }

    public int getReqId() {
        return reqId;
    }

    private void addWsRequestListner(WebServiceResultListener listener) {
        if (listener != null) {
            requestListeners.add(listener);
        }
    }

    public List<WebServiceResultListener> getRequestListeners() {
        return requestListeners;
    }

    public void postService(String serviceUrl, JSONObject jsonRequest, String tag) {

        Log.d("server url", serviceUrl);
        JsonObjectRequest reqs = new JsonObjectRequest(Request.Method.POST, serviceUrl, jsonRequest,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("server response", response.toString());
                        parseResponse(response.toString());
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("server error", error.toString());
                broadcastError(error);
            }
        }
        ) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return setHeaders();
            }

        };
        reqs.setRetryPolicy(new DefaultRetryPolicy(
                50000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        reqs.setTag(tag);
        queue.add(reqs);
    }

    public void getService(String serviceUrl, JSONObject jsonRequest, String tag) {

        Log.d("server url", serviceUrl);
        JsonObjectRequest reqs = new JsonObjectRequest(Request.Method.GET, serviceUrl, jsonRequest,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("server response", response.toString());
                        parseResponse(response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("server error", error.toString());
                broadcastError(error);
            }
        }
        ) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return setHeaders();
            }

        };
        reqs.setRetryPolicy(new DefaultRetryPolicy(
                50000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        reqs.setTag(tag);
        queue.add(reqs);
    }


    protected void broadcastError(VolleyError error) {
        for (WebServiceResultListener listener : getRequestListeners()) {
            if (listener != null) {
                try {
                    if (error != null) {
                        if (error.networkResponse != null) {
                            Log.e("error code", error.networkResponse.statusCode + "");
                            if (error.networkResponse.statusCode == 404) {
                                int err = 404;
                                listener.onServiceCompleted(null, err, getReqId());
                            } else if (error.networkResponse.statusCode == 412) {

                                String errorMsg = "You may only flag this once";
                                error = new VolleyError(new String(errorMsg));
                                listener.onServiceCompleted(errorMsg, error, getReqId());
                            } else if (error.networkResponse.statusCode == 422) {
                                String errorMsg = "Process failed due to incorrect data";
                                error = new VolleyError(new String(errorMsg));
                                listener.onServiceCompleted(errorMsg, error, getReqId());
                            } else if (error.networkResponse.statusCode == 500 || error.networkResponse.statusCode == 406) {
                                if (error.networkResponse.statusCode == 406) {
                                }
                                listener.onServiceCompleted("", error, getReqId());
                            } else if (error.networkResponse.statusCode == 401) {
                                String errorMsg = null;
                                if (error.networkResponse != null && error.networkResponse.data != null) {
                                    VolleyError volleyError = new VolleyError(new String(error.networkResponse.data));
                                    errorMsg = volleyError.getMessage();
                                    JSONObject jsonObject = new JSONObject(errorMsg);
                                    Log.d("errorMsg", jsonObject.getString("error") + " " + errorMsg);
                                    errorMsg = jsonObject.getString("error");
                                    listener.onServiceCompleted(errorMsg, error, getReqId());
                                }
                            } else {
                                error = new VolleyError(new String());
                                listener.onServiceCompleted("", error, getReqId());
                            }
                        } else {
                            if (error.getMessage().contains("No authentication challenges found")) {
                                statusCode = 401;
                            }
                            Log.d("status code", "<>" + error.getMessage());
                            //String errorMsg = FHFApplication.getAppContext().getResources().getString(R.string.net_error);

                            listener.onServiceCompleted("", error, getReqId());
                        }
                    } else {
                        listener.onServiceCompleted("", error, getReqId());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    listener.onServiceCompleted(null, error, getReqId());
                }
            }
        }
    }

    protected void broadcastResponse(Object result) {
        for (WebServiceResultListener listener : getRequestListeners()) {
            if (listener != null) {
                listener.onServiceCompleted(result, null, getReqId());
            }
        }
    }

    public Map<String, String> setHeaders() {
        HashMap<String, String> headers = new HashMap<String, String>();
        headers.put("Content-Type", "application/json");
        headers.put("Accept", "application/json");
        return headers;
    }

    protected abstract void parseResponse(Object response);
}
