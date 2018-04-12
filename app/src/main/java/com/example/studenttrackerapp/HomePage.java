package com.example.studenttrackerapp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.studenttrackerapp.R;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
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
        final String currentTime = date.format(now);

        final AttendanceContract attendanceContract = new AttendanceContract(getApplicationContext());
        attendanceContract.insert(1, 0.25);
        attendanceContract.insert(2, 0.25);
        attendanceContract.insert(3, 0.5);

        if (currentTime.compareTo("08:50:00")>0 && currentTime.compareTo("09:10:00")<0){
            startTimer(currentTime, "09:10:00");
        }
        else if (currentTime.compareTo("10:50:00")>0 && currentTime.compareTo("11:10:00")<0){
            startTimer(currentTime, "11:10:00");
        }
        else if (currentTime.compareTo("13:50:00")>0 && currentTime.compareTo("14:10:00")<0){
            startTimer(currentTime, "14:10:00");
        }

        button.setOnClickListener(new View.OnClickListener() {

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
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.profilePage) startActivity(new Intent(this, ProfilePage.class));
        if (id == R.id.checkAttendance) startActivity(new Intent(this, CheckAttendance.class));
        if (id == R.id.courseProfile) startActivity(new Intent(this, CourseProfile.class));
        return true;
    }

    public void showProfile(View view) {
        Intent intent = new Intent(this, ProfilePage.class);
        startActivity(intent);
    }

    public void checkAttendance(View view){
        Intent intent = new Intent(this, CheckAttendance.class);
        startActivity(intent);
    }

    void startTimer(String time1, String time2) {
        try{
            SimpleDateFormat date = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
            Date d1 = date.parse(time2);
            Date d2 = date.parse(time1);

            final TextView hh = findViewById(R.id.hh);
            final TextView mm = findViewById(R.id.mm);
            final TextView ss = findViewById(R.id.ss);
            long millis = d1.getTime() - d2.getTime();
            CountDownTimer countDownTimer = new CountDownTimer(millis, 1000) {

                public void onTick(long millisUntilFinished) {
                    long t =  millisUntilFinished / 1000;
                    DecimalFormat formatter = new DecimalFormat("00");
                    String hhFormatted = formatter.format(t/3600);
                    String mmFormatted = formatter.format((t%3600)/60);
                    String ssFormatted = formatter.format((t%3600)%60);
                    hh.setText(hhFormatted);
                    mm.setText(mmFormatted);
                    ss.setText(ssFormatted);
                }

                public void onFinish() {
                    hh.setText("00");
                    mm.setText("00");
                    ss.setText("00");
                }
            }.start();
        }
        catch (ParseException e){
            e.printStackTrace();
        }

    }
}

