package com.kanjanawit.imagengine;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.preference.PreferenceManager;

import com.kanjanawit.imagengine.Application.MyApplication;
import com.kanjanawit.imagengine.Database.DatabaseConnection;
import com.kanjanawit.imagengine.Object.DetailImageData;
import com.kanjanawit.imagengine.Object.ImageData;

public class DetailImageActivity extends AppCompatActivity {
    DetailImageData mDetailImageData;
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
        Uri imageUri = Uri.withAppendedPath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, imageData.getImageId());
        ImageView mainDetailImageView = findViewById(R.id.mainDetailImageView);
        mainDetailImageView.setImageURI(imageUri);
        new Thread(new Runnable() {
            @Override
            public void run() {
                mDetailImageData = DatabaseConnection.getDetailImageData(MyApplication.getAppContext(), imageData);
            }
        }).run();

    }

    /**
     * Initialize the contents of the Activity's standard options menu.  You
     * should place your menu items in to <var>menu</var>.
     *
     * <p>This is only called once, the first time the options menu is
     * displayed.  To update the menu every time it is displayed, see
     * {@link #onPrepareOptionsMenu}.
     *
     * <p>The default implementation populates the menu with standard system
     * menu items.  These are placed in the {@link Menu#CATEGORY_SYSTEM} group so that
     * they will be correctly ordered with application-defined menu items.
     * Deriving classes should always call through to the base implementation.
     *
     * <p>You can safely hold on to <var>menu</var> (and any items created
     * from it), making modifications to it as desired, until the next
     * time onCreateOptionsMenu() is called.
     *
     * <p>When you add items to the menu, you can implement the Activity's
     * {@link #onOptionsItemSelected} method to handle them there.
     *
     * @param menu The options menu in which you place your items.
     * @return You must return true for the menu to be displayed;
     * if you return false it will not be shown.
     * @see #onPrepareOptionsMenu
     * @see #onOptionsItemSelected
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.detail_activity_menu, menu);
        return true;
    }

    /**
     * This hook is called whenever an item in your options menu is selected.
     * The default implementation simply returns false to have the normal
     * processing happen (calling the item's Runnable or sending a message to
     * its Handler as appropriate).  You can use this method for any items
     * for which you would like to do processing without those other
     * facilities.
     *
     * <p>Derived classes should call through to the base class for it to
     * perform the default menu handling.</p>
     *
     * @param item The menu item that was selected.
     * @return boolean Return false to allow normal menu processing to
     * proceed, true to consume it here.
     * @see #onCreateOptionsMenu
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.detail_menu_view_file_detail) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            DetailImageDataDialogFragment detailImageDataDialogFragment = DetailImageDataDialogFragment.newInstance(mDetailImageData);
            detailImageDataDialogFragment.show(fragmentManager, "detail_dialog");
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }
}
