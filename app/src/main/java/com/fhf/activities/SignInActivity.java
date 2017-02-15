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

import com.fhf.R;
import com.fhf.constants.AppConstants;
import com.fhf.fragments.SignInFragment;
import com.fhf.interfaces.CommunicationListener;

/**
 * Created by santosh on 2/10/2017.
 */

public class SignInActivity extends BaseActivity implements CommunicationListener {

    SignInServiceResultReceiver signInServiceResultReceiver;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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

        }
    }

    private void registerReceiver() {
        if (signInServiceResultReceiver == null) {
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
}
