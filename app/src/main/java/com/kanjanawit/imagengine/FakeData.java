package com.kanjanawit.imagengine;

import android.content.ContentResolver;
import android.content.Context;
import android.content.res.Resources;
import android.net.Uri;

import java.util.ArrayList;

public class FakeData {
    public static ArrayList<Uri> getImageData(Context context) {
        Resources resources = context.getResources();
        Uri uri = new Uri.Builder()
                .scheme(ContentResolver.SCHEME_ANDROID_RESOURCE)
                .authority(resources.getResourcePackageName(R.drawable.image_009))
                .appendPath(resources.getResourceTypeName(R.drawable.image_009))
                .appendPath(resources.getResourceEntryName(R.drawable.image_009))
                .build();
        ArrayList<Uri> fakeUris = new ArrayList<Uri>();
        for (int i = 0; i <= 15; i++) {
            fakeUris.add(uri);
        }

        return fakeUris;
    }
}
