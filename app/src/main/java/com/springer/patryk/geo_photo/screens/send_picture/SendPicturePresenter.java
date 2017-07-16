package com.springer.patryk.geo_photo.screens.send_picture;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.net.Uri;
import android.util.Log;

import com.springer.patryk.geo_photo.model.Picture;
import com.springer.patryk.geo_photo.utils.FileUtils;

import java.io.File;

/**
 * Created by Patryk on 2017-03-22.
 */

public class SendPicturePresenter implements SendPictureContract.Presenter {

    private SendPictureContract.View mView;
    private Bitmap imageResource;

    public SendPicturePresenter(SendPictureContract.View mapView) {
        mView = mapView;
    }



    @Override
    public void subscribe() {

    }

    @Override
    public void unsubscribe() {

    }

    @Override
    public void savePicture(String userId, Location location, boolean isPublic, String pictureDescription) {
        Picture picture = new Picture(userId, location.getLongitude(), location.getLatitude(), imageResource);
        picture.setDescription(pictureDescription);
        picture.saveToFirebase(isPublic)
                .subscribe(() ->
                                mView.savePictureSuccessCallback(),
                        throwable -> {
                            mView.savePictureErrorCallback();
                            Log.e("SaveToFirebase", throwable.getLocalizedMessage());
                        });

    }

    @Override
    public void convertUriToBitmap(Uri imageUri) {
        File file = new File(imageUri.getPath());
        imageResource = FileUtils.decodeBitmapFromFile(file.getAbsolutePath(), 1920, 1080);
        mView.setImageResource(imageResource);
    }
}
