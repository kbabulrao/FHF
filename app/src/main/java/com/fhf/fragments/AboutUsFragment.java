package com.fhf.fragments;

import android.content.Context;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.fhf.R;

/**
 * Created by santosh on 2/16/2017.
 */

public class AboutUsFragment extends BaseFragment {

    TextView tvAbout;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_about_us, container, false);

        findViews(view);
        return view;
    }

    void findViews(View view) {
        tvAbout = (TextView) view.findViewById(R.id.tv_about_us);
        tvAbout.setMovementMethod(new ScrollingMovementMethod());
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }
}
