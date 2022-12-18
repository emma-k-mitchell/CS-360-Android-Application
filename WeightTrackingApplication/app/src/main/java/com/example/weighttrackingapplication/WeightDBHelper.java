package com.example.weighttrackingapplication;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.Date;

public class WeightDBHelper extends SQLiteOpenHelper {

    // database name
    public static final String DBNAME = "Weight.db";

    public WeightDBHelper(Context context) {
        super(context, "Weight.db", null, 1);
    }

    // create the weights table with date and weight as columns (TEXT = String, REAL = decimal value)
    @Override
    public void onCreate(SQLiteDatabase weightDB) {
        weightDB.execSQL("create Table weights(date TEXT, weight REAL)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase weightDB, int i, int i1) {
        weightDB.execSQL("drop Table if exists weights");
    }

    // Used to insert data into the weights database
    public void insertData (String date, Double weight) {
        SQLiteDatabase weightDB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put("date", date);
        contentValues.put("weight", weight);

        weightDB.insert("weights", null, contentValues);
    }

    // Used to update data entered into the weights database
    public void updateData(String date, Double weight){
        SQLiteDatabase weightDB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put("date", date);
        contentValues.put("weight", weight);

        Cursor cursor = weightDB.rawQuery("Select * from weights where date = ?", new String[] {date});
        if (cursor.getCount() > 0){
            weightDB.update("weights", contentValues,"date=?", new String[] {date});
        }
    }

    // Used to delete data entered into the weights database
    public void deleteData(String date){
        SQLiteDatabase weightDB = this.getWritableDatabase();

        Cursor cursor = weightDB.rawQuery("Select * from weights where date = ?", new String[] {date});

        if (cursor.getCount() > 0){
            weightDB.delete("weights","date=?", new String[] {date});
        }
    }
}
