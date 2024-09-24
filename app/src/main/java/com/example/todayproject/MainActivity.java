package com.example.todayproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

  TextView tv1,tv2;
  Intent it;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tv1=findViewById(R.id.tv);
    }

    //addList, memo 버튼 클릭시 구현
    public void goadd(View v)
    {
        int id = v.getId();
        if (id == R.id.tv) { //addlist면 addlist 클래스로 이동
            it = new Intent(this, AddList.class);
            startActivity(it);
        } else if (id == R.id.memotv) { //memotv면 memolistDb 클래스로 이동
            it = new Intent(this, MemoListDB.class);
            startActivity(it);
        }
    }


}
