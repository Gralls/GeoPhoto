package com.springer.patryk.geo_photo.picture_details;

import com.springer.patryk.geo_photo.model.Picture;

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
        picture.removeFromFirebase(this);
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
