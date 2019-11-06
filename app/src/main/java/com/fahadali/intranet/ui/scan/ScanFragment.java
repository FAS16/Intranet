package com.fahadali.intranet.ui.scan;

import android.content.Intent;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ToggleButton;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.fahadali.intranet.R;
import com.fahadali.intranet.activities.AttendanceRegistrationActivity;

public class ScanFragment extends Fragment implements View.OnClickListener {

    private ScanViewModel scanViewModel;
    private static final String TAG = "ScanFragment";
    private NfcAdapter nfcAdapter;
    private ImageButton registrationButton;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        Log.d(TAG, "onCreateView: called...");
        scanViewModel = ViewModelProviders.of(this).get(ScanViewModel.class);
        View root = inflater.inflate(R.layout.fragment_scan, container, false);

        registrationButton = root.findViewById(R.id.registrationButton);
        registrationButton.setOnClickListener(this);



        return root;
    }


    @Override
    public void onClick(View v) {
        if(v == registrationButton) {
            Intent i = new Intent(getContext(), AttendanceRegistrationActivity.class);
            startActivity(i);
        }
    }
}