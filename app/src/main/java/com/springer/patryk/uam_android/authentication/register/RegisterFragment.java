package com.springer.patryk.uam_android.authentication.register;

import android.content.Context;
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

import com.springer.patryk.uam_android.R;
import com.springer.patryk.uam_android.authentication.AuthenticationActivity;


/**
 * Created by Patryk on 2017-03-11.
 */

public class RegisterFragment extends Fragment implements RegisterContract.View {

    private RegisterContract.Presenter mPresenter;
    private RegisterFragmentCallback mCallback;

    private TextInputEditText mEmail;
    private TextInputLayout mEmailLayout;
    private TextInputEditText mPassword;
    private TextInputLayout mPasswordLayout;
    private TextInputEditText mConfirmPassword;
    private TextInputLayout mConfirmPasswordLayout;
    private AppCompatButton mSubmit;
    private TextView loginLink;

    public static RegisterFragment newInstance() {
        return new RegisterFragment();
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
    public void setPresenter(@NonNull RegisterContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void showAuthenticationError() {
        Toast.makeText(getContext(), "Authentication error", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showEmailError(int errorMessage) {
        mEmailLayout.setError(getString(errorMessage));
    }

    @Override
    public void showPasswordError(int errorMessage) {
        mPasswordLayout.setError(getString(errorMessage));
    }

    @Override
    public void showConfirmPasswordError(int errorMessage) {
        mConfirmPasswordLayout.setError(getString(errorMessage));
    }

    @Override
    public void showMainPage() {
        Toast.makeText(getContext(), "Authentication succeeded", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresenter = new RegisterPresenter(this);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mCallback = (AuthenticationActivity) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(mCallback.toString() +
                    "must implement RegisterFragmentCallback");
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View registerView = inflater.inflate(R.layout.fragment_registration, null, false);

        mEmail = (TextInputEditText) registerView.findViewById(R.id.register_email);
        mEmailLayout = (TextInputLayout) registerView.findViewById(R.id.register_email_layout);
        mPassword = (TextInputEditText) registerView.findViewById(R.id.register_password);
        mPasswordLayout = (TextInputLayout) registerView.findViewById(R.id.register_password_layout);
        mConfirmPassword = (TextInputEditText) registerView.findViewById(R.id.confirm_password);
        mConfirmPasswordLayout = (TextInputLayout) registerView.findViewById(R.id.register_confirm_password_layout);
        mSubmit = (AppCompatButton) registerView.findViewById(R.id.register_button);
        loginLink = (TextView) registerView.findViewById(R.id.signin_link);

        mSubmit.setOnClickListener(view -> mPresenter.checkCredentials(mEmail.getText().toString()
                , mPassword.getText().toString(), mConfirmPassword.getText().toString()));

        loginLink.setOnClickListener(view -> mCallback.showLoginFragment());

        return registerView;
    }

    public interface RegisterFragmentCallback {
        void showLoginFragment();
    }
}
