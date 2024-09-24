package com.example.todayproject;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

public class other extends AppCompatActivity {
    Intent it;
    EditText editmemo;
    Button btn;
    InputMethodManager imm;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       setContentView(R.layout.useradd);
        imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        it=getIntent();
        editmemo=findViewById(R.id.edit);
        imm.hideSoftInputFromWindow(editmemo.getWindowToken(),0);
        editmemo.setTextIsSelectable(true); //커서 보이게

        btn=findViewById(R.id.finish);
    }

    public void btnOther(View v)
    {
        int id=v.getId();
        if (id == R.id.finish) {//finish 버튼 누르면 값 전달하면서 종료
            final String TEXT = editmemo.getText().toString();
            it.putExtra("TEXT", TEXT);
            setResult(RESULT_OK, it);
            finish();
        } else if (id == R.id.cancel) {//cancel 버튼 누르면 그냥 종료
            setResult(RESULT_CANCELED);
            finish();
        }
    }
}
