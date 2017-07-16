package com.springer.patryk.geo_photo.screens.pictures_list;

import com.springer.patryk.geo_photo.BasePresenter;
import com.springer.patryk.geo_photo.BaseView;
import com.springer.patryk.geo_photo.model.Picture;

/**
 * Created by Patryk on 2017-04-12.
 */

interface PicturesListContract {

    interface View extends BaseView<PicturesListContract.Presenter> {

        void setPictureResource(String url);

        void setPictureDescription(String description);

        void onPictureDeleteSuccess();

        void onPictureDeleteFailure();
    }

    interface Presenter extends BasePresenter {

        void removePicture();

        void setPicture(Picture picture);
    }
}
