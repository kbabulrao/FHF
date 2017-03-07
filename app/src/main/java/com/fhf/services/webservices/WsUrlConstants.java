package com.fhf.services.webservices;

import com.fhf.FHFApplication;

/**
 * Created by Santosh on 11/10/2015.
 */
public class WsUrlConstants {

    public static String USERNAME = "[USERNAME]";
    public static String PASSWORD = "[PASSWORD]";
    public static String PHONE_NUMBER = "[PHONE_NUMBER]";
    public static String EMAIL = "[EMAIL]";

    public static String LOGIN_SERVICE_ENDPOINT = FHFApplication.getEnvSettings().getBaseUrl() + "login/dologin?username=" + USERNAME + "&password=" + PASSWORD;
    public static String REGISTER_SERVICE_ENDPOINT = FHFApplication.getEnvSettings().getBaseUrl() + "register/doregister?username=" + USERNAME + "&phone_number=" + PHONE_NUMBER +
            "&email=" + EMAIL + "&password=" + PASSWORD;

}
