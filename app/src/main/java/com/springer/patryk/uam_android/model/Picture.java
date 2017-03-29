package com.springer.patryk.uam_android.model;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import com.google.android.gms.maps.model.LatLng;
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

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public Double getLongtitude() {
        return longtitude;
    }

    public void setLongtitude(Double longtitude) {
        this.longtitude = longtitude;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
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

    public String convertToBase64(Bitmap image) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        byte[] byteFormat = stream.toByteArray();
        return Base64.encodeToString(byteFormat, Base64.NO_WRAP);
    }

    public static Bitmap convertBase64ToBitmap(String base64) {
        byte[] decodedString = Base64.decode(base64, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
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

    public LatLng getPosition() {
        return new LatLng(this.getLatitude(), this.longtitude);
    }

    @Override
    public String toString() {
        return "Picture{" +
                "uid='" + uid + '\'' +
                ", longtitude=" + longtitude +
                ", latitude=" + latitude +
                '}';
    }
}
