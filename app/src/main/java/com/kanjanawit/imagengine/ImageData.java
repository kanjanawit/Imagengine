package com.kanjanawit.imagengine;

import android.graphics.Bitmap;

import java.util.Date;

public class ImageData {
    private String mImageId;
    private Date mDateTaken;
    private Date mDateAdded;
    private String mDisplayName;
    private Bitmap mThumbNail;

    public ImageData(String mImageId, Date mDateTaken, Date mDateAdded, String mDisplayName, Bitmap mThumbNail) {
        this.mImageId = mImageId;
        this.mDateTaken = mDateTaken;
        this.mDateAdded = mDateAdded;
        this.mDisplayName = mDisplayName;
        this.mThumbNail = mThumbNail;
    }

    public String getImageId() {
        return mImageId;
    }

    public Date getDateTaken() {
        return mDateTaken;
    }

    public Date getDateAdded() {
        return mDateAdded;
    }

    public String getDisplayName() {
        return mDisplayName;
    }

    public Bitmap getThumbNail() {
        return mThumbNail;
    }
}
