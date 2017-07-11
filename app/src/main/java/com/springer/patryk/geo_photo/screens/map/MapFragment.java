package com.springer.patryk.geo_photo.screens.map;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.maps.android.clustering.ClusterItem;
import com.google.maps.android.clustering.ClusterManager;
import com.jakewharton.rxbinding2.view.RxView;
import com.springer.patryk.geo_photo.MainActivity;
import com.springer.patryk.geo_photo.R;
import com.springer.patryk.geo_photo.model.Picture;
import com.springer.patryk.geo_photo.utils.PermissionUtils;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;

/**
 * Created by Patryk on 2017-03-22.
 */

public class MapFragment extends Fragment implements OnMapReadyCallback, MapContract.View, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {

    private static final int CAPTURE_IMAGE_ACTIVITY = 1;
    private static final String PICTURE_DIR = "pictures";
    private static final String PICTURE_FILENAME = "image.jpg";
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 100;
    @BindView(R.id.take_picture)
    FloatingActionButton takePicture;
    @BindView(R.id.bottom_sheet)
    FrameLayout frameLayout;
    @BindView(R.id.cluster_pictures)
    RecyclerView bottomPictures;
    private MapContract.Presenter mPresenter;
    private PictureTakenCallback mCallback;
    private ClusterClicked clusterClickedCallback;
    private ClusterManager<Picture> mClusterManager;
    private Picture clickedClusterItem;
    private SupportMapFragment mapFragment;
    private GoogleMap map;
    private LocationRequest mLocationRequest;
    private GoogleApiClient mGoogleApiClient;
    private Location mLastLocation;
    private ClusterAdapter adapter;
    private Observable takePictureObservable;
    private BottomSheetBehavior bottomSheetBehavior;

    public MapFragment() {
    }

    public static MapFragment newInstance() {
        return new MapFragment();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mCallback = (MainActivity) context;
            clusterClickedCallback = (MainActivity) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(mCallback.toString() +
                    "must implement PictureTakenCallback");
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresenter = new MapPresenter(this);
        prepareMapFragment(savedInstanceState == null);
        mGoogleApiClient = new GoogleApiClient.Builder(getContext())
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

    private void prepareMapFragment(boolean createNewInstance) {
        FragmentManager fm = getChildFragmentManager();
        if (createNewInstance) {
            mapFragment = SupportMapFragment.newInstance();
            FragmentTransaction ft = fm.beginTransaction();
            ft.add(R.id.mapContainer, mapFragment, "mapFragment");
            ft.commit();
        } else {
            mapFragment = (SupportMapFragment) fm.findFragmentByTag("mapFragment");
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View mapView = inflater.inflate(R.layout.fragment_map, null, false);
        ButterKnife.bind(this, mapView);


        prepareBottomSheet();
        takePictureObservable = RxView.clicks(takePicture)
                .observeOn(AndroidSchedulers.mainThread())
                .debounce(100, TimeUnit.MILLISECONDS);
        if (map == null) {

            mapFragment.getMapAsync(this);
        }
        return mapView;
    }

    private void prepareBottomSheet() {
        bottomSheetBehavior = BottomSheetBehavior.from(frameLayout);
        bottomSheetBehavior.setHideable(true);
        bottomSheetBehavior.setPeekHeight(0);
        adapter = new ClusterAdapter(getContext(), clusterClickedCallback);
        bottomPictures.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        bottomPictures.setAdapter(adapter);
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.subscribe();
        takePictureObservable.subscribe(onClick -> {
            startCameraActivity();
        });
    }

    private void startCameraActivity() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File path = new File(getActivity().getFilesDir(), PICTURE_DIR);
        if (!path.exists()) path.mkdirs();
        File file = new File(path, PICTURE_FILENAME);
        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, FileProvider.getUriForFile(getActivity(), getActivity().getApplicationContext().getPackageName() + ".provider", file));
        startActivityForResult(takePictureIntent, CAPTURE_IMAGE_ACTIVITY);
    }

    @Override
    public void onPause() {
        super.onPause();
        mPresenter.unsubscribe();
        mGoogleApiClient.disconnect();
        takePictureObservable.unsubscribeOn(AndroidSchedulers.mainThread());
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAPTURE_IMAGE_ACTIVITY && resultCode == Activity.RESULT_OK) {
            File path = new File(getActivity().getFilesDir(), PICTURE_DIR);
            if (!path.exists()) path.mkdirs();
            File file = new File(path, PICTURE_FILENAME);
            mCallback.onPictureTaken(Uri.fromFile(file));
        }
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        map.setInfoWindowAdapter(new CustomInfoWindowAdapter(LayoutInflater.from(getActivity())));

        mGoogleApiClient.connect();
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            map.setMyLocationEnabled(true);
        } else {
            PermissionUtils.requestPermission(getActivity(), LOCATION_PERMISSION_REQUEST_CODE, Manifest.permission.ACCESS_FINE_LOCATION, false);
        }
        mClusterManager = new ClusterManager<>(getContext(), map);
        mClusterManager.setRenderer(new com.springer.patryk.geo_photo.screens.map.ClusterRenderer(getContext(), map, mClusterManager));
        mClusterManager.setOnClusterItemClickListener(picture -> {
            clickedClusterItem = picture;
            return false;
        });
        mClusterManager.setOnClusterClickListener(cluster -> {
            adapter.setPictureList(new ArrayList<>(cluster.getItems()));
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
            LatLngBounds.Builder builder = new LatLngBounds.Builder();
            for (ClusterItem item : cluster.getItems()) {
                builder.include(item.getPosition());
            }
            map.animateCamera(CameraUpdateFactory.newLatLngBounds(builder.build(), 100));
            return true;
        });
        map.setOnCameraIdleListener(mClusterManager);
        map.setOnMarkerClickListener(mClusterManager);
        map.setOnMapClickListener(latLng -> bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN));
    }


    @Override
    public void setMarkers(List<Picture> pictures) {
        map.clear();
        mClusterManager.clearItems();
        mClusterManager.addItems(pictures);
        mClusterManager.cluster();
    }

    @Override
    public void setPresenter(MapContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(1000);
        mLocationRequest.setFastestInterval(1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        } else {
            PermissionUtils.requestPermission(getActivity(), LOCATION_PERMISSION_REQUEST_CODE, Manifest.permission.ACCESS_FINE_LOCATION, false);
        }


    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {
        mLastLocation = location;

    }

    public interface PictureTakenCallback {
        void onPictureTaken(Uri args);
    }

    public interface ClusterClicked {
        void onClusterClickedListener(Picture picture);
    }

    private class CustomInfoWindowAdapter implements GoogleMap.InfoWindowAdapter {

        private View view;

        CustomInfoWindowAdapter(LayoutInflater inflater) {
            view = inflater.inflate(R.layout.marker_info_window, null);
        }

        @Override
        public View getInfoWindow(Marker marker) {
            return null;
        }

        @Override
        public View getInfoContents(Marker marker) {
            final ImageView image = (ImageView) view.findViewById(R.id.user_picture);
            Picasso.with(getContext())
                    .load(clickedClusterItem.getDownloadUrl())
                    .into(image, new Callback() {
                        @Override
                        public void onSuccess() {
                            if (marker != null && marker.isInfoWindowShown()) {
                                marker.hideInfoWindow();
                                marker.showInfoWindow();
                            }
                        }

                        @Override
                        public void onError() {

                        }
                    });

            map.setOnInfoWindowClickListener(marker1 ->
                    clusterClickedCallback.onClusterClickedListener(clickedClusterItem));

            return view;

        }
    }
}
