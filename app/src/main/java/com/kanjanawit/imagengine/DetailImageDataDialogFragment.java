package com.kanjanawit.imagengine;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.preference.PreferenceManager;

import com.kanjanawit.imagengine.Application.MyApplication;
import com.kanjanawit.imagengine.Object.DetailImageData;

import java.text.SimpleDateFormat;
import java.util.Locale;

public class DetailImageDataDialogFragment extends DialogFragment {
    //widget
    private TextView mDisplayNameTextView;
    private TextView mDateAddedTextView;
    private TextView mDateTakenTextView;
    private TextView mResolutionTextView;
    private TextView mFileSizeTextView;
    //variable
    private DetailImageData mDetailImagedata;

    public DetailImageDataDialogFragment() {
    }

    public static DetailImageDataDialogFragment newInstance(@NonNull DetailImageData detailImageData) {
        DetailImageDataDialogFragment detailImageDataDialogFragment = new DetailImageDataDialogFragment();
        Bundle args = new Bundle();
        args.putParcelable("detailimagedata", detailImageData);
        detailImageDataDialogFragment.setArguments(args);
        return detailImageDataDialogFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(MyApplication.getAppContext()); //get preferences
        boolean is_light_theme = sharedPreferences.getBoolean(getResources().getString(R.string.is_light_theme_key), true); //set theme from preference
        if (is_light_theme) {
            setStyle(DialogFragment.STYLE_NO_TITLE, R.style.AppTheme_LightMode_Dialog);
        } else {
            setStyle(DialogFragment.STYLE_NO_TITLE, R.style.AppTheme_DarkMode_Dialog); //TODO: No dark mode for now
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d("dialog", "inflating");
        return inflater.inflate(R.layout.dialog_imagedata_detail, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.d("dialog", "bind view");
        mDisplayNameTextView = view.findViewById(R.id.display_name_imagedata_textview);
        mDateAddedTextView = view.findViewById(R.id.date_added_imagedata_textview);
        mDateTakenTextView = view.findViewById(R.id.date_taken_imagedata_textview);
        mResolutionTextView = view.findViewById(R.id.resolution_imagedata_textview);
        mFileSizeTextView = view.findViewById(R.id.filesize_imagedata_textview);
        Log.d("dialog", "load data");
        mDetailImagedata = getArguments().getParcelable("detailimagedata");
        mDisplayNameTextView.setText(mDetailImagedata.getDisplayName());
        SimpleDateFormat sdf = new SimpleDateFormat("dd/mm/yyyy", Locale.ENGLISH);
        Log.d("Date added", String.valueOf(mDetailImagedata.getDateAdded().getTime()));
        mDateAddedTextView.setText(sdf.format(mDetailImagedata.getDateAdded()));
        mDateTakenTextView.setText(sdf.format(mDetailImagedata.getDateTaken()));
        mResolutionTextView.setText(mDetailImagedata.getImageWidth() + "x" + mDetailImagedata.getImageHeight());
        float fileSizeinKB = mDetailImagedata.getFileSize() / 1024;
        if (fileSizeinKB > 1024) {
            float fileSizeinMB = fileSizeinKB / 1024;
            mFileSizeTextView.setText(fileSizeinMB + " MB");
        } else {
            mFileSizeTextView.setText(fileSizeinKB + " KB");
        }
    }

}
