package com.test.android.hashem.mona.weather.isitsunny;

import android.content.Intent;
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
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements DaysAdapter.DaysAdapterOnClickHandler {

    RecyclerView mRecyclerView;
    DaysAdapter daysAdapter;
    RequestQueue requestQueue;
    ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        progressBar = (ProgressBar) findViewById(R.id.progressBar2);
        adjustRecyclerView();
        new WeatherAsyncTask().execute();
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
            Intent map = new Intent(this, MapsActivity.class);
            startActivity(map);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onClick(HashMap<String,String> weatherForDay) {
        //starts dayDetail intent
        Intent dayDetail = new Intent(this,DayDetail.class);
        for(HashMap.Entry<String,String> e: weatherForDay.entrySet())
            dayDetail.putExtra(e.getKey(),e.getValue());
        startActivity(dayDetail);
    }

    public class WeatherAsyncTask extends AsyncTask<String, Void, HashMap<String,String>[]> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected HashMap<String,String>[] doInBackground(String... params) {

            final HashMap<String,String> result [] = new HashMap [7];

            SharedPreferences sharedPreference = getSharedPreferences(getResources().getString(R.string.shared_preference),MODE_PRIVATE);
            int latDeg =sharedPreference.getInt(getResources().getString(R.string.latitude_degree),0);
            int latMin =sharedPreference.getInt(getResources().getString(R.string.latitude_minute),0);
            int latSec =sharedPreference.getInt(getResources().getString(R.string.latitude_second),0);
            int lonDeg =sharedPreference.getInt(getResources().getString(R.string.longitude_degree),0);
            int lonMin =sharedPreference.getInt(getResources().getString(R.string.longitude_minute),0);
            int lonSec =sharedPreference.getInt(getResources().getString(R.string.longitude_second),0);

            double lat = latDeg + (latMin/60.0) + (latSec/3600.0);
            double lon = lonDeg + (lonMin/60.0) + (lonSec/3600.0);
//            if(lat == 0 && lon ==0)
//            {
//                //dialogBox check networkConnection
//                return null;
//            }
            final boolean finish[] = new boolean[1];
            String url =getResources().getString(R.string.weather_api);
            url += "lat="+lat+"&lon="+lon+"&cnt=7&appid=" + getResources().getString(R.string.weather_key);
            JsonObjectRequest jsonObjectRequest= new JsonObjectRequest(Request.Method.GET,url,null,new Response.Listener<JSONObject>() {

                @Override
                public void onResponse(JSONObject response) {
                    //handle response
                    JSONArray list= null;
                    System.err.println("-----------------------hey");

                    try {
                        list= response.getJSONArray("list");

                        for(int i = 0; i < 7; i++) {
                            JSONObject day = list.getJSONObject(i);
                            JSONObject temp = day.getJSONObject("temp");
                            int min = (int) Math.round(temp.getDouble("min")) - 273;
                            int max = (int) Math.round(temp.getDouble("max")) - 273;
                            int pressure = (int) Math.round(day.getDouble("pressure"));
                            int humidity = (int) Math.round(day.getDouble("humidity"));
                            String weatherMain = day.getJSONArray("weather").getJSONObject(0).getString("main");
                            String weatherDesc = day.getJSONArray("weather").getJSONObject(0).getString("description");
                            result[i] = new HashMap<String,String>();
                            result[i].put("min",min+"");
                            result[i].put("max",max+"");
                            result[i].put("pressure",pressure+"");
                            result[i].put("humidity",humidity+"");
                            result[i].put("weatherMain",weatherMain);
                            result[i].put("weatherDesc",weatherDesc);
                        }
                        System.err.println("------------------------------------I am here");
                        result[0].put("day","Today");
                        result[1].put("day","Tomorrow");

                        Calendar calendar = Calendar.getInstance();
                        int day = calendar.get(Calendar.DAY_OF_WEEK);
                        String days [] = getResources().getStringArray(R.array.days_of_week);
                        for(int i = 2; i < 7; i++)
                            result[i].put("day",days[(i+day)%7]);
                        finish[0] = true;
                        System.err.println("------------------------------------I am here2");

                    } catch (JSONException e) {
                        e.printStackTrace();

                        finish[0]= true;
                    }



                }
            }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    // TODO Auto-generated method stub
                    NetworkResponse response = error.networkResponse;
                    if (error instanceof ServerError && response != null) {

                        try {
                            String res = new String(response.data,
                                    HttpHeaderParser.parseCharset(response.headers, "utf-8"));
                        System.out.println("res " +res);
                            // Now you can use any deserializer to make sense of data
                            JSONObject obj = new JSONObject(res);
                        } catch (UnsupportedEncodingException e1) {
                            // Couldn't properly decode data to string
                            e1.printStackTrace();
                        } catch (JSONException e2) {
                            // returned data is not JSONObject?
                            e2.printStackTrace();
                        }
                    }
                    finish[0]= true;
                }

            });

            requestQueue.add(jsonObjectRequest);

            while(!finish[0]){

                for(int i = 0; i < 100; i++);
                System.out.println("");
            }
            return result;
      }

        @Override
        protected void onPostExecute(HashMap<String,String>[] weatherData) {

            if (weatherData != null) {
                daysAdapter.setWeatherData(weatherData);
                progressBar.setVisibility(View.INVISIBLE);
                mRecyclerView.setVisibility(View.VISIBLE);
            } else {
                //show error message
                System.out.println("--------------------------------Error");
            }
        }
    }






}
