package com.test.android.hashem.mona.weather.isitsunny;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class DaysAdapter extends RecyclerView.Adapter<DaysAdapter.DaysAdapterViewHolder> {

    private String [] weatherInfo;


    private final DaysAdapterOnClickHandler mClickHandler;


    public interface DaysAdapterOnClickHandler {
        void onClick(String weatherForDay);
    }


    public DaysAdapter(DaysAdapterOnClickHandler clickHandler) {
        mClickHandler = clickHandler;
    }


    public class DaysAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public final TextView mDayNameTextView , mWeatherTextView;
        public final ImageView mImageView;

        public DaysAdapterViewHolder(View view) {
            super(view);
            mDayNameTextView = (TextView) view.findViewById(R.id.tv_day_name);
            mWeatherTextView = (TextView) view.findViewById(R.id.tv_weather);
            mImageView = (ImageView) view.findViewById(R.id.image_icon);
            view.setOnClickListener(this);
        }


        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            String weatherForDay = weatherInfo[adapterPosition];
            mClickHandler.onClick(weatherForDay);
        }
    }


    @Override
    public DaysAdapterViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();
        int layoutIdForListItem = R.layout.activity_days_adapter;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;
        View view = inflater.inflate(layoutIdForListItem, viewGroup, shouldAttachToParentImmediately);
        return new DaysAdapterViewHolder(view);
    }


    @Override
    public void onBindViewHolder(DaysAdapterViewHolder forecastAdapterViewHolder, int position) {
        String weatherForThisDay = weatherInfo[position];
        forecastAdapterViewHolder.mWeatherTextView.setText(weatherForThisDay);
    }


    @Override
    public int getItemCount() {
        if (weatherInfo == null) return 0;
        return weatherInfo.length;
    }


    public void setWeatherData(String[] weatherData) {
        weatherInfo = weatherData;
        notifyDataSetChanged();
    }
}