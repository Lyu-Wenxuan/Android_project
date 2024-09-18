package com.example.myapplication;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class ChatLogHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "ChatLog.db";
    private static final String TABLE_NAME = "chatlog";
    private static final String CREATE_TABLE = "create table " + TABLE_NAME + "(" +
            "_id integer primary key autoincrement," +
            "value text" +
            ")";
    private SQLiteDatabase myDB = null;

    public ChatLogHelper(Context context) {
        super(context, DB_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_TABLE);
        myDB = sqLiteDatabase;
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public void insert(ContentValues values) {
        SQLiteDatabase DB = this.getWritableDatabase();
        DB.insert(TABLE_NAME, null, values);
        DB.close();
    }

    public Cursor select() {
        SQLiteDatabase DB = this.getReadableDatabase();
        Cursor cursor = DB.rawQuery("SELECT * FROM " + TABLE_NAME + " ORDER BY _id DESC LIMIT 40", null);
//        DB.close();
        return cursor;
    }

    public void deleteAll(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME,null,null);
        db.close();
    }

    public void close(){
        if(myDB != null){
            myDB.close();
        }
    }
}
