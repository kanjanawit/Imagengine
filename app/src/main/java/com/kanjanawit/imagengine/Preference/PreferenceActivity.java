package com.kanjanawit.imagengine.Preference;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

import com.kanjanawit.imagengine.R;

public class PreferenceActivity extends AppCompatActivity implements SharedPreferences.OnSharedPreferenceChangeListener {
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
        setContentView(R.layout.activity_preference);
        sharedPreferences.registerOnSharedPreferenceChangeListener(this);
        getSupportFragmentManager().beginTransaction().replace(R.id.preference_container, new PreferenceFragment()).commit();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
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
        String is_light_theme_key = getResources().getString(R.string.is_light_theme_key);
        if (key.equals(is_light_theme_key)) {
            this.recreate();
        }
    }
}
