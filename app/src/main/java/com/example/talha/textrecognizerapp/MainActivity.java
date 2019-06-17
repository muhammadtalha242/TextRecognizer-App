package com.example.talha.textrecognizerapp;


import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;


public class MainActivity extends AppCompatActivity {
    private static final String TAG = "In main activity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(TAG, "Inside onCreate()");
    }

    public void CameraActivity(View view) {
        Intent intent = new Intent(this,CameraActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.v(TAG, "Inside onResume()");
    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.v(TAG, "Inside onDestroy()");
    }



}
