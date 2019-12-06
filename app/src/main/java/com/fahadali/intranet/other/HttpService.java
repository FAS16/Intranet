package com.fahadali.intranet.other;

import android.util.Log;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class HttpService {

    private OkHttpClient client;
    private Request request;
    private String response;
    private static final String TAG = "HttpService";

    public String getRequest(String url) {

        client = new OkHttpClient();

        request = new Request.Builder()
                .url(url)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {

                Log.e(TAG, "onFailure: exception = " + e.getMessage());


            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response res) throws IOException {

                HttpService.this.response = res.body().string();
                Log.i(TAG, "onResponse: response = " + response);
            }
        });

        return response;
    }

}


