package com.kanjanawit.imagengine;

import android.content.ContentResolver;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.preference.PreferenceManager;
import androidx.print.PrintHelper;

import com.kanjanawit.imagengine.Application.MyApplication;
import com.kanjanawit.imagengine.Database.DatabaseConnection;
import com.kanjanawit.imagengine.Object.DetailImageData;
import com.kanjanawit.imagengine.Object.ImageData;

import java.io.IOException;

public class DetailImageActivity extends AppCompatActivity {
    DetailImageData mDetailImageData;
    Uri mFullImageUri;
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
        final ImageData imageData = intent.getParcelableExtra(RecyclerImageViewAdapter.IMAGEDATA_EXTRA);
        mFullImageUri = Uri.withAppendedPath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, imageData.getImageId());
        ImageView mainDetailImageView = findViewById(R.id.mainDetailImageView);
        mainDetailImageView.setImageURI(mFullImageUri);
        //load detail in background
        new Thread(new Runnable() {
            @Override
            public void run() {
                mDetailImageData = DatabaseConnection.getDetailImageData(MyApplication.getAppContext(), imageData);
            }
        }).run();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.detail_activity_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.detail_menu_view_file_detail) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            DetailImageDataDialogFragment detailImageDataDialogFragment = DetailImageDataDialogFragment.newInstance(mDetailImageData);
            detailImageDataDialogFragment.show(fragmentManager, "detail_dialog");
            return true;
        } else if (item.getItemId() == R.id.detail_menu_print) {
            PrintHelper printHelper = new PrintHelper(this);
            ContentResolver contentResolver = getContentResolver();
            printHelper.setScaleMode(PrintHelper.SCALE_MODE_FIT);
            try {
                Bitmap printBitmap = MediaStore.Images.Media.getBitmap(contentResolver, mFullImageUri);
                printHelper.printBitmap(mDetailImageData.getDisplayName(), printBitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (item.getItemId() == R.id.detail_menu_share) {
            //share
        }
        return super.onOptionsItemSelected(item);
    }
}
