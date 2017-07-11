package com.springer.patryk.geo_photo.screens.picture_details;

import com.springer.patryk.geo_photo.model.Picture;

import io.reactivex.CompletableObserver;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;

/**
 * Created by Patryk on 2017-04-12.
 */

public class PictureDetailsPresenter implements PictureDetailsContract.Presenter, Picture.OnPictureRemovedListener {

    private PictureDetailsContract.View mView;
    private Picture picture;

    public PictureDetailsPresenter(PictureDetailsContract.View view) {
        this.mView = view;
    }

    @Override
    public void subscribe() {

    }

    @Override
    public void unsubscribe() {

    }


    @Override
    public void removePicture() {
        picture.removeFromFirebase()
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onComplete() {
                        mView.onPictureDeleteSuccess();
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        mView.onPictureDeleteFailure();
                    }
                });
    }

    @Override
    public void setPicture(Picture picture) {
        this.picture = picture;
        mView.setPictureDescription(picture.getDescription());
        mView.setPictureResource(picture.getDownloadUrl());
    }

    @Override
    public void onSuccess() {
        mView.onPictureDeleteSuccess();
    }

    @Override
    public void onFailure() {
        mView.onPictureDeleteFailure();
    }
}
