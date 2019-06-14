package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.session.PlaybackState;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

public class suji_info extends AppCompatActivity {
    Button searchBt;
    EditText input;
    DbHelper sujiDb;
    SQLiteDatabase db;
    Cursor cursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_suji_info);

        final ArrayList<String> arrayList = new ArrayList<String>();
        sujiDb=new DbHelper(this);
        db=sujiDb.getWritableDatabase();

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

        final ArrayAdapter<String> adapter;
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, arrayList);
        searchBt=(Button)findViewById(R.id.infoBt);
        input=(EditText)findViewById(R.id.label);
        final ListView listView = (ListView) findViewById(R.id.listview);
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
        searchBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String info=input.getText().toString();
                cursor=db.rawQuery("SELECT * FROM sujiTable WHERE name='"+info+"';",null);
                arrayList.clear();
                cursor.moveToFirst();
                do {
                    arrayList.add("명칭: "+cursor.getString(cursor.getColumnIndex("name"))+", ");
                    listView.setAdapter(adapter);
                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                        }
                    });
                }while(cursor.moveToNext());
            }
        });

    }

    public byte[] getBytes(Bitmap bitmap)
    {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, stream);
        return stream.toByteArray();
    }

    public Bitmap getImage(byte[] image)
    {
        return BitmapFactory.decodeByteArray(image,0,image.length);
    }
}

class DbHelper extends SQLiteOpenHelper
{
    public DbHelper(Context context)
    {
        super(context,"suji_base.db",null,4);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE sujiTable (name TEXT, img BLOB);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}