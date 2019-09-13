package com.kanjanawit.imagengine.Object;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

public class DetailImageData extends ImageData implements Parcelable {
    public static final Creator<DetailImageData> CREATOR = new Creator<DetailImageData>() {
        @Override
        public DetailImageData createFromParcel(Parcel source) {
            return new DetailImageData(source);
        }

        @Override
        public DetailImageData[] newArray(int size) {
            return new DetailImageData[size];
        }
    };
    protected long mFileSize;
    protected int mImageHeight;

    public DetailImageData(ImageData imageData, long fileSize, int imageHeight, int imageWidth) {
        super(imageData.getImageId(), imageData.getDateTaken(), imageData.getDateAdded(), imageData.getDisplayName());
        mFileSize = fileSize;
        mImageHeight = imageHeight;
        mImageWidth = imageWidth;
    }

    protected int mImageWidth;

    protected DetailImageData(Parcel in) {
        super(in);
        this.mFileSize = in.readLong();
        this.mImageHeight = in.readInt();
        this.mImageWidth = in.readInt();
        this.mImageId = in.readString();
        long tmpMDateTaken = in.readLong();
        this.mDateTaken = tmpMDateTaken == -1 ? null : new Date(tmpMDateTaken);
        long tmpMDateAdded = in.readLong();
        this.mDateAdded = tmpMDateAdded == -1 ? null : new Date(tmpMDateAdded);
        this.mDisplayName = in.readString();
    }

    public long getFileSize() {
        return mFileSize;
    }

    public int getImageHeight() {
        return mImageHeight;
    }

    public int getImageWidth() {
        return mImageWidth;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeLong(this.mFileSize);
        dest.writeInt(this.mImageHeight);
        dest.writeInt(this.mImageWidth);
        dest.writeString(this.mImageId);
        dest.writeLong(this.mDateTaken != null ? this.mDateTaken.getTime() : -1);
        dest.writeLong(this.mDateAdded != null ? this.mDateAdded.getTime() : -1);
        dest.writeString(this.mDisplayName);
    }
}
