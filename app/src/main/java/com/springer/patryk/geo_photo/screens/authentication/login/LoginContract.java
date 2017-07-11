package com.springer.patryk.geo_photo.screens.authentication.login;

import android.support.annotation.StringRes;

import com.springer.patryk.geo_photo.BasePresenter;
import com.springer.patryk.geo_photo.BaseView;

/**
 * Created by Patryk on 2017-03-11.
 */

interface LoginContract {

    interface View extends BaseView<Presenter> {

        void showAuthenticationError();

        void setEmailError(@StringRes int errorMessage);

        void setPasswordError(@StringRes int errorMessage);

        void showMainPage();

    }

    interface Presenter extends BasePresenter {

        void checkCredentials(String email, String password);

    }

}
