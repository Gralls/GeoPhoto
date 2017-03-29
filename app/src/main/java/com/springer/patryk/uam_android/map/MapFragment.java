package com.springer.patryk.uam_android.map;

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
import com.springer.patryk.uam_android.MainActivity;
import com.springer.patryk.uam_android.R;
import com.springer.patryk.uam_android.authentication.AuthenticationActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Patryk on 2017-03-22.
 */

public class MapFragment extends Fragment implements OnMapReadyCallback, MapContract.View {


    private static final int CAPTURE_IMAGE_ACTIVITY = 1;

    private MapContract.Presenter mPresenter;
    private PictureTakenCallback mCallback;

    SupportMapFragment mapFragment;

    @BindView(R.id.take_picture)
    FloatingActionButton takePicture;
    @BindView(R.id.picture)
    ImageView pictureView;

    public static MapFragment newInstance() {
        return new MapFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresenter = new MapPresenter(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View mapView = inflater.inflate(R.layout.fragment_map, null, false);
        ButterKnife.bind(this, mapView);

        mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        takePicture.setOnClickListener(view -> {
            Intent takePicture1 = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(takePicture1, CAPTURE_IMAGE_ACTIVITY);
            if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        1);
            }
        });

        return mapView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mCallback = (MainActivity) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(mCallback.toString() +
                    "must implement PictureTakenCallback");
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAPTURE_IMAGE_ACTIVITY && resultCode == Activity.RESULT_OK) {
            Bitmap bitmap = (Bitmap) data.getExtras().get("data");

            mCallback.onPictureTaken(data.getExtras());
            /*pictureView.setVisibility(View.VISIBLE);
            pictureView.setImageBitmap(bitmap);

            LocationManager locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
            if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }

            Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            mPresenter.savePicture(FirebaseAuth.getInstance().getCurrentUser().getUid(), bitmap, location);*/

        }
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
    public void onMapReady(GoogleMap googleMap) {
    }


    @Override
    public void setPresenter(MapContract.Presenter presenter) {
        mPresenter = presenter;
    }

    public interface PictureTakenCallback{
        void onPictureTaken(Bundle args);
    }
}
