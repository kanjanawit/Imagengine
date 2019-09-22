package com.kanjanawit.imagengine;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.kanjanawit.imagengine.Application.MyApplication;
import com.kanjanawit.imagengine.Database.DatabaseConnection;
import com.kanjanawit.imagengine.Object.ImageData;
import com.kanjanawit.imagengine.Preference.PreferenceActivity;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements SharedPreferences.OnSharedPreferenceChangeListener, SearchView.OnQueryTextListener {
    //Constant
    private static final int DEFAULT_COLUMNS_NUMBERS = 3;
    private static final int REQUEST_PERMISSION = 9998;
    private RecyclerImageViewAdapter mRecyclerImageViewAdapter;
    private ProgressBar mLoadingBar;
    //private widgets
    private RecyclerView mRecyclerView;
    //private variable
    private boolean permissionGranted = false;

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
        setContentView(R.layout.activity_main); //load main layout
        sharedPreferences.registerOnSharedPreferenceChangeListener(this); // set preference change listener
        mRecyclerView = findViewById(R.id.main_recyclerview);
        mLoadingBar = findViewById(R.id.loading_progressbar);
        GetExternalReadWriteStoragePermission(); //check permission to load/save/delete picture if passed load datas
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_activity_menu, menu);
        MenuItem searchItem = menu.findItem(R.id.main_menu_action_search);
        SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setOnQueryTextListener(this);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.main_menu_to_preferences_menu_item) {
            Intent intent = new Intent(this, PreferenceActivity.class);
            startActivity(intent);
            return true;
        } else if (item.getItemId() == R.id.main_menu_sort_by_name) {
            mRecyclerImageViewAdapter.sortByName();
        } else if (item.getItemId() == R.id.main_menu_sort_by_date_taken) {
            mRecyclerImageViewAdapter.sortByDateTaken();
        } else if (item.getItemId() == R.id.main_menu_sort_by_date_added) {
            mRecyclerImageViewAdapter.sortByDateAdded();
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Called when a shared preference is changed, added, or removed. This
     * may be called even if a preference is set to its existing value.
     *
     * <p>This callback will be run on your main thread.
     *
     * @param sharedPreferences The {@link SharedPreferences} that received
     *                          the change.
     * @param key               The key of the preference that was changed, added, or
     */
    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        String col_numbers_key = getResources().getString(R.string.numbers_of_column_preference_key);
        String is_light_theme_key = getResources().getString(R.string.is_light_theme_key);
        if (key.equals(col_numbers_key)) {
            String default_col = String.valueOf(DEFAULT_COLUMNS_NUMBERS);
            int new_columns_number = Integer.parseInt(sharedPreferences.getString(col_numbers_key, default_col));
            mRecyclerView.setLayoutManager(new GridLayoutManager(MyApplication.getAppContext(), new_columns_number));
        } else if (key.equals(is_light_theme_key)) {
            this.recreate();
        }
    }

    /**
     * Called when the user submits the query. This could be due to a key press on the
     * keyboard or due to pressing a submit button.
     * The listener can override the standard behavior by returning true
     * to indicate that it has handled the submit request. Otherwise return false to
     * let the SearchView handle the submission by launching any associated intent.
     *
     * @param query the query text that is to be submitted
     * @return true if the query has been handled by the listener, false to let the
     * SearchView perform the default action.
     */
    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        mRecyclerImageViewAdapter.getFilter().filter(newText);
        return true;
    }

    private void refreshGridView() {
        AsyncTask backgroundWork = new AsyncTask() {
            ArrayList<ImageData> imageDatas;

            protected void onPreExecute() {
                mRecyclerView.setVisibility(View.GONE);
                mLoadingBar.setVisibility(View.VISIBLE);
            }

            @Override
            protected Object doInBackground(Object[] objects) {
                imageDatas = DatabaseConnection.getAllImages(getApplicationContext());
                return null;
            }

            @Override
            protected void onPostExecute(Object o) {
                mRecyclerImageViewAdapter = new RecyclerImageViewAdapter(getApplicationContext(), imageDatas);
                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(MyApplication.getAppContext());
                String col_key = getResources().getString(R.string.numbers_of_column_preference_key);
                String default_col = String.valueOf(DEFAULT_COLUMNS_NUMBERS);
                int columns = Integer.parseInt(sharedPreferences.getString(col_key, default_col));
                mRecyclerView.setLayoutManager(new GridLayoutManager(MyApplication.getAppContext(), columns));
                mRecyclerView.setAdapter(mRecyclerImageViewAdapter);
                mRecyclerView.setVisibility(View.VISIBLE);
                mLoadingBar.setVisibility(View.GONE);
            }
        };
        backgroundWork.execute();
    }

    /**
     * Callback for the result from requesting permissions. This method
     * is invoked for every call on {@link #requestPermissions(String[], int)}.
     * <p>
     * <strong>Note:</strong> It is possible that the permissions request interaction
     * with the user is interrupted. In this case you will receive empty permissions
     * and results arrays which should be treated as a cancellation.
     * </p>
     *
     * @param requestCode  The request code passed in {@link #requestPermissions(String[], int)}.
     * @param permissions  The requested permissions. Never null.
     * @param grantResults The grant results for the corresponding permissions
     *                     which is either {@link PackageManager#PERMISSION_GRANTED}
     *                     or {@link PackageManager#PERMISSION_DENIED}. Never null.
     * @see #requestPermissions(String[], int)
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_PERMISSION) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                permissionGranted = true;
                Log.d("Permission", "READ/WRITE granted");
            } else {
                Log.d("Permission", "Permission Denied");
                finish();
            }
        }
        if (permissionGranted) {
            Log.d("Permission", "Starting app");
            refreshGridView();
        }
    }

    private void GetExternalReadWriteStoragePermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if ((this.checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) ||
                    (this.checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                            != PackageManager.PERMISSION_GRANTED)) {
                this.requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        REQUEST_PERMISSION);
                Log.d("Permission", "Request READ/WRITE");
            } else {
                permissionGranted = true;
                Log.d("Permission", "READ/WRITE already granted");
                Log.d("Permission", "Starting app");
                refreshGridView();
            }

        }
    }
}
