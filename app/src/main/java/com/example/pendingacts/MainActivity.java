package com.example.pendingacts;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.google.android.material.transition.platform.MaterialContainerTransformSharedElementCallback;

import static android.app.ActivityOptions.makeSceneTransitionAnimation;

public class MainActivity extends AppCompatActivity{
    /*
    Khoi tao bien:
    ImageView: iv_phone, iv_sms
     */
    private ImageView iv_phone, iv_sms;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();
        //Khoi tao bien Animation
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.anim_scale);

        /*
        Tham chieu bien:
        iv_phone
        iv_sms
         */
        iv_phone = findViewById(R.id.iv_phone);
        iv_phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*
                Bat su kien:
                Chay animation
                intent di chuyen MainActivity -> PhoneActivity
                 */
                v.startAnimation(animation);
                Intent intent = new Intent(MainActivity.this, PhoneActivity.class);
                startActivity(intent);
            }
        });
        iv_sms = findViewById(R.id.iv_sms);
        iv_sms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*
                Bat su kien:
                Chay animation
                intent di chuyen MainActivity -> SmsActivity
                 */
                v.startAnimation(animation);
                Intent intent = new Intent(MainActivity.this, SmsActivity.class);
                startActivity(intent);
            }
        });
    }
}