package com.springer.patryk.geo_photo.screens.pictures_list;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewAnimator;

import com.google.firebase.auth.FirebaseAuth;
import com.jakewharton.rxbinding2.view.RxView;
import com.springer.patryk.geo_photo.R;
import com.springer.patryk.geo_photo.model.Picture;
import com.squareup.picasso.Picasso;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;

/**
 * Created by Patryk on 2017-04-08.
 */

public class PicturesListFragment extends Fragment implements PicturesListContract.View {


    @BindView(R.id.picture_remove)
    ImageView removePicture;
    @BindView(R.id.picture_view)
    ImageView pictureImage;
    @BindView(R.id.picture_label)
    TextView pictureDescription;
    @BindView(R.id.remove_picture_animator)
    ViewAnimator viewAnimator;

    private Observable removePictureObservable;
    private PicturesListContract.Presenter mPresenter;
    private Context mContext;

    public static PicturesListFragment newInstance() {
        return new PicturesListFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresenter = new PicturesListPresenter(this);
        mContext = getContext();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_picture_details, null, false);
        ButterKnife.bind(this, view);

        Picture picture = (Picture) getArguments().getSerializable("picture");
        mPresenter.setPicture(picture);

        if (!picture.getUid().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
            removePicture.setVisibility(View.INVISIBLE);
        }
        removePictureObservable = RxView.clicks(removePicture)
                .observeOn(AndroidSchedulers.mainThread())
                .debounce(100, TimeUnit.MILLISECONDS);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.subscribe();
        removePictureObservable.subscribe(onNext ->
                Snackbar.make(removePicture, R.string.delete_picture_prove, Snackbar.LENGTH_LONG)
                        .setAction("Delete", snackbar -> {
                            viewAnimator.setDisplayedChild(1);
                            mPresenter.removePicture();
                        }).show());
    }

    @Override
    public void onPause() {
        super.onPause();
        mPresenter.unsubscribe();
        removePictureObservable.unsubscribeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public void setPresenter(PicturesListContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void setPictureResource(String url) {
        Picasso.with(getContext()).load(url).into(pictureImage);
    }

    @Override
    public void setPictureDescription(String description) {
        pictureDescription.setText(description);
    }

    @Override
    public void onPictureDeleteSuccess() {
        Toast.makeText(mContext, R.string.picture_removed_info, Toast.LENGTH_SHORT).show();
        getActivity().onBackPressed();
    }

    @Override
    public void onPictureDeleteFailure() {
        viewAnimator.setDisplayedChild(0);
        Toast.makeText(mContext, R.string.picture_removed_error, Toast.LENGTH_SHORT).show();
    }
}
