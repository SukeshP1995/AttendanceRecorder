package com.example.attendancerecorder;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

class AttendanceContract {
    private AttendanceHelper mDbHelper;
    private SQLiteDatabase db;
     AttendanceContract(Context context){
        mDbHelper = new AttendanceHelper(context);
        db = mDbHelper.getWritableDatabase();
    }

    ContentValues values = new ContentValues();


    private static class AttendanceEntry implements BaseColumns {
        private static final String TABLE_NAME = "AttendanceEntry";
        private static final String COLUMN_NAME_DATE = "date";
        private static final String COLUMN_NAME_SESSION = "session";
        private static final String COLUMN_NAME_PENALTY = "penalty";
    }

    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + AttendanceEntry.TABLE_NAME + " (" +
                    AttendanceEntry.COLUMN_NAME_DATE + " TEXT," +
                    AttendanceEntry.COLUMN_NAME_SESSION + " INTEGER," +
                    AttendanceEntry.COLUMN_NAME_PENALTY + " FLOAT," +
                    "PRIMARY KEY (date, session))";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + AttendanceEntry.TABLE_NAME;


    public class AttendanceHelper extends SQLiteOpenHelper {
        private static final int DATABASE_VERSION = 1;
        private static final String DATABASE_NAME = "AttendanceDatabase.db";

        public AttendanceHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        public void onCreate(SQLiteDatabase db) {
            db.execSQL(SQL_CREATE_ENTRIES);
        }

        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL(SQL_DELETE_ENTRIES);
            onCreate(db);
        }

        public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            onUpgrade(db, oldVersion, newVersion);
        }

    }

    void insert(int session, double penalty){
        values.put(AttendanceEntry.COLUMN_NAME_SESSION, session);
        values.put(AttendanceEntry.COLUMN_NAME_PENALTY, penalty);
        values.put(AttendanceEntry.COLUMN_NAME_DATE,  new SimpleDateFormat("yyyy-MM-dd", new Locale("en", "IN")).format(Calendar.getInstance().getTime()));
        long newRowId = db.insert(AttendanceEntry.TABLE_NAME, null, values);
        System.out.println(newRowId);
    }
    private static final String TAG = "MyActivity";
    void update(int session){
        values = new ContentValues();
        String date = new SimpleDateFormat("yyyy-MM-dd", new Locale("en", "IN")).format(Calendar.getInstance().getTime());
        values.put(AttendanceEntry.COLUMN_NAME_PENALTY, 0.0);
        db.update(AttendanceEntry.TABLE_NAME,
                values,
                AttendanceEntry.COLUMN_NAME_DATE + " = ? AND " + AttendanceEntry.COLUMN_NAME_SESSION + " = ?",
                new String[]{date, session+"" });
    }
}


