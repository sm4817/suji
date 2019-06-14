package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log);
        search=(EditText)findViewById(R.id.label_log);
        btn=(Button)findViewById(R.id.logBt);
        sex=(Switch)findViewById(R.id.sex_switch);
        age=(Switch)findViewById(R.id.age_switch);
        date=(Switch)findViewById(R.id.date_switch);

        final ArrayList<String> arrayList = new ArrayList<String>();
        db=lobdb.getReadableDatabase();
        cursor=db.rawQuery("SELECT * FROM logTable",null);
        if(cursor!=null)
        {
            if(cursor.moveToFirst())
            {
                do {
                    int id=cursor.getInt(cursor.getColumnIndex("ID"));
                    String sex=cursor.getString(cursor.getColumnIndex("SEX"));
                    String age=cursor.getString(cursor.getColumnIndex("AGE"));
                    String suji=cursor.getString(cursor.getColumnIndex("SUJI"));
                    String datetime=cursor.getString(cursor.getColumnIndex("DATETIME"));

                    String log_str="id: "+id+", 성별: "+sex+", 나이: "+age+", 사용내역: "+suji+", 사용날짜: "+datetime;
                    arrayList.add(log_str);
                }while (cursor.moveToNext());
            }
        }

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(sex.isChecked())
                {
                    String info=search.getText().toString();
                    cursor=db.rawQuery("SELECT * FROM logTable WHERE SEX=''"+info+"';",null);
                    if(cursor!=null)
                    {
                        if(cursor.moveToFirst())
                        {
                            do {
                                int id=cursor.getInt(cursor.getColumnIndex("ID"));
                                String sex=cursor.getString(cursor.getColumnIndex("SEX"));
                                String age=cursor.getString(cursor.getColumnIndex("AGE"));
                                String suji=cursor.getString(cursor.getColumnIndex("SUJI"));
                                String datetime=cursor.getString(cursor.getColumnIndex("DATETIME"));

                                String log_str="id: "+id+", 성별: "+sex+", 나이: "+age+", 사용내역: "+suji+", 사용날짜: "+datetime;
                                arrayList.add(log_str);
                            }while (cursor.moveToNext());
                        }
                    }
                }
                else if(age.isChecked())
                {
                    String info=search.getText().toString();
                    cursor=db.rawQuery("SELECT * FROM logTable WHERE AGE=''"+info+"';",null);
                    if(cursor!=null)
                    {
                        if(cursor.moveToFirst())
                        {
                            do {
                                int id=cursor.getInt(cursor.getColumnIndex("ID"));
                                String sex=cursor.getString(cursor.getColumnIndex("SEX"));
                                String age=cursor.getString(cursor.getColumnIndex("AGE"));
                                String suji=cursor.getString(cursor.getColumnIndex("SUJI"));
                                String datetime=cursor.getString(cursor.getColumnIndex("DATETIME"));

                                String log_str="id: "+id+", 성별: "+sex+", 나이: "+age+", 사용내역: "+suji+", 사용날짜: "+datetime;
                                arrayList.add(log_str);
                            }while (cursor.moveToNext());
                        }
                    }
                }
                else if(date.isChecked())
                {
                    String info=search.getText().toString();
                    cursor=db.rawQuery("SELECT * FROM logTable WHERE DATE=''"+info+"';",null);
                    if(cursor!=null)
                    {
                        if(cursor.moveToFirst())
                        {
                            do {
                                int id=cursor.getInt(cursor.getColumnIndex("ID"));
                                String sex=cursor.getString(cursor.getColumnIndex("SEX"));
                                String age=cursor.getString(cursor.getColumnIndex("AGE"));
                                String suji=cursor.getString(cursor.getColumnIndex("SUJI"));
                                String datetime=cursor.getString(cursor.getColumnIndex("DATETIME"));

                                String log_str="id: "+id+", 성별: "+sex+", 나이: "+age+", 사용내역: "+suji+", 사용날짜: "+datetime;
                                arrayList.add(log_str);
                            }while (cursor.moveToNext());
                        }
                    }
                }
            }
        });
    }
}

class DbHelper_log extends SQLiteOpenHelper
{
    public DbHelper_log(Context context)
    {
        super(context,"log.db",null,4);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE logTable (_id INTEGER PRIMARY KEY AUTOINCREMENT, sex TEXT, " +
                "age TEXT,suji TEXT,datetime date default CURRENT_DATE, FOREIGN KEY(suji) REFERENCES sujiTable(name));");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}