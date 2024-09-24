package com.example.todayproject;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class WriteDB extends AppCompatActivity {

    private EditText contentView;
    private Button savebtn, backbtn;
    private TextView contentview1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.memowrite);

        contentView=(EditText)findViewById(R.id.editmemo);
        savebtn=(Button)findViewById(R.id.savebtn);
        backbtn=(Button)findViewById(R.id.backbtn);
        contentview1=findViewById(R.id.textmemo);
        contentview1.setVisibility(View.GONE);
        contentView.setVisibility(View.VISIBLE);

    }

    public void mClick(View v) {

        int id = v.getId();
        if (id == R.id.savebtn) { //저장버튼
            String content = contentView.getText().toString();
            SimpleDateFormat mSimpleDateFormat = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss. ", Locale.KOREA);
            String date = mSimpleDateFormat.format(new Date());
            try {
                DBManager manager = new DBManager(this);
                SQLiteDatabase db = manager.getWritableDatabase();

                ContentValues values = new ContentValues();
                values.put("date", date);
                values.put("content", content);
                long newRowId = db.insert("memodb", null, values);

                ListViewItem tempdata = new ListViewItem(content, date);
                MemoListDB.memoArr.add(tempdata);

                db.close();
                manager.close();
                Intent it = new Intent(this, MemoListDB.class);
                it.putExtra("it_date", date);
                it.putExtra("it_content", content);
                setResult(RESULT_OK, it);
                finish();
            } catch (SQLiteException e) {
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
            }
        } else if (id == R.id.backbtn) { //뒤로가기

            finish();
        } else if (id == R.id.deletebtn) { //삭제 (데이터를 쓰는 클래스이므로 삭제할 데이터가 항상 없으므로 toast 사용)
            Toast.makeText(this, "삭제할 내용이 없습니다.", Toast.LENGTH_LONG).show();
        }
    }
}




