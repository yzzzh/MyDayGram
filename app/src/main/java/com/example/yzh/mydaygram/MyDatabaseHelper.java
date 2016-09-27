package com.example.yzh.mydaygram;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by YZH on 2016/9/25.
 */
public class MyDatabaseHelper extends SQLiteOpenHelper {
    private static final String CREATE_DIARY = "create table Diary(" +
            "id integer primary key autoincrement," +
            "year text," +
            "month text," +
            "dayOfMonth text," +
            "dayOfWeek text," +
            "isWritten integer," +
            "content text)";

    public MyDatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory,int version){
        super(context,name,factory,version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_DIARY);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
