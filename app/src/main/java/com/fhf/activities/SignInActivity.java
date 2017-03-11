package com.fhf.activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.fhf.R;
import com.fhf.constants.AppConstants;
import com.fhf.data.AppSessionData;
import com.fhf.fragments.SignInFragment;
import com.fhf.interfaces.CommunicationListener;
import com.fhf.services.SignInService;
import com.fhf.utils.Utils;

/**
 * Created by santosh on 2/10/2017.
 */

public class SignInActivity extends BaseActivity implements CommunicationListener {

    SignInServiceResultReceiver signInServiceResultReceiver;
    ForgotPasswordResultReceiver forgotPasswordResultReceiver;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        mTitle = (TextView) findViewById(R.id.toolbar_title);
        mTitle.setText(getString(R.string.sign_in));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getSupportFragmentManager().popBackStack();
                fragmentHandle();
            }
        });


        loadFragment(new SignInFragment(), R.id.container_body, getString(R.string.sign_in));
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public void loadFragment(Fragment fragment, String tag) {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mTitle.setText(tag);
        loadFragmentByAddingToBackStack(fragment, R.id.container_body, tag);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        fragmentHandle();
    }

    void fragmentHandle() {
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        mTitle.setText(R.string.sign_in);
    }

    private class SignInServiceResultReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            unRegisterSignInServiceResultReceiver();
            if (!isFinishing()) {
                if (progressDlg != null && progressDlg.isShowing()) {
                    progressDlg.dismiss();
                }
                if (intent.getBooleanExtra(AppConstants.SUCCESS_TEXT, false)) {
                    if (intent.getStringExtra("error_msg") == null) {
                        redirectToHomeActivity();
                    } else {
                        Utils.showSnackBarWithoutAction(SignInActivity.this, toolbar, intent.getStringExtra("error_msg"));
                        AppSessionData.getSessionDataInstance().setUserRemember(false);
                    }
                } else {
                    AppSessionData.getSessionDataInstance().setUserRemember(false);
                    VolleyError volleyError = new VolleyError(intent.getStringExtra(AppConstants.ERROR_TEXT));
                    Utils.showSnackBarWithoutAction(SignInActivity.this, toolbar, volleyError);
//                Toast.makeText(SignInActivity.this, intent.getStringExtra(AppConstants.ERROR_TEXT), Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    void redirectToHomeActivity() {
        Intent homeActivity = new Intent(this, MainActivity.class);
        startActivity(homeActivity);
        finish();
    }

    private void registerSignInReceiver() {
        if (signInServiceResultReceiver == null) {
            progressDlg.show();
            signInServiceResultReceiver = new SignInServiceResultReceiver();
            IntentFilter intentFilter = new IntentFilter(AppConstants.LOGIN_SERVICE);
            LocalBroadcastManager.getInstance(this).registerReceiver(signInServiceResultReceiver, intentFilter);
        }
    }

    private void unRegisterSignInServiceResultReceiver() {
        if (signInServiceResultReceiver != null) {
            LocalBroadcastManager.getInstance(this).unregisterReceiver(signInServiceResultReceiver);
            signInServiceResultReceiver = null;
        }
    }

    public void callSignInWebService(String username, String password) {
        registerSignInReceiver();
        SignInService signInService = new SignInService(this);
        signInService.validateUser(username, password);
    }

    public void callSignUpWebService(String username, String phone, String email, String password) {
        registerSignInReceiver();
        SignInService signInService = new SignInService(this);
        signInService.registerUser(username, phone, email, password);
    }

    public void callForgotPasswordWebService(String email) {
        registerForgotPasswordReceiver();
        SignInService signInService = new SignInService(this);
        signInService.recoveryPassword(email);
    }

    private void registerForgotPasswordReceiver() {
        if (signInServiceResultReceiver == null) {
            progressDlg.show();
            forgotPasswordResultReceiver = new ForgotPasswordResultReceiver();
            IntentFilter intentFilter = new IntentFilter(AppConstants.LOGIN_SERVICE);
            LocalBroadcastManager.getInstance(this).registerReceiver(forgotPasswordResultReceiver, intentFilter);
        }
    }

    private void unRegisterForgotPasswordServiceResultReceiver() {
        if (forgotPasswordResultReceiver != null) {
            LocalBroadcastManager.getInstance(this).unregisterReceiver(forgotPasswordResultReceiver);
            forgotPasswordResultReceiver = null;
        }
    }

    private class ForgotPasswordResultReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            unRegisterForgotPasswordServiceResultReceiver();
            if (!isFinishing()) {
                if (progressDlg != null && progressDlg.isShowing()) {
                    progressDlg.dismiss();
                }
                if (intent.getBooleanExtra(AppConstants.SUCCESS_TEXT, false)) {
                    if (intent.getStringExtra("msg").equalsIgnoreCase("reset password link sent to mail")) {
                        getSupportFragmentManager().popBackStack();
                        fragmentHandle();
                    } else {
                        Utils.showSnackBarWithoutAction(SignInActivity.this, toolbar, intent.getStringExtra("msg"));
                    }
                } else {
                    VolleyError volleyError = new VolleyError(intent.getStringExtra(AppConstants.ERROR_TEXT));
                    Utils.showSnackBarWithoutAction(SignInActivity.this, toolbar, volleyError);
//                Toast.makeText(SignInActivity.this, intent.getStringExtra(AppConstants.ERROR_TEXT), Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
}
