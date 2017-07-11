package com.springer.patryk.geo_photo.screens.profile_settings;

import com.springer.patryk.geo_photo.BasePresenter;
import com.springer.patryk.geo_photo.BaseView;

/**
 * Created by Patryk on 2017-03-22.
 */

interface ProfileSettingsContract {

    interface View extends BaseView<Presenter> {

        void onChangeCompleted();

        void onChangeFailure(Throwable throwable);

    }

    interface Presenter extends BasePresenter {

        void changeProfile(String username, String email);

    }

}
