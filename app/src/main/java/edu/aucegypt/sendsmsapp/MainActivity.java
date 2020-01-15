package edu.aucegypt.sendsmsapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.PendingIntent;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private EditText txtMobile;
    private EditText txtMessage;
    private Button btnSms;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        txtMobile = (EditText)findViewById(R.id.mblTxt);
        txtMessage = (EditText)findViewById(R.id.msgTxt);
        btnSms = (Button)findViewById(R.id.btnSend);
        btnSms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
//
                    String smsNumber = String.format("smsto: %s",
                            txtMobile.getText().toString());

                    // Set the service center address if needed, otherwise null.
                    String scAddress = null;
                    // Set pending intents to broadcast
                    // when message sent and when delivered, or set to null.
                    PendingIntent sentIntent = null, deliveryIntent = null;
                    // Use SmsManager.
                    SmsManager smsManager = SmsManager.getDefault();
                    String smsMessage = txtMessage.getText().toString();
                    smsManager.sendTextMessage
                            (smsNumber, scAddress, smsMessage,
                                    sentIntent, deliveryIntent);

                    Toast.makeText(MainActivity.this, "SMS Sent Successfully!", Toast.LENGTH_SHORT).show();

                }
                catch(Exception e){
                    Toast.makeText(MainActivity.this, "SMS Failed to Send, Please try again", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}