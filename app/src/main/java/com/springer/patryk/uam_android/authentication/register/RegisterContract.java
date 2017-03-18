package com.springer.patryk.uam_android.authentication.register;

import android.support.annotation.StringRes;

import com.springer.patryk.uam_android.BasePresenter;
import com.springer.patryk.uam_android.BaseView;

/**
 * Created by Patryk on 2017-03-11.
 */

public interface RegisterContract {

    interface View extends BaseView<Presenter> {

        void showAuthenticationError();

        void showEmailError(@StringRes int errorMessage);

        void showPasswordError(@StringRes int errorMessage);

        void showConfirmPasswordError(@StringRes int errorMessage);

        void showMainPage();

    }

    interface Presenter extends BasePresenter {

        void checkCredentials(String email, String password, String confirmPassword);

    }

}
