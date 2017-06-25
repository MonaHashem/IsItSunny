package com.test.android.hashem.mona.weather.isitsunny;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

public class DayDetail extends AppCompatActivity {

    String min,max, hum, press, weatherMain, weatherDesc;
    TextView mMin,mMax, mHum, mPres, mWeatherMain, mWeatherDesc;
    ImageView image;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_day_detail);
        Intent parent = getIntent();
        min = parent.getStringExtra("min");
        max = parent.getStringExtra("max");
        hum = parent.getStringExtra("humidity");
        press = parent.getStringExtra("pressure");
        weatherMain = parent.getStringExtra("weatherMain");
        weatherDesc = parent.getStringExtra("weatherDesc");
        mMin = (TextView) findViewById(R.id.min);
        mMax = (TextView) findViewById(R.id.max);
        mHum = (TextView) findViewById(R.id.humidity);
        mPres = (TextView) findViewById(R.id.pressure);
        mWeatherMain = (TextView) findViewById(R.id.weather_main);
        mWeatherDesc = (TextView) findViewById(R.id.weather_desc);
        image = (ImageView) findViewById(R.id.image);
        mMin.setText(min);
        mMax.setText(max);
        mHum.setText(hum);
        mPres.setText(press);
        mWeatherMain.setText(weatherMain);
        mWeatherDesc.setText(weatherDesc);

    }
}
