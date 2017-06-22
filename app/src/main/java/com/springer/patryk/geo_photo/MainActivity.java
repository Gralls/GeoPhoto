package com.springer.patryk.geo_photo;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.springer.patryk.geo_photo.map.MapFragment;
import com.springer.patryk.geo_photo.model.Picture;
import com.springer.patryk.geo_photo.picture_details.PictureDetailsFragment;
import com.springer.patryk.geo_photo.send_picture.SendPictureFragment;
import com.springer.patryk.geo_photo.utils.ActivityUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Patryk on 2017-03-25.
 */

public class MainActivity extends AppCompatActivity implements MapFragment.PictureTakenCallback, MapFragment.ClusterClicked {

    private static final int PERMISSION_ALL_REQUEST_CODEON = 1;
    private MapFragment mapFragment;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.drawer_layout)
    DrawerLayout mDrawerLayout;
    @BindView(R.id.nav_view)
    NavigationView mNavigationView;
    private ActionBarDrawerToggle mDrawerToggle;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        mapFragment = (MapFragment) getSupportFragmentManager().findFragmentById(R.id.container);
        if (mapFragment == null) {
            mapFragment = MapFragment.newInstance();
            ActivityUtils.addFragmentToActivity(getSupportFragmentManager(), mapFragment, R.id.container);
        }

        checkPermissions(this);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        View navHeader = mNavigationView.getHeaderView(0);
        ((TextView) navHeader.findViewById(R.id.name)).setText(user.getEmail());
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        setupDrawer();
    }

    public static void checkPermissions(Activity activity) {
        String[] PERMISSIONS = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.CAMERA};
        if (!hasPermissions(activity, PERMISSIONS)) {
            ActivityCompat.requestPermissions(activity, PERMISSIONS, PERMISSION_ALL_REQUEST_CODEON);
        }
    }

    public static boolean hasPermissions(Context context, String... permissions) {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

    private void setupDrawer() {

        mNavigationView.setNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.nav_logout:
                    FirebaseAuth.getInstance().signOut();
                    finish();
                    break;
                case R.id.nav_account:
                    //TODO Open profile settings screen
                    break;
                default:
                    break;
            }
            item.setChecked(!item.isChecked());
            return true;
        });

        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
                R.string.drawer_open, R.string.drawer_close) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                invalidateOptionsMenu();
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                invalidateOptionsMenu();
            }
        };

        mDrawerToggle.setDrawerIndicatorEnabled(true);
        mDrawerLayout.addDrawerListener(mDrawerToggle);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() == 0) {
            Intent a = new Intent(Intent.ACTION_MAIN);
            a.addCategory(Intent.CATEGORY_HOME);
            a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(a);

        } else
            super.onBackPressed();
    }

    @Override
    public void onPictureTaken(Uri args) {
        SendPictureFragment sendPictureFragment = SendPictureFragment.newInstance();
        Bundle bundle = new Bundle();
        bundle.putString("fileUri", args.toString());
        sendPictureFragment.setArguments(bundle);
        ActivityUtils.replaceFragment(
                getSupportFragmentManager(),
                sendPictureFragment,
                R.id.container,
                true,
                android.R.anim.fade_in,
                android.R.anim.fade_out
        );
    }

    @Override
    public void onClusterClickedListener(Picture picture) {
        PictureDetailsFragment pictureDetailsFragment = PictureDetailsFragment.newInstance();
        Bundle bundle = new Bundle();
        bundle.putSerializable("picture", picture);
        pictureDetailsFragment.setArguments(bundle);
        ActivityUtils.replaceFragment(
                getSupportFragmentManager(),
                pictureDetailsFragment,
                R.id.container,
                true,
                android.R.anim.fade_in,
                android.R.anim.fade_out
        );
    }
}
