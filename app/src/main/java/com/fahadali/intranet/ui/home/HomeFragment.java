package com.fahadali.intranet.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.fahadali.intranet.R;
import com.fahadali.intranet.activities.AttendanceRegistrationActivity;
import com.google.gson.Gson;

import retrofit2.Retrofit;

public class HomeFragment extends Fragment implements View.OnClickListener {

    private HomeViewModel homeViewModel;
    private static final String TAG = "HomeFragment";
    private Button registrationButton, sendRequset;
    private TextView retrofitTextView, header;
    private String url = "http://www.mocky.io/v2/5de91ca031000063006b173f";
    private Gson gson;
    private Retrofit retrofit;



    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        homeViewModel = ViewModelProviders.of(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_scan, container, false);
        Log.d(TAG, "onCreateView: called...");






//        registrationButton = root.findViewById(R.id.registrationButton);
//        retrofitTextView = root.findViewById(R.id.okHttp);
//        registrationButton.setOnClickListener(this);
//        retrofitTextView.setOnClickListener(this);
//        sendRequset = root.findViewById(R.id.send_request);
//        sendRequset.setOnClickListener(this);

            return root;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }

    @Override
    public void onClick(View v) {
//        if(v == registrationButton) {
//            Intent i = new Intent(getContext(), AttendanceRegistrationActivity.class);
//            startActivity(i);
//        }
//
//        else if( v == sendRequset) {
//
//
//        }
    }
}