package com.springer.patryk.geo_photo.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

/**
 * Created by Patryk on 2017-07-16.
 */

public class FileUtils {

    public static Bitmap decodeBitmapFromFile(String path, int reqWidth, int reqHeight) {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, options);

        final int height = options.outHeight;
        final int width = options.outWidth;
        options.inPreferredConfig = Bitmap.Config.RGB_565;
        int inSampleSize = 1;

        if (height > reqHeight)
            inSampleSize = Math.round((float) height / (float) reqHeight);

        int expectedWidth = width / inSampleSize;

        if (expectedWidth > reqWidth)
            inSampleSize = Math.round((float) width / (float) reqWidth);

        options.inSampleSize = inSampleSize;
        options.inJustDecodeBounds = false;

        return BitmapFactory.decodeFile(path, options);
    }
}
