package com.springer.patryk.uam_android.login;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.springer.patryk.uam_android.R;
import com.springer.patryk.uam_android.utils.ActivityUtils;

public class LoginActivity extends AppCompatActivity {

    private LoginPresenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authentication);

        LoginFragment authenticationFragment = (LoginFragment) getSupportFragmentManager().findFragmentById(R.id.container);

        if (authenticationFragment == null) {
            authenticationFragment = LoginFragment.newInstance();
            ActivityUtils.addFragmentToActivity(getSupportFragmentManager(), authenticationFragment, R.id.container);
        }

        mPresenter = new LoginPresenter(authenticationFragment);
        authenticationFragment.setPresenter(mPresenter);

    }
}
