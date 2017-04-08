package com.springer.patryk.uam_android.map;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.FileProvider;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.FirebaseDatabase;
import com.springer.patryk.uam_android.MainActivity;
import com.springer.patryk.uam_android.R;
import com.springer.patryk.uam_android.model.Picture;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Patryk on 2017-03-22.
 */

public class MapFragment extends Fragment implements OnMapReadyCallback, MapContract.View {


    private static final int CAPTURE_IMAGE_ACTIVITY = 1;

    private MapContract.Presenter mPresenter;
    private PictureTakenCallback mCallback;
    private CameraPosition cameraPosition;

    SupportMapFragment mapFragment;
    private GoogleMap map;
    @BindView(R.id.take_picture)
    FloatingActionButton takePicture;

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
        FragmentManager fm = getChildFragmentManager();

        if (savedInstanceState == null) {
            mapFragment = SupportMapFragment.newInstance();
            FragmentTransaction ft = fm.beginTransaction();
            ft.add(R.id.mapContainer, mapFragment, "mapFragment");
            ft.commit();
        } else {
            mapFragment = (SupportMapFragment) fm.findFragmentByTag("mapFragment");
        }


        takePicture.setOnClickListener(view -> {
            Intent takePicture1 = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            File path = new File(getActivity().getFilesDir(), "pictures");
            if (!path.exists()) path.mkdirs();
            File file = new File(path, "image.jpg");
            takePicture1.putExtra(MediaStore.EXTRA_OUTPUT, FileProvider.getUriForFile(getActivity(), getActivity().getApplicationContext().getPackageName() + ".provider", file));
            startActivityForResult(takePicture1, CAPTURE_IMAGE_ACTIVITY);
        });

        return mapView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mapFragment.getMapAsync(this);
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
            File path = new File(getActivity().getFilesDir(), "pictures");
            if (!path.exists()) path.mkdirs();
            File file = new File(path, "image.jpg");
            mCallback.onPictureTaken(Uri.fromFile(file));
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
        map = googleMap;
        map.setInfoWindowAdapter(new CustomInfoWindowAdapter(LayoutInflater.from(getActivity())));
    }

    @Override
    public void setMarkers(List<Picture> pictures) {
        for (Picture picture : pictures) {
            Marker marker = map.addMarker(new MarkerOptions()
                    .position(picture.getPosition()));
            marker.setTag(picture);
        }
        if (pictures.size() > 0) {
            cameraPosition = new CameraPosition.Builder()
                    .target(pictures.get(0).getPosition())
                    .zoom(17).build();
            map.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        }
    }

    @Override
    public void setPresenter(MapContract.Presenter presenter) {
        mPresenter = presenter;
    }

    public interface PictureTakenCallback {
        void onPictureTaken(Uri args);
    }

    private class CustomInfoWindowAdapter implements GoogleMap.InfoWindowAdapter {

        private View view;

        public CustomInfoWindowAdapter(LayoutInflater inflater) {
            view = inflater.inflate(R.layout.marker_info_window, null);
        }

        @Override
        public View getInfoWindow(Marker marker) {
            final ImageView image = (ImageView) view.findViewById(R.id.user_picture);
            final ImageView delete = (ImageView) view.findViewById(R.id.remove_picture);
            final TextView pictureInfo = (TextView) view.findViewById(R.id.picture_info);

            Picture picture = (Picture) marker.getTag();
            Picasso.with(getContext()).load(picture.getDownloadUrl()).into(image);
            pictureInfo.setText(picture.getDescription());
            map.setOnInfoWindowClickListener(marker1 -> {
                FirebaseDatabase.getInstance().getReference()
                        .child("pictures")
                        .child(picture.getPictureId())
                        .removeValue();
                marker.remove();
            });
            return view;
        }

        @Override
        public View getInfoContents(Marker marker) {
            return null;
        }
    }
}
