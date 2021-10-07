package com.example.pendingacts;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.telephony.SmsManager;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class SmsActivity extends AppCompatActivity {
    /*
    Khoi tao bien:
    */
    private static final int REQUEST_SEND = 100;
    private EditText et_inputPhoneNumber, et_inputMessage, bt_time;
    private ImageView iv_sendMessage;
    private Spinner spinnerTime;
    int timeDelay = 0;
    String text = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sms);
        /*
        Set Actionbar:
        setTitle: SMS
        setDisplayShowHomeEnabled: back activity
         */
        getSupportActionBar().setTitle("SMS");
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        //Khoi tao bien Animation
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.anim_alpha);

        /*
        Tham chieu bien:
        et_inputPhoneNumber
        et_inputMessage
        bt_time
        iv_sendMessage
        spinnerTime
         */
        et_inputPhoneNumber = findViewById(R.id.et_inputPhoneNumber);
        et_inputMessage = findViewById(R.id.et_inputMessage);
        bt_time = findViewById(R.id.bt_time);
        iv_sendMessage = findViewById(R.id.iv_sendMessage);
        /*
        Bat su kien:
        Chay animation
        call method: sendMessage()
         */
        iv_sendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(animation);
                sendMessage();
            }
        });
        /*
        Set ArrayAdapter:
        array: time (strings)
        layout:  simple_spinner_item
        layout drop: simple_spinner_dropdown_item
         */
        spinnerTime = findViewById(R.id.spinnerTime);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.time, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerTime.setAdapter(adapter);
        spinnerTime.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                text = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    /*
    Method: sendMessage
    No param
    return: void
     */
    public void sendMessage() {
        /*
        Get data:
        et_inputPhoneNumber: sdt
        et_inputMessage: message
        bt_time: time delay
         */
        String sPhone = et_inputPhoneNumber.getText().toString();
        String sMessage = et_inputMessage.getText().toString();
        String sTime = bt_time.getText().toString();

        /*
        Dk if: sPhone, sMessage, sTime != null
        else: print: Please input phone first/ Please set time first
         */
        if (!sPhone.equals("") && !sMessage.equals("") && !sTime.equals("")) {
            /*
            Dk if: Check permission: false
            else: Check permission: true
             */
            if (ContextCompat.checkSelfPermission(SmsActivity.this, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.SEND_SMS}, REQUEST_SEND);
            } else {
                /*
                Get SmsManager
                Get time delay theo(Hour/Minute/second)
                 */
                SmsManager smsManager = SmsManager.getDefault();
                timeDelay = Integer.parseInt(sTime);
                if (text.equals("Hour")) {
                    timeDelay = timeDelay * 1000 * 60 * 60;
                }
                if (text.equals("Minute")) {
                    timeDelay = timeDelay * 1000 * 60;
                } else {
                    timeDelay *= 1000;
                }
                Toast.makeText(this, "A call will be done after " + sTime + text, Toast.LENGTH_LONG).show();
                /*
                intent di chuyen tu PhoneActivity -> TimeDelayActivity:
                    countdown time delay
                bundle truyen data: timeDelay
                 */
                Intent intent = new Intent(this, TimeDelayActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt("timeDelay", timeDelay);
                intent.putExtras(bundle);
                startActivity(intent);

                /*
                handler: delay function
                delay: Intent ACTION_CALL
                 */
                final Handler handler = new Handler(Looper.getMainLooper());
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        smsManager.sendTextMessage(sPhone, null, sMessage, null, null);
                        Toast.makeText(getApplicationContext(), "SMS send sucessfully!", Toast.LENGTH_SHORT).show();
                    }
                }, timeDelay);
            }
        } else {
            if (sPhone.equals("") && sMessage.equals("")) {
                Toast.makeText(this, "Please check your information", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Please set time first", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
            /*
            Dk if:
                grantResults: do dai > 0
                grantResults[0] == PackageManager.PERMISSION_GRANTED
             else:
                print: Please allow permission for using it
             */
        if (requestCode == REQUEST_SEND && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            sendMessage();
        } else {
            startActivity(new Intent(this, MainActivity.class));
            Toast.makeText(this, "Please allow permission for using it", Toast.LENGTH_SHORT).show();
        }
    }
}
