package com.pacmac.helperpaclog;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.pacmac.paclog.PacLog;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        PacLog pacLog = PacLog.setUP(getApplicationContext());

        PacLog pacLog2 = PacLog.setUP(getApplicationContext(), 1024*5, "paclog");

    }
}
