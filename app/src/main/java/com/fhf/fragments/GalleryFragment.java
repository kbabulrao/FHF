package com.fhf.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.fhf.R;

/**
 * Created by santosh on 2/16/2017.
 */

public class GalleryFragment extends BaseFragment {

    TextView tvTitle;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        findViews(view);
        return view;
    }

    void findViews(View view) {
        tvTitle = (TextView) view.findViewById(R.id.tvHome);
        tvTitle.setText("Gallery");
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
