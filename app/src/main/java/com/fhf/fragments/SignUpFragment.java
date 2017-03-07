package com.fhf.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.fhf.R;
import com.fhf.activities.SignInActivity;
import com.fhf.interfaces.CommunicationListener;
import com.fhf.utils.Utils;

/**
 * Created by santosh on 3/7/2017.
 */

public class SignUpFragment extends BaseFragment implements View.OnClickListener {

    CommunicationListener listener;
    TextView tvSignUp;
    EditText edtUsername, edtPhone, edtEmail, edtPwd, edtCnfPwd;
    CheckBox checkBoxTerms;
    ProgressDialog progressDialog;
    CallbackManager callbackManager;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (listener == null) {
            listener = (CommunicationListener) context;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_signup, container, false);

        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        findViews(view);
        hideKeyBoard(edtEmail);
        return view;
    }

    void findViews(View view) {
        tvSignUp = (TextView) view.findViewById(R.id.tv_sign_up);

        edtUsername = (EditText) view.findViewById(R.id.edt_user_name);
        edtPhone = (EditText) view.findViewById(R.id.edt_phone_number);
        edtEmail = (EditText) view.findViewById(R.id.edt_mail);
        edtPwd = (EditText) view.findViewById(R.id.edt_password);
        edtCnfPwd = (EditText) view.findViewById(R.id.edt_cnf_password);

        checkBoxTerms = (CheckBox) view.findViewById(R.id.cb_agree_terms);
        tvSignUp.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_sign_up:
                validateUserDetails();
                break;
        }
    }

    void validateUserDetails() {
        if (!TextUtils.isEmpty(edtUsername.getText().toString().trim())) {
            if (!TextUtils.isEmpty(edtEmail.getText().toString().trim())) {
                if (Utils.isValidEmail(edtEmail.getText().toString().trim())) {
                    if (!TextUtils.isEmpty(edtPhone.getText().toString().trim())) {
                        if (!TextUtils.isEmpty(edtPwd.getText().toString().trim())) {
                            if (edtPwd.getText().toString().trim().equals(edtCnfPwd.getText().toString().trim())) {
                                ((SignInActivity) getActivity()).callSignUpWebService(edtUsername.getText().toString().trim(),
                                        edtPhone.getText().toString().trim(), edtEmail.getText().toString().trim(),
                                        edtPwd.getText().toString().trim());
                            } else {
                                Toast.makeText(getActivity(), "Password and confirm password should be same", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(getActivity(), "Please enter password", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(getActivity(), "Please enter phone number", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getActivity(), "Please enter valid email", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(getActivity(), "Please enter email", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(getActivity(), "Please enter username", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}
