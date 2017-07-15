package com.springer.patryk.geo_photo.screens.profile_settings;

/**
 * Created by Patryk on 2017-03-22.
 */

public class ProfileSettingsPresenter implements ProfileSettingsContract.Presenter {

    private final String LOG_TAG = ProfileSettingsPresenter.class.getName();
    private ProfileSettingsContract.View mView;

    public ProfileSettingsPresenter(ProfileSettingsContract.View view) {
        mView = view;
    }

    @Override
    public void subscribe() {

    }

    @Override
    public void unsubscribe() {

    }

    //TODO
    @Override
    public void changeProfile(String username, String email) {
//        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
//        UserProfileChangeRequest.Builder profileChangeRequest = new UserProfileChangeRequest.Builder();
//
//        Observable.create(subscriber -> {
//
//        });
//        if (!username.isEmpty()) {
//            profileChangeRequest.setDisplayName(username);
//            user.updateProfile(profileChangeRequest.build())
//                    .addOnSuccessListener(aVoid -> {
//
//                    });
//        }
//        if (!email.isEmpty()) {
//            user.updateEmail(email);
//        }
//        Log.v(LOG_TAG, "Username: " + username + ", Email: " + email);
    }
}
