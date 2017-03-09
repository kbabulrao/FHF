package com.fhf.services.webservices;

import com.fhf.FHFApplication;

/**
 * Created by Santosh on 11/10/2015.
 */
public class WsUrlConstants {

    public static String BASE_URL = FHFApplication.getEnvSettings().getBaseUrl();
    public static String USERNAME = "[USERNAME]";
    public static String PASSWORD = "[PASSWORD]";
    public static String PHONE_NUMBER = "[PHONE_NUMBER]";
    public static String EMAIL = "[EMAIL]";
    public static String USER_ID = "[USER_ID]";

    //LOGIN SERVICE
    public static String LOGIN_SERVICE_ENDPOINT = BASE_URL + "user_login.php?email=" + EMAIL + "&password=" + PASSWORD;
    //            "login/dologin?username=" + USERNAME + "&password=" + PASSWORD;

    //REGISTRATION SERVICE
    public static String REGISTER_SERVICE_ENDPOINT = BASE_URL + "user_signup.php?username=" + USERNAME + "&phone_number=" + PHONE_NUMBER +
            "&email=" + EMAIL + "&password=" + PASSWORD;
//            "register/doregister?username=" + USERNAME + "&phone_number=" + PHONE_NUMBER +
//            "&email=" + EMAIL + "&password=" + PASSWORD;

    //USER DETAILS SERVICE
    public static String USER_DETAILS_SERVICE_ENDPOINT = BASE_URL + "user_details.php?userid=" + USER_ID;

    //FORGOT PASSWORD SERVICE
    public static String FORGOT_PWD_SERVICE_ENDPOINT = BASE_URL + "forgot_password.php?email=" + EMAIL;

}
