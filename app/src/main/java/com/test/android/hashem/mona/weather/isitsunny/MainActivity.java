package com.test.android.hashem.mona.weather.isitsunny;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URL;

public class MainActivity extends AppCompatActivity implements DaysAdapter.DaysAdapterOnClickHandler {

    RecyclerView mRecyclerView;
    DaysAdapter daysAdapter;
    RequestQueue requestQueue;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        adjustRecyclerView();
    }


    protected void adjustRecyclerView(){

        LinearLayoutManager layoutManager
                = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);

        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);
        daysAdapter = new DaysAdapter(this);
        mRecyclerView.setAdapter(daysAdapter);
        requestQueue = Volley.newRequestQueue(this);

    }


    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_location) {
//           start location intent
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onClick(String weatherForDay) {
        //starts dayDetail intent
    }

    public class WeatherAsyncTask extends AsyncTask<String, Void, String[]> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected String[] doInBackground(String... params) {

            final String result [] = new String [7];
            SharedPreferences sharedPreference = getSharedPreferences(getResources().getString(R.string.shared_preference),MODE_PRIVATE);
            int latDeg =sharedPreference.getInt(getResources().getString(R.string.latitude_degree),0);
            int latMin =sharedPreference.getInt(getResources().getString(R.string.latitude_minute),0);
            int latSec =sharedPreference.getInt(getResources().getString(R.string.latitude_second),0);
            int lonDeg =sharedPreference.getInt(getResources().getString(R.string.longitude_degree),0);
            int lonMin =sharedPreference.getInt(getResources().getString(R.string.longitude_minute),0);
            int lonSec =sharedPreference.getInt(getResources().getString(R.string.longitude_second),0);

            double lat = latDeg + (latMin/60.0) + (latSec/3600.0);
            double lon = lonDeg + (lonMin/60.0) + (lonSec/3600.0);
            if(lat == 0 && lon ==0)
            {
                //dialogBox check networkConnection
                return null;
            }

            String url =getResources().getString(R.string.weather_api);
            url += "lat="+lat+"&lon="+lon+"&cnt=7";
            JsonObjectRequest jsonObjectRequest= new JsonObjectRequest(Request.Method.PUT,url,null,new Response.Listener<JSONObject>() {

                @Override
                public void onResponse(JSONObject response) {
                    //handle response
                    JSONArray list= null;
                    boolean correct = false;
                    try {
                        list= response.getJSONArray("list");

                        for(int i = 0; i < 7; i++) {
                            JSONObject day = list.getJSONObject(i);
                            JSONObject temp = day.getJSONObject("temp");
                            int min = (int) Math.round(temp.getDouble("min")) - 273;
                            int max = (int) Math.round(temp.getDouble("min")) - 273;
                            result[i] = min+"\\"+ max;
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();

                    }



                }
            }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    // TODO Auto-generated method stub

                }
            });

            requestQueue.add(jsonObjectRequest);
            return result;
      }

        @Override
        protected void onPostExecute(String[] weatherData) {

            if (weatherData != null) {
//                showWeatherDataView();
                daysAdapter.setWeatherData(weatherData);
            } else {
                //show error message
            }
        }
    }






}
