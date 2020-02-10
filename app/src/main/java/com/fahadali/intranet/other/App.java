package com.fahadali.intranet.other;


import android.app.Activity;
import android.app.Application;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import android.util.Log;
import android.widget.Toast;

import com.fahadali.intranet.activities.LoginActivity;
import com.fahadali.intranet.activities.Main2Activity;
import com.fahadali.intranet.clients.UserClient;
import com.fahadali.intranet.model.Student;
import com.fahadali.intranet.model.Token;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class App extends Application {

    private static final String TAG = "App";
    public static final String SHARED_PREFS = "sharedPrefs";
    public static ConnectivityManager connectivityManager;
    public static NetworkStatus network;
    public static App   instance;
    private OkHttpClient okHttpClient;
    private UserClient userClient;



    @Override
    public void onCreate() {
        super.onCreate();
        Log.i(TAG, "onCreate: Called!");
        instance = this;

        connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        network = new NetworkStatus();
        registerReceiver(network, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
        network.onReceive(this, null);

        initHttpClient();
    }


    private void initHttpClient() {
        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
        httpLoggingInterceptor.level(HttpLoggingInterceptor.Level.BODY);

        okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(httpLoggingInterceptor)
                .build();


        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://attendingengine20191213102651.azurewebsites.net/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build();

        userClient = retrofit.create(UserClient.class);

    }

    public static boolean isOnline() {
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }

    public static String getTodaysDate() {
        SimpleDateFormat format = new SimpleDateFormat("EEE dd-MM-yyyy");
        format.setTimeZone(TimeZone.getTimeZone("GMT+1"));
        Date date = new Date();
        String dateString = format.format(date);

        return dateString;
    }


    public static String formatDate(Date date) {


        SimpleDateFormat format = new SimpleDateFormat("EEE HH:mm:ss");
        format.setTimeZone(TimeZone.getTimeZone("GMT+1"));
        String newDateString = format.format(date);

        return newDateString;
    }

    public static String formatDateString(String dateString) {
        Date date = null;
        try {
            date = new SimpleDateFormat("yyyy-MM-ddTHH:mm:ss").parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        SimpleDateFormat format = new SimpleDateFormat("EEE dd-MM-yyyy HH:mm:ss");
        format.setTimeZone(TimeZone.getTimeZone("GMT+1"));
        String newDateString = format.format(date);

        return newDateString;
    }


    public static String getTodaysDate(String formatString) {
//        SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss", Locale.ENGLISH);

        SimpleDateFormat format = new SimpleDateFormat(formatString, Locale.ENGLISH);
        format.setTimeZone(TimeZone.getTimeZone("GMT+1"));
        String dateString = format.format(new Date());
        return dateString;


    }

    public boolean isUserSignedIn() {

        Token token = getTokenFromSharedPrefs();
        if(token.getAccessToken().isEmpty()){
            return false;
        }
        Date expiryDate = null;
        Date today = new Date();

        SimpleDateFormat formatter = new SimpleDateFormat("dd MMM yyyy", Locale.ENGLISH);
        formatter.setTimeZone(TimeZone.getTimeZone("GMT+1"));

        String dateInString = token.getExpires().substring(5,16);
        try {
            expiryDate = formatter.parse(dateInString);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if(!token.getAccessToken().isEmpty() && expiryDate != null) {
            // We have a token saved locally
            if(today.after(expiryDate)) {
                // Token is expired
                return false;
            }
        }

        return true;
    }

    public void removeTokenFromSharedPreds() {
        SharedPreferences sp = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);

        SharedPreferences sharedPreferences = getSharedPreferences(App.SHARED_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.remove(Token.ACCESS_TOKEN);
        editor.remove(Token.TOKEN_TYPE);
        editor.remove(Token.EXPIRES_IN);
        editor.remove(Token.USERNAME);
        editor.remove(Token.EXPIRES);
        editor.apply();

    }

    public Token getTokenFromSharedPrefs() {

        SharedPreferences sp = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        Token token = new Token(
                sp.getString(Token.ACCESS_TOKEN, ""),
                sp.getString(Token.TOKEN_TYPE, ""),
                sp.getString(Token.EXPIRES_IN, ""),
                sp.getString(Token.USERNAME, ""),
                sp.getString(Token.EXPIRES, "")
        );

        return token;
    }

    public static void shortToast(Context context, String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }

    public static void longToast(Context context, String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
    }

    public static int getDayOfWeek() {
        Date now = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(now);
        int dayOfWeek = (calendar.get(Calendar.DAY_OF_WEEK))-1;

        return dayOfWeek;
    }

    public UserClient getUserClient() {
        return userClient;
    }

    public void signOut(Context currentContext) {

        App.instance.removeTokenFromSharedPreds();
        Student.getInstance().nullify();
        Intent intent = new Intent(currentContext, LoginActivity.class);
        ((Activity)currentContext).startActivity(intent);
        ((Activity)currentContext).finish();

    }
}
