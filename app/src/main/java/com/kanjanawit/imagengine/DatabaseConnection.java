package com.kanjanawit.imagengine;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;

import java.util.ArrayList;
import java.util.Date;

public class DatabaseConnection {
    public static ArrayList<ImageData> getAllImages(Context context) {
        Uri mediaImagesUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        String sortOrder = MediaStore.Images.Media.DATE_ADDED + " ASC";
        ContentResolver contentResolver = context.getContentResolver();
        String[] projection = {MediaStore.Images.Media._ID, MediaStore.Images.Media.DATE_ADDED, MediaStore.Images.Media.DATE_TAKEN, MediaStore.Images.Media.DISPLAY_NAME};
        Cursor imagecursor = contentResolver.query(mediaImagesUri, projection, null, null, sortOrder);
        ArrayList<ImageData> resultImageDatas = new ArrayList<ImageData>();

        if (imagecursor.moveToFirst()) {
            String id;
            Date dateTaken;
            Date dateAdded;
            String displayName;
            Bitmap thumbNail;
            int idColumn = imagecursor.getColumnIndex(
                    MediaStore.Images.Media._ID);
            int dateTakenColumn = imagecursor.getColumnIndex(
                    MediaStore.Images.Media.DATE_TAKEN);
            int dateAddedColumn = imagecursor.getColumnIndex(
                    MediaStore.Images.Media.DATE_ADDED);
            int displayNameColumn = imagecursor.getColumnIndex(
                    MediaStore.Images.Media.DISPLAY_NAME);
            do {
                id = imagecursor.getString(idColumn);
                dateTaken = new Date(imagecursor.getLong(dateTakenColumn));
                dateAdded = new Date(imagecursor.getLong(dateAddedColumn));
                displayName = imagecursor.getString(displayNameColumn);
                thumbNail = MediaStore.Images.Thumbnails.getThumbnail(contentResolver, Integer.parseInt(id), MediaStore.Images.Thumbnails.MICRO_KIND, null);
                resultImageDatas.add(new ImageData(id, dateTaken, dateAdded, displayName, thumbNail));
            } while (imagecursor.moveToNext());
        }
        return resultImageDatas;
    }

    public static void deleteImage(Context context, ImageData imageData) {
        Uri mediaImagesUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        ContentResolver contentResolver = context.getContentResolver();
        String[] args = new String[1];
        args[0] = imageData.getImageId();
        contentResolver.delete(mediaImagesUri, "_ID = ?", args);
    }
}
