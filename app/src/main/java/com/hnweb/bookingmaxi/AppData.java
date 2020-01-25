package com.hnweb.bookingmaxi;

import android.app.Application;
import android.content.SharedPreferences;

public class AppData extends Application {

    public static String id = null;
    public static  String KEY_ID = null;
    public static  String KEY_FULLNAME = null;
    public static  String KEY_EMAIL = null;
    public static  String KEY_PASSWORD = null;
    public static  String KEY_PHONENO = null;
    public static  String KEY_ADDRESS = null;
    public static  String KEY_PROFILE_PIC =null;
    public static  String KEY_NOTIFICATION_STATUS =null;
    public static SharedPreferences myPref;

    @Override
    public void onCreate() {
        super.onCreate();
        myPref = getSharedPreferences("BookMaxi",MODE_PRIVATE);

    }
}
