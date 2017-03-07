package com.fhf.data;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Santosh on 11/8/2015.
 */
public class EnvironmentSettings implements Serializable {

    @SerializedName("base_url")
    private String base_url;
    @SerializedName("crittercism_api_key")
    private String crittercism_api_key;

    public String getBaseUrl() {
        return base_url;
    }

    public void setBaseUrl(String base_url) {
        this.base_url = base_url;
    }

    public String getCrittercism_api_key() {
        return crittercism_api_key;
    }

    public void setCrittercism_api_key(String crittercism_api_key) {
        this.crittercism_api_key = crittercism_api_key;
    }
}
