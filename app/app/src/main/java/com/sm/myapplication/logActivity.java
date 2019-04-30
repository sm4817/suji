package com.sm.myapplication;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.CursorAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

public class logActivity extends AppCompatActivity {
    EditText editText, editText2;
    ListView listView, listView2;

    DbHelper dbHelper;
    SQLiteDatabase db;
    Cursor cursor;
    ArrayAdapter<String> adapter, adapter2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log);

        editText = (EditText) findViewById(R.id.editText);
        editText2 = (EditText) findViewById(R.id.editText2);
        listView = (ListView) findViewById(R.id.listView);
        listView2 = (ListView) findViewById(R.id.listView2);

        dbHelper = new DbHelper(this);
        db = dbHelper.getWritableDatabase();    // 읽기/쓰기 모드로 데이터베이스를 오픈

        cursor = db.rawQuery("SELECT * FROM tableName", null);
        startManagingCursor(cursor);    //엑티비티의 생명주기와 커서의 생명주기를 같게 한다.

        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);
        adapter2 = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);

        while (cursor.moveToNext()) {
            adapter.add(cursor.getString(1));
            adapter2.add(cursor.getString(2));
        }

        listView.setAdapter(adapter);
        listView2.setAdapter(adapter2);
    }

    public void insert(View v) {
        String name = editText.getText().toString();
        String info = editText2.getText().toString();
        db.execSQL("INSERT INTO tableName VALUES (null, '" + name + "', '" + info + "');");

        Toast.makeText(getApplicationContext(), "추가 성공", Toast.LENGTH_SHORT).show();

        editText.setText("");
        editText2.setText("");
    }


    public void delete(View v) {
        String name = editText.getText().toString();
        db.execSQL("DELETE FROM tableName WHERE name = '" + name + "';");

        Toast.makeText(getApplicationContext(), "삭제 성공", Toast.LENGTH_SHORT).show();

    }


    class DbHelper extends SQLiteOpenHelper {
        static final String DATABASE_NAME = "test.db";
        static final int DATABASE_VERSION = 1;

        public DbHelper(Context context ) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL("CREATE TABLE tableName (_id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT, info TEXT);");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS tableName");
            onCreate(db);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(0, 0);
    }
}
