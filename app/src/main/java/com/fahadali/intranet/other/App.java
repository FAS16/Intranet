package com.fahadali.intranet.other;

import android.app.Application;

import java.text.SimpleDateFormat;
import java.util.Date;

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
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
