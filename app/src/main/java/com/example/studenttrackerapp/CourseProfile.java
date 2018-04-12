package com.example.studenttrackerapp;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.studenttrackerapp.CheckAttendance;
import com.example.studenttrackerapp.HomePage;
import com.example.studenttrackerapp.ProfilePage;
import com.example.studenttrackerapp.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ArrayList;

class CourseDetails{
    String course_id, course_name, grade, mentor;
    CourseDetails(String course_id,String course_name,String grade,String mentor){
        this.course_id = course_id;
        this.course_name = course_name;
        this.grade = grade;
        this.mentor = mentor;
    }
}

class CourseDetailsAdapter extends ArrayAdapter<CourseDetails>{
    /**
     *
     * @param context context of the app
     * @param users Arraylist of the users
     */
    CourseDetailsAdapter(Context context, ArrayList<CourseDetails> users) {
        super(context, 0, users);
    }

    /**
     *
     * @param position of type int
     * @param convertView of type View
     * @param parent of type ViewGroup
     * @return of type view
     */
    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        // Get the data item for this position
        CourseDetails courseDetails = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item2, parent, false);
        }
        // Lookup view for data population

        TextView course_id = convertView.findViewById(R.id.course_id);
        TextView course_name = convertView.findViewById(R.id.course_name);
        TextView grade = convertView.findViewById(R.id.grade);
        TextView mentor = convertView.findViewById(R.id.mentor);
        // Populate the data into the template view using the data object
        assert courseDetails != null;
        course_id.setText(courseDetails.course_id);
        course_name.setText(courseDetails.course_name);
        grade.setText(courseDetails.grade);
        mentor.setText(courseDetails.mentor);
        // Return the completed view to render on screen
        return convertView;
    }
}

public class CourseProfile extends AppCompatActivity {
    ListView listView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_profile);
        new GetDetails(this).execute();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.homePage) startActivity(new Intent(this, HomePage.class));
        if (id == R.id.checkAttendance) startActivity(new Intent(this, CheckAttendance.class));
        if (id == R.id.profilePage) startActivity(new Intent(this, ProfilePage.class));
        return true;
    }

    @SuppressLint("StaticFieldLeak")
    private class GetDetails extends AsyncTask<Void, Void, Void> {
        String data = "";
        JSONObject myObj;
        Activity activity;
        private ProgressDialog progressDialog;

        /**
         *
         * @param activity pf type ProfilePage
         */
        GetDetails(CourseProfile activity) {
            progressDialog = new ProgressDialog(activity);
            this.activity = activity;
        }

        /**
         * Runs before a background process
         */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.setMessage("Downloading your data...");
            progressDialog.show();
            progressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                public void onCancel(DialogInterface arg0) {
                    GetDetails.this.cancel(true);
                }
            });
        }

        /**
         *
         * @param arg0 of type Void
         * @return returns null
         */
        @Override
        protected Void doInBackground(Void... arg0) {
            try {
                URL url = new URL("http://msitis-iiith.appspot.com/api/course/ag5ifm1zaXRpcy1paWl0aHIUCxIHU3R1ZGVudBiAgICAutyfCgw");
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                InputStream inputStream = connection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                String line = "";
                StringBuilder dataBuilder = new StringBuilder("");
                while(line != null){
                    line = bufferedReader.readLine();
                    dataBuilder = dataBuilder.append(data).append(line);
                }
                data = dataBuilder.toString();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }


        /**
         *
         * @param result of type Void
         *               The function takes result as argument and runs after the background process finishes
         */
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            ArrayList<CourseDetails> arrayOfUsers = new ArrayList<>();
            ArrayAdapter<CourseDetails> adapter = new CourseDetailsAdapter(CourseProfile.this, arrayOfUsers);
            try {
                double cgpa = 0.0;
                int tc = 0;
                listView = findViewById(R.id.details_list_view);
                JSONObject obj = new JSONObject(data);
                JSONArray JA = obj.getJSONArray("data");
                for (int i=0; i<JA.length(); i++){
                    myObj = JA.getJSONObject(i);
                    String grade = myObj.get("grade").toString();
                    String course_name =  myObj.get("course_name").toString();
                    adapter.add(new CourseDetails(myObj.get("course_id").toString(), course_name, grade, myObj.get("mentor_name").toString()));
                    cgpa += getGP(grade)*getMultiplier(course_name);
                    tc += getMultiplier(course_name);
                }
                cgpa = cgpa/tc;
                DecimalFormat formatter = new DecimalFormat("0.00");
                TextView textView = findViewById(R.id.cgpa);
                String text = "CGPA: "+formatter.format(cgpa);
                textView.setText(text);
                listView.setAdapter(adapter);
                progressDialog.dismiss();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        double getGP(String grade){
            if (grade.equals("Ex"))
                return 10.0;
            if (grade.equals("A+"))
                return 9.5;
            if (grade.equals("A"))
                return 9.0;
            if (grade.equals("B+"))
                return 8.5;
            if (grade.equals("B"))
                return 8.0;
            if (grade.equals("C"))
                return 7.0;
            return 8.0;
        }

        int getMultiplier(String course){
            if(course.equals("Digital Literacy")) return 1;
            if(course.equals("Computational Thinking")) return 2;
            if(course.equals("CSPP-1")) return 4;
            if(course.equals("CSPP-2")) return 4;
            if(course.equals("ADS-1")) return 4;
            if(course.equals("ADS-1")) return 4;
            if(course.equals("DBMS")) return 2;
            if(course.equals("Computer Network Foundation")) return 2;
            if(course.equals("Cyber Security")) return 2;
            if(course.equals("Introduction to Computer Systems")) return 4;
            if(course.equals("Web programming")) return 3;
            if(course.equals("Mobile Programming")) return 3;
            return 3;
        }
    }
}
