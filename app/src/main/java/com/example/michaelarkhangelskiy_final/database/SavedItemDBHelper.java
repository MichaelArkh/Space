package com.example.michaelarkhangelskiy_final.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class SavedItemDBHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "items.db";
    private static final int DATABASE_VERSION = 1;
    private static final String table = "item";

    private static final String CREATE_TABLE =
            "create table " + table + " (_id integer primary key autoincrement, "
                    + "title text, author text, "
                    + "date text, summary text, "
                    + "image blob, click_link text);";

    public SavedItemDBHelper(@Nullable Context context){
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS item");
        onCreate(db);
    }

    public String getTable(){ return table; }
}
