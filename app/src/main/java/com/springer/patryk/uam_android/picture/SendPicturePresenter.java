package com.springer.patryk.uam_android.picture;

import android.graphics.Bitmap;
import android.location.Location;

import com.springer.patryk.uam_android.model.Picture;

/**
 * Created by Patryk on 2017-03-22.
 */

public class SendPicturePresenter implements SendPictureContract.Presenter {

    private Picture picture;
    private SendPictureContract.View mView;

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
    public void savePicture(String userId, Bitmap image, Location location) {
        picture = new Picture(userId, location.getLongitude(), location.getLatitude(), image);
        picture.saveToFirebase();
    }
}
