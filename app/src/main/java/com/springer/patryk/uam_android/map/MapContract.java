package com.springer.patryk.uam_android.map;

import com.springer.patryk.uam_android.BasePresenter;
import com.springer.patryk.uam_android.BaseView;
import com.springer.patryk.uam_android.model.Picture;

import java.util.List;

/**
 * Created by Patryk on 2017-03-22.
 */

public class MapContract {

    interface View extends BaseView<Presenter> {

        void setMarkers(List<Picture> pictures);

    }

    interface Presenter extends BasePresenter {

    }

}
