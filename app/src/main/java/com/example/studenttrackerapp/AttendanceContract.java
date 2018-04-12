package com.example.studenttrackerapp;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
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

    ArrayList<Fields> showEntries(){
        ArrayList<Fields> allEntries = new ArrayList<>();
        String query = "SELECT  * FROM " + AttendanceEntry.TABLE_NAME ;
        String sumQuery = "SELECT "+ AttendanceEntry.COLUMN_NAME_DATE +", SUM("+AttendanceEntry.COLUMN_NAME_PENALTY+") AS sum FROM " + AttendanceEntry.TABLE_NAME  + " GROUP BY "+AttendanceEntry.COLUMN_NAME_DATE;
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        Cursor sumCursor = db.rawQuery(sumQuery, null);
        if (sumCursor != null && sumCursor.moveToFirst()){
            cursor.moveToFirst();
            do{

                int sumI = sumCursor.getColumnIndex("sum");
                String leaves = sumCursor.getString(sumI);
                int dateI = sumCursor.getColumnIndex(AttendanceEntry.COLUMN_NAME_DATE);
                String date = sumCursor.getString(dateI);
                int sessionI = cursor.getColumnIndex(AttendanceEntry.COLUMN_NAME_PENALTY);
                String session1;
                if (Double.parseDouble(cursor.getString(sessionI)) == 0){
                    session1 = "Present";
                }
                else{
                    session1 = "Absent";
                }
                cursor.moveToNext();
                sessionI = cursor.getColumnIndex(AttendanceEntry.COLUMN_NAME_PENALTY);
                String session2;
                if (Double.parseDouble(cursor.getString(sessionI)) == 0){
                    session2 = "Present";
                }
                else{
                    session2 = "Absent";
                }
                cursor.moveToNext();
                sessionI = cursor.getColumnIndex(AttendanceEntry.COLUMN_NAME_PENALTY);
                String session3;
                if (Double.parseDouble(cursor.getString(sessionI)) == 0){
                    session3 = "Present";
                }
                else{
                    session3 = "Absent";
                }
                cursor.moveToNext();
                allEntries.add(new Fields(date, leaves, session1, session2, session3));
            }while (sumCursor.moveToNext());
            cursor.close();
            sumCursor.close();
        }
        return  allEntries;
    }

}