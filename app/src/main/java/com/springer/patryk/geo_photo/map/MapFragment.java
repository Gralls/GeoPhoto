package com.springer.patryk.geo_photo.map;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.FileProvider;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.Marker;
import com.google.maps.android.clustering.ClusterManager;
import com.springer.patryk.geo_photo.MainActivity;
import com.springer.patryk.geo_photo.R;
import com.springer.patryk.geo_photo.model.Picture;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Patryk on 2017-03-22.
 */

public class MapFragment extends Fragment implements OnMapReadyCallback, MapContract.View {

    @BindView(R.id.take_picture)
    FloatingActionButton takePicture;
    @BindView(R.id.bottom_sheet)
    FrameLayout frameLayout;
    @BindView(R.id.cluster_pictures)
    RecyclerView bottomPictures;

    private static final int CAPTURE_IMAGE_ACTIVITY = 1;

    private MapContract.Presenter mPresenter;
    private PictureTakenCallback mCallback;
    private ClusterClicked clusterClickedCallback;
    private ClusterManager<Picture> mClusterManager;
    private Picture clickedClusterItem;
    private SupportMapFragment mapFragment;
    private GoogleMap map;

    private ClusterAdapter adapter;


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
        if (savedInstanceState == null)
            mPresenter = new MapPresenter(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View mapView = inflater.inflate(R.layout.fragment_map, null, false);
        ButterKnife.bind(this, mapView);
        FragmentManager fm = getChildFragmentManager();

        if (savedInstanceState == null) {
            mapFragment = SupportMapFragment.newInstance();
            FragmentTransaction ft = fm.beginTransaction();
            ft.add(R.id.mapContainer, mapFragment, "mapFragment");
            ft.commit();
        } else {
            mapFragment = (SupportMapFragment) fm.findFragmentByTag("mapFragment");
        }

        return mapView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        prepareBottomSheet();
        if (savedInstanceState == null) {
            takePicture.setOnClickListener(view1 -> {
                Intent takePicture1 = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                File path = new File(getActivity().getFilesDir(), "pictures");
                if (!path.exists()) path.mkdirs();
                File file = new File(path, "image.jpg");
                takePicture1.putExtra(MediaStore.EXTRA_OUTPUT, FileProvider.getUriForFile(getActivity(), getActivity().getApplicationContext().getPackageName() + ".provider", file));
                startActivityForResult(takePicture1, CAPTURE_IMAGE_ACTIVITY);
            });
            mapFragment.getMapAsync(this);

        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelableArrayList("Pictures", (ArrayList<? extends Parcelable>) mPresenter.getPictures());
        super.onSaveInstanceState(outState);

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
    }

    @Override
    public void onPause() {
        super.onPause();
        mPresenter.unsubscribe();
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAPTURE_IMAGE_ACTIVITY && resultCode == Activity.RESULT_OK) {
            File path = new File(getActivity().getFilesDir(), "pictures");
            if (!path.exists()) path.mkdirs();
            File file = new File(path, "image.jpg");
            mCallback.onPictureTaken(Uri.fromFile(file));
        }
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        map.setInfoWindowAdapter(new CustomInfoWindowAdapter(LayoutInflater.from(getActivity())));
        mClusterManager = new ClusterManager<>(getContext(), map);
        mClusterManager.setRenderer(new com.springer.patryk.geo_photo.map.ClusterRenderer(getContext(), map, mClusterManager));
        mClusterManager.setOnClusterItemClickListener(picture -> {
            clickedClusterItem = picture;
            return false;
        });
        mClusterManager.setOnClusterClickListener(cluster -> {
            adapter.setPictureList(new ArrayList<>(cluster.getItems()));
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
            return true;
        });
        map.setOnCameraIdleListener(mClusterManager);
        map.setOnMarkerClickListener(mClusterManager);
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
