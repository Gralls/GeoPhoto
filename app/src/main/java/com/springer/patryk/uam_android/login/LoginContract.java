package com.springer.patryk.uam_android.login;

import com.springer.patryk.uam_android.BasePresenter;
import com.springer.patryk.uam_android.BaseView;

/**
 * Created by Patryk on 2017-03-11.
 */

public interface LoginContract {

    interface View extends BaseView<Presenter> {

        void showAuthenticationError();

        void showMainPage();


    }

    interface Presenter extends BasePresenter {

        void checkCredentials(String email, String password);

    }

}
