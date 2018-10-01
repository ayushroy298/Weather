package com.example.ayushroy.weather;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    public class DownloadTask extends AsyncTask<String, Void, String>
    {

        @Override
        protected String doInBackground(String... urls)
        {
            String result="";
            URL url;
            HttpURLConnection urlConnection=null;
            try
            {
                url=new URL(urls[0]);
                urlConnection=(HttpURLConnection)url.openConnection();
                InputStream in=urlConnection.getInputStream();
                InputStreamReader reader=new InputStreamReader(in);
                int data =reader.read();
                while(data!=-1)
                {
                    char current=(char) data;
                    result+=current;
                    data =reader.read();
                }
                return result;
            }
            catch (Exception e)
            {
                e.printStackTrace();
                return "Failed";
            }

        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try
            {
                JSONObject jsonObject=new JSONObject(s);
                String weatherInfo= jsonObject.getString("weather");
                String tempInfo= jsonObject.getString("main");
                //Log.i("Weather Content", weatherInfo);
                JSONArray arr =new JSONArray(weatherInfo);
                //JSONArray str=new JSONArray(tempInfo);
                for(int i=0;i<arr.length();i++)
                {
                    JSONObject jsonPart= arr.getJSONObject(i);
                    main.setText("Main: " + jsonPart.getString("main"));
                    description.setText("Description: " + jsonPart.getString("description"));
                    Log.i("main",jsonPart.getString("main"));
                    Log.i("description",jsonPart.getString("description"));
                }
                JSONObject jsonTemp=new JSONObject(tempInfo);
                double mx,mn,h;
                h=Double.parseDouble(jsonTemp.getString("humidity"));
                mx=Double.parseDouble(jsonTemp.getString("temp_max"))-273.15;
                mn=Double.parseDouble(jsonTemp.getString("temp_min"))-273.15;
                hum.setText("Humidity : " + String.format("%.1f",h));
                max.setText("Maximum Temperature : " + String.format("%.1f",mx) + "°C");
                min.setText("Minimum Temperature : " + String.format("%.1f",mn) + "°C");
            }
            catch(Exception e)
            {
                e.printStackTrace();
            }
        }
    }
    TextView heading;
    TextView main;
    TextView description;
    TextView max;
    TextView min;
    TextView hum;

    public void click(View view)
    {
        EditText editText= findViewById(R.id.editText);
        DownloadTask task=new DownloadTask();
        String result=null;

        if(editText.getText().toString().isEmpty())
        {
            Toast.makeText(this, "Please enter a city Name", Toast.LENGTH_SHORT).show();
        }
        else
        {
            heading.setText("The weather in " + editText.getText().toString() + " is as follows");
            result="http://api.openweathermap.org/data/2.5/weather?q="+editText.getText().toString()+"&appid=87b58b31dbf3549cdcf91a969f76639f";
            try
            {
                task.execute(result).get();
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }

        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        heading=findViewById(R.id.headingTextView);
        main=findViewById(R.id.mainTextView);
        description=findViewById(R.id.descriptionTextView);
        max=findViewById(R.id.maxTextView);
        min=findViewById(R.id.minTextView);
        hum=findViewById(R.id.humTextView);

    }
}
