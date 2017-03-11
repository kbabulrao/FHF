package com.fhf.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;

import com.fhf.R;
import com.fhf.data.AppSessionData;

/**
 * Created by santosh on 3/11/2017.
 */

public class SplashActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        /* New Handler to start the Menu-Activity
         * and close this Splash-Screen after some seconds.*/
        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
                /* Create an Intent that will start the Menu-Activity. */
                if (AppSessionData.getSessionDataInstance().getUseRemember()) {
                    startActivity(MainActivity.class);
                } else {
                    startActivity(SignInActivity.class);
                }

            }
        }, 3000);

        /*new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                if (AppSessionData.getSessionDataInstance().getUseRemember()) {
                    startActivity(MainActivity.class);
                } else {
                    startActivity(SignInActivity.class);
                }

            }
        }).run();*/
    }

    private void startActivity(Class cls) {
        Intent intent = new Intent(SplashActivity.this, cls);
        startActivity(intent);
        finish();
    }
}
