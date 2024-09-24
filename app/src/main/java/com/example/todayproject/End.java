package com.example.todayproject;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class End extends AppCompatActivity {

    private ListView completetext,omgtext;
    private ArrayList<String>endarray1=new ArrayList<String>();
    private ArrayList<String>endarray2=new ArrayList<String>();
    private Button sleepbtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.finally_the_end);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        //리스트뷰 등록
        ArrayAdapter adapter1=new ArrayAdapter(this, android.R.layout.simple_list_item_1,endarray1);
        ArrayAdapter adapter2=new ArrayAdapter(this,android.R.layout.simple_list_item_1,endarray2);
        completetext=findViewById(R.id.completetext);
        completetext.setAdapter(adapter1);

        omgtext=findViewById(R.id.omgtext);
        omgtext.setAdapter(adapter2);

        //데이터 가지고 오기
        for(int i=0;i<start.sendArr.size();i++){
            ListViewItem listViewItem=start.sendArr.get(i);
            endarray1.add("\n"+(i+1)+". "+listViewItem.getName()+", "+listViewItem.getTime());
        }

        //데이터 가지고 오기
        for(int i=0;i<AddList.list.size();i++){
            String str=AddList.list.get(i);
            endarray2.add("\n"+(i+1)+". "+str+"\n");
        }

        //초기화 버튼 설정
        sleepbtn=findViewById(R.id.sleepbtn);
        sleepbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteAll();
               Intent it =new Intent(getApplicationContext(),MainActivity.class);
                it.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(it);
            }
        });
    }

    //데이터 삭제 메소드
    protected void deleteAll(){

        SharedPreferences sp=getSharedPreferences("sharedpreferences",0);
        SharedPreferences.Editor editor=sp.edit();
        editor.clear();
        editor.commit();

        sp=getSharedPreferences("sharedpreferences2",0);
        editor=sp.edit();
        editor.clear();
        editor.commit();

        AddList.list.clear();
        start.sendArr.clear();
    }


}
