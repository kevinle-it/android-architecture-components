package com.example.trile.poc.ui.activity;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.example.trile.poc.R;
import com.example.trile.poc.adapter.ViewPagerAdapter;
import com.example.trile.poc.customview.CustomViewPager;
import com.example.trile.poc.databinding.ActivityMainBinding;
import com.example.trile.poc.helper.BottomNavigationViewHelper;
import com.example.trile.poc.helper.Constants;
import com.example.trile.poc.ui.fragment.RootFragment;

public class MainActivity extends AppCompatActivity {
    public static final String TAG = MainActivity.class.getSimpleName();

    private ActivityMainBinding mBinding;

    private DrawerLayout mDrawerLayout;
    private BottomNavigationView mBottomNavigationView;
    private CustomViewPager mViewPager;
    private ViewPagerAdapter mViewPagerAdapter;

    private MenuItem mPrevMenuItem;

    private ViewPager.SimpleOnPageChangeListener mViewPagerOnPageChangeListener = new ViewPager.SimpleOnPageChangeListener() {
        @Override
        public void onPageSelected(int position) {
            if (mPrevMenuItem != null) {
                mPrevMenuItem.setChecked(false);
            } else {
                mBottomNavigationView.getMenu().getItem(0).setChecked(false);
            }
            mBottomNavigationView.getMenu().getItem(position).setChecked(true);
            mPrevMenuItem = mBottomNavigationView.getMenu().getItem(position);
        }
    };

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case Constants.NAVIGATION_INDEX_DISCOVER:
                    if (mViewPager.getCurrentItem() == Constants.VIEW_PAGER_INDEX_DISCOVER) {
                        return false;
                    } else {
                        mViewPager.setCurrentItem(Constants.VIEW_PAGER_INDEX_DISCOVER, false);
                    }
                    break;
                case Constants.NAVIGATION_INDEX_FAVORITES:
                    if (mViewPager.getCurrentItem() == Constants.VIEW_PAGER_INDEX_FAVORITES) {
                        return false;
                    } else {
                        mViewPager.setCurrentItem(Constants.VIEW_PAGER_INDEX_FAVORITES, false);
                    }
                    break;
                case Constants.NAVIGATION_INDEX_RECENT:
                    if (mViewPager.getCurrentItem() == Constants.VIEW_PAGER_INDEX_RECENT) {
                        return false;
                    } else {
                        mViewPager.setCurrentItem(Constants.VIEW_PAGER_INDEX_RECENT, false);
                    }
                    break;
                case Constants.NAVIGATION_INDEX_DOWNLOADS:
                    if (mViewPager.getCurrentItem() == Constants.VIEW_PAGER_INDEX_DOWNLOADS) {
                        return false;
                    } else {
                        mViewPager.setCurrentItem(Constants.VIEW_PAGER_INDEX_DOWNLOADS, false);
                    }
                    break;
            }
            return true;
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        mBinding.setLifecycleOwner(this);

        // Initialize Drawer Layout
        mDrawerLayout = mBinding.drawerLayout;

        // Initialize View Pager.
        mViewPager = mBinding.viewPager;
        mViewPager.setPagingEnabled(false);
        mViewPager.setOffscreenPageLimit(Constants.VIEW_PAGER_OFF_SCREEN_PAGE_LIMIT);

        // Initialize Bottom Navigation View
        mBottomNavigationView = mBinding.bottomNavigation;
        mBottomNavigationView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        BottomNavigationViewHelper.disableShiftMode(mBottomNavigationView);

        setupViewPager(mViewPager);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mViewPager.addOnPageChangeListener(mViewPagerOnPageChangeListener);
    }

    @Override
    protected void onStop() {
        mViewPager.removeOnPageChangeListener(mViewPagerOnPageChangeListener);
        super.onStop();
    }

    @Override
    public void onBackPressed() {
        Fragment currentFragment = ((RootFragment) mViewPagerAdapter.getItem(mViewPager.getCurrentItem())).getCurrentFragment();
        switch (mViewPager.getCurrentItem()) {
            case Constants.VIEW_PAGER_INDEX_DISCOVER:
//                if (!((DiscoverFragment) currentFragment).onBackPressed()) {
//                    super.onBackPressed();
//                }
                break;
            case Constants.VIEW_PAGER_INDEX_FAVORITES:
//                if (!((FavoritesFragment) currentFragment).onBackPressed()) {
//                    super.onBackPressed();
//                }
                break;
            case Constants.VIEW_PAGER_INDEX_RECENT:
//                if (!((RecentFragment) currentFragment).onBackPressed()) {
//                    super.onBackPressed();
//                }
                break;
            case Constants.VIEW_PAGER_INDEX_DOWNLOADS:
//                if (!((DownloadsFragment) currentFragment).onBackPressed()) {
//                    super.onBackPressed();
//                }
                break;
            default:
                super.onBackPressed();
                break;
        }
    }

    private void setupViewPager(CustomViewPager viewPager) {
        mViewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());

        Fragment fragment1 = RootFragment.newInstance(Constants.VIEW_PAGER_INDEX_DISCOVER);
//        ((RootFragment) fragment1).setMangaListInteractionListener(mMangaListInteractionListener);
        mViewPagerAdapter.addFragment(fragment1);

        Fragment fragment2 = RootFragment.newInstance(Constants.VIEW_PAGER_INDEX_FAVORITES);
//        ((RootFragment) fragment2).setMangaListInteractionListener(mMangaListInteractionListener);
        mViewPagerAdapter.addFragment(fragment2);

        Fragment fragment3 = RootFragment.newInstance(Constants.VIEW_PAGER_INDEX_RECENT);
//        ((RootFragment) fragment3).setMangaListInteractionListener(mMangaListInteractionListener);
        mViewPagerAdapter.addFragment(fragment3);

        Fragment fragment4 = RootFragment.newInstance(Constants.VIEW_PAGER_INDEX_DOWNLOADS);
//        ((RootFragment) fragment4).setMangaListInteractionListener(mMangaListInteractionListener);
        mViewPagerAdapter.addFragment(fragment4);

        viewPager.setAdapter(mViewPagerAdapter);
    }
}
