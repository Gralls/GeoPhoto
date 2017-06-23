package com.springer.patryk.geo_photo.profile_settings;

import android.graphics.Bitmap;

import io.reactivex.disposables.Disposable;

/**
 * Created by Patryk on 2017-03-22.
 */

public class ProfileSettingsPresenter implements ProfileSettingsContract.Presenter {

    private ProfileSettingsContract.View mView;
    private Bitmap imageResource;
    private Disposable savePictureDisposable;

    public ProfileSettingsPresenter(ProfileSettingsContract.View mapView) {
        mView = mapView;
    }

    @Override
    public void subscribe() {

    }

    @Override
    public void unsubscribe() {

    }


}
