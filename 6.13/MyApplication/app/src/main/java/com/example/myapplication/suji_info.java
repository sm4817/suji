package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.session.PlaybackState;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class suji_info extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_suji_info);

        ArrayList<String> arrayList = new ArrayList<String>();

        arrayList.add("두통");
        arrayList.add("눈이 침침할때");
        arrayList.add("어깨가 결릴때");
        arrayList.add("수면부족");
        arrayList.add("허리 요통");
        arrayList.add("위");
        arrayList.add("기관지");
        arrayList.add("폐");
        arrayList.add("심장");
        arrayList.add("폐, 대장");
        arrayList.add("비장, 위");
        arrayList.add("신장");
        arrayList.add("난소, 자궁");
        arrayList.add("심장, 소장");
        arrayList.add("간");

        ArrayAdapter<String> adapter;
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, arrayList);
        ListView listView = (ListView) findViewById(R.id.listview);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                if (i == 0)
                {
                    Intent intent = new Intent(suji_info.this, one_head.class);
                    startActivity(intent);
                }
                else if (i == 1)
                {
                    Intent intent = new Intent(suji_info.this, two_list.class);
                    startActivity(intent);
                }
                else if (i == 2)
                {
                    Intent intent = new Intent(suji_info.this, three_list.class);
                    startActivity(intent);
                }
                else if (i == 3)
                {
                    Intent intent = new Intent(suji_info.this, four_list.class);
                    startActivity(intent);
                }
                else if (i == 4)
                {
                    Intent intent = new Intent(suji_info.this, five_list.class);
                    startActivity(intent);
                }
                else
                {
                    Intent intent = new Intent(suji_info.this, six_list.class);
                    startActivity(intent);
                }
            }
        });

    }
}