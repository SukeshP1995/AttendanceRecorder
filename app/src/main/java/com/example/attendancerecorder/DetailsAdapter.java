package com.example.attendancerecorder;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class DetailsAdapter extends ArrayAdapter<Details> {
    /**
     *
     * @param context context of the app
     * @param users Arraylist of the users
     */
    DetailsAdapter(Context context, ArrayList<Details> users) {
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
        Details details = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item, parent, false);
        }
        // Lookup view for data population

        TextView field = convertView.findViewById(R.id.field);
        TextView value = convertView.findViewById(R.id.value);
        // Populate the data into the template view using the data object
        assert details != null;
        field.setText(details.field);
        value.setText(details.value);
        // Return the completed view to render on screen
        return convertView;
    }
}
