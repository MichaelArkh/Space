package com.example.space_final.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

/**
 * Database class
 */
public class SavedItemDBHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "items.db";
    private static final int DATABASE_VERSION = 3;
    private static final String table = "item";

    private static final String CREATE_TABLE =
            "create table " + table + " (_id integer primary key autoincrement, "
                    + "title text, author text, "
                    + "date text, summary text, "
                    + "image text, click_link text, status integer);";

    /**
     * Required constructor
     * @param context the context
     */
    public SavedItemDBHelper(@Nullable Context context){
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /**
     * Creates the table for the db
     * @param db
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);
    }

    /**
     * If database structure changes this method is called.
     * @param db
     * @param oldVersion
     * @param newVersion
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS item");
        onCreate(db);
    }

    public String getTable(){ return table; }
}
