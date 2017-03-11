package com.springer.patryk.uam_android.authentication;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.springer.patryk.uam_android.R;
import com.springer.patryk.uam_android.utils.ActivityUtils;

public class AuthenticationActivity extends AppCompatActivity {

    private AuthenticationPresenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authentication);

        AuthenticationFragment authenticationFragment = (AuthenticationFragment) getSupportFragmentManager().findFragmentById(R.id.container);

        if (authenticationFragment == null) {
            authenticationFragment = AuthenticationFragment.newInstance();
            ActivityUtils.addFragmentToActivity(getSupportFragmentManager(), authenticationFragment, R.id.container);
        }

        mPresenter = new AuthenticationPresenter(authenticationFragment);
        authenticationFragment.setPresenter(mPresenter);

    }
}
