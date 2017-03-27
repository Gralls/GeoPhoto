package com.springer.patryk.uam_android.map;

import android.graphics.Bitmap;
import android.location.Location;

import com.springer.patryk.uam_android.model.Picture;

/**
 * Created by Patryk on 2017-03-22.
 */

public class MapPresenter implements MapContract.Presenter {

    private Picture picture;
    private MapContract.View mView;

    public MapPresenter(MapContract.View mapView) {
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
