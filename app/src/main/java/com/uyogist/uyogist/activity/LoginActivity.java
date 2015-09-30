package com.uyogist.uyogist.activity;

import android.content.Intent;
import android.content.SharedPreferences;

import android.os.Bundle;
import android.view.View;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;

import com.uyogist.uyogist.util.Constants;
import com.uyogist.uyogist.R;


/**
 * A login screen that offers login via email/password and via Google+ sign in.
 * <p/>
 * ************ IMPORTANT SETUP NOTES: ************
 * In order for Google+ sign in to work with your app, you must first go to:
 * https://developers.google.com/+/mobile/android/getting-started#step_1_enable_the_google_api
 * and follow the steps in "Step 1" to create an OAuth 2.0 client for your package.
 */
public class LoginActivity extends GoogleAPIBaseActivity implements
        View.OnClickListener {

    private View mProgressView;
    private SignInButton mPlusSignInButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Find the Google+ sign in button.
        mPlusSignInButton = (SignInButton) findViewById(R.id.plus_sign_in_button);
        if (supportsGooglePlayServices()) {
            // Set a listener to connect the user when the G+ button is clicked.
            mPlusSignInButton.setOnClickListener(this);
        } else {
            // Tell the app to install Google Play Services

        }

        createGPlusClient();

        mProgressView = findViewById(R.id.login_progress);
    }



    /**
     * Go To Home
     */
    private void login() {
        SharedPreferences.Editor editor = getSharedPreferences(
                Constants.PREFS_NAME, 0).edit();
        editor.putBoolean(Constants.PREFS_IS_SIGNED_IN, true);
        editor.apply();
        Intent i = new Intent(this, HomeActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);
        finish();
    }

    @Override
    public void onConnected(Bundle bundle) {
        super.onConnected(bundle);
        login();
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        super.onConnectionFailed(connectionResult);
        mProgressView.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.plus_sign_in_button) {
            onSignInClicked();
        }
    }

    private void onSignInClicked() {
        mProgressView.setVisibility(View.VISIBLE);
        // User clicked the sign-in button, so begin the sign-in process and automatically
        // attempt to resolve any errors that occur.
        mShouldResolve = true;
        mGoogleApiClient.connect();

        // Show a message to the user that we are signing in.
//        mStatus.setText(R.string.signing_in);
    }

}

