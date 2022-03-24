package com.example.jsonparsingtodb;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import org.json.JSONObject;

import java.util.ArrayList;

public class Database extends SQLiteOpenHelper {
    private String id;
    private String name;

    public Database(Context context){
        super(context, "MY_DB.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String tableNames = " CREATE TABLE Names(id VARCHAR, name VARCHAR)";
        sqLiteDatabase.execSQL(tableNames);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public void insertData(String id, String name){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("id", id);
        values.put("name", name);

        sqLiteDatabase.insert("Names", null, values);
    }

    public ArrayList fetchData(){
        ArrayList<String> stringArrayList = new ArrayList<String>();
        String fetchData = " SELECT * FROM Names ";
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();

        Cursor cursor = sqLiteDatabase.rawQuery(fetchData, null);
        if (cursor.moveToFirst()){
            do {
                stringArrayList.add(cursor.getString(0));
                stringArrayList.add(cursor.getString(1));
            }while (cursor.moveToNext());
        }
        return stringArrayList;
    }

    public void insertData(JSONObject obj) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("id", id);
        values.put("name", name);

        sqLiteDatabase.insert("Names", null, values);
    }
}
