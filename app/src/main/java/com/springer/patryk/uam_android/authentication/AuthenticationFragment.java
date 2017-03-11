package com.springer.patryk.uam_android.authentication;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.springer.patryk.uam_android.R;


/**
 * Created by Patryk on 2017-03-11.
 */

public class AuthenticationFragment extends Fragment implements AuthenticationContract.View {

    private AuthenticationContract.Presenter mPresenter;

    private EditText mEmail;
    private EditText mPassword;
    private Button mSubmit;

    public static AuthenticationFragment newInstance() {
        return new AuthenticationFragment();
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
    public void setPresenter(@NonNull AuthenticationContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void showAuthenticationError() {
        Toast.makeText(getContext(), "Authentication error", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showMainPage() {
        Toast.makeText(getContext(), "Authentication succeeded", Toast.LENGTH_SHORT).show();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_authentication, container);

        mEmail = (EditText) view.findViewById(R.id.email);
        mPassword = (EditText) view.findViewById(R.id.password);
        mSubmit = (Button) view.findViewById(R.id.submitButton);

        mSubmit.setOnClickListener(view1 -> mPresenter.checkCredentials(mEmail.getText().toString()
                , mPassword.getText().toString()));

        return super.onCreateView(inflater, container, savedInstanceState);
    }
}
