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
import android.widget.TextView;
import android.widget.Toast;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    private EditText txtMobile;
    private EditText txtMessage;
    private Button btnSms;
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        txtMobile = (EditText)findViewById(R.id.mblTxt);
        txtMessage = (EditText)findViewById(R.id.msgTxt);
        textView = (TextView) findViewById(R.id.text_result);

        Thread getSMSthread = new Thread(){
          @Override
          public  void run(){
              while (!isInterrupted()){
                  try {
                      Thread.sleep(1000);

                      OkHttpClient client = new OkHttpClient();
                      String url = "http://192.168.1.4:3000/myroute/hw";
                      final Request request = new Request.Builder()
                              .url(url)
                              .build();

                      client.newCall(request).enqueue(new Callback() {
                          @Override
                          public void onFailure(@NotNull Call call, @NotNull IOException e) {
                              e.printStackTrace();
                          }

                          @Override
                          public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                                if(response.isSuccessful()){
                                    final String myResponse = response.body().string();

                                    MainActivity.this.runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            textView.setText(myResponse);
                                        }
                                    });
                                }

                          }
                      });


                  } catch (InterruptedException e) {
                      e.printStackTrace();
                  }
              }
          }
        };

        getSMSthread.start();
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