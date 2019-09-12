package com.kanjanawit.imagengine;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

public class ImageData implements Parcelable {
    private String mImageId;
    private Date mDateTaken;
    private Date mDateAdded;
    private String mDisplayName;

    public ImageData(String mImageId, Date mDateTaken, Date mDateAdded, String mDisplayName) {
        this.mImageId = mImageId;
        this.mDateTaken = mDateTaken;
        this.mDateAdded = mDateAdded;
        this.mDisplayName = mDisplayName;
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

    public static final Parcelable.Creator<ImageData> CREATOR = new Parcelable.Creator<ImageData>() {
        @Override
        public ImageData createFromParcel(Parcel source) {
            return new ImageData(source);
        }

        @Override
        public ImageData[] newArray(int size) {
            return new ImageData[size];
        }
    };

    protected ImageData(Parcel in) {
        this.mImageId = in.readString();
        long tmpMDateTaken = in.readLong();
        this.mDateTaken = tmpMDateTaken == -1 ? null : new Date(tmpMDateTaken);
        long tmpMDateAdded = in.readLong();
        this.mDateAdded = tmpMDateAdded == -1 ? null : new Date(tmpMDateAdded);
        this.mDisplayName = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.mImageId);
        dest.writeLong(this.mDateTaken != null ? this.mDateTaken.getTime() : -1);
        dest.writeLong(this.mDateAdded != null ? this.mDateAdded.getTime() : -1);
        dest.writeString(this.mDisplayName);
    }
}
