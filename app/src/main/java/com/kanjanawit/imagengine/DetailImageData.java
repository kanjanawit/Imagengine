package com.kanjanawit.imagengine;

public class DetailImageData extends ImageData {
    private long mFileSize;
    private int mImageHeight;
    private int mImageWidth;

    public DetailImageData(ImageData imageData, long fileSize, int imageHeight, int imageWidth) {
        super(imageData.getImageId(), imageData.getDateTaken(), imageData.getDateAdded(), imageData.getDisplayName());
        mFileSize = fileSize;
        mImageHeight = imageHeight;
        mImageWidth = imageWidth;
    }


}
