package com.kanjanawit.imagengine;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class DetailImageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_image);
        Intent intent = getIntent();
        String imageId = intent.getStringExtra(RecyclerImageViewAdapter.URI_EXTRA);
        Uri imageUri = Uri.withAppendedPath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, imageId);
        ImageView mainDetailImageView = findViewById(R.id.mainDetailImageView);
        mainDetailImageView.setImageURI(imageUri);
    }
}
