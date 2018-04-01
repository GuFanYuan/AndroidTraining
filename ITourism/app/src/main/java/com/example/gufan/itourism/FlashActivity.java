package com.example.gufan.itourism;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class FlashActivity extends AppCompatActivity {

    protected Handler mHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flash);
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                    startActivity(new Intent(FlashActivity.this,MainActivity.class));
            }
        },2000);
    }
}
