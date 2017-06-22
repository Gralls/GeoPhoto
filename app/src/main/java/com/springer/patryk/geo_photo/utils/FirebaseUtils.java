package com.springer.patryk.geo_photo.utils;

import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by Patryk on 2017-06-22.
 */

public class FirebaseUtils {

    private static FirebaseDatabase mDatabase;

    public static FirebaseDatabase getDatabaseInstace() {
        if (mDatabase == null) {
            mDatabase = FirebaseDatabase.getInstance();
            mDatabase.setPersistenceEnabled(true);
        }
        return mDatabase;
    }

}
