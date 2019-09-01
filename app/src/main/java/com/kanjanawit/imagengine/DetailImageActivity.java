package com.kanjanawit.imagengine;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;

public class DetailImageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_image);
        Intent intent = getIntent();
        String uriString = intent.getStringExtra(RecyclerImageViewAdapter.URI_EXTRA);
        Uri imageUri = Uri.parse(uriString);
        ImageView mainDetailImageView = findViewById(R.id.mainDetailImageView);
        mainDetailImageView.setImageURI(imageUri);
    }
}
