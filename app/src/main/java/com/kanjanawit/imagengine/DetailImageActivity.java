package com.kanjanawit.imagengine;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

public class DetailImageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this); //get preferences
        boolean is_light_theme = sharedPreferences.getBoolean(getResources().getString(R.string.is_light_theme_key), true); //set theme from preference
        if (is_light_theme) {
            setTheme(R.style.AppTheme_LightMode);
        } else {
            setTheme(R.style.AppTheme_DarkMode);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_image);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Intent intent = getIntent();
        ImageData imageData = intent.getParcelableExtra(RecyclerImageViewAdapter.IMAGEDATA_EXTRA);
        Uri imageUri = Uri.withAppendedPath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, imageData.getImageId());
        ImageView mainDetailImageView = findViewById(R.id.mainDetailImageView);
        mainDetailImageView.setImageURI(imageUri);
    }
}
