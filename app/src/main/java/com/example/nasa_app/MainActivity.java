package com.example.nasa_app;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.squareup.picasso.Picasso;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    private static final String NASA_API_KEY = "Zbfa6Fp28Zzhz7ptLA9hMd3zqwZ8GISAS2aersXf";
    private static final String NASA_API_URL = "https://api.nasa.gov/planetary/apod";

    private ImageView imageView;
    private TextView titleTextView;
    private TextView dateTextView;
    private TextView descriptionTextView;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imageView = findViewById(R.id.imageView);
        titleTextView = findViewById(R.id.titleTextView);
        dateTextView = findViewById(R.id.dateTextView);
        descriptionTextView = findViewById(R.id.descriptionTextView);

        // Perform network request in a background thread (AsyncTask)
        new FetchNasaDataTask().execute();
    }

    private class FetchNasaDataTask extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... voids) {
            try {
                URL url = new URL(NASA_API_URL + "?api_key=" + NASA_API_KEY);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                int responseCode = connection.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    StringBuilder response = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        response.append(line);
                    }
                    reader.close();
                    return response.toString();
                }
                connection.disconnect();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String response) {
            super.onPostExecute(response);
            if (response != null) {
                try {
                    JSONObject jsonObject = new JSONObject(response);

                    String imageUrl = jsonObject.getString("url");
                    String title = jsonObject.getString("title");
                    String date = jsonObject.getString("date");
                    String description = jsonObject.getString("explanation");

                    // Update UI
                    Picasso.get().load(imageUrl).into(imageView);
                    titleTextView.setText(title);
                    dateTextView.setText(date);
                    descriptionTextView.setText(description);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
