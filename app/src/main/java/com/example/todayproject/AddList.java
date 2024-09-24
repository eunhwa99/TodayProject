package com.example.todayproject;

import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class AddList extends AppCompatActivity {
    Button btn;
    TextView memotext;
    Intent it;
    final static int CODE = 1;
    boolean std = false, bok = false, brk = false, fod = false, exe = false;
    public static ArrayList<String> list = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.addlist);

        memotext = findViewById(R.id.memo);
        memotext.setMovementMethod(new ScrollingMovementMethod());
        //데이터 가지고 오기
        loadData();
        for(int i=0;i<list.size();i++) {
            if(list.get(i).equals("STUDY")) std=true;
            else if(list.get(i).equals("BOOK")) bok=true;
            else if(list.get(i).equals("BREAKTIME")) brk=true;
            else if(list.get(i).equals("FOOD")) fod=true;
            else if(list.get(i).equals("EXERCISE")) exe=true;
            memotext.append(list.get(i) + "\n");
        }
    }

    public void settext(String str) {
         memotext.append(str + "\n");
    }

    public void btnMain(View v) {
        int id = v.getId();
        btn = findViewById(id);
        //버튼 마다 구현 해주기
        if (id == R.id.studybtn) {
            if (std) Toast.makeText(this, "이미 추가하였습니다. ", Toast.LENGTH_SHORT).show();
            else {
                std = true;
                settext(btn.getText().toString());
            }
        } else if (id == R.id.bookbtn) {
            if (bok) Toast.makeText(this, "이미 추가하였습니다. ", Toast.LENGTH_SHORT).show();
            else {
                bok = true;
                settext(btn.getText().toString());
            }
        } else if (id == R.id.breakbtn) {
            if (brk) Toast.makeText(this, "이미 추가하였습니다. ", Toast.LENGTH_SHORT).show();
            else {
                brk = true;
                settext(btn.getText().toString());
            }
        } else if (id == R.id.foodbtn) {
            if (fod) Toast.makeText(this, "이미 추가하였습니다. ", Toast.LENGTH_SHORT).show();
            else {
                fod = true;
                settext(btn.getText().toString());
            }
        } else if (id == R.id.exercisebtn) {
            if (exe) Toast.makeText(this, "이미 추가하였습니다. ", Toast.LENGTH_SHORT).show();
            else {
                exe = true;
                settext(btn.getText().toString());
            }
        } else if (id == R.id.otherbtn) { //other class에서 값 전달받아야함.
            it = new Intent(this, other.class);
            startActivityForResult(it, CODE);

            //시작 버튼
        } else if (id == R.id.startbtn) {//배열에 텍스트 다 넣어주기
            String[] str = memotext.getText().toString().split("\n");
            if (!(str[0].equals(""))) {
                list.clear();
                for (int i = 0; i < str.length; i++) {
                    if (str[i].equals("")) continue;
                    list.add(str[i]);
                }

            }
            it = new Intent(this, start.class);
            it.putStringArrayListExtra("list", list);
            startActivity(it);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 1:
                if (resultCode == RESULT_OK) {
                    String edtAddr = data.getStringExtra("TEXT");
                    String[] str=edtAddr.split("\n");
                    for(int i=0;i<str.length;i++)
                    {
                        if(str[i].equals("")) continue;
                        memotext.append(str[i]+"\n");
                    }
                } else {

                }
                break;
        }
    }

    //데이터 가지고 오기
    public void loadData()  {
        SharedPreferences preferences = getSharedPreferences("sharedpreferences2", MODE_PRIVATE);
        String json = preferences.getString("listitem2", null);
        if (json != null) {
           try {
               JSONArray a = new JSONArray(json);
               list.clear();
               for (int i = 0; i < a.length(); i++) {
                   String url = a.optString(i);
                   list.add(url);
               }
           }catch(JSONException e){
               e.printStackTrace();
           }
        }
    }
}



