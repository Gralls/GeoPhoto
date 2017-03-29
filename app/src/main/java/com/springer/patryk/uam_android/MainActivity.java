package com.springer.patryk.uam_android;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.springer.patryk.uam_android.map.MapFragment;
import com.springer.patryk.uam_android.picture.SendPictureFragment;
import com.springer.patryk.uam_android.utils.ActivityUtils;

/**
 * Created by Patryk on 2017-03-25.
 */

public class MainActivity extends AppCompatActivity implements MapFragment.PictureTakenCallback {

    private MapFragment mapFragment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mapFragment = (MapFragment) getSupportFragmentManager().findFragmentById(R.id.container);

        if (mapFragment == null) {
            mapFragment = MapFragment.newInstance();
            ActivityUtils.addFragmentToActivity(getSupportFragmentManager(), mapFragment, R.id.container);
        }

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }

    }

    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() == 0)
            FirebaseAuth.getInstance().signOut();
        else
            super.onBackPressed();
    }

    @Override
    public void onPictureTaken(Bundle args) {
        SendPictureFragment sendPictureFragment = SendPictureFragment.newInstance();
        sendPictureFragment.setArguments(args);
        ActivityUtils.replaceFragment(getSupportFragmentManager(), sendPictureFragment, R.id.container, true, android.R.anim.fade_in, android.R.anim.fade_out);
    }

}
