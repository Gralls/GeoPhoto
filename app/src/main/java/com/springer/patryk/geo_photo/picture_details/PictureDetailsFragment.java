package com.springer.patryk.geo_photo.picture_details;

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

import com.springer.patryk.geo_photo.R;
import com.springer.patryk.geo_photo.model.Picture;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Patryk on 2017-04-08.
 */

public class PictureDetailsFragment extends Fragment implements PictureDetailsContract.View {


    @BindView(R.id.picture_remove)
    ImageView removePicture;
    @BindView(R.id.picture_view)
    ImageView pictureImage;
    @BindView(R.id.picture_label)
    TextView pictureDescription;

    private PictureDetailsContract.Presenter mPresenter;

    public static PictureDetailsFragment newInstance() {
        return new PictureDetailsFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresenter = new PictureDetailsPresenter(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_picture_details, null, false);
        ButterKnife.bind(this, view);

        Picture picture = (Picture) getArguments().getSerializable("picture");
        mPresenter.setPicture(picture);

        removePicture.setOnClickListener(removePicture -> Snackbar.make(removePicture, R.string.delete_picture_prove, Snackbar.LENGTH_LONG)
                .setAction("Delete", snackbar -> mPresenter.removePicture()).show());

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.subscribe();
    }

    @Override
    public void onPause() {
        super.onPause();
        mPresenter.unsubscribe();
    }

    @Override
    public void setPresenter(PictureDetailsContract.Presenter presenter) {
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
        Toast.makeText(getContext(), R.string.picture_removed_info, Toast.LENGTH_SHORT).show();
        getActivity().onBackPressed();
    }

    @Override
    public void onPictureDeleteFailure() {
        Toast.makeText(getContext(), R.string.picture_removed_error, Toast.LENGTH_SHORT).show();
    }
}
