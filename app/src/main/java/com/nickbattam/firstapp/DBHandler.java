package com.nickbattam.firstapp;

import android.content.Context;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by nick on 01/12/15.
 */
public class DBHandler extends SQLiteOpenHelper {
// Database Version
    private static final int DATABASE_VERSION = 1;
    // Database Name
    private static final String DATABASE_NAME = "bloodInfo";

    private static final String TABLE_BLOODS = "bloods";

    private static final String KEY_ID = "id";
    private static final String KEY_VALUE = "value";
    private static final String KEY_DATETIME = "datetime";

    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat(
            "yyyy-MM-dd HH:mm:ss", Locale.getDefault());

    public DBHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_BLOODS_TABLE = "CREATE TABLE " + TABLE_BLOODS + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_VALUE + " REAL,"
                + KEY_DATETIME + " TEXT" + ")";
        db.execSQL(CREATE_BLOODS_TABLE);
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
// Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_BLOODS);
// Creating tables again
        onCreate(db);
    }

    // Adding new blood
    public void addBlood(Blood blood) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_VALUE, blood.getValue()); // Blood reading
        values.put(KEY_DATETIME, datetime_to_string(blood.getDatetime())); // Blood timestamp
// Inserting Row
        db.insert(TABLE_BLOODS, null, values);
        db.close(); // Closing database connection
    }

    // Getting one blood
    public Blood getBlood(int id) throws NullPointerException {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_BLOODS, new String[]{KEY_ID,
                        KEY_DATETIME, KEY_VALUE}, KEY_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);

        if (cursor != null)
            cursor.moveToFirst();

// return
        return new Blood(
                Integer.parseInt(cursor.getString(0)),
                string_to_datetime(cursor.getString(1)),
                cursor.getDouble(2));
    }


    // Getting All Bloods
    public List<Blood> getAllBloods() {
        List<Blood> bloodList = new ArrayList<>();
// Select All Query
        String selectQuery = "SELECT * FROM " + TABLE_BLOODS;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = null;
        try {
            cursor = db.rawQuery(selectQuery, null);
// looping through all rows and adding to list
            if (cursor.moveToFirst()) {
                do {
                    Blood blood = new Blood();
                    blood.setId(Integer.parseInt(cursor.getString(0)));
                    blood.setValue(cursor.getDouble(1));
                    blood.setDatetime(string_to_datetime(cursor.getString(2)));
// Adding contact to list
                    bloodList.add(blood);
                } while (cursor.moveToNext());
            }
        }
        finally {
            if (cursor != null) {
                cursor.close();
            }
        }
// return contact list
        return bloodList;
    }


    // Updating a blood
    public int updateBlood(Blood blood) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_VALUE, blood.getValue());
        values.put(KEY_DATETIME, datetime_to_string(blood.getDatetime()));
// updating row
        return db.update(TABLE_BLOODS, values, KEY_ID + " = ?",
                new String[]{String.valueOf(blood.getId())});
    }

    // Deleting a blood
    public void deleteBlood(Blood blood) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_BLOODS, KEY_ID + " = ?",
                new String[]{String.valueOf(blood.getId())});
        db.close();
    }

    // Getting blood Count
    public int getBloodsCount() {
        String countQuery = "SELECT * FROM " + TABLE_BLOODS;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.close();
// return count
        return cursor.getCount();
    }

    private Date string_to_datetime(String datetime_str) {
        Date datetime;
        try {
            datetime = DATE_FORMAT.parse(datetime_str);
        }
        catch (ParseException ex) {
            datetime = new Date();
            Log.e("TrackerApp", "Error parsing blood datestamp: " + datetime_str, ex);
        }
        return datetime;
    }

    private String datetime_to_string(Date datetime) {
        return DATE_FORMAT.format(datetime);
    }
}
