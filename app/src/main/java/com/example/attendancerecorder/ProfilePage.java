package com.example.attendancerecorder;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Array;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

class Details {
    public String field;
    public String value;

    public Details(String field, String value) {
        this.field = field;
        this.value = value;
    }

    @Override
    public String toString() {
        return field+" "+value;
    }
}

class DetailsAdapter extends ArrayAdapter<Details> {
    public DetailsAdapter(Context context, ArrayList<Details> users) {
        super(context, 0, users);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        Details details = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.activity_profile_page, parent, false);
        }
        // Lookup view for data population
        TextView field = (TextView) convertView.findViewById(R.id.field);
        TextView value = (TextView) convertView.findViewById(R.id.value);
        // Populate the data into the template view using the data object
        field.setText(details.field);
        value.setText(details.value);
        // Return the completed view to render on screen
        return convertView;
    }
}

public class ProfilePage extends AppCompatActivity {
    private final String TAG = ProfilePage.class.getSimpleName();
    private ListView lv;
    ArrayList<HashMap<String, String>> dataList;
    ArrayAdapter<Details> adapter;
    ListView listView;
    HashMap<String, String> data;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_page);


        new GetDetails().execute();
    }

    private class GetDetails extends AsyncTask<Void, Void, Void>{



        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Toast.makeText(ProfilePage.this,"Json Data is downloading",Toast.LENGTH_LONG).show();
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            data = new HashMap<>();
            HttpHandler sh = new HttpHandler();
            String url = "http://msitis-iiith.appspot.com/api/profile/ag5ifm1zaXRpcy1paWl0aHIUCxIHU3R1ZGVudBiAgICAutyfCgw";
            String jsonStr = sh.makeServiceCall(url);
            Log.e(TAG, "Response from url: " + jsonStr);

            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);

                    // Getting JSON Array node
                    JSONArray dataArray = jsonObj.getJSONArray("data");
                    int k = dataArray.length();
                    for (int i = 0; i < k; i++){
                        JSONObject d = dataArray.getJSONObject(i);
                        String image = d.getString("image");
                        String student_fullname = d.getString("student_fullname");
                        String roll_number = d.getString("roll_number");
                        String student_email = d.getString("student_email");

                        data.put("image", image);
                        data.put("student_fullname", student_fullname);
                        data.put("roll_number", roll_number);
                        data.put("student_email", student_email);

                    }
                }
                catch (final JSONException e) {
                    Log.e(TAG, "Json parsing error: " + e.getMessage());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(),
                                    "Json parsing error: " + e.getMessage(),
                                    Toast.LENGTH_LONG).show();
                        }
                    });

                }
            }
            else {
                Log.e(TAG, "Couldn't get json from server.");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(),
                                "Couldn't get json from server. Check LogCat for possible errors!",
                                Toast.LENGTH_LONG).show();
                    }
                });
            }


            return null;
        }


        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            ArrayList<Details> arrayOfUsers = new ArrayList<>();
            DetailsAdapter adapter = new DetailsAdapter(ProfilePage.this, arrayOfUsers);
            listView = (ListView) findViewById(R.id.details_list_view);
            adapter.add(new Details("Name", data.get("student_fullname")));
            adapter.add(new Details("Roll No.", data.get("roll_number")));
            adapter.add(new Details("Email ID", data.get("student_email")));
            String image = data.get("image");
            byte[] latin1;
            byte[] imageBytes = new byte[0];
            try {
                latin1 = image.getBytes("UTF-8");
                System.out.println(Arrays.toString(latin1));
                String imageString = Base64.encodeToString(latin1, Base64.DEFAULT);
                System.out.println("pulihora");
                System.out.println(imageString.substring(0,12));
                imageBytes = new String(latin1, "ISO-8859-1").getBytes("UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

//            imageBytes = Base64.decode(imageString, Base64.DEFAULT);
//            Bitmap decodedImage = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
//            ImageView imageView = (ImageView) findViewById(R.id.profile_image);
//            imageView.setImageBitmap(decodedImage);
            LayoutInflater inflater = getLayoutInflater();
            ViewGroup header = (ViewGroup)inflater.inflate(R.layout.list_view_header,listView,false);
            listView.addHeaderView(header);
            listView.setAdapter(adapter);

        }
    }
}
