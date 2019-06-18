package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Switch;

import java.util.ArrayList;

public class Log extends AppCompatActivity {
    DbHelper_log lobdb;
    Cursor cursor;
    SQLiteDatabase db;
    EditText search;
    Button btn;
    Switch sex;
    Switch age;
    Switch date;
    ApplicationClass app;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log);
        Intent intent=getIntent();
        search=(EditText)findViewById(R.id.label_log);
        btn=(Button)findViewById(R.id.logBt);
        sex=(Switch)findViewById(R.id.sex_switch);
        age=(Switch)findViewById(R.id.age_switch);
        date=(Switch)findViewById(R.id.date_switch);

        final ArrayList<String> arrayList = new ArrayList<String>();
        final ArrayAdapter<String> adapter;
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, arrayList);
        final ListView listView = (ListView) findViewById(R.id.logview);
        lobdb=new DbHelper_log(this);
        db=lobdb.getWritableDatabase();
        if(app.sex!=null&&app.age!=null&&app.suji!=null) {
            db.execSQL("INSERT INTO logTable VALUES('" + app.sex + "','" + app.age + "','" + app.suji + "',DATETIME('NOW','localtime'));");
        }
        cursor=db.rawQuery("SELECT * FROM logTable",null);
        if(cursor!=null)
        {
            if(cursor.moveToFirst())
            {
                do {
                    String sex=cursor.getString(cursor.getColumnIndex("sex"));
                    String age=cursor.getString(cursor.getColumnIndex("age"));
                    String suji=cursor.getString(cursor.getColumnIndex("suji"));
                    String datetime=cursor.getString(cursor.getColumnIndex("datetime"));

                    String log_str="성별: "+sex+", 나이: "+age+", 사용내역: "+suji+", 사용날짜: "+datetime;
                    arrayList.add(log_str);
                }while (cursor.moveToNext());
                listView.setAdapter(adapter);
            }
        }

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                arrayList.clear();
                if(sex.isChecked())
                {
                    String info=search.getText().toString();
                    cursor=db.rawQuery("SELECT * FROM logTable WHERE SEX='"+info+"';",null);
                    if(cursor!=null)
                    {
                        if(cursor.moveToNext())
                        {
                            do {
                                String sex=cursor.getString(cursor.getColumnIndex("sex"));
                                String age=cursor.getString(cursor.getColumnIndex("age"));
                                String suji=cursor.getString(cursor.getColumnIndex("suji"));
                                String datetime=cursor.getString(cursor.getColumnIndex("datetime"));

                                String log_str="성별: "+sex+", 나이: "+age+", 사용내역: "+suji+", 사용날짜: "+datetime;
                                arrayList.add(log_str);
                            }while (cursor.moveToNext());
                        }
                    }
                    listView.setAdapter(adapter);
                }
                else if(age.isChecked())
                {
                    String info=search.getText().toString();
                    cursor=db.rawQuery("SELECT * FROM logTable WHERE AGE='"+info+"';",null);
                    if(cursor!=null)
                    {
                        if(cursor.moveToFirst())
                        {
                            do {
                                String sex=cursor.getString(cursor.getColumnIndex("sex"));
                                String age=cursor.getString(cursor.getColumnIndex("age"));
                                String suji=cursor.getString(cursor.getColumnIndex("suji"));
                                String datetime=cursor.getString(cursor.getColumnIndex("datetime"));

                                String log_str="성별: "+sex+", 나이: "+age+", 사용내역: "+suji+", 사용날짜: "+datetime;
                                arrayList.add(log_str);
                            }while (cursor.moveToNext());
                        }
                    }
                    listView.setAdapter(adapter);
                }
                else if(date.isChecked())
                {
                    String info=search.getText().toString();
                    cursor=db.rawQuery("SELECT * FROM logTable WHERE DATE='"+info+"';",null);
                    if(cursor!=null)
                    {
                        if(cursor.moveToFirst())
                        {
                            do {
                                String sex=cursor.getString(cursor.getColumnIndex("sex"));
                                String age=cursor.getString(cursor.getColumnIndex("age"));
                                String suji=cursor.getString(cursor.getColumnIndex("suji"));
                                String datetime=cursor.getString(cursor.getColumnIndex("datetime"));

                                String log_str="성별: "+sex+", 나이: "+age+", 사용내역: "+suji+", 사용날짜: "+datetime;
                                arrayList.add(log_str);
                            }while (cursor.moveToNext());
                        }
                    }
                    listView.setAdapter(adapter);
                }
            }
        });
    }
}

class DbHelper_log extends SQLiteOpenHelper
{

    public DbHelper_log(Context context)
    {
        super(context,"log.db",null,1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE logTable (sex TEXT, age TEXT, suji TEXT, datetime DATE);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}