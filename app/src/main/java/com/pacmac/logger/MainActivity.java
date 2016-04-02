package com.pacmac.logger;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.pacmac.pacmaclogger.*;

public class MainActivity extends AppCompatActivity implements PacLogListener{

    private int index = 1;
    private PacLog pacLog = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btn1 = (Button) findViewById(R.id.btnTest1);
        Button btn2 = (Button) findViewById(R.id.btnTest2);
        Button btn3 = (Button) findViewById(R.id.btnTest3);

        pacLog = PacLog.setUP(MainActivity.this);

        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pacLog.writePacLog("THIS IS NEW ENTRY "+ index);
                index ++;
                Toast.makeText(getApplicationContext(), "THIS IS NEW ENTRY "+ index, Toast.LENGTH_SHORT).show();
            }
        });


        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(getApplicationContext(), "version " + pacLog.getVersion(), Toast.LENGTH_SHORT).show();
            }
        });

        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pacLog.exoportLog();
            }
        });

    }

    @Override
    public void onExport(boolean isExported) {
        Log.d("TAG", "Log Exported: " + isExported);
    }
}
