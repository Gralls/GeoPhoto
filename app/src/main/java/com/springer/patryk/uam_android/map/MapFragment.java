package com.springer.patryk.uam_android.map;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.springer.patryk.uam_android.MainActivity;
import com.springer.patryk.uam_android.R;
import com.springer.patryk.uam_android.model.Picture;

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

    SupportMapFragment mapFragment;
    private GoogleMap map;
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
        FragmentManager fm = getChildFragmentManager();
        mapFragment = (SupportMapFragment) fm.findFragmentByTag("mapFragment");

        if (mapFragment == null) {
            mapFragment = new SupportMapFragment();
            FragmentTransaction ft = fm.beginTransaction();
            ft.add(R.id.mapContainer, mapFragment, "mapFragment");
            ft.commit();
        }


        takePicture.setOnClickListener(view -> {
            Intent takePicture1 = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
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
            mCallback.onPictureTaken(data.getExtras());
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
            map.addMarker(new MarkerOptions().position(picture.getPosition())
                    .snippet(picture.getImage()));
        }
        if (pictures.size() > 0)
            map.moveCamera(CameraUpdateFactory.newLatLng(pictures.get(0).getPosition()));
    }

    private BitmapDescriptor prepareMarker(Picture picture) {
        Bitmap.Config conf = Bitmap.Config.ARGB_8888;
        Bitmap bmp = Bitmap.createBitmap(80, 80, conf);
        Canvas canvas1 = new Canvas(bmp);
        Paint color = new Paint();
        color.setTextSize(35);
        color.setColor(Color.BLACK);
        canvas1.drawBitmap(Picture.convertBase64ToBitmap(picture.getImage()), 0, 0, color);
        canvas1.drawText(picture.getUid(), 30, 40, color);
        return BitmapDescriptorFactory.fromBitmap(bmp);
    }

    @Override
    public void setPresenter(MapContract.Presenter presenter) {
        mPresenter = presenter;
    }

    public interface PictureTakenCallback {
        void onPictureTaken(Bundle args);
    }

    private class CustomInfoWindowAdapter implements GoogleMap.InfoWindowAdapter {

        private View view;

        public CustomInfoWindowAdapter(LayoutInflater inflater) {
            view = inflater.inflate(R.layout.marker_info_window, null);
        }

        @Override
        public View getInfoWindow(Marker marker) {

            final ImageView image = (ImageView) view.findViewById(R.id.user_picture);
            image.setImageBitmap(Picture.convertBase64ToBitmap(marker.getSnippet()));
            return view;
        }

        @Override
        public View getInfoContents(Marker marker) {
            return null;
        }
    }
}
