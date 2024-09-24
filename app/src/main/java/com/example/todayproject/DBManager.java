package com.example.todayproject;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

//SQLite 데이터 베이스 구현
public class DBManager extends SQLiteOpenHelper {

    public DBManager(Context context) {
        super(context, "today.db",null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table memodb (date text, content text);");
        db.execSQL("create table plandb (time text, content text);");
        db.execSQL("create table finishlist (time text, content text);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

      db.execSQL("DROP TABLE IF EXISTS "+"memodb");
      db.execSQL("DROP TABLE IF EXISTS "+ "plandb");
        db.execSQL("DROP TABLE IF EXISTS "+ "finishlist");
      onCreate(db);
    }

}
