package com.example.attendancerecorder;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;


public class HomePage extends AppCompatActivity {
    private static final String TAG = "MyActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        Button button = findViewById(R.id.check_in_button);
        final Calendar session1before = Calendar.getInstance();
        final Calendar session1after = Calendar.getInstance();
        final Calendar session2before = Calendar.getInstance();
        final Calendar session2after = Calendar.getInstance();
        final Calendar session3before = Calendar.getInstance();
        final Calendar session3after = Calendar.getInstance();
        final Calendar calendar = Calendar.getInstance();

        try{
            session1before.setTime(new SimpleDateFormat("HH:mm:ss",new Locale("en", "IN")).parse("08:50:00"));
            session1after.setTime(new SimpleDateFormat("HH:mm:ss",new Locale("en", "IN")).parse("09:10:00"));
            session2before.setTime(new SimpleDateFormat("HH:mm:ss",new Locale("en", "IN")).parse("10:50:00"));
            session2after.setTime(new SimpleDateFormat("HH:mm:ss",new Locale("en", "IN")).parse("11:10:00"));
            session3before.setTime(new SimpleDateFormat("HH:mm:ss",new Locale("en", "IN")).parse("14:50:00"));
            session3after.setTime(new SimpleDateFormat("HH:mm:ss",new Locale("en", "IN")).parse("16:30:00"));
        }
        catch (Exception e){
            e.getStackTrace();
        }
        int flag;
        if (calendar.after(session1before.getTime()) && calendar.before(session1after.getTime())){
            flag = 1;
        }
        else if (calendar.after(session2before.getTime()) && calendar.before(session2after.getTime())){
            flag = 2;
        }
        else if (calendar.after(session3before.getTime()) && calendar.before(session3after.getTime())){
            flag = 3;
        }

        final AttendanceContract attendanceContract = new AttendanceContract(getApplicationContext());
        attendanceContract.insert(1, 0.25);
        attendanceContract.insert(2, 0.25);
        attendanceContract.insert(3, 0.5);

        button.setOnClickListener(new View.OnClickListener() {
            Calendar calendar = Calendar.getInstance();
            @Override
            public void onClick(View v) {

                if (calendar.after(session1before.getTime()) && calendar.before(session1after.getTime())){
                    attendanceContract.update(1);
                }
                else if (calendar.after(session2before.getTime()) && calendar.before(session2after.getTime())){
                    attendanceContract.update(2);
                }
                else if (calendar.after(session3before.getTime()) && calendar.before(session3after.getTime())){
                    attendanceContract.update(3);
                }
            }
        });
    }

    public void showProfile(View view) {
        Intent intent = new Intent(this, ProfilePage.class);
        startActivity(intent);
    }

    public void checkAttendance(View view){
        Intent intent = new Intent(this, CheckAttendance.class);
        startActivity(intent);
    }
}
