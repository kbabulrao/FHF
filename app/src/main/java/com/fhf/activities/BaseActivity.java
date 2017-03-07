package com.fhf.activities;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.fhf.R;

/**
 * Created by santosh on 2/11/2017.
 */

public class BaseActivity extends AppCompatActivity {

    protected Toolbar toolbar;
    public TextView mTitle;
    public ProgressDialog progressDlg;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        progressDlg = new ProgressDialog(this, R.style.MyTheme);
        progressDlg.setProgressStyle(android.R.style.Widget_ProgressBar_Small);
        progressDlg.setCancelable(false);
    }

    protected void loadFragment(Fragment destFragment, int id, String tag) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        try {
            if (fragmentManager != null) {
                fragmentManager.popBackStack();
                if (fragmentManager.beginTransaction() != null)
                    fragmentManager.beginTransaction().replace(id, destFragment, tag).commit();
            }
        } catch (Exception e) {
            e.printStackTrace();
            if (fragmentManager.beginTransaction() != null)
                fragmentManager.beginTransaction().replace(id, destFragment, tag).commit();
        }
    }

    protected void loadFragmentByAddingToBackStack(Fragment destFragment, int id, String tag) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        try {
            if (fragmentManager != null) {
                if (fragmentManager.beginTransaction() != null)
                    fragmentManager.beginTransaction().replace(id, destFragment).addToBackStack(null).commit();
            }
        } catch (Exception e) {
            e.printStackTrace();
            if (fragmentManager.beginTransaction() != null)
                fragmentManager.beginTransaction().replace(id, destFragment, tag).addToBackStack(null).commit();
        }
    }

}
