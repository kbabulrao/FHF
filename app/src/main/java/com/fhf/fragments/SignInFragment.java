package com.fhf.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.fhf.R;
import com.fhf.activities.SignInActivity;
import com.fhf.data.AppSessionData;
import com.fhf.data.User;
import com.fhf.interfaces.CommunicationListener;
import com.fhf.utils.Utils;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.plus.People.LoadPeopleResult;
import com.google.android.gms.plus.Plus;
import com.google.android.gms.plus.model.people.Person;
import com.google.android.gms.plus.model.people.PersonBuffer;

import org.json.JSONObject;

import java.io.InputStream;
import java.util.ArrayList;

import static android.app.Activity.RESULT_OK;

/**
 * Created by santosh on 2/11/2017.
 */

public class SignInFragment extends BaseFragment implements View.OnClickListener, OnConnectionFailedListener, ConnectionCallbacks, ResultCallback<LoadPeopleResult> {

    CommunicationListener listener;
    TextView tvSignIn, tvSignUp, tvForgotPwd;
    EditText edtEmail, edtPwd;
    Button btnFacebook, btnGoogle;
    LoginButton loginButton;
    ProgressDialog progressDialog;
    CallbackManager callbackManager;
    User user;

    GoogleApiClient google_api_client;
    GoogleApiAvailability google_api_availability;
    SignInButton signIn_btn;
    private static final int SIGN_IN_CODE = 0;
    private ConnectionResult connection_result;
    private boolean is_intent_inprogress;
    private boolean is_signInBtn_clicked;
    private int request_code;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (listener == null) {
            listener = (CommunicationListener) context;
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_signin, container, false);

        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        findViews(view);
        hideKeyBoard(edtEmail);
        return view;
    }

    void findViews(View view) {
        tvSignIn = (TextView) view.findViewById(R.id.tv_sign_in);
        tvSignUp = (TextView) view.findViewById(R.id.tv_sign_up);
        tvForgotPwd = (TextView) view.findViewById(R.id.tv_forgot_pwd);

        edtEmail = (EditText) view.findViewById(R.id.edt_mail);
        edtPwd = (EditText) view.findViewById(R.id.edt_password);

        btnFacebook = (Button) view.findViewById(R.id.btn_facebook);
        btnGoogle = (Button) view.findViewById(R.id.btn_google_plus);

        loginButton = (LoginButton) view.findViewById(R.id.login_button);

        customizeSignBtn(view);

        btnFacebook.setOnClickListener(this);
        btnGoogle.setOnClickListener(this);
        tvSignIn.setOnClickListener(this);
        tvSignUp.setOnClickListener(this);
        tvForgotPwd.setOnClickListener(this);

        edtEmail.setText("santu758.msb@gmail.com");
        edtPwd.setText("test1234");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_sign_in:
                validateUser();
                break;
            case R.id.tv_sign_up:
                listener.loadFragment(new SignUpFragment(), getString(R.string.sign_up));
                break;
            case R.id.tv_forgot_pwd:
                listener.loadFragment(new ForgotPasswordFragment(), getString(R.string.forgot_pwd));
                break;
            case R.id.btn_facebook:
                loginToFacebook();
                break;
            case R.id.btn_google_plus:
                break;
        }
    }

    void validateUser() {
        if (Utils.isValidEmail(edtEmail.getText().toString().trim()) && !TextUtils.isEmpty(edtPwd.getText().toString().trim())) {
            ((SignInActivity) getActivity()).callSignInWebService(edtEmail.getText().toString().trim(), edtPwd.getText().toString().trim());
        } else {
            Toast.makeText(getActivity(), "Please check your username/password", Toast.LENGTH_SHORT).show();
        }
    }

    void loginToFacebook() {
        callbackManager = CallbackManager.Factory.create();

        loginButton.setReadPermissions("public_profile", "email", "user_friends");

        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Loading...");
        progressDialog.show();

        loginButton.performClick();

        loginButton.setPressed(true);

        loginButton.invalidate();

        loginButton.registerCallback(callbackManager, mCallBack);

        loginButton.setPressed(false);

        loginButton.invalidate();

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);

        // Check which request we're responding to
        if (requestCode == SIGN_IN_CODE) {
            request_code = requestCode;
            if (resultCode != RESULT_OK) {
                is_signInBtn_clicked = false;
                progressDialog.dismiss();
            }

            is_intent_inprogress = false;

            if (!google_api_client.isConnecting()) {
                google_api_client.connect();
            }
        }

    }

    private FacebookCallback<LoginResult> mCallBack = new FacebookCallback<LoginResult>() {
        @Override
        public void onSuccess(LoginResult loginResult) {

            progressDialog.dismiss();

            // App code
            GraphRequest request = GraphRequest.newMeRequest(
                    loginResult.getAccessToken(),
                    new GraphRequest.GraphJSONObjectCallback() {
                        @Override
                        public void onCompleted(
                                JSONObject object,
                                GraphResponse response) {

                            Log.e("response: ", response + "");
                            try {
                                user = new User();
                                user.facebookID = object.getString("id").toString();
                                user.email = object.getString("email").toString();
                                user.name = object.getString("name").toString();
                                user.gender = object.getString("gender").toString();
                                AppSessionData.getSessionDataInstance().setCurrentUser(user, getActivity());

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            Toast.makeText(getActivity(), "welcome " + user.name, Toast.LENGTH_LONG).show();
                        }
                    });

            Bundle parameters = new Bundle();
            parameters.putString("fields", "id,name,email,gender, birthday");
            request.setParameters(parameters);
            request.executeAsync();
        }

        @Override
        public void onCancel() {
            progressDialog.dismiss();
        }

        @Override
        public void onError(FacebookException e) {
            progressDialog.dismiss();
        }
    };

    /*
    create and  initialize GoogleApiClient object to use Google Plus Api.
    While initializing the GoogleApiClient object, request the Plus.SCOPE_PLUS_LOGIN scope.
    */


    private void buildNewGoogleApiClient() {

        google_api_client = new GoogleApiClient.Builder(getActivity())
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(Plus.API, Plus.PlusOptions.builder().build())
                .addScope(Plus.SCOPE_PLUS_LOGIN)
                .addScope(Plus.SCOPE_PLUS_PROFILE)
                .build();
    }

    /*
      Customize sign-in button. The sign-in button can be displayed in
      multiple sizes and color schemes. It can also be contextually
      rendered based on the requested scopes. For example. a red button may
      be displayed when Google+ scopes are requested, but a white button
      may be displayed when only basic profile is requested. Try adding the
      Plus.SCOPE_PLUS_LOGIN scope to see the  difference.
    */

    private void customizeSignBtn(View view) {

        signIn_btn = (SignInButton) view.findViewById(R.id.sign_in_button);
        signIn_btn.setSize(SignInButton.SIZE_STANDARD);
        signIn_btn.setScopes(new Scope[]{Plus.SCOPE_PLUS_LOGIN});

    }

    public void onStart() {
        super.onStart();
//        google_api_client.connect();
    }

    public void onStop() {
        super.onStop();
/*
        if (google_api_client.isConnected()) {
            google_api_client.disconnect();
        }
*/
    }

    public void onResume() {
        super.onResume();
/*
        if (google_api_client.isConnected()) {
            google_api_client.connect();
        }
*/
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onConnectionFailed(ConnectionResult result) {
        if (!result.hasResolution()) {
            google_api_availability.getErrorDialog(getActivity(), result.getErrorCode(), request_code).show();
            return;
        }

        if (!is_intent_inprogress) {

            connection_result = result;

            if (is_signInBtn_clicked) {

                resolveSignInError();
            }
        }
    }

    @Override
    public void onConnected(Bundle arg0) {
        is_signInBtn_clicked = false;
        // Get user's information and set it into the layout
        getProfileInfo();

        // Update the UI after signin
//        changeUI(true);

    }

    @Override
    public void onConnectionSuspended(int arg0) {
        google_api_client.connect();
//        changeUI(false);
    }

        /*
      Sign-in into the Google + account
     */

    private void gPlusSignIn() {
        if (!google_api_client.isConnecting()) {
            Log.d("user connected", "connected");
            is_signInBtn_clicked = true;
            progressDialog.show();
            resolveSignInError();

        }
    }

    /*
     Revoking access from Google+ account
     */

    private void gPlusRevokeAccess() {
        if (google_api_client.isConnected()) {
            Plus.AccountApi.clearDefaultAccount(google_api_client);
            Plus.AccountApi.revokeAccessAndDisconnect(google_api_client)
                    .setResultCallback(new ResultCallback<Status>() {
                        @Override
                        public void onResult(Status arg0) {
                            Log.d("MainActivity", "User access revoked!");
                            buildNewGoogleApiClient();
                            google_api_client.connect();
//                            changeUI(false);
                        }

                    });
        }
    }


    /*
      Method to resolve any signin errors
     */

    private void resolveSignInError() {
        if (connection_result.hasResolution()) {
            try {
                is_intent_inprogress = true;
                connection_result.startResolutionForResult(getActivity(), SIGN_IN_CODE);
                Log.d("resolve error", "sign in error resolved");
            } catch (IntentSender.SendIntentException e) {
                is_intent_inprogress = false;
                google_api_client.connect();
            }
        }
    }

    /*
      Sign-out from Google+ account
     */

    private void gPlusSignOut() {
        if (google_api_client.isConnected()) {
            Plus.AccountApi.clearDefaultAccount(google_api_client);
            google_api_client.disconnect();
            google_api_client.connect();
//            changeUI(false);
        }
    }



    /*
     get user's information name, email, profile pic,Date of birth,tag line and about me
     */

    private void getProfileInfo() {

        try {

            if (Plus.PeopleApi.getCurrentPerson(google_api_client) != null) {
                Person currentPerson = Plus.PeopleApi.getCurrentPerson(google_api_client);
                setPersonalInfo(currentPerson);

            } else {
                Toast.makeText(getActivity(),
                        "No Personal info mention", Toast.LENGTH_LONG).show();

            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /*
     set the User information into the views defined in the layout
     */

    private void setPersonalInfo(Person currentPerson) {

        String personName = currentPerson.getDisplayName();
        String personPhotoUrl = currentPerson.getImage().getUrl();
        String email = Plus.AccountApi.getAccountName(google_api_client);
//        setProfilePic(personPhotoUrl);
        progressDialog.dismiss();
        Toast.makeText(getActivity(), "Person information is shown!", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onResult(LoadPeopleResult peopleData) {
        if (peopleData.getStatus().getStatusCode() == CommonStatusCodes.SUCCESS) {
            PersonBuffer personBuffer = peopleData.getPersonBuffer();
            ArrayList<String> list = new ArrayList<String>();
            ArrayList<String> img_list = new ArrayList<String>();
            try {
                int count = personBuffer.getCount();

                for (int i = 0; i < count; i++) {
                    list.add(personBuffer.get(i).getDisplayName());
                    img_list.add(personBuffer.get(i).getImage().getUrl());
                }
//                Intent intent = new Intent(getActivity(),FriendActivity.class);
//                intent.putStringArrayListExtra("friendsName",list);
//                intent.putStringArrayListExtra("friendsPic",img_list);
//                startActivity(intent);
            } finally {
                personBuffer.release();
            }
        } else {
            Log.e("circle error", "Error requesting visible circles: " + peopleData.getStatus());
        }
    }


   /*
    Perform background operation asynchronously, to load user profile picture with new dimensions from the modified url
    */

    private class LoadProfilePic extends AsyncTask<String, Void, Bitmap> {
        ImageView bitmap_img;

        public LoadProfilePic(ImageView bitmap_img) {
            this.bitmap_img = bitmap_img;
        }

        protected Bitmap doInBackground(String... urls) {
            String url = urls[0];
            Bitmap new_icon = null;
            try {
                InputStream in_stream = new java.net.URL(url).openStream();
                new_icon = BitmapFactory.decodeStream(in_stream);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return new_icon;
        }

        protected void onPostExecute(Bitmap result_img) {

            bitmap_img.setImageBitmap(result_img);
        }
    }

}

