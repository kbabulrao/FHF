package com.fhf.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.fhf.R;
import com.fhf.activities.SignInActivity;
import com.fhf.utils.Utils;

/**
 * Created by santosh on 2/11/2017.
 */

public class ForgotPasswordFragment extends BaseFragment implements View.OnClickListener {

    EditText edtEmail;
    TextView tvSend;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_forgot_pwd, container, false);

        findViews(view);
        return view;
    }

    void findViews(View view) {
        edtEmail = (EditText) view.findViewById(R.id.edt_mail);
        tvSend = (TextView) view.findViewById(R.id.tv_send);

        tvSend.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_send:
                validateAndSend();
                break;
        }
    }

    void validateAndSend() {
        if (Utils.isValidEmail(edtEmail.getText().toString().trim())) {
            ((SignInActivity) getActivity()).callForgotPasswordWebService(edtEmail.getText().toString().trim());
        } else {
            Toast.makeText(getActivity(), "Please check your email", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}

