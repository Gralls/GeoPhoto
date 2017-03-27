package com.springer.patryk.uam_android.model;

import android.graphics.Bitmap;
import android.util.Base64;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Patryk on 2017-03-26.
 */


public class Picture {

    private String uid;
    private Double longtitude;
    private Double latitude;
    private String image;

    public Picture() {
    }

    public Picture(String uid, Double longtitude, Double latitude, Bitmap image) {
        this.uid = uid;
        this.longtitude = longtitude;
        this.latitude = latitude;
        this.image = convertToBase64(image);
    }

    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("uid", uid);
        result.put("longtitude", longtitude);
        result.put("latitude", latitude);
        result.put("image", image);
        return result;
    }

    private String convertToBase64(Bitmap image) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        byte[] byteFormat = stream.toByteArray();
        return Base64.encodeToString(byteFormat, Base64.NO_WRAP);
    }

    public void saveToFirebase() {
        DatabaseReference database = FirebaseDatabase.getInstance().getReference();
        String key = database.child("picture").push().getKey();
        Map<String, Object> pictureValues = this.toMap();
        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("/pictures/" + key, pictureValues);
        childUpdates.put("/user-picture/" + this.uid + "/" + key,
                pictureValues);

        database.updateChildren(childUpdates);
    }
}
