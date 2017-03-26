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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.springer.patryk.uam_android.R;
import com.springer.patryk.uam_android.model.Picture;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Patryk on 2017-03-22.
 */

public class MapFragment extends Fragment implements OnMapReadyCallback {


    private static final int CAPTURE_IMAGE_ACTIVITY = 1;


    SupportMapFragment mapFragment;

    @BindView(R.id.take_picture)
    FloatingActionButton takePicture;
    @BindView(R.id.picture)
    ImageView pictureView;

    public static MapFragment newInstance() {
        return new MapFragment();
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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAPTURE_IMAGE_ACTIVITY && resultCode == Activity.RESULT_OK) {
            Bitmap bitmap = (Bitmap) data.getExtras().get("data");
            pictureView.setVisibility(View.VISIBLE);
            pictureView.setImageBitmap(bitmap);

            LocationManager locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
            if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

            Picture picture = new Picture(FirebaseAuth.getInstance().getCurrentUser().getUid(),
                    location.getLongitude(),
                    location.getLatitude(),
                    bitmap
            );

            DatabaseReference database = FirebaseDatabase.getInstance().getReference();
            String key = database.child("picture").push().getKey();
            Map<String, Object> pictureValues = picture.toMap();
            Map<String, Object> childUpdates = new HashMap<>();
            childUpdates.put("/pictures/" + key, pictureValues);
            childUpdates.put("/user-picture/" + FirebaseAuth.getInstance().getCurrentUser().getUid() + "/" + key,
                    pictureValues);

            database.updateChildren(childUpdates);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
    }


}
