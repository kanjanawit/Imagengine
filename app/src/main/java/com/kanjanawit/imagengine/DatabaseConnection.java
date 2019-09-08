package com.kanjanawit.imagengine;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

import java.util.ArrayList;

public class DatabaseConnection {
    public static ArrayList<String> getAllImageIds(Context context) {
        Uri mediaImagesUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        ContentResolver contentResolver = context.getContentResolver();
        String[] projection = {MediaStore.Images.Media._ID};
        Cursor imagecursor = contentResolver.query(mediaImagesUri, projection, null, null, null);
        ArrayList<String> resultImageIds = new ArrayList<String>();

        if (imagecursor.moveToFirst()) {
            String id;
            int idColumn = imagecursor.getColumnIndex(
                    MediaStore.Images.Thumbnails._ID);
            do {
                // Get the field values
                id = imagecursor.getString(idColumn);
                Log.v("image id", id);
                // Do something with the values.
                resultImageIds.add(id);
            } while (imagecursor.moveToNext());
        }
        return resultImageIds;
    }

    public static void deleteImage(Context context, String imageId) {
        Uri mediaImagesUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        ContentResolver contentResolver = context.getContentResolver();
        String[] args = new String[1];
        args[0] = imageId;
        contentResolver.delete(mediaImagesUri, "_ID = ?", args);
    }
}
