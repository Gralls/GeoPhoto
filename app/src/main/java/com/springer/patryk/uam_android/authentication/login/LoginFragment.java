package com.springer.patryk.uam_android.authentication.login;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.springer.patryk.uam_android.MainActivity;
import com.springer.patryk.uam_android.R;
import com.springer.patryk.uam_android.authentication.AuthenticationActivity;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * Created by Patryk on 2017-03-11.
 */

public class LoginFragment extends Fragment implements LoginContract.View {

    private LoginContract.Presenter mPresenter;
    private LoginFragmentCallback mCallback;

    @BindView(R.id.login_email)
    TextInputEditText mEmail;
    @BindView(R.id.login_email_layout)
    TextInputLayout mEmailLayout;
    @BindView(R.id.login_password)
    TextInputEditText mPassword;
    @BindView(R.id.login_password_layout)
    TextInputLayout mPasswordLayout;
    @BindView(R.id.login_button)
    AppCompatButton mSubmit;
    @BindView(R.id.signup_link)
    TextView registerLink;

    public static LoginFragment newInstance() {
        return new LoginFragment();
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.subscribe();
    }

    @Override
    public void onPause() {
        super.onPause();
        mPresenter.unsubscribe();
    }

    @Override
    public void setPresenter(@NonNull LoginContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void showAuthenticationError() {
        Toast.makeText(getContext(), "Authentication error", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void setEmailError(int errorMessage) {
        mEmailLayout.setError(getString(errorMessage));
    }

    @Override
    public void setPasswordError(int errorMessage) {
        mPasswordLayout.setError(getString(errorMessage));
    }

    @Override
    public void showMainPage() {
        Intent intent = new Intent(getContext(), MainActivity.class);
        startActivity(intent);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mCallback = (AuthenticationActivity) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(mCallback.toString() +
                    "must implement LoginFragmentCallback");
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresenter = new LoginPresenter(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View loginView = inflater.inflate(R.layout.fragment_authentication, null, false);
        ButterKnife.bind(this, loginView);

        mEmail.setText("patryk.springer@gmail.com");
        mPassword.setText("Qwerty123");

        mSubmit.setOnClickListener(view -> mPresenter.checkCredentials(mEmail.getText().toString()
                , mPassword.getText().toString()));

        registerLink.setOnClickListener(view -> mCallback.showRegisterFragment());

        return loginView;
    }

    public interface LoginFragmentCallback {
        void showRegisterFragment();
    }
}
