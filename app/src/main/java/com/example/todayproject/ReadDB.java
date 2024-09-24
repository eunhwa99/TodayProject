package com.example.todayproject;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ReadDB extends AppCompatActivity {
    private DBManager dbmanager;
    private SQLiteDatabase db;

    private String str_date;
    private String str_content;

    private EditText contentView;
    private Button savebtn, backbt,deletebtn;
    private TextView contentview1;
    private int get_id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.memowrite);

        contentView=(EditText)findViewById(R.id.editmemo);
        savebtn=(Button)findViewById(R.id.savebtn);
        backbt=(Button)findViewById(R.id.backbtn);
        deletebtn=(Button)findViewById(R.id.deletebtn);
        contentview1 = findViewById(R.id.textmemo);

         Intent it = getIntent();
         str_content = it.getStringExtra("it_content");
         get_id=it.getIntExtra("id",1);

         //읽을 때와 메모를 쓸 때 다른 뷰 나오도록 설정
        contentView.setVisibility(View.GONE);
        contentview1.setVisibility(View.VISIBLE);
        contentview1.setText(str_content);
    }

    public void mClick(View v) {

        int id = v.getId();
        if (id == R.id.savebtn) { //저장
            String content = contentView.getText().toString();
            SimpleDateFormat mSimpleDateFormat = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss. ", Locale.KOREA);
            String date = mSimpleDateFormat.format(new Date());
            Log.i("back", "savebtb");
            try {
                DBManager manager = new DBManager(this);
                SQLiteDatabase db = manager.getWritableDatabase();

                ContentValues values = new ContentValues();
                values.put("date", date);
                values.put("content", content);

                long newRowId = db.update("memodb", values, "date=?", new String[]{str_date});

                db.close();
                manager.close();

                finish();
            } catch (SQLiteException e) {
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
            }
        } else if (id == R.id.deletebtn) { //삭제

            try {
                DBManager manager = new DBManager(this);
                SQLiteDatabase db = manager.getWritableDatabase();
                db.delete("memodb", "content=?", new String[]{str_content});
                db.close();
                manager.close();

                Intent it = getIntent();
                int position = it.getIntExtra("id", 1);
                it = new Intent(this, MemoListDB.class);
                // it.putExtra("id",get_id);
                MemoListDB.memoArr.remove(position);
                setResult(RESULT_OK, it);
                finish();
            } catch (SQLiteException e) {
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
            }
        } else if (id == R.id.backbtn) { //뒤로가기

            finish();
        }
    }

}

