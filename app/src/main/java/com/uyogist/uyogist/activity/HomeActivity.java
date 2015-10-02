package com.uyogist.uyogist.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.plus.Plus;
import com.google.android.gms.plus.model.people.Person;
import com.squareup.picasso.Picasso;
import com.uyogist.uyogist.util.CircularImageTransformation;
import com.uyogist.uyogist.util.Constants;
import com.uyogist.uyogist.fragment.CreateGistDialogFragment;
import com.uyogist.uyogist.adapter.GistAdapter;
import com.uyogist.uyogist.R;

//TODO: Extend the GoogleAPIBaseActivity
public class HomeActivity extends AppCompatActivity {

    private Toolbar mToolbar;
    private NavigationView navigationView;
    private DrawerLayout drawer;
    private View loadingView;
    private RecyclerView mRecyclerView;
    private FloatingActionButton fab;
    private TextView mNameText;
    private ImageView mProfileImageView;

    private String mName = "Anonymous";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //check if user is Logged in
        SharedPreferences settings = getSharedPreferences(
                Constants.PREFS_NAME, 0);
        boolean isSignedIn = settings.getBoolean(Constants.PREFS_IS_SIGNED_IN, false);
        if (!isSignedIn){
            backToLogin();
            return;
        }

        setContentView(R.layout.activity_home);

        mToolbar = (Toolbar) findViewById(R.id.toolbar_actionbar);
        navigationView = (NavigationView) findViewById(R.id.drawer_view);
        drawer = (DrawerLayout) findViewById(R.id.drawer);
        loadingView = findViewById(R.id.loading_view);
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);
        fab = (FloatingActionButton) findViewById(R.id.fab);
        mNameText = (TextView) findViewById(R.id.name);
        mProfileImageView = (ImageView) findViewById(R.id.picture);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // close existing dialog fragments
                FragmentManager manager = getSupportFragmentManager();
                Fragment frag = manager.findFragmentByTag("fragment_camera");
                if (frag != null) {
                    manager.beginTransaction().remove(frag).commit();
                }

                Bundle bundle = new Bundle();
                bundle.putString(Constants.PROFILE_NAME, mName);

                CreateGistDialogFragment alertDialogFragment = new CreateGistDialogFragment();
                alertDialogFragment.setArguments(bundle);
                alertDialogFragment.show(manager, "fragment_camera");
            }
        });

        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(new GistAdapter(HomeActivity.this, loadingView));
        setSupportActionBar(mToolbar);
        setUpNavDrawer();

        //TODO: Create a GooglePlus API client to allow us get the user's profile information
    }

    private void backToLogin() {
        Intent i = new Intent(this, LoginActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);
        finish();
    }

    //TODO: Override the onConnected Method in the GoogleAPIBaseActivity class
    // Retrieve the Profile name and photo and update the NavigationDrawer Header


    //TODO: Override the onConnectionFailed method in the GoogleAPIBaseActivity class
    // send the user back to the Login page (backToLogin()) if the user is not signed in


    private void setUpNavDrawer() {
        if (mToolbar != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            mToolbar.setNavigationIcon(R.drawable.ic_menu_white_24dp);
            mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    drawer.openDrawer(GravityCompat.START);
                }
            });
        }
        navigationView.setNavigationItemSelectedListener(new NavigationListener());
    }

    private class NavigationListener implements NavigationView.OnNavigationItemSelectedListener{

        @Override
        public boolean onNavigationItemSelected(MenuItem menuItem) {
            menuItem.setChecked(true);
            switch (menuItem.getItemId()) {
                case R.id.navigation_item_help:

                    drawer.closeDrawers();
                    return true;
                default:
                    return true;
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_logout) {
            SharedPreferences.Editor editor = getSharedPreferences(
                    Constants.PREFS_NAME, 0).edit();
            editor.putBoolean(Constants.PREFS_IS_SIGNED_IN, false);
            editor.apply();

            //TODO: Log the user out of the GooglePlus client

            backToLogin();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
