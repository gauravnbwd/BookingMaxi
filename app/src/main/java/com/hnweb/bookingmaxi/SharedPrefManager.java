package com.hnweb.bookingmaxi;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.hnweb.bookingmaxi.Pojo.User;


/**
 * Created by priyanka sonawane 18 dec 2019
 */

public class SharedPrefManager {

    public static final String KEY_ID = "id";
    public static final String KEY_FULLNAME = "full_name";
    public static final String KEY_EMAIL = "email";
    private static final String KEY_PASSWORD = "password";
    public static final String KEY_PHONENO = "phone";
    public static final String KEY_ADDRESS = "address";
    public static final String KEY_PROFILE_PIC = "img";
    public static final String SHARED_PREF_NAME = "BookmaxPref";


    private static SharedPrefManager mInstance;
    private static Context mCtx;

    private SharedPrefManager(Context context) {
        mCtx = context;
    }

    public static synchronized SharedPrefManager getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new SharedPrefManager(context);
        }
        return mInstance;
    }


    public void userLogin(User user) {

        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_ID, user.getId());
        editor.putString(KEY_FULLNAME, user.getFullname());
        editor.putString(KEY_EMAIL, user.getEmail());
        editor.putString(KEY_PHONENO, user.getPhonno());
        editor.putString(KEY_PASSWORD, user.getPassword());
        editor.putString(KEY_ADDRESS, user.getPassword());
        editor.apply();

    }

    //this method will checker whether user is already logged in or not
    public boolean isLoggedIn() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_ID, null) != null;
    }

    //this method will give the logged in user
   /* public User getUser() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return new User(
                sharedPreferences.getString(KEY_ID, null),
                sharedPreferences.getString(KEY_FULLNAME, null),
                sharedPreferences.getString(KEY_EMAIL, null),
                sharedPreferences.getString(KEY_PHONENO, null),
                sharedPreferences.getString(KEY_PASSWORD, null),
                sharedPreferences.getString(KEY_ADDRESS, null),
                sharedPreferences.getString(KEY_PROFILE_PIC, null)

        );
    }*/

    //this method will logout the user
    public void logout() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
        mCtx.startActivity(new Intent(mCtx, LoginActivity.class));
    }


}
