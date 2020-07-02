package com.example.michaelarkhangelskiy_final.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

public class SavedItemDataSource {
    private SQLiteDatabase database;
    private SavedItemDBHelper helper;
    private String table;

    public SavedItemDataSource(Context context) {
        helper = new SavedItemDBHelper(context);
        table = helper.getTable();
    }

    public void open() throws SQLException {
        database = helper.getWritableDatabase();
    }

    public void close() {
        helper.close();
    }

    public ArrayList<SavedItem> getItems() {
        ArrayList<SavedItem> items = new ArrayList<SavedItem>();
        try {
            String query = "Select _id from " + table;
            Cursor cursor = database.rawQuery(query, null);
            cursor.moveToFirst();
            while(!cursor.isAfterLast()){
                items.add(getItem(cursor.getInt(0)));
                cursor.moveToNext();
            }
            cursor.close();
        } catch (Exception e) {
            Log.e("Database", "Database err occured");
        }
        return items;
    }

    public ArrayList<SavedItem> getItems(String sortBy, String sortOrder){
        ArrayList<SavedItem> items = new ArrayList<SavedItem>();
        try {
            String query = "Select _id from " + table + " order by " + sortBy + " " + sortOrder;
            Cursor cursor = database.rawQuery(query, null);
            cursor.moveToFirst();
            while(!cursor.isAfterLast()){
                items.add(getItem(cursor.getInt(0)));
                cursor.moveToNext();
            }
            cursor.close();
        } catch (Exception e) {
            Log.e("Database", "Database err occured");
        }
        return items;
    }

    public boolean insertItem(SavedItem i) {
        boolean succ = false;
        try {
            String query = "SELECT  * FROM " + table + " WHERE date ='" + i.getDate() +"'AND author ='" + i.getAuthor() + "'";
            Cursor cursor = database.rawQuery(query, null);
            if(cursor.getCount() > 0){
                cursor.close();
                return succ;
            }

            ContentValues cv = new ContentValues();

            cv.put("title", i.getTitle());
            cv.put("author", i.getAuthor());
            cv.put("date", i.getDate());
            cv.put("summary", i.getSummary());
            cv.put("image", i.getImage());
            cv.put("click_link", i.getClicked());

            succ = database.insert(table, null, cv) > 0;
        } catch (Exception e) {

        }
        return succ;
    }

    public boolean updateItem(SavedItem i){
        boolean succ = false;
        try {
            ContentValues cv = new ContentValues();

            cv.put("title", i.getTitle());
            cv.put("author", i.getAuthor());
            cv.put("date", i.getDate());
            cv.put("summary", i.getSummary());
            cv.put("image", i.getImage());
            cv.put("click_link", i.getClicked());

            succ = database.update(table, cv, "_id=" + (long)i.getItemId(), null) > 0;
        } catch (Exception e) {

        }
        return succ;
    }

    public SavedItem getItem(int id){
        SavedItem i = new SavedItem();
        String query = "SELECT  * FROM " + table + " WHERE _id =" + id;
        Cursor cursor = database.rawQuery(query, null);

        /**
         * (_id integer primary key autoincrement, "
         *                     + "title text, author text, "
         *                     + "date text, summary text, "
         *                     + "image blob, click_link text);";
         */

        if(cursor.moveToFirst()){
            i.setItemId(cursor.getInt(0));
            i.setTitle(cursor.getString(1));
            i.setAuthor(cursor.getString(2));
            i.setDate(cursor.getString(3));
            i.setSummary(cursor.getString(4));
            i.setImage(cursor.getString(5));
            i.setClicked(cursor.getString(6));
        }
        return i;
    }

    public boolean removeItem(SavedItem i){
        boolean succ = false;
        try {
            ContentValues cv = new ContentValues();

            succ = database.delete(table, "_id=" + (long)i.getItemId(), null) > 0;
        } catch (Exception e) {

        }
        return succ;
    }

    public int getLastID() {
        int lastId;
        try {
            String query = "Select MAX(_id) from " + table;
            Cursor cursor = database.rawQuery(query, null);

            cursor.moveToFirst();
            lastId = cursor.getInt(0);
            cursor.close();
        }
        catch (Exception e) {
            lastId = -1;
        }
        return lastId;
    }
}
