package com.example.todayproject;

import android.content.ContentProviderClient;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.w3c.dom.Text;

import java.lang.reflect.Array;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class start extends AppCompatActivity {
    public static ArrayList<ListViewItem> sendArr = new ArrayList<ListViewItem>();
    private ListView listView;
    private MyAdapter Adapter;
    private Button finish;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.start);

        //데이터 저장
        saveData();
        //데이터 불러오기
        loadData("listitem");

        finish = findViewById(R.id.finish);
        finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(start.this, End.class);
                startActivity(it);
            }
        });

      //  Intent intent = getIntent();

        Adapter = new MyAdapter(this, R.layout.list_icontext, AddList.list);

        listView = findViewById(R.id.list);
        listView.setAdapter(Adapter);
        listView.invalidate();

    }

    //stopWatch class에 전달하는 intent
    public void intent(int pos) {
        Intent it = new Intent(this, stopwatch.class);
        String TEXT = (String) Adapter.getItem(pos);
        it.putExtra("TEXT", TEXT);
        startActivity(it);
    }


    //할 일 목록 나타내는 리스트 뷰
    class MyAdapter extends BaseAdapter implements View.OnClickListener {
        private Context context;
        private LayoutInflater inflater;
        private ArrayList<String> string;
        private int layout;
        AlertDialog.Builder alert;

        public MyAdapter(Context context, int alayout, ArrayList<String> string) {
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
        public View getView(int position, View convertView, ViewGroup parent) {
            final int pos = position;
            if (convertView == null) convertView = inflater.inflate(layout, parent, false);

            TextView txt = (TextView) convertView.findViewById(R.id.listtext);
            txt.setText(string.get(position));
            txt.setSelected(true);

            Button btn = (Button) convertView.findViewById(R.id.btn1);
            btn.setOnClickListener(this);

            //알림창 구현
            btn.setOnClickListener(new Button.OnClickListener() {
                @Override
                public void onClick(View v) {
                    alert = new AlertDialog.Builder(context);
                    alert.setTitle("프로그램");
                    alert
                            .setMessage("시작할까?")
                            .setCancelable(false)
                            .setPositiveButton("시작", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    intent(pos);
                                }
                            })
                            .setNegativeButton("취소", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            });
                    AlertDialog alertDialog = alert.create();
                    alertDialog.show();
                }

            });

            Button btn2;
            btn2 = convertView.findViewById(R.id.btn2);
            btn2.setOnClickListener(this);
            btn2.setOnClickListener(new Button.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final int count;
                    final int[] checked = new int[1];
                    count = Adapter.getCount();

                    alert = new AlertDialog.Builder(context);
                    alert.setTitle("완료");
                    alert
                            .setMessage("다 했어?")
                            .setCancelable(false)
                            .setPositiveButton("완료", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    if (count > 0) {
                                        checked[0] = pos;
                                        if (checked[0] > -1 && checked[0] < count) {
                                            Log.i("인덱스: ", String.valueOf(checked[0]));
                                          //  String str_content = (String) getItem(pos);
                                          //  deleteDB(str_content); //해당 인덱스 문자열 삭제(데이터 삭제)
                                            AddList.list.remove(checked[0]);
                                            Adapter.notifyDataSetChanged();
                                        }
                                    } else {
                                        listView.setBackgroundColor(Color.YELLOW);
                                    }
                                }
                            })
                            .setNegativeButton("취소", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();

                                }
                            });
                    AlertDialog alertDialog = alert.create();
                    alertDialog.show();
                }
            });

            return convertView;
        }

        @Override
        public void onClick(View v) {

        }
    }

    //데이터 호출
    public void loadData(String str) {
        SharedPreferences preferences = getSharedPreferences("sharedpreferences", MODE_PRIVATE);
        Gson gson = new Gson();
        String json = preferences.getString(str, null);

        if(str.equals("listitem")) {
            Type type = new TypeToken<ArrayList<ListViewItem>>() {
            }.getType();
            if (gson.fromJson(json, type) != null) {
                sendArr = gson.fromJson(json, type);
            }
        }

    }

    public void saveData() {
        SharedPreferences preferences = getSharedPreferences("sharedpreferences2", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(AddList.list);
        editor.putString("listitem2", json);
        //editor.putInt("num", num);
        editor.apply();
        /*if (DATA == null) {
            DATA = new ArrayList<>();
        }*/
    }
    @Override
    protected void onResume() {
        super.onResume();

        saveData();
    }
}