package com.springer.patryk.geo_photo.screens.map;

import com.springer.patryk.geo_photo.BasePresenter;
import com.springer.patryk.geo_photo.BaseView;
import com.springer.patryk.geo_photo.model.Picture;

import java.util.List;

/**
 * Created by Patryk on 2017-03-22.
 */

interface MapContract {

    interface View extends BaseView<Presenter> {

        void setMarkers(List<Picture> pictures);

    }

    interface Presenter extends BasePresenter {
        List<Picture> getPictures();

        void setPictures(List<Picture> pictures);
    }

}
