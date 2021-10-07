package com.example.pendingacts;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
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

public class PhoneActivity extends AppCompatActivity {
    /*
   Khoi tao bien:
    */
    private static final int REQUEST_CALL = 1;
    private EditText et_inputPhoneNumber, bt_time;
    private ImageView iv_calling;
    private Spinner spinnerTime;
    int timeDelay = 0;
    String text = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone);
        /*
        Set Actionbar:
        setTitle: phone
        setDisplayShowHomeEnabled: back activity
         */
        getSupportActionBar().setTitle("Phone");
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        //Khoi tao bien Animation
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.anim_alpha);

        /*
        Tham chieu bien:
        et_inputPhoneNumber
        bt_time
        iv_calling
        spinnerTime
         */
        et_inputPhoneNumber = findViewById(R.id.et_inputPhoneNumber);
        bt_time = findViewById(R.id.bt_time);
        iv_calling = findViewById(R.id.iv_calling);

        /*
        Bat su kien:
        Chay animation
        call method: makePhoneCall()
         */
        iv_calling.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(animation);
                makePhoneCall();
            }
        });
        spinnerTime = findViewById(R.id.spinnerTime);

        /*
        Set ArrayAdapter:
        array: time (strings)
        layout:  simple_spinner_item
        layout drop: simple_spinner_dropdown_item
         */
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
    Method: makePhoneCall
    No param
    return: void
     */
    public void makePhoneCall(){
        /*
        Get data:
        et_inputPhoneNumber: sdt
        bt_time: time delay
         */
        String number = et_inputPhoneNumber.getText().toString();
        String sTime = bt_time.getText().toString();
        /*
        Dk if: number,time: do dai > 0
        else: print: Please input phone first/ Please set time first
         */
        if(number.trim().length() > 0 && sTime.trim().length() > 0){
            /*
            Dk if: Check permission: false
            else: Check permission: true
             */
            if(ContextCompat.checkSelfPermission(PhoneActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE}, REQUEST_CALL);
            }else{
                /*
                Get string dail: tel: sdt
                Get time delay theo(Hour/Minute/second)
                 */
                String dial = "tel:" + number;
                timeDelay = Integer.parseInt(sTime);
                if (text.equals("Hour")){
                    timeDelay = timeDelay * 1000 * 60 * 60;
                }
                if(text.equals("Minute")){
                    timeDelay = timeDelay * 1000 * 60;
                }
                else{
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
                        startActivity(new Intent(Intent.ACTION_CALL, Uri.parse(dial)));
                    }
                }, timeDelay);
            }
        }else {
            if (!(number.trim().length() > 0)){
                Toast.makeText(this, "Please input phone first", Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(this, "Please set time first", Toast.LENGTH_SHORT).show();
            }
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        /*
        permissions: list permissions da gui
        grantResults: result permissions theo vi tri permissions
         */
        if(requestCode == REQUEST_CALL){
            /*
            Dk if:
                grantResults: do dai > 0
                grantResults[0] == PackageManager.PERMISSION_GRANTED
             else:
                print: Please allow permission for using it
             */
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                makePhoneCall();
            }else{
                startActivity(new Intent(this, MainActivity.class));
                Toast.makeText(this, "Please allow permission for using it", Toast.LENGTH_SHORT).show();
            }
        }
    }

}
