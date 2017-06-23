package com.springer.patryk.geo_photo.send_picture;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.net.Uri;
import android.util.Log;

import com.springer.patryk.geo_photo.model.Picture;

import java.io.File;

import io.reactivex.disposables.Disposable;

/**
 * Created by Patryk on 2017-03-22.
 */

public class SendPicturePresenter implements SendPictureContract.Presenter {

    private SendPictureContract.View mView;
    private Bitmap imageResource;
    private Disposable savePictureDisposable;

    public SendPicturePresenter(SendPictureContract.View mapView) {
        mView = mapView;
    }

    @Override
    public void subscribe() {

    }

    @Override
    public void unsubscribe() {
        if (!savePictureDisposable.isDisposed()) {
            savePictureDisposable.dispose();
        }
    }

    @Override
    public void savePicture(String userId, Location location, boolean isPublic, String pictureDescription) {
        Picture picture = new Picture(userId, location.getLongitude(), location.getLatitude(), imageResource);
        picture.setDescription(pictureDescription);
        savePictureDisposable = picture.saveToFirebase(isPublic)
                .subscribe(onNext -> {
                    mView.savePictureSuccessCallback();
                }, onError -> {
                    mView.savePictureErrorCallback();
                    Log.e("SaveToFirebase", onError.getLocalizedMessage());
                });
    }

    @Override
    public void convertUriToBitmap(Uri imageUri) {
        File file = new File(imageUri.getPath());
        imageResource = decodeBitmapFromFile(file.getAbsolutePath(), 1920, 1080);
        mView.setImageResource(imageResource);
    }

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
