package com.springer.patryk.uam_android.authentication;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.springer.patryk.uam_android.R;
import com.springer.patryk.uam_android.authentication.login.LoginFragment;
import com.springer.patryk.uam_android.authentication.register.RegisterFragment;
import com.springer.patryk.uam_android.utils.ActivityUtils;

public class AuthenticationActivity extends AppCompatActivity implements LoginFragment.LoginFragmentCallback, RegisterFragment.RegisterFragmentCallback {

    private LoginFragment mLoginFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authentication);

        mLoginFragment = (LoginFragment) getSupportFragmentManager().findFragmentById(R.id.container);

        if (mLoginFragment == null) {
            mLoginFragment = LoginFragment.newInstance();
            ActivityUtils.addFragmentToActivity(getSupportFragmentManager(), mLoginFragment, R.id.container);
        }
    }

    @Override
    public void showLoginFragment() {
        ActivityUtils.replaceFragment(getSupportFragmentManager(), LoginFragment.newInstance()
                , R.id.container, R.anim.enter_from_left, R.anim.exit_to_left);
    }

    @Override
    public void showRegisterFragment() {
        ActivityUtils.replaceFragment(getSupportFragmentManager(), RegisterFragment.newInstance()
                , R.id.container, R.anim.enter_from_right, R.anim.exit_to_right);
    }
}
