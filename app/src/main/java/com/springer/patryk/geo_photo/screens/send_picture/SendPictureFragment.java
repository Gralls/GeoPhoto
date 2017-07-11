package com.springer.patryk.geo_photo.screens.send_picture;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.ViewAnimator;

import com.google.firebase.auth.FirebaseAuth;
import com.springer.patryk.geo_photo.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Patryk on 2017-03-22.
 */

public class SendPictureFragment extends Fragment implements SendPictureContract.View {


    @BindView(R.id.new_picture)
    ImageView pictureView;
    @BindView(R.id.send_picture)
    FloatingActionButton sendPicture;
    @BindView(R.id.remove_picture)
    FloatingActionButton removePicture;
    @BindView(R.id.send_picture_animator)
    ViewAnimator viewAnimator;
    private SendPictureContract.Presenter mPresenter;

    public static SendPictureFragment newInstance() {
        return new SendPictureFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresenter = new SendPicturePresenter(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View mapView = inflater.inflate(R.layout.fragment_send_picture, null, false);
        ButterKnife.bind(this, mapView);
        viewAnimator.setDisplayedChild(0);

        mPresenter.convertUriToBitmap(Uri.parse(getArguments().getString("fileUri")));
        sendPicture.setOnClickListener(view -> {
            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());
            View dialogView = inflater.inflate(R.layout.dialog_picture_options, null);
            dialogBuilder.setView(dialogView)
                    .setPositiveButton(R.string.send_picture, (dialogInterface, i) -> {
                        viewAnimator.setDisplayedChild(1);
                        CheckBox isPublic = (CheckBox) dialogView.findViewById(R.id.picture_status);
                        EditText pictureDescription = (EditText) dialogView.findViewById(R.id.picture_description);

                        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                            return;
                        }
                        LocationManager locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
                        Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                        mPresenter.savePicture(FirebaseAuth.getInstance().getCurrentUser().getUid(),
                                location, isPublic.isChecked(), pictureDescription.getText().toString());

                    })
                    .setNegativeButton(R.string.cancel, (dialogInterface, i) -> {

                    });
            dialogBuilder.create().show();

        });

        removePicture.setOnClickListener(view -> getActivity().onBackPressed());
        return mapView;
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
    public void setPresenter(SendPictureContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void setImageResource(Bitmap bmp) {
        pictureView.setImageBitmap(bmp);
    }

    //TODO Map freezing if navigating back from here
    @Override
    public void savePictureSuccessCallback() {
        Toast.makeText(getContext(), "Picture saved!", Toast.LENGTH_SHORT).show();
        getActivity().onBackPressed();
    }

    @Override
    public void savePictureErrorCallback() {
        viewAnimator.setDisplayedChild(0);
        Toast.makeText(getContext(), R.string.picture_removed_error, Toast.LENGTH_LONG).show();
    }
}
