package com.example.poppetx;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        new socketWork(this, getApplicationContext()).execute();
        Log.d("POPPET", "socketWork got called from MainActivity");
    }
}