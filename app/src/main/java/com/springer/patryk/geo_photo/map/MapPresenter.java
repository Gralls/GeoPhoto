package com.springer.patryk.geo_photo.map;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.springer.patryk.geo_photo.model.Picture;

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

        valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //TODO do something clever with separate private and public pictures
                pictures = new ArrayList<>();
                for (DataSnapshot data : dataSnapshot.child("pictures").getChildren()) {
                    Picture picture = data.getValue(Picture.class);
                    picture.setPictureId(data.getKey());
                    if (!picture.getIsPublic() && !picture.getUid().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
                        continue;
                    }
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

}
