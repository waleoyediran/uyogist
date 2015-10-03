package com.uyogist.uyogist.activity;

import android.content.Intent;
import android.content.SharedPreferences;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;

import com.uyogist.uyogist.util.Constants;
import com.uyogist.uyogist.R;


/**
 * A login screen that offers login Google+ sign in.
 * <p/>
 * ************ IMPORTANT SETUP NOTES: ************
 * In order for Google+ sign in to work with your app, you must first go to:
 * https://developers.google.com/identity/sign-in/android/start-integrating
 * and follow the steps
 */
public class LoginActivity extends AppCompatActivity implements
        View.OnClickListener {

    private View mProgressView;
    private SignInButton mPlusSignInButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Find the Google+ sign in button.
        mPlusSignInButton = (SignInButton) findViewById(R.id.plus_sign_in_button);

        /**
         * TODO: Check if the device has Google Play Services installed
         * If installed, setClick Listener,
         * if not, show a dialog asking the user to install GooglePlay services
         */
        mPlusSignInButton.setOnClickListener(this);

        //TODO: Create GPlus Client

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

    //TODO: Override onConnected, and log the user into the app (login())


    //TODO: Override onConnectionFailed, hide the progressBar and show ar error message


    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.plus_sign_in_button) {
            onSignInClicked();
        }
    }

    private void onSignInClicked() {
        // Show a message to the user that we are signing in.
        mProgressView.setVisibility(View.VISIBLE);
        // User clicked the sign-in button, so begin the sign-in process and automatically
        // attempt to resolve any errors that occur.
        //mShouldResolve = true;//TODO: uncomment this line to allow GooglePlay Services resolve errors

        // TODO: Remove the block below and Connect the GPlusAPI client
        // To allow GPlus authentication
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                login();
            }
        }).start();

    }

}

