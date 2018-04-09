package com.example.attendancerecorder;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

class Details {
    public String field;
    public String value;

    Details(String field, String value) {
        this.field = field;
        this.value = value;
    }

    @Override
    public String toString() {
        return field+" "+value;
    }
}

class DetailsAdapter extends ArrayAdapter<Details> {
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

public class ProfilePage extends AppCompatActivity {
    ListView listView;
    HashMap<String, String> data;
    Bitmap decodedImage;

    /**
     *
     * @param savedInstanceState of type bundle
     * Runs after creating the layout
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_page);
        new GetDetails(this).execute();
    }

    @SuppressLint("StaticFieldLeak")
    private class GetDetails extends AsyncTask<Void, Void, Void>{
        String data = "";
        JSONObject myObj;
        Activity activity;
        private ProgressDialog progressDialog;

        /**
         *
         * @param activity pf type ProfilePage
         */
        GetDetails(ProfilePage activity) {
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
                URL url = new URL("http://msitis-iiith.appspot.com/api/profile/ag5ifm1zaXRpcy1paWl0aHIUCxIHU3R1ZGVudBiAgICAutyfCgw");
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
            ArrayList<Details> arrayOfUsers = new ArrayList<>();
            ArrayAdapter<Details> adapter = new DetailsAdapter(ProfilePage.this, arrayOfUsers);
            try {
                listView = findViewById(R.id.details_list_view);
                JSONObject obj = new JSONObject(data);
                JSONArray JA = obj.getJSONArray("data");
                myObj = JA.getJSONObject(0);
                String image_data = myObj.get("image").toString();
                adapter.add(new Details("Name", myObj.get("student_fullname").toString()));
                adapter.add(new Details("Roll No.", myObj.get("roll_number").toString()));
                adapter.add(new Details("Email ID", myObj.get("student_email").toString()));
                byte[] latinBytes = image_data.getBytes("ISO-8859-1");
                String b64 = Base64.encodeToString(latinBytes,Base64.DEFAULT);
                byte[] imgBytes = Base64.decode(b64, Base64.DEFAULT);
                System.out.println(imgBytes.length);
                decodedImage = BitmapFactory.decodeByteArray(imgBytes, 0, imgBytes.length);
                ImageView imageView = findViewById(R.id.profile_image);
                imageView.setImageBitmap(decodedImage);
                listView.setAdapter(adapter);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
