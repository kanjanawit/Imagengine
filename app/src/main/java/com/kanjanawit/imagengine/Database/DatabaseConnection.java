package com.kanjanawit.imagengine.Database;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

import com.kanjanawit.imagengine.Object.DetailImageData;
import com.kanjanawit.imagengine.Object.ImageData;

import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;

public class DatabaseConnection {
    public static ArrayList<ImageData> getAllImages(Context context) {
        Uri mediaImagesUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        String sortOrder = MediaStore.Images.Media.DATE_ADDED + " ASC";
        ContentResolver contentResolver = context.getContentResolver();
        String[] projection = {MediaStore.Images.Media._ID, MediaStore.Images.Media.DATE_ADDED, MediaStore.Images.Media.DATE_TAKEN, MediaStore.Images.Media.DISPLAY_NAME};
        Cursor imagecursor = contentResolver.query(mediaImagesUri, projection, null, null, sortOrder);
        ArrayList<ImageData> resultImageDatas = new ArrayList<>();

        if (imagecursor.moveToFirst()) {
            String id;
            Date dateTaken;
            Date dateAdded;
            String displayName;
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
                resultImageDatas.add(new ImageData(id, dateTaken, dateAdded, displayName));
            } while (imagecursor.moveToNext());
        }
        return resultImageDatas;
    }

    public static void deleteImage(Context context, ImageData imageData) {
        Uri mediaImagesUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        ContentResolver contentResolver = context.getContentResolver();
        String[] args = {imageData.getImageId()};
        contentResolver.delete(mediaImagesUri, MediaStore.Images.Media._ID + " = ?", args);
    }

    public static DetailImageData getDetailImageData(Context context, ImageData imageData) {
        DetailImageData returnDetailImageData;
        Uri mediaImagesUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        ContentResolver contentResolver = context.getContentResolver();
        String[] projection = {MediaStore.Images.Media._ID
                , MediaStore.Images.Media.SIZE
                , MediaStore.Images.Media.WIDTH
                , MediaStore.Images.Media.HEIGHT};
        String selection = MediaStore.Images.Media._ID + " = ?";
        String[] selectionArgs = {imageData.getImageId()};
        Cursor imagecursor = contentResolver.query(mediaImagesUri, projection, selection, selectionArgs, null);

        Objects.requireNonNull(imagecursor).moveToFirst();
        long fileSize;
        int imageWidth;
        int imageHeight;
        int fileSizeColumn = imagecursor.getColumnIndex(MediaStore.Images.Media.SIZE);
        int imageHeightColumn = imagecursor.getColumnIndex(MediaStore.Images.Media.HEIGHT);
        int imageWidthColumn = imagecursor.getColumnIndex(MediaStore.Images.Media.WIDTH);
        fileSize = imagecursor.getLong(fileSizeColumn);
        imageWidth = imagecursor.getInt(imageWidthColumn);
        imageHeight = imagecursor.getInt(imageHeightColumn);
        returnDetailImageData = new DetailImageData(imageData, fileSize, imageHeight, imageWidth);
        imagecursor.close();
        return returnDetailImageData;
    }
}
