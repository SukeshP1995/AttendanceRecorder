package com.example.studenttrackerapp;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.Notification;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

import android.content.pm.PackageManager;
import android.os.Build;
import android.os.CountDownTimer;
import android.os.Handler;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;

public class HomePage extends AppCompatActivity {

    AttendanceContract attendanceContract;
    private ArrayList<String> permissionsToRequest;
    private ArrayList<String> permissionsRejected = new ArrayList<>();
    private ArrayList<String> permissions = new ArrayList<>();
    Context context = this;
    private final static int ALL_PERMISSIONS_RESULT = 101;
    LocationTrack locationTrack;
    Handler mHandler;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        attendanceContract = new AttendanceContract(getApplicationContext());
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        startService(new Intent(this, NotificationService.class));
        permissions.add(ACCESS_FINE_LOCATION);
        permissions.add(ACCESS_COARSE_LOCATION);
        locationTrack = new LocationTrack(context);
        permissionsToRequest = findUnAskedPermissions(permissions);
        ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {


            if (permissionsToRequest.size() > 0)
                requestPermissions(permissionsToRequest.toArray(new String[permissionsToRequest.size()]), ALL_PERMISSIONS_RESULT);
        }


        this.mHandler = new Handler();
        this.mHandler.post(m_Runnable);
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


    public void checkAttendance(View view){
        Intent intent = new Intent(this,  CheckAttendance.class);
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

    private ArrayList<String> findUnAskedPermissions(ArrayList<String> wanted) {
        ArrayList<String> result = new ArrayList<>();

        for (String perm : wanted) {
            if (!hasPermission(perm)) {
                result.add(perm);
            }
        }

        return result;
    }

    private boolean hasPermission(String permission) {
        if (canMakeSmores()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                return (checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED);
            }
        }
        return true;
    }

    private boolean canMakeSmores() {
        return (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1);
    }

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        switch (requestCode) {

            case ALL_PERMISSIONS_RESULT:
                for (String perms : permissionsToRequest) {
                    if (!hasPermission(perms)) {
                        permissionsRejected.add(perms);
                    }
                }

                if (permissionsRejected.size() > 0) {


                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (shouldShowRequestPermissionRationale(permissionsRejected.get(0))) {
                            showMessageOKCancel("These permissions are mandatory for the application. Please allow access.",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                                requestPermissions(permissionsRejected.toArray(new String[permissionsRejected.size()]), ALL_PERMISSIONS_RESULT);
                                            }
                                        }
                                    });
                            return;
                        }
                    }

                }

                break;
        }

    }

    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }

    private final Runnable m_Runnable = new Runnable()
    {
        public void run()

        {
            Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT+5:30"));
            Date now = calendar.getTime();
            SimpleDateFormat date = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
            final String currentTime = date.format(now);

            attendanceContract.insert(1, 0.25);
            attendanceContract.insert(2, 0.25);
            attendanceContract.insert(3, 0.50);

            if (currentTime.compareTo("08:50:00")>0 && currentTime.compareTo("09:10:00")<0){
                startTimer(currentTime, "09:10:00");
            }
            else if (currentTime.compareTo("10:20:00")>0 && currentTime.compareTo("11:10:00")<0){
                startTimer(currentTime, "11:10:00");
            }
            else if (currentTime.compareTo("13:50:00")>0 && currentTime.compareTo("14:10:00")<0){
                startTimer(currentTime, "14:10:00");
            }

            if (!locationTrack.canGetLocation()) {
                locationTrack.showSettingsAlert();

            }
            Button button = findViewById(R.id.check_in_button);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    int i = 0;
                    locationTrack = new LocationTrack(context);
                    if (currentTime.compareTo("08:50:00")>0 && currentTime.compareTo("09:10:00")<0){
                        i = 1;
                    }
                    else if (currentTime.compareTo("10:50:00")>0 && currentTime.compareTo("11:10:00")<0){
                        i = 2;
                    }
                    else if (currentTime.compareTo("12:34:00")>0 && currentTime.compareTo("14:10:00")<0){
                        i = 3;
                    }

                    if(i!=0){
                        if (locationTrack.canGetLocation()) {
                            System.out.println(locationTrack.getLocation());

                            double longitude = locationTrack.getLongitude();
                            double latitude = locationTrack.getLatitude();

                            attendanceContract.update(i, latitude+"N, "+longitude+"E");
                            Toast.makeText(getApplicationContext(), "Longitude:" + Double.toString(longitude) + "\nLatitude:" + Double.toString(latitude), Toast.LENGTH_SHORT).show();
                        } else {

                            locationTrack.showSettingsAlert();
                        }
                    }
                }
            });

            HomePage.this.mHandler.postDelayed(m_Runnable,5000);
        }

    };


    @Override
    protected void onDestroy() {
        super.onDestroy();
        locationTrack.stopListener();
    }

}

