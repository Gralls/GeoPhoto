package com.springer.patryk.geo_photo.model;

import android.graphics.Bitmap;
import android.net.Uri;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.maps.android.clustering.ClusterItem;
import com.springer.patryk.geo_photo.utils.FirebaseUtils;

import java.io.ByteArrayOutputStream;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import io.reactivex.Observable;

/**
 * Created by Patryk on 2017-03-26.
 */


public class Picture implements ClusterItem, Serializable {

    private String pictureId;
    private String uid;
    private Double longtitude;
    private Double latitude;
    private String description;
    private String downloadUrl;
    private boolean publicPhoto;
    private DatabaseReference mDatabase;
    private byte[] imageStorage;

    public Picture() {
    }

    public Picture(String uid, Double longitude, Double latitude, Bitmap image) {
        this.uid = uid;
        this.longtitude = longitude;
        this.latitude = latitude;
        mDatabase = FirebaseUtils.getDatabaseInstace().getReference();
        convertBitmapToBytes(image);
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

    public String getDownloadUrl() {
        return downloadUrl;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("uid", uid);
        result.put("longtitude", longtitude);
        result.put("latitude", latitude);
        result.put("description", description);
        result.put("downloadUrl", downloadUrl);
        result.put("publicPhoto", publicPhoto);
        return result;
    }

    public void convertBitmapToBytes(Bitmap image) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        imageStorage = stream.toByteArray();
    }

    @SuppressWarnings("VisibleForTests")
    public Observable<Picture> saveToFirebase(boolean isPublic) {
        this.publicPhoto = isPublic;
        UploadTask uploadTask = FirebaseStorage
                .getInstance()
                .getReference()
                .child(uid)
                .child(UUID.randomUUID().toString() + ".jpg")
                .putBytes(imageStorage);

        return Observable
                .create(subscriber -> {
                    uploadTask.addOnSuccessListener(taskSnapshot -> {
                        Uri downloadUri = taskSnapshot.getDownloadUrl();
                        downloadUrl = downloadUri.toString();
                        String key = mDatabase.child("picture").push().getKey();

                        Map<String, Object> pictureValues = this.toMap();
                        Map<String, Object> childUpdates = new HashMap<>();

                        childUpdates.put("/pictures/" + key, pictureValues);

                        mDatabase.updateChildren(childUpdates);
                        subscriber.onNext(this);
                    });
                    uploadTask.addOnFailureListener(error -> subscriber.onError(error.getCause()));

                });

    }

    public void removeFromFirebase(OnPictureRemovedListener callback) {
        DatabaseReference database = FirebaseUtils.getDatabaseInstace().getReference("/pictures/" + this.pictureId);
        StorageReference storage = FirebaseStorage.getInstance().getReferenceFromUrl(this.downloadUrl);

        database.removeValue()
                .addOnFailureListener(databaseRef -> callback.onFailure())
                .addOnSuccessListener(databaseRef -> storage.delete()
                        .addOnFailureListener(storageRef -> callback.onFailure())
                        .addOnSuccessListener(storageRef -> callback.onSuccess()));


    }

    public LatLng getPosition() {
        return new LatLng(this.getLatitude(), this.longtitude);
    }

    @Override
    public String getTitle() {
        return "title";
    }

    @Override
    public String getSnippet() {
        return "snippet";
    }

    @Override
    public String toString() {
        return "Picture{" +
                "uid='" + uid + '\'' +
                ", longtitude=" + longtitude +
                ", latitude=" + latitude +
                '}';
    }

    public String getPictureId() {
        return pictureId;
    }

    public void setPictureId(String pictureId) {
        this.pictureId = pictureId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isPublicPhoto() {
        return publicPhoto;
    }

    public void setPublicPhoto(boolean publicPhoto) {
        this.publicPhoto = publicPhoto;
    }

    public interface OnPictureRemovedListener {
        void onSuccess();

        void onFailure();
    }

}
