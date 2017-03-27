package com.springer.patryk.uam_android.map;

import android.graphics.Bitmap;
import android.location.Location;

import com.springer.patryk.uam_android.BasePresenter;
import com.springer.patryk.uam_android.BaseView;

/**
 * Created by Patryk on 2017-03-22.
 */

public class MapContract {

    interface View extends BaseView<Presenter> {


    }

    interface Presenter extends BasePresenter {

        void savePicture(String userId, Bitmap image, Location location);

    }

}
