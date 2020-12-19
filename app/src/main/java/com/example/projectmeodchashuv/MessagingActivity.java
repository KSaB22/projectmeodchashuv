package com.example.projectmeodchashuv;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MessagingActivity extends AppCompatActivity implements View.OnClickListener {
    final int SEND_SMS_PERMISSION_REQUEST_CODE = 1;
    EditText etnum, etmsg;
    Button send;
    private final int REQUEST_CODE = 99;
    Button btPick;
    String pnum = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messaging);
        etmsg = findViewById(R.id.msg);
        send = findViewById(R.id.snd);
        send.setOnClickListener(this);
        btPick = findViewById(R.id.btpick_contact);
        btPick.setOnClickListener(this);
        btPick.setEnabled(false);
        if (CheckPermission(Manifest.permission.SEND_SMS)) {
            btPick.setEnabled(true);
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_CONTACTS}, SEND_SMS_PERMISSION_REQUEST_CODE);
        }
        send.setEnabled(false);
        if (CheckPermission(Manifest.permission.SEND_SMS)) {
            send.setEnabled(true);
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.SEND_SMS}, SEND_SMS_PERMISSION_REQUEST_CODE);
        }
    }

    public void onSend(View v) {
        String message = etmsg.getText().toString();
        if (pnum == null || (pnum.length() < 2) || message == null || message.length() < 1) {
            return;
        }
        if (CheckPermission(Manifest.permission.SEND_SMS)) {
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(pnum, null, message, null, null);
            Toast.makeText(this, "Message Sent", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
        }

    }

    public boolean CheckPermission(String perm) {
        int check = ContextCompat.checkSelfPermission(this, perm);
        return check == PackageManager.PERMISSION_GRANTED;
    }
    public boolean CheckPermission1(String perm) {
        int check = ContextCompat.checkSelfPermission(this, perm);
        return check == PackageManager.PERMISSION_GRANTED;
    }

    @Override
    public void onClick(View v) {
        if (v == send){onSend(v);}
        else if (v == btPick) {
            if(CheckPermission1(Manifest.permission.READ_CONTACTS)){
            Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
            startActivityForResult(intent, REQUEST_CODE);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch(requestCode) {
            case (REQUEST_CODE):
                if (resultCode == Activity.RESULT_OK) {
                    Uri contactData = data.getData();
                    Cursor c = getContentResolver().query(contactData, null, null, null, null);
                    if (c.moveToFirst()) {
                        String contactId = c.getString(c.getColumnIndex(ContactsContract.Contacts._ID));
                        String hasNumber = c.getString(c.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));

                        if (Integer.valueOf(hasNumber) == 1) {
                            Cursor numbers = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + contactId, null, null);
                            while (numbers.moveToNext()) {
                                pnum = numbers.getString(numbers.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                                Toast.makeText(this, "Number=" + pnum, Toast.LENGTH_LONG).show();
                            }
                        }
                    }
                    break;
                }
        }
    }
}