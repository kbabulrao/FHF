package com.fhf.services;

/**
 * Created by Santosh on 11/9/2015.
 */
public interface WebServiceResultListener {
    public void onServiceCompleted(Object response, Object error, int reqId);
}
