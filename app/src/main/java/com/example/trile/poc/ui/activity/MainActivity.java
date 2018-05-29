package com.example.trile.poc.ui.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.example.trile.poc.R;
import com.example.trile.poc.ui.fragment.MainFragment;

/**
 * @author trile
 * @since 5/22/18 at 14:25
 */
public class MainActivity extends AppCompatActivity {
    public static final String TAG = MainActivity.class.getSimpleName();

    private final static String MAIN_FRAGMENT_TAG = "MAIN_FRAGMENT_TAG";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MainFragment mainFragment = MainFragment.newInstance();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.main_container, mainFragment);
        transaction.commit();
    }

    @Override
    public void onBackPressed() {
        final Fragment fragment = getSupportFragmentManager()
                .findFragmentById(R.id.main_container);

        if (fragment instanceof MainFragment && ((MainFragment) fragment).onBackPressed()) {
            super.onBackPressed();
        }
    }
}
