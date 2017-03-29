package com.springer.patryk.uam_android.map;

import android.graphics.Bitmap;
import android.location.Location;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.springer.patryk.uam_android.model.Picture;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Patryk on 2017-03-22.
 */

public class MapPresenter implements MapContract.Presenter {

    private MapContract.View mView;

    private DatabaseReference mDatabase;
    private ValueEventListener valueEventListener;
    private List<Picture> pictures;

    public MapPresenter(MapContract.View mapView) {
        mView = mapView;
        mDatabase = FirebaseDatabase.getInstance().getReference();
        pictures = new ArrayList<>();
        valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot data : dataSnapshot.child("pictures").getChildren()) {
                    Picture picture = data.getValue(Picture.class);
                    pictures.add(picture);
                }

                mView.setMarkers(pictures);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
    }

    @Override
    public void subscribe() {
        mDatabase.addValueEventListener(valueEventListener);
    }

    @Override
    public void unsubscribe() {
        mDatabase.removeEventListener(valueEventListener);
    }

    @Override
    public void savePicture(String userId, Bitmap image, Location location) {
        Picture picture = new Picture(userId, location.getLongitude(), location.getLatitude(), image);
        picture.saveToFirebase();
    }

}
