package com.hnweb.bookingmaxi;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.gson.JsonArray;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;


// created by priyanka kadam sonawane 9 dec,2019
public class LoginActivity extends AppCompatActivity {

    // facebook

    private CallbackManager callbackManager;
    private TextView textView;

    private AccessTokenTracker accessTokenTracker;
    private ProfileTracker profileTracker;

    //.....................................
    private static final String TAG = MainActivity.class.getSimpleName();
    private static final int RC_SIGN_IN = 007;

    private GoogleApiClient mGoogleApiClient;
    private ProgressDialog mProgressDialog;

    private SignInButton btnSignIn;
    private Button btnSignOut, btnRevokeAccess;
    private LinearLayout llProfileLayout;
    private ImageView imgProfilePic;
    private TextView txtName, txtEmail;


    LoginButton loginButton;
    Button btn_label_login,btn_label_register,btn_login,btn_register,btn_facebook,btn_google_plus;
    RelativeLayout layout_login,layout_register;

    EditText et_username,et_password_login;//Login
    EditText et_fullname,et_email,et_phoneno,et_password_register,et_confirm_password;
    String str_fullname,str_email,str_phoneno,str_password_register,str_confirm_password;
    String str_username,str_password_login;
    String id,full_name,email,password,address,phoneno,noti_status;
    Boolean CheckEditText;
    String message;
    int message_code;
    StringRequest stringRequest;
    RequestQueue requestQueue;
Activity activity;
    //google plus


    //.......end.....

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().hide();



        // ...........facebook...........

        FacebookSdk.sdkInitialize(getApplicationContext());

        callbackManager = CallbackManager.Factory.create();

        accessTokenTracker= new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken oldToken, AccessToken newToken) {

            }
        };

        profileTracker = new ProfileTracker() {
            @Override
            protected void onCurrentProfileChanged(Profile oldProfile, Profile newProfile) {
                displayMessage(newProfile);
            }
        };

        accessTokenTracker.startTracking();
        profileTracker.startTracking();

         loginButton = (LoginButton) findViewById(R.id.login_button);

        //Our custom Facebook button
        btn_facebook = (Button) findViewById(R.id.btn_facebook);


      // textView = (TextView) findViewById(R.id.txt);

        loginButton.setReadPermissions("user_friends");
        loginButton.registerCallback(callbackManager, callback);


        //...................................



        et_username=findViewById(R.id.et_username);
        et_password_login=findViewById(R.id.et_password);

        et_fullname=findViewById(R.id.et_fullname);
        et_email=findViewById(R.id.et_email);
        et_phoneno=findViewById(R.id.et_phonno);
        et_password_register=findViewById(R.id.et_passwor_reg);
        et_confirm_password=findViewById(R.id.et_confirmpass);



        //login
        str_username=et_username.getText().toString();
        str_password_login=et_username.getText().toString();

        //register


        btn_label_login=findViewById(R.id.btn_login_lable);
        btn_label_register=findViewById(R.id.btn_register_lable);

       btn_login=findViewById(R.id.btn_login);
        btn_register=findViewById(R.id.btn_register);

       layout_login=findViewById(R.id.layout_login);
        layout_register=findViewById(R.id.layout_register);



        btn_label_login.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {

               layout_login.setVisibility(View.VISIBLE);
               layout_register.setVisibility(View.GONE);
           }
       });


        btn_label_register.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {


               layout_login.setVisibility(View.GONE);
               layout_register.setVisibility(View.VISIBLE);
           }
       });

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Login_api();

            }
        });


        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Registration_api();
            }
        });

        getHashkey();//for facebook sha1 key




    }


    public void Login_api(){

        str_username=et_username.getText().toString();
        str_password_login=et_password_login.getText().toString();


        if (TextUtils.isEmpty(str_username)) {
            et_username.setError("Please enter username");
            et_username.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(str_password_login)) {
            et_password_login.setError("Enter a password");
            et_password_login.requestFocus();
            return;
        }
        // Creating string request with post method.
        stringRequest = new StringRequest(Request.Method.POST, AppConstant.LOGIN_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String ServerResponse) {

                        Log.e("Login responce>>",ServerResponse);

                        try {

                            JSONObject obj = new JSONObject(ServerResponse);
                            message_code = obj.getInt("message_code");
                            message = obj.getString("message");

                            if (message_code == 1) {

                                JSONArray jsonArray = obj.getJSONArray("details");
                                for(int i=0;i<jsonArray.length();i++)
                                {
                                    /*"id": "10",
                                        "full_name": "Priyanka",
                                        "email": "pk@gmail.com",
                                        "phone": "9881162718",
                                        "password": "1234567",
                                        "address": null,
                                        "notification_receive": "Yes",
                                        "created_at": "2019-12-18 04:14:42",
                                        "updated_at": "2019-12-18 04:14:42"
                                    */
                                    JSONObject data = jsonArray.getJSONObject(i);
                                    id=data.getString("id");
                                    full_name=data.getString("full_name");
                                    email=data.getString("email");
                                    phoneno=data.getString("phone");
                                    address=data.getString("address");
                                    password=data.getString("password");
                                    noti_status=data.getString("notification_receive");

                                    AppData.id = id;
                                    AppData.KEY_FULLNAME = full_name;
                                    AppData.KEY_EMAIL = email;
                                    AppData.KEY_ADDRESS = address;
                                    AppData.KEY_PHONENO = phoneno;
                                    AppData.KEY_PASSWORD = password;
                                    AppData.KEY_NOTIFICATION_STATUS = noti_status;

                                }

                                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();

                                startActivity(new Intent(getApplicationContext(), HomeActivity.class));


                            } else {
                                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();

                            }

                        } catch (JSONException e) {
                            e.printStackTrace();

                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {

                        Log.e("login volley error>>",""+volleyError);

                        Toast.makeText(LoginActivity.this, ""+volleyError, Toast.LENGTH_SHORT).show();

                    }
                }) {
            @Override
            protected Map<String, String> getParams() {

                // Creating Map String Params.
                Map<String, String> params = new HashMap<String, String>();

                params.put("email", str_username);
                params.put("password", str_password_login);

                Log.e("param>>",params.toString());

                return params;
            }

        };

        requestQueue = Volley.newRequestQueue(LoginActivity.this);
        requestQueue.add(stringRequest);
    }

//.....................reg.....................................

    public void Registration_api() {

        str_password_register = et_password_register.getText().toString();
        str_confirm_password = et_confirm_password.getText().toString();
        str_email = et_email.getText().toString();
        str_phoneno = et_phoneno.getText().toString();
        str_fullname = et_fullname.getText().toString();


        if (TextUtils.isEmpty(str_fullname)) {
            et_fullname.setError("Please enter Full name");
            et_fullname.requestFocus();
            CheckEditText = false;
        }


        if (TextUtils.isEmpty(str_phoneno)) {
            et_phoneno.setError("Enter a phone number");
            et_phoneno.requestFocus();
            CheckEditText = false;
        }
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(str_email).matches()) {
            et_email.setError("Enter a valid email");
            et_email.requestFocus();
            CheckEditText = false;

        }
        if (TextUtils.isEmpty(str_password_register)) {
            et_password_register.setError("Enter a password");
            et_password_register.requestFocus();
            CheckEditText = false;
        } else {
            Log.e("PasswordMatch", str_password_register + " :: " + str_confirm_password);
            if (TextUtils.isEmpty(str_confirm_password)) {
                et_confirm_password.setError("Enter a Confirm password");
                et_confirm_password.requestFocus();
                CheckEditText = false;
            }else {
                if (!str_password_register.matches(str_confirm_password)) {
                    Toast.makeText(getApplicationContext(), "Password Not matching", Toast.LENGTH_SHORT).show();
                    CheckEditText = false;
                } else {
                    CheckEditText = false;

            // Creating string request with post method.
            stringRequest = new StringRequest(Request.Method.POST, AppConstant.REGISTER_URL,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String ServerResponse) {

                            Log.e("Registration responce>>", ServerResponse);

                            try {

                                JSONObject obj = new JSONObject(ServerResponse);
                                message_code = obj.getInt("message_code");
                                message = obj.getString("message");

                                if (message_code == 1) {

                                    Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();

                                    startActivity(new Intent(getApplicationContext(), LoginActivity.class));


                                } else {
                                    Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();

                                }

                            } catch (JSONException e) {
                                e.printStackTrace();

                            }

                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError volleyError) {

                            Log.e("volley reg error>>", "" + volleyError);

                            Toast.makeText(LoginActivity.this, "" + volleyError, Toast.LENGTH_SHORT).show();

                        }
                    }) {
                @Override
                protected Map<String, String> getParams() {

                    // Creating Map String Params.
                    Map<String, String> params = new HashMap<String, String>();

                    params.put("full_name", str_fullname);
                    params.put("email", str_email);
                    params.put("phone", str_phoneno);
                    params.put("password", str_password_register);


                    Log.e("param>>", params.toString());

                    return params;
                }

            };

            requestQueue = Volley.newRequestQueue(LoginActivity.this);
            requestQueue.add(stringRequest);
        }
            }
        }
    }



    //For facebook integration >> hash key

    public void getHashkey(){
        try {
            PackageInfo info = getPackageManager().getPackageInfo(getApplicationContext().getPackageName(), PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());

                Log.e("Base64", Base64.encodeToString(md.digest(),Base64.NO_WRAP));
            }
        } catch (PackageManager.NameNotFoundException e) {
            Log.d("Name not found", e.getMessage(), e);

        } catch (NoSuchAlgorithmException e) {
            Log.d("Error", e.getMessage(), e);
        }
    }

    //.................................




    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);


    }

    private void displayMessage(Profile profile){
        if(profile != null){
           // textView.setText(profile.getName());
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        accessTokenTracker.stopTracking();
        profileTracker.stopTracking();
    }

    @Override
    public void onResume() {
        super.onResume();
        Profile profile = Profile.getCurrentProfile();
        displayMessage(profile);
    }

    private FacebookCallback<LoginResult> callback = new FacebookCallback<LoginResult>() {
        @Override
        public void onSuccess(LoginResult loginResult) {
            AccessToken accessToken = loginResult.getAccessToken();
            Profile profile = Profile.getCurrentProfile();
            displayMessage(profile);
        }

        @Override
        public void onCancel() {

        }

        @Override
        public void onError(FacebookException e) {

        }
    };

    public void onClickFacebookButton(View view) {

        if (view == btn_facebook) {
            loginButton.performClick();
        }
    }

   /* public void onClickgooglrplusButton(View view) {


        if (view == btn_google_plus) {
            btnSignIn.performClick();
        }
    }*/

//................................................................





}
