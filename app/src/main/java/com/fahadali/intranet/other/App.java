package com.fahadali.intranet.other;

import android.app.Application;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Date;

import retrofit2.Retrofit;

public class App extends Application {


    public static Retrofit retrofit;
    private static final String TAG = "App";
    @Override
    public void onCreate() {
        super.onCreate();
        Log.i(TAG, "onCreate: called!");


    }


    public static String getTodaysDate() {
        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
        String dateString = format.format(new Date());
        return dateString;
    }

    private String getTodaysDate(String formatString) {
        SimpleDateFormat format = new SimpleDateFormat(formatString);
        String dateString = format.format(new Date());
        return dateString;
    }
}
