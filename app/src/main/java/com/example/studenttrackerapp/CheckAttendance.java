package com.example.studenttrackerapp;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;


class FieldsAdapter extends ArrayAdapter<Fields> {
    /**
     *
     * @param context context of the app
     * @param fields Arraylist of the users
     */
    FieldsAdapter(Context context, ArrayList<Fields> fields) {
        super(context, 0, fields);
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
        Fields fields = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item1, parent, false);
        }
        // Lookup view for data population

        TextView date = convertView.findViewById(R.id.date);
        TextView leaves = convertView.findViewById(R.id.leaves);
        TextView session1 = convertView.findViewById(R.id.session1);
        TextView session2 = convertView.findViewById(R.id.session2);
        TextView session3 = convertView.findViewById(R.id.session3);
        TextView location1 = convertView.findViewById(R.id.location1);
        TextView location2 = convertView.findViewById(R.id.location2);
        TextView location3 = convertView.findViewById(R.id.location3);
        // Populate the data into the template view using the data object
        assert fields != null;
        date.setText(fields.date);
        String leave = "Leaves - "+fields.leaves;
        leaves.setText(leave);
        session1.setText(fields.session1);
        session2.setText(fields.session2);
        session3.setText(fields.session3);
        location1.setText(fields.location1);
        location2.setText(fields.location2);
        location3.setText(fields.location3);
        // Return the completed view to render on screen
        return convertView;
    }
}

public class CheckAttendance extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_attendance);
        ListView listView = findViewById(R.id.attendance_list_view);
        AttendanceContract ac = new AttendanceContract(getApplicationContext());
        ArrayAdapter<Fields> adapter = new FieldsAdapter(this, ac.showEntries());
        listView.setAdapter(adapter);
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
        if (id == R.id.profilePage) startActivity(new Intent(this, ProfilePage.class));
        if (id == R.id.courseProfile) startActivity(new Intent(this, CourseProfile.class));
        return true;
    }
}
