package com.apsinnovations.livlyf;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.apsinnovations.livlyf.utils.PrefManager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;

public class SplashActivity extends AppCompatActivity {
    private static final String TAG = "SplashActivity";
    @SuppressLint("HandlerLeak")
    Handler handler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            if (msg.what == 111) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                finish();
            } else if (msg.what == 222) {
                Intent intent = new Intent(getApplicationContext(), SignupActivity.class);
                startActivity(intent);
                finish();
            } else if (msg.what == 333) {
                Intent intent = new Intent(getApplicationContext(), WelcomeActivity.class);
                startActivity(intent);
                finish();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        Objects.requireNonNull(getSupportActionBar()).hide();

        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser != null) {
            handler.sendEmptyMessageDelayed(111, 1000);
        } else {
            PrefManager prefManager = new PrefManager(getApplicationContext());
            Log.i(TAG, "onCreate: " + prefManager.isFirstTimeLaunch());
            if (prefManager.isFirstTimeLaunch()) {
                handler.sendEmptyMessageDelayed(333, 1000);
            } else {
                handler.sendEmptyMessageDelayed(222, 1000);
            }
        }
    }
}