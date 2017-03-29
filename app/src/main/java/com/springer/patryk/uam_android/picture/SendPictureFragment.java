package com.springer.patryk.uam_android.picture;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.springer.patryk.uam_android.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Patryk on 2017-03-22.
 */

public class SendPictureFragment extends Fragment implements SendPictureContract.View {


    private static final int CAPTURE_IMAGE_ACTIVITY = 1;

    private SendPictureContract.Presenter mPresenter;

    @BindView(R.id.new_picture)
    ImageView pictureView;
    @BindView(R.id.send_picture)
    FloatingActionButton sendPicture;
    @BindView(R.id.remove_picture)
    FloatingActionButton removePicture;


    Bitmap imageResource;
    public static SendPictureFragment newInstance() {
        return new SendPictureFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresenter = new SendPicturePresenter(this);
        imageResource=(Bitmap) getArguments().get("data");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View mapView = inflater.inflate(R.layout.fragment_send_picture, null, false);
        ButterKnife.bind(this, mapView);

        pictureView.setImageBitmap(imageResource);
        sendPicture.setOnClickListener(view -> {

            LocationManager locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
            if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }

            Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            mPresenter.savePicture(FirebaseAuth.getInstance().getCurrentUser().getUid(),
                    imageResource,
                    location);
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

    public interface PictureSent{
        void onPictureSent();
    }
}
