package com.example.trile.poc.ui.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.example.trile.poc.R;
import com.example.trile.poc.ui.fragment.MainFragment;
import com.example.trile.poc.ui.fragment.SearchFragment;

import androidx.navigation.NavController;
import androidx.navigation.NavController.OnNavigatedListener;
import androidx.navigation.NavDestination;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;

/**
 * @author trile
 * @since 5/22/18 at 14:25
 */
public class MainActivity extends AppCompatActivity {
    public static final String TAG = MainActivity.class.getSimpleName();

    private final static String MAIN_FRAGMENT_TAG = "MAIN_FRAGMENT_TAG";

    private NavHostFragment mNavHostFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mNavHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
        mNavHostFragment.getNavController().addOnNavigatedListener(new OnNavigatedListener() {
            @Override
            public void onNavigated(@NonNull NavController controller, @NonNull NavDestination destination) {
                /**
                 * This will be called whenever we navigate between destinations in the app.
                 * 
                 * This will intercept the back button press event.
                 * 
                 * Example: 
                 * Press search button (from {@link MainFragment}) -> this method get called.
                 * 
                 * Press back button on device (from {@link SearchFragment}) ->
                 * {@link MainActivity#onBackPressed()} get called -> then this method also get
                 * called immediately right after {@link MainActivity#onBackPressed()}.
                 * 
                 * Press back button on device (from {@link MainFragment}) ->
                 * ONLY {@link MainActivity#onBackPressed()} get called because
                 * {@link Navigation#findNavController(Activity, int).navigateUp()}
                 * return {@false}.
                 */
            }
        });
    }

    @Override
    public void onBackPressed() {
        /**
         * {@link Navigation#findNavController(Activity, int).navigateUp()} return {@true} if
         * navigation was successful, {@false} otherwise.
         *
         * In this case, it returns false when we are on the {@link MainFragment} which is the root
         * element in {@link NavController.mBackStack}, then we just exit the app.
         */
        if (!Navigation.findNavController(this, R.id.nav_host_fragment).navigateUp()) {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        return Navigation.findNavController(this, R.id.nav_host_fragment).navigateUp();
    }
}
