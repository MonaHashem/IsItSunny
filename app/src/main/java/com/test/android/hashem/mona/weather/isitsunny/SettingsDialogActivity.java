package com.test.android.hashem.mona.weather.isitsunny;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ToggleButton;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by mona on 25/06/17.
 */

public class SettingsDialogActivity extends Activity {

    ToggleButton C,F;
    Button mOk, mCancel;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_settings);
        C = (ToggleButton) findViewById(R.id.celcius);
        F = (ToggleButton) findViewById(R.id.feh);
        C.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                F.setChecked(false);
            }
        });
        F.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                C.setChecked(false);
            }
        });

        mOk = (Button) findViewById(R.id.ok);
        mCancel = (Button)findViewById(R.id.cancel);

        mOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sharedPreference = getSharedPreferences(getResources().getString(R.string.shared_preference),MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreference.edit();

                if(C.isChecked() || F.isChecked())
                    editor.putBoolean("C",C.isChecked());

                editor.commit();

                startActivity(new Intent(SettingsDialogActivity.this,MainActivity.class));
            }
        });

        mCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SettingsDialogActivity.this,MainActivity.class));
            }
        });


    }



}