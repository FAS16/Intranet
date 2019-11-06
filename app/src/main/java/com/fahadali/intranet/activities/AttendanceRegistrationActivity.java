package com.fahadali.intranet.activities;
import androidx.appcompat.app.AppCompatActivity;

import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.nfc.tech.NdefFormatable;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.fahadali.intranet.R;
import com.github.ybq.android.spinkit.SpinKitView;

import java.io.UnsupportedEncodingException;

public class AttendanceRegistrationActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "AttendanceRegActivity";

//    private ToggleButton readWriteButton;
    private SpinKitView spinKit;
    private ImageView checkMark;
    private NfcAdapter nfcAdapter;
    private TextView tagContent;
    private TextView readNotification;
    private TextView readChipText;
    private Button closeButton;
    private boolean correctClassRoom;
    private boolean read;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_att_reg);
        Log.d(TAG, "onCreate: called...");



        // Back button
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("");
        //Set color of Action Bar
//        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#FFFFFF")));


        //readWriteButton = findViewById(R.id.readWriteButton);
//        readWriteButton.setOnClickListener(this);
        tagContent = findViewById(R.id.tagContentTextView);
        spinKit = findViewById(R.id.spin_kit);
        checkMark = findViewById(R.id.checkMark);
        readNotification = findViewById(R.id.readNotification);
        readChipText = findViewById(R.id.readChipText);
        closeButton = findViewById(R.id.closeDialog);
        closeButton.setOnClickListener(this);



        nfcAdapter = NfcAdapter.getDefaultAdapter(this);
        checkNfcAvailablity(nfcAdapter);


    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    private void checkNfcAvailablity(NfcAdapter nfcAdapter) {

        if (nfcAdapter != null && nfcAdapter.isEnabled()) {
            Log.i(TAG, "checkNfcAvailablity: NFC is available!");
        } else {
            finish();
        }

    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Log.d(TAG, "onNewIntent: NFC intent recieved!");

        if(!read && !correctClassRoom) {



        if (intent.hasExtra(NfcAdapter.EXTRA_TAG)) {
            Log.i(TAG, "onNewIntent: Intent has EXTRA_TAG!");
            /*if (readWriteButton.isChecked()) {*/

                //READ
                Parcelable[] parcelables = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
                if (parcelables != null && parcelables.length > 0) {

                    readTextFromTag((NdefMessage) parcelables[0]);
                    read = true;


                } else {
                    Log.i(TAG, "onNewIntent: No NDEF messages found...");
                }

            /*} else {

                //WRITE
                Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
                NdefMessage ndefMessage = createNdefMessage(tagContent.getText().toString());
                writeNdefMessage(tag, ndefMessage);

            }*/


        }
        }
    }

    private void readTextFromTag(NdefMessage ndefMessage) {

        NdefRecord[] ndefRecords = ndefMessage.getRecords();

        if (ndefRecords != null && ndefRecords.length > 0) {

            NdefRecord ndefRecord = ndefRecords[0];

            String content = getTextFromNdefRecord(ndefRecord);

            this.tagContent.setText(content);

        } else {
            Log.i(TAG, "readTextFromTag: No NDEF records found...");
        }

    }

    @Override
    protected void onResume() {
        super.onResume();

        enableForegroundDispatchSystem();


    }

    @Override
    protected void onPause() {
        super.onPause();

        disableForegroundDispatchSystem();
    }

    private void enableForegroundDispatchSystem() {

        Intent intent = new Intent(this, AttendanceRegistrationActivity.class);
        intent.addFlags(Intent.FLAG_RECEIVER_REPLACE_PENDING);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);
        IntentFilter[] intentFilters = new IntentFilter[]{};
        nfcAdapter.enableForegroundDispatch(this, pendingIntent, intentFilters, null);

    }

    private void disableForegroundDispatchSystem() {

        nfcAdapter.disableForegroundDispatch(this);
    }

    private void formatTag(Tag tag, NdefMessage ndefMessage) {

        try {

            NdefFormatable ndefFormatable = NdefFormatable.get(tag);

            if (ndefFormatable == null) {
                Log.i(TAG, "formatTag: Tag is not NDEF formatable!");
            }
            ndefFormatable.connect();
            ndefFormatable.format(ndefMessage);
            ndefFormatable.close();
            Log.i(TAG, "formatTag: SUCCESS: Tag formatted!");

        } catch (Exception e) {
            Log.e(TAG, "formatTag: "+e.getMessage());

        }

    }

    private void writeNdefMessage(Tag tag, NdefMessage ndefMessage) {
        try {

            if (tag == null) {
                Log.i(TAG, "writeNdefMessage: Tag object cannot be null");
                return;
            }

            Ndef ndef = Ndef.get(tag);
            if (ndef == null) {
                //Tag is not NDEF formatted, so format..
                formatTag(tag, ndefMessage);
            } else {
                //Tag is NDEF formatted, so let's connect to it!
                ndef.connect();

                if (!ndef.isWritable()) {
                    Log.i(TAG, "writeNdefMessage: Tag is not writable!");
                    ndef.close();
                    return;

                }
                ndef.writeNdefMessage(ndefMessage);
                ndef.close();
                Log.i(TAG, "writeNdefMessage: SUCCESS: Tag written. Content: " + ndefMessage.getRecords().toString());
            }

        } catch (Exception e) {
            Log.e(TAG, "writeNdefMessage: "+e.getMessage());
        }
    }

    private NdefMessage createNdefMessage(String content) {

        NdefRecord ndefRecord = NdefRecord.createTextRecord("en", content);

        NdefMessage ndefMessage = new NdefMessage(new NdefRecord[]{ndefRecord});

        return ndefMessage;

    }

    public String getTextFromNdefRecord(NdefRecord ndefRecord) {

        String tagContent = null;

        try {
            byte[] payload = ndefRecord.getPayload();
            String textEncoding = ((payload[0] & 128) == 0) ? "UTF-8" : "UTF-16";
            int languageSize = payload[0] & 0063;
            tagContent = new String(payload, languageSize + 1,
                    payload.length - languageSize - 1, textEncoding).trim();

            ChangeUiWhenRead();

            Log.i(TAG, "getTextFromNdefRecord: SUCCESS: Tag read! Content: (" + tagContent + ")");
        } catch (UnsupportedEncodingException e) {
            Log.e(TAG, "getTextFromNdefRecord: " + e.getMessage(), e);
        }
        return tagContent;
    }

    private void ChangeUiWhenRead() {
        this.spinKit.setVisibility(View.GONE);
        this.checkMark.setVisibility(View.VISIBLE);
        this.readNotification.setText("Din deltagelse er registeret.\nHusk at scanne på vej ud.");
        this.readChipText.setText("Velkommen!");
    }

    @Override
    public void onClick(View v) {

        if(v == this.closeButton) {
            finish();
        }

    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}
