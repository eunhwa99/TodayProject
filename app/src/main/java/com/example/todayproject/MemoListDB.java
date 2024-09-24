package com.example.todayproject;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class MemoListDB extends AppCompatActivity {

    private LinearLayout layout;

    private Button addbtn;
    private Intent it;
    private Button deletebtn;

    private DBManager dbmanager;
    private SQLiteDatabase db;
    ListView memolistview;

    public static ArrayList<ListViewItem> memoArr=new ArrayList<ListViewItem>();
    MemoListAdapter adapter;
    int i;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.memolistview);

        layout=findViewById(R.id.memoList);
        memolistview=findViewById(R.id.memolistview);

        //데이터 읽어오기
        readData();

        //리스트뷰 어댑터 저장
        adapter = new MemoListAdapter(this, R.layout.custom_memolistview, memoArr);

        memolistview.setAdapter(adapter);

        //메모 등록 버튼==>데이터 베이스 write
        addbtn=findViewById(R.id.addbtn);
        addbtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                it=new Intent(getApplicationContext(),WriteDB.class);
                startActivity(it);
            }
        });
    }

    //데이터 읽어오는 메소드
    private void readData(){
        try {
            dbmanager = new DBManager(this);
            db = dbmanager.getReadableDatabase();

            Cursor cursor = db.query("memodb", null, null,null,null,null,null);

            i=0;
            memoArr.clear();
            while(cursor.moveToNext()){
                String str_date=cursor.getString(cursor.getColumnIndex("date"));
                String str_content=cursor.getString(cursor.getColumnIndex("content"));

                ListViewItem temp=new ListViewItem(str_content,str_date);
                memoArr.add(temp);

            }
            cursor.close();

            db.close();
            dbmanager.close();
        } catch (SQLiteException e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
        }

    }

    //메모 목록 보여주는 리스트의 어댑터 구현
    class MemoListAdapter extends BaseAdapter{

        private Context context;
        private LayoutInflater inflater;
        private int layout;
        private ArrayList<ListViewItem> string;

        public MemoListAdapter(Context context, int alayout, ArrayList<ListViewItem> string) {
            this.context = context;
            this.string = string;
            layout = alayout;
            inflater = LayoutInflater.from(this.context);
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
            final String str_content=listViewItem.getName();

            TextView memo_date = convertView.findViewById(R.id.memo_date);
            TextView memo_content = convertView.findViewById(R.id.memo_content);

            memo_content.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showContent(pos,str_content);
                }
            });
            memo_date.setText(listViewItem.getTime());
            memo_content.setText(listViewItem.getName());

            return convertView;
        }
    }

    //클릭된 항목의 데이터를 가지고와서 보여주는 메소드
    public void showContent(int id, String str){
        Intent it=new Intent(this, ReadDB.class);
        it.putExtra("it_content", str);
        it.putExtra("id",id);
        startActivityForResult(it, 0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch(requestCode){
            case 0:
                if(resultCode==RESULT_OK){
                    memolistview.setAdapter(adapter);
                }
                else
                {

                }
                break;
        }
    }

}
