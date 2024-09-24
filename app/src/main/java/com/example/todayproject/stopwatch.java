package com.example.todayproject;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class stopwatch extends AppCompatActivity {
    TextView Title;
    TextView output;
    Button completebtn;
    Button stopbtn;
    public static boolean flag = true;

    final static int Init = 0;
    final static int Run = 1;
    final static int Pause = 2;

    int cur_Status = Run; //현재의 상태를 저장할변수를 초기화함.
    int myCount = 1;
    long myBaseTime;
    long myPauseTime;

    Intent it;
    private ListView listView;
    private ListViewAdapter Adapter;

    String doingnow; //지금 하고 있는 것

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.stopwatch);

        //텍스트 뷰 및 버튼 생성
        Title = findViewById(R.id.watchtext);
        output = findViewById(R.id.time_out);
        completebtn = findViewById(R.id.completebtn);
        stopbtn = findViewById(R.id.stopbtn);

        it = getIntent();

        doingnow = it.getExtras().getString("TEXT");
        Title.setText(doingnow); //지금 하고 있는 것을 타이머 위에 보이게 하기

        //Adapter 생성
        Adapter = new ListViewAdapter(this, R.layout.watchlist, start.sendArr);

        //리스트뷰 생성
        listView = findViewById(R.id.watchlist);
        listView.setAdapter(Adapter);

        //타이머 구현(시간 받아오기)
        myBaseTime = SystemClock.elapsedRealtime();
        myTimer.sendEmptyMessage(0);
        cur_Status = Run;
        flag=true;

        //타이머 구현(핸들러 사용)
        Handler delayHandler=new Handler();
        delayHandler.postDelayed(new Runnable(){
            @Override
            public void run(){
                startService(new Intent(getApplicationContext(),sensoractivity.class));
               //Toast.makeText(getApplicationContext(),"init: 서비스 시작",Toast.LENGTH_LONG).show();
            }
        },3000);

    }

    //리스트뷰어댑터
    class ListViewAdapter extends BaseAdapter {
        private Context context;
        private LayoutInflater inflater;
        private int layout;
        private ArrayList<ListViewItem> string;

        public ListViewAdapter(Context context, int alayout, ArrayList<ListViewItem> string) {
            this.context = context;
            this.string = string;
            layout = alayout;
            inflater = LayoutInflater.from(this.context);
        }

        @Override
        public void notifyDataSetChanged() {
            super.notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            return string.size();
        }

        @Override
        public Object getItem(int position) {
            return string.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }


        @Override
        public int getViewTypeCount() {
            return 2;
        }


        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            final int pos = position;
            if (convertView == null) convertView = inflater.inflate(layout, parent, false);
            ListViewItem listViewItem = string.get(position);

            //해야 할 일과 시간 텍스트뷰 받아오고 각각 설정하기
            TextView todotext = convertView.findViewById(R.id.todotext);
            TextView rec = convertView.findViewById(R.id.rec);
            todotext.setText(listViewItem.getName());
            rec.setText(listViewItem.getTime());
            return convertView;
        }

        //리스트 뷰에 아이템 추가하는 메소드
        public void addItem(String Name, String Time) {
            ListViewItem item = new ListViewItem(Name, Time);
            start.sendArr.add(item);
        }
    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
    }

    //스탑워치 구현
    public void myonclick(View v) {
        int id = v.getId();
        if (id == R.id.stopbtn) { //일시정지/시작 버튼
            switch (cur_Status) {

                case Init: //초기화 상태일 때 눌렸을 때
                    flag = true;
                    myBaseTime = SystemClock.elapsedRealtime();
                    System.out.println(myBaseTime);
                    myTimer.sendEmptyMessage(0);
                    //핸드폰이 꺼져있는 상태에서도 센서 감지할 수 있도록 서비스 사용, 3초 뒤에 서비스 시작
                    Handler delayHandler = new Handler();
                    delayHandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            startService(new Intent(getApplicationContext(), sensoractivity.class));
                            //   Toast.makeText(getApplicationContext(),"init: 서비스 시작",Toast.LENGTH_LONG).show();
                        }
                    }, 3000);

                    stopbtn.setText("일시정지");
                    cur_Status = Run;
                    break;

                case Run: //움직이고 있을 때 멈춘다
                    flag = false;
                    myTimer.removeMessages(0);
                    myPauseTime = SystemClock.elapsedRealtime();
                    stopbtn.setText("시작");
                    cur_Status = Pause;
                    stopService(new Intent(this, sensoractivity.class));
                    break;

                case Pause: //시작한다
                    flag = true;
                    long now = SystemClock.elapsedRealtime();
                    myTimer.sendEmptyMessage(0);
                    myBaseTime += (now - myPauseTime);
                    delayHandler = new Handler();
                    delayHandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            startService(new Intent(getApplicationContext(), sensoractivity.class));
                            //   Toast.makeText(getApplicationContext(),"pause에서 run: 서비스 시작",Toast.LENGTH_LONG).show();
                        }
                    }, 3000);
                    stopbtn.setText("일시정지");
                    cur_Status = Run;
                    break;
            }
        } else if (id == R.id.completebtn) { //완료 버튼 (리스트뷰에 추가)
            flag = false;
            myTimer.removeMessages(0);
            String str = output.getText().toString();

            Adapter.addItem(doingnow, str);
            listView.setAdapter(Adapter);

            //sharedpreferences 데이터 저장
            saveData();
            output.setText("00:00:00");
            cur_Status = Init;
            stopbtn.setText("시작");
            myCount = 1;
            stopService(new Intent(this, sensoractivity.class));
        }
    }

    //타이머 구현(핸들러 사용)
    Handler myTimer = new Handler() {
        @SuppressLint("HandlerLeak")
        public void handleMessage(Message msg) {
            output.setText(getTimeOut());
            myTimer.sendEmptyMessage(0);
        }
    };

    String getTimeOut() {
        long now = SystemClock.elapsedRealtime();
        long outTime = now - myBaseTime;

        long sec = outTime / 1000;
        long min = sec / 60;
        long hour = min / 60;
        sec = sec % 60;

        String real_outTime = String.format("%02d:%02d:%02d", hour, min, sec);
        return real_outTime;
    }

    private void saveData() {
        SharedPreferences preferences = getSharedPreferences("sharedpreferences", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(start.sendArr);
        editor.putString("listitem", json);
        editor.apply();
    }
}

