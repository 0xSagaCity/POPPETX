package com.poppet.poppetx;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import com.poppet.network.socketWork;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        new socketWork(this, getApplicationContext()).execute();
    }
}