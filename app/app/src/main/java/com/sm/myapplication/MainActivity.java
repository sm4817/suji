package com.sm.myapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button spinnerButton = (Button) findViewById(R.id.camera);
        Button logButton = (Button)findViewById(R.id.log);
        spinnerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this , SpinnerActivity.class);
                startActivity(intent);
                overridePendingTransition(0, 0);
            }
        });

        logButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this , logActivity.class);
                startActivity(intent);
                overridePendingTransition(0, 0);
            }
        });
    }
}
