package com.springer.patryk.geo_photo.screens.profile_settings;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.springer.patryk.geo_photo.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Patryk on 2017-03-22.
 */

public class ProfileSettingsFragment extends Fragment implements ProfileSettingsContract.View {

    @BindView(R.id.profile_settings_picture)
    protected ImageView picture;
    @BindView(R.id.profile_settings_username)
    protected TextInputEditText username;
    @BindView(R.id.profile_settings_email)
    protected TextInputEditText email;
    private ProfileSettingsContract.Presenter mPresenter;

    public static ProfileSettingsFragment newInstance() {
        return new ProfileSettingsFragment();
    }

    @OnClick(R.id.profile_settings_confirm)
    protected void onSaveClick() {
        String emailValue = email.getEditableText().toString();
        String usernameValue = username.getEditableText().toString();
        mPresenter.changeProfile(usernameValue, emailValue);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresenter = new ProfileSettingsPresenter(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile_settings, null, false);
        ButterKnife.bind(this, view);

        return view;
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
    public void setPresenter(ProfileSettingsContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void onChangeCompleted() {

    }

    @Override
    public void onChangeFailure(Throwable throwable) {

    }
}
