package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    // Used to load the 'native-lib' library on application startup.
    static {
        System.loadLibrary("native-lib");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        Button painbnt = (Button)findViewById(R.id.pain_bnt);
        Button sujiInfobnt = (Button)findViewById(R.id.sujiinfo_bnt);
        Button logbnt = (Button)findViewById(R.id.log_bnt);
        Button setbnt = (Button)findViewById(R.id.setting_bnt);
        painbnt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent painintent = new Intent(MainActivity.this, pain_choice.class);
                startActivity(painintent);
            }
        });

        sujiInfobnt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent infointent = new Intent(MainActivity.this, suji_info.class);
                startActivity(infointent);
            }
        });

        logbnt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent logintent = new Intent(MainActivity.this , Log.class);
                startActivity(logintent);
            }
        });

        setbnt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent setintent = new Intent(MainActivity.this, setting.class);
                startActivity(setintent);
            }
        });


    }
}
