package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

public class pain_choice extends AppCompatActivity {
    String age;
    String sex;
    String suji;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pain_choice);
        final Spinner painspn = (Spinner) findViewById(R.id.painspinner);
        Spinner sexspn = (Spinner) findViewById(R.id.sexspinner);
        Spinner agespn = (Spinner) findViewById(R.id.agespinner);
        Button nextbtn = (Button) findViewById(R.id.nextbtn);

        final ArrayAdapter<CharSequence> painadapter = ArrayAdapter.createFromResource(this, R.array.pain_array, android.R.layout.simple_spinner_item);
        painadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        painspn.setAdapter(painadapter);
        painspn.setPrompt("@string/pain_prompt");
        painspn.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    suji = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        ArrayAdapter<CharSequence> sexadapter = ArrayAdapter.createFromResource(this, R.array.sex_array, android.R.layout.simple_spinner_item);
        sexadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sexspn.setAdapter(sexadapter);
        sexspn.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    sex = parent.getItemAtPosition(position).toString();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        ArrayAdapter<CharSequence> ageadapter = ArrayAdapter.createFromResource(this, R.array.age_array, android.R.layout.simple_spinner_item);
        ageadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        agespn.setAdapter(ageadapter);
        agespn.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    age = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        nextbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String str_pain = painspn.getSelectedItem().toString();
                Intent camerapageIntent = new Intent(pain_choice.this, Camera.class);
                camerapageIntent.putExtra("pain", str_pain);
                ApplicationClass.sex=sex;
                ApplicationClass.age=age;
                ApplicationClass.suji=suji;
                startActivity(camerapageIntent);
            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
