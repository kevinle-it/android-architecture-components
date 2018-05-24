package com.example.trile.poc.ui.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;

import com.example.trile.poc.Constants;
import com.example.trile.poc.R;
import com.example.trile.poc.database.model.MangaItem;
import com.example.trile.poc.ui.adapter.ViewPagerAdapter;
import com.example.trile.poc.ui.customview.CustomViewPager;
import com.example.trile.poc.ui.fragment.MainFragment;
import com.example.trile.poc.ui.fragment.RootFragment;
import com.example.trile.poc.ui.helper.BottomNavigationViewHelper;
import com.example.trile.poc.ui.listener.OnMangaListInteractionListener;

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
