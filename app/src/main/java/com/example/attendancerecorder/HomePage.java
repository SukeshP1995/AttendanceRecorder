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
import java.util.TimeZone;

public class HomePage extends AppCompatActivity {
    private static final String TAG = "MyActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        Button button = findViewById(R.id.check_in_button);
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT+5:30"));
        Date now = calendar.getTime();
        SimpleDateFormat date = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
        String currentTime = date.format(now);

        final AttendanceContract attendanceContract = new AttendanceContract(getApplicationContext());
        attendanceContract.insert(1, 0.25);
        attendanceContract.insert(2, 0.25);
        attendanceContract.insert(3, 0.5);

        button.setOnClickListener(new View.OnClickListener() {
            Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT+5:30"));
            Date now = calendar.getTime();
            SimpleDateFormat date = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
            String currentTime = date.format(now);
            @Override
            public void onClick(View v) {
                if (currentTime.compareTo("08:50:00")>0 && currentTime.compareTo("09:10:00")<0){
                    attendanceContract.update(1);
                }
                else if (currentTime.compareTo("10:50:00")>0 && currentTime.compareTo("11:10:00")<0){
                    attendanceContract.update(2);
                }
                else if (currentTime.compareTo("13:50:00")>0 && currentTime.compareTo("14:10:00")<0){

                    attendanceContract.update(3);
                }
                System.out.println(attendanceContract.showEntries().toString());
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

