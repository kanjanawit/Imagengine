package com.kanjanawit.imagengine;

import android.content.ContentResolver;
import android.content.Context;
import android.content.res.Resources;
import android.media.Image;
import android.net.Uri;

public class FakeData {
    public static Uri[] getImageData(Context context){
        Resources resources = context.getResources();
        Uri uri = new Uri.Builder()
                .scheme(ContentResolver.SCHEME_ANDROID_RESOURCE)
                .authority(resources.getResourcePackageName(R.drawable.image_009))
                .appendPath(resources.getResourceTypeName(R.drawable.image_009))
                .appendPath(resources.getResourceEntryName(R.drawable.image_009))
                .build();

        Uri[] uris = {uri,uri,uri,uri,uri,uri,uri,uri,uri,uri,uri,uri,uri,uri,uri,uri,uri,uri,uri,uri,uri,uri,uri,uri,uri,uri,uri,uri,uri};
        return uris;
    }
}
