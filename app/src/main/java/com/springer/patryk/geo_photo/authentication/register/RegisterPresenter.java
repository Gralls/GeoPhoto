package com.springer.patryk.geo_photo.authentication.register;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.springer.patryk.geo_photo.R;

/**
 * Created by Patryk on 2017-03-11.
 */

public class RegisterPresenter implements RegisterContract.Presenter {

    private static final String TAG = "RegisterPresenter";

    private RegisterContract.View mAuthenticationView;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    public RegisterPresenter(@NonNull RegisterContract.View authenticationView) {
        mAuthenticationView = authenticationView;
        mAuthenticationView.setPresenter(this);


        mAuth = FirebaseAuth.getInstance();
        mAuthListener = firebaseAuth -> {
            FirebaseUser user = firebaseAuth.getCurrentUser();
            if (user != null) {
                Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
            } else {
                Log.d(TAG, "onAuthStateChanged:signed_out");
            }

        };
    }

    @Override
    public void subscribe() {
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void unsubscribe() {
        mAuth.removeAuthStateListener(mAuthListener);
    }

    @Override
    public void checkCredentials(String email, String password, String confirmPassword) {
        //TODO add watchers to editText
        boolean validation = true;
        if (email.isEmpty()) {
            validation = false;
            mAuthenticationView.showEmailError(R.string.required_field);
        }
        if (password.isEmpty()) {
            validation = false;
            mAuthenticationView.showPasswordError(R.string.required_field);
        }
        if (confirmPassword.isEmpty()) {
            validation = false;
            mAuthenticationView.showConfirmPasswordError(R.string.required_field);
        }
        if (!confirmPassword.equals(password)) {
            validation = false;
            mAuthenticationView.showPasswordError(R.string.different_passwords_error);
            mAuthenticationView.showConfirmPasswordError(R.string.different_passwords_error);
        }
        if (validation)
            createUser(email, password);
    }

    private void createUser(String email, String password) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    Log.d(TAG, "signInWithEmail:onComplete:" + task.isSuccessful());
                    if (!task.isSuccessful()) {
                        Log.w(TAG, "signInWithEmail:failed", task.getException());
                        mAuthenticationView.showAuthenticationError();
                    } else {
                        mAuthenticationView.showMainPage();
                    }
                });
    }
}
