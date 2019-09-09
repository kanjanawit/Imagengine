package com.kanjanawit.imagengine;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class PreferenceActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preference);
        getSupportFragmentManager().beginTransaction().replace(R.id.preference_container, new PreferenceFragment()).commit();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
}
