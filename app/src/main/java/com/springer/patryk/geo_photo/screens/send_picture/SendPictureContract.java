package com.springer.patryk.geo_photo.screens.send_picture;

import android.graphics.Bitmap;
import android.location.Location;
import android.net.Uri;

import com.springer.patryk.geo_photo.BasePresenter;
import com.springer.patryk.geo_photo.BaseView;

/**
 * Created by Patryk on 2017-03-22.
 */

interface SendPictureContract {

    interface View extends BaseView<Presenter> {

        void setImageResource(Bitmap bmp);

        void savePictureSuccessCallback();

        void savePictureErrorCallback();
    }

    interface Presenter extends BasePresenter {

        void savePicture(String userId, Location location, boolean isPublic, String description);

        void convertUriToBitmap(Uri imageUri);
    }

}
