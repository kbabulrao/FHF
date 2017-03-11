package com.fhf.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.fhf.R;
import com.fhf.utils.Utils;

/**
 * Created by santosh on 2/16/2017.
 */

public class ContactUsFragment extends BaseFragment {

    EditText edtName, edtMail, edtMessage;
    TextView tvSend;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_contact_us, container, false);

        findViews(view);
        return view;
    }

    void findViews(View view) {
        edtName = (EditText) view.findViewById(R.id.edt_name);
        edtMail = (EditText) view.findViewById(R.id.edt_mail);
        edtMessage = (EditText) view.findViewById(R.id.edt_message);
        tvSend = (TextView) view.findViewById(R.id.tv_send);
        tvSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateFields();
            }
        });
    }

    void validateFields() {
        if (!TextUtils.isEmpty(edtName.getText().toString().trim())) {
            if (!TextUtils.isEmpty(edtMail.getText().toString().trim())) {
                if (Utils.isValidEmail(edtMail.getText().toString().trim())) {
                    if (!TextUtils.isEmpty(edtMessage.getText().toString().trim())) {
                        sendMail(edtName.getText().toString().trim(), edtMessage.getText().toString().trim(), edtMail.getText().toString().trim());
                    } else {
                        Toast.makeText(getActivity(), "Please enter message", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getActivity(), "Please enter valid email", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(getActivity(), "Please enter mail", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(getActivity(), "Please enter name", Toast.LENGTH_SHORT).show();
        }
    }

    void sendMail(String name, String message, String mail) {
        Intent email = new Intent(Intent.ACTION_SEND);
        email.putExtra(Intent.EXTRA_EMAIL, new String[]{"friendshelpingfriends@hotmail.com"});
        email.putExtra(Intent.EXTRA_SUBJECT, "REG:FHF User");
        email.putExtra(Intent.EXTRA_TEXT, "Name: " + name + "\nMessage: " + message + "\nMail: " + mail);
        //need this to prompts email client only.
        email.setType("message/rfc822");
        startActivity(email);
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
