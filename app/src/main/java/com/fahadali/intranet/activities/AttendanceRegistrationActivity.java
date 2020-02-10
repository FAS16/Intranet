package com.fahadali.intranet.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
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
import com.fahadali.intranet.model.Attendance;
import com.fahadali.intranet.model.Room;
import com.fahadali.intranet.model.Student;
import com.fahadali.intranet.model.Token;
import com.fahadali.intranet.other.App;
import com.github.ybq.android.spinkit.SpinKitView;

import android.telephony.TelephonyManager;


import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
;


public class AttendanceRegistrationActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "AttendanceRegActivity";

    //    private ToggleButton readWriteButton;
    private SpinKitView spinKitBeforeCheckIn;
    private ImageView checkMark;
    private ImageView afterScanMark;
    private ImageView crossMark;
    private NfcAdapter nfcAdapter;
    private TextView tagContentTextView;
    private TextView readNotification;
    private TextView infoText;
    private Button postBtn;
    private boolean correctClassRoom;
    private boolean read;
    private String tagId;
    private int lessonId;
    private String roomId;
    private int checkType;
    private ArrayList<Room> rooms;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_att_reg);
        Log.d(TAG, "onCreate: called...");
        setTitle("");

        rooms = new ArrayList<>();
        retrieveAllRooms(App.instance.getTokenFromSharedPrefs());
        tagContentTextView = findViewById(R.id.tagContentTextView);
        spinKitBeforeCheckIn = findViewById(R.id.spin_kit);
        checkMark = findViewById(R.id.checkMark);
        afterScanMark = findViewById(R.id.afterScanMark);
        crossMark = findViewById(R.id.crossMark);
        readNotification = findViewById(R.id.readNotification);
        infoText = findViewById(R.id.readChipText);
        postBtn = findViewById(R.id.checkInBtn);
        postBtn.setOnClickListener(this);

        nfcAdapter = NfcAdapter.getDefaultAdapter(this);
        checkNfcAvailablity(nfcAdapter);

        lessonId = getIntent().getExtras().getInt("lessonId");
        roomId = getIntent().getExtras().getString("roomId");
        checkType = getCheckType(lessonId);

        if (checkType == -1) {
            App.longToast(this, "Du har allerede registreret for denne lektion");
            finish();
        }

    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    private void checkNfcAvailablity(NfcAdapter nfcAdapter) {

        if (nfcAdapter != null && nfcAdapter.isEnabled()) {
            Log.i(TAG, "checkNfcAvailablity: NFC is available!");
        } else {
            App.longToast(this, "Slå venligst NFC til i telefonens indstillinger");
            finish();
        }

    }

    public static String byteArrayToHex(byte[] a) {
        StringBuilder sb = new StringBuilder(a.length * 2);
        for (byte b : a)
            sb.append(String.format("%02x", b));
        return sb.toString();
    }


    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Log.d(TAG, "onNewIntent: NFC intent recieved!");

        if (!read) {
            if (intent.hasExtra(NfcAdapter.EXTRA_TAG)) {
                Log.i(TAG, "onNewIntent: Intent has EXTRA_TAG!");

                Parcelable[] parcelables =
                        intent
                                .getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);

                if (parcelables != null && parcelables.length > 0) {

                    Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
                    tagId = byteArrayToHex(tag.getId());
                    Log.i(TAG, "onNewIntent: tagId = " + tagId);
                    readTextFromTag((NdefMessage) parcelables[0]);
                    read = true;

                    validateRoom(tagId, roomId);
                    if (tagId.equals(roomId)) {
                        showCorrectRoomUi();
                    } else if (!this.rooms.isEmpty() && !getRoomIdentifiers(this.rooms).contains(tagId)) {
                            showWrongRoomUi("Ukendt klasselokale", "");
                    } else {
                        // User is at the wrong room
                        showWrongRoomUi("Du skal ikke være her", "");
                    }

                }

            } else {
                Log.i(TAG, "onNewIntent: No NDEF messages found...");
            }
        }
    }



    private void showWrongRoomUi(String header, String text) {

        afterScanMark.setVisibility(View.GONE);
        this.spinKitBeforeCheckIn.setVisibility(View.GONE);
        this.infoText.setText(header);
        if (!text.isEmpty()) {
            this.tagContentTextView.setText(text);
        }
        crossMark.setVisibility(View.VISIBLE);
        postBtn.setVisibility(View.GONE);
    }

    private void validateRoom(String tagId, String roomId) {
    }


    private void readTextFromTag(NdefMessage ndefMessage) {

        NdefRecord[] ndefRecords = ndefMessage.getRecords();

        if (ndefRecords != null && ndefRecords.length > 0) {

            NdefRecord ndefRecord = ndefRecords[0];

            String content = getTextFromNdefRecord(ndefRecord);

            this.tagContentTextView.setText("Lokale " + content);

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
            Log.e(TAG, "formatTag: " + e.getMessage());

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
            Log.e(TAG, "writeNdefMessage: " + e.getMessage());
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


            Log.i(TAG, "getTextFromNdefRecord: SUCCESS: Tag read! Content: (" + tagContent + ")");
        } catch (UnsupportedEncodingException e) {
            Log.e(TAG, "getTextFromNdefRecord: " + e.getMessage(), e);
        }
        return tagContent;
    }

    private void showCorrectRoomUi() {
        this.spinKitBeforeCheckIn.setVisibility(View.GONE);
        if (checkType == 0) {
            this.postBtn.setText("Check ind");
            this.infoText.setText("Velkommen!");


        } else {
            this.postBtn.setText("Check ud");
            this.infoText.setText("Farvel!");


        }
        postBtn.setVisibility(View.VISIBLE);
        afterScanMark.setVisibility(View.VISIBLE);

    }

    private void showCheckInSuccesfullUi() {
        String infoMsg = "";
        if (checkType == 0) {
            infoMsg = "Check ind fuldført!";
        } else {
            infoMsg = "Check ud fuldført!";

        }
        afterScanMark.setVisibility(View.GONE);
        checkMark.setVisibility(View.VISIBLE);
        infoText.setText(infoMsg);
        postBtn.setVisibility(View.GONE);
        afterScanMark.setVisibility(View.GONE);

    }

    @Override
    public void onClick(View v) {

        if (v == this.postBtn) {
            this.postBtn.setEnabled(false);
            postAttendance();
        }

    }

    private int getCheckType(int lessonId) {

        ArrayList<Attendance> attendances = Student.getInstance().getAttendances();
        int attendanceCounter = 0;
        if (!attendances.isEmpty()) {
            for (Attendance a : attendances) {
                if (a.getLessonId() == lessonId) {
                    attendanceCounter++;
                }
            }

            if (attendanceCounter == 1) {
                return 1;
            } else if (attendanceCounter == 2) {
                return -1;
            }
        }
        return 0;
    }

    private void postAttendance() {


        if (!App.instance.isUserSignedIn()) {
            App.instance.signOut(this);
            return;
        }

//        if(this.checkType == 1) {
//            App.longToast(this, "Du har allerede checket ud");
//            return;
//        }

        // Gather body data
        Token token = App.instance.getTokenFromSharedPrefs();
        String pattern = "yyyy-MM-dd HH:mm:ss";
        String date = App.getTodaysDate(pattern);
        String tagId = this.tagId;
        int studentId = Student.getInstance().getId();
        int lessonId = this.lessonId;
        int checkType = this.checkType;

        TelephonyManager tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        if (checkSelfPermission(Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    this,
                    new String[]{Manifest.permission.READ_PHONE_STATE},
                    1
            );
            return;
        }
        String deviceId = tm.getDeviceId();


        Call<Attendance> call = App.instance.getUserClient().checkIn(lessonId, studentId, date, checkType, tagId, deviceId, false, token.getBearerToken());

        call.enqueue(new Callback<Attendance>() {
            @Override
            public void onResponse(Call<Attendance> call, Response<Attendance> response) {
                if (response.isSuccessful()) {

                    showCheckInSuccesfullUi();
                    Log.i(TAG, "onResponse: (isSuccessful) response.body() = " + response.body());
                    App.longToast(AttendanceRegistrationActivity.this, "Succes: body = " + response.body());
                    Student.getInstance().getAttendances().add(response.body());


                } else {

                    String errorBodyString = "";
                    String message = "";
                    try {
                        errorBodyString = response.errorBody().string();
                        Log.i(TAG, "onResponse: (!isSuccessful) response.errorBody() = " + errorBodyString);
                        message = getMessage(errorBodyString);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    App.longToast(AttendanceRegistrationActivity.this, "Not succes: message = " + message);
                    showWrongRoomUi("Fejl", message);

                }
            }

            @Override
            public void onFailure(Call<Attendance> call, Throwable t) {
                Log.e(TAG, "onFailure: " + t.getMessage(), t);

                App.longToast(AttendanceRegistrationActivity.this, "Uknown error! (Check maybe connection or API?) " + t.getMessage());


                // Stop progessbar
            }
        });
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    private String getMessage(String errorBodyString) {

        String m = errorBodyString;
        m = m.replace("{", "");
        m = m.replace("}", "");
        m = m.replace("\n ", "");
        m = m.replace("\n", "");
        m = m.replace("\r", "");
        m = m.replace(":", "");
        m = m.replace("\"", "");
        m = m.replace(",", ",\n");
        m = m.replace(" message ", "");

        return m;

    }

    private void retrieveAllRooms(Token token) {

        Call<ArrayList<Room>> call = App.instance.getUserClient().getRooms(App.instance.getTokenFromSharedPrefs().getBearerToken());


        call.enqueue(new Callback<ArrayList<Room>>() {
            @Override
            public void onResponse(Call<ArrayList<Room>> call, Response<ArrayList<Room>> response) {
                if (response.isSuccessful()) {
                    setRoomsList(response.body());

                    App.shortToast(AttendanceRegistrationActivity.this, "Retrieved rooms from backend!");
                    Log.i(TAG, "onResponse: Rooms retrieved = " + Student.getInstance().toString());

                } else {
                    App.longToast(AttendanceRegistrationActivity.this, "Error code: " + response.code());
                    Log.i(TAG, "onResponse: Response not successful. Code = " + response.body());

                }
            }

            @Override
            public void onFailure(Call<ArrayList<Room>> call, Throwable t) {
                App.longToast(AttendanceRegistrationActivity.this, "Failed to retrieve room data. Message: " + t.getMessage());

            }
        });

    }

    private void setRoomsList(ArrayList<Room> rooms) {

        this.rooms = rooms;
    }

    private ArrayList<String> getRoomIdentifiers(ArrayList<Room> rooms) {

        ArrayList<String> roomIdentifiers = new ArrayList<>();

        for (Room room : rooms) {

            roomIdentifiers.add(room.getRoomIdentifier());

        }

        return roomIdentifiers;
    }
}
