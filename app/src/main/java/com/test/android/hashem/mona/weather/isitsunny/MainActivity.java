package com.test.android.hashem.mona.weather.isitsunny;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements DaysAdapter.DaysAdapterOnClickHandler {

    RecyclerView mRecyclerView;
    DaysAdapter daysAdapter;
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
}
