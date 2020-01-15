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
import org.json.JSONException;
import org.json.JSONObject;

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
    private TextView textView2;
    String msg_id ="";
    int count=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView = (TextView) findViewById(R.id.text_count);

        Thread getSMSthread = new Thread(){
          @Override
          public  void run(){
              while (!isInterrupted()){
                  try {
                      Thread.sleep(1000);

                      OkHttpClient client = new OkHttpClient();
                      String url = "http://10.40.47.60:3000/myroute/getSMS";


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
                                    JSONObject myResponseReader;

                                    try {
                                        if(myResponse != "") {
                                            myResponseReader = new JSONObject(myResponse);
                                            final String PhoneNumber = myResponseReader.getString("Phone");
                                            final String Body = myResponseReader.getString("Body");
                                            msg_id = myResponseReader.getString("id");

                                           try{
//
                                                String smsNumber = String.format("smsto: %s",PhoneNumber);
                                                String scAddress = null;

                                                PendingIntent sentIntent = null, deliveryIntent = null;
                                                SmsManager smsManager = SmsManager.getDefault();
                                                String smsMessage = Body;
                                                smsManager.sendTextMessage
                                                        (smsNumber, scAddress, smsMessage,
                                                                sentIntent, deliveryIntent);

                                                count++;

                                            MainActivity.this.runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    textView.setText(Integer.toString(count));
                                                    Toast.makeText(MainActivity.this, "SMS Sent Successfully!", Toast.LENGTH_SHORT).show();



                                                }
                                            });

                                                OkHttpClient client2 = new OkHttpClient();

                                               String url2 = "http://10.40.47.60:3000/myroute/sentSMS?id=";
                                               url2 += msg_id;


                                               final Request sentSMSRequest = new Request.Builder()
                                                       .url(url2)
                                                       .build();
                                               client2.newCall(sentSMSRequest).enqueue(new Callback() {
                                                   @Override
                                                   public void onFailure(@NotNull Call call, @NotNull IOException e) {
                                                       e.printStackTrace();
                                                   }

                                                   @Override
                                                   public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {

                                                   }
                                               });



                                             }
                                            catch(Exception e){

                                               MainActivity.this.runOnUiThread(new Runnable() {
                                                   @Override
                                                   public void run() {
                                                       Toast.makeText(MainActivity.this, "SMS Failed to Send, Please try again", Toast.LENGTH_SHORT).show();
                                                   }
                                               });

                                            }



                                        }

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }



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
    }
}