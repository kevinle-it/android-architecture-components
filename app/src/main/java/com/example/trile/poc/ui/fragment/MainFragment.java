package com.example.trile.poc.ui.fragment;


import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.trile.poc.Constants;
import com.example.trile.poc.R;
import com.example.trile.poc.database.model.MangaItem;
import com.example.trile.poc.databinding.FragmentMainBinding;
import com.example.trile.poc.ui.adapter.ViewPagerAdapter;
import com.example.trile.poc.ui.customview.CustomViewPager;
import com.example.trile.poc.ui.helper.BottomNavigationViewHelper;
import com.example.trile.poc.ui.listener.OnMangaListInteractionListener;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MainFragment#newInstance} factory method to
 * create an instance of this fragment.
 *
 * @author trile
 * @since 5/22/18 at 14:14
 */
public class MainFragment extends Fragment {

    private FragmentMainBinding mBinding;

    private DrawerLayout mDrawerLayout;
    private NavigationView mNavigationView;
    private BottomNavigationView mBottomNavigationView;
    private CustomViewPager mViewPager;
    private ViewPagerAdapter mViewPagerAdapter;

    private MenuItem mPrevMenuItem;

    private View.OnClickListener mNavigationToolbarButtonOnClickListener =
            new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mDrawerLayout.openDrawer(GravityCompat.START);
                }
            };

    private OnMangaListInteractionListener mMangaListInteractionListener =
            new OnMangaListInteractionListener() {
                @Override
                public void onOpenMangaInfo(MangaItem mangaItem) {

                }

                @Override
                public void onOpenMangaChapter(MangaItem mangaItem) {

                }
            };

    private ViewPager.SimpleOnPageChangeListener mViewPagerOnPageChangeListener =
            new ViewPager.SimpleOnPageChangeListener() {
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
                        mViewPager.setCurrentItem(
                                Constants.VIEW_PAGER_INDEX_DISCOVER, false
                        );
                    }
                    break;
                case Constants.NAVIGATION_INDEX_FAVORITES:
                    if (mViewPager.getCurrentItem() == Constants.VIEW_PAGER_INDEX_FAVORITES) {
                        return false;
                    } else {
                        mViewPager.setCurrentItem(
                                Constants.VIEW_PAGER_INDEX_FAVORITES, false
                        );
                    }
                    break;
                case Constants.NAVIGATION_INDEX_RECENT:
                    if (mViewPager.getCurrentItem() == Constants.VIEW_PAGER_INDEX_RECENT) {
                        return false;
                    } else {
                        mViewPager.setCurrentItem(
                                Constants.VIEW_PAGER_INDEX_RECENT, false
                        );
                    }
                    break;
                case Constants.NAVIGATION_INDEX_DOWNLOADS:
                    if (mViewPager.getCurrentItem() == Constants.VIEW_PAGER_INDEX_DOWNLOADS) {
                        return false;
                    } else {
                        mViewPager.setCurrentItem(
                                Constants.VIEW_PAGER_INDEX_DOWNLOADS, false
                        );
                    }
                    break;
            }
            return true;
        }

    };


    public MainFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment.
     *
     * @return A new instance of fragment MainFragment.
     */
    public static MainFragment newInstance() {
        MainFragment fragment = new MainFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_main, container, false);
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

        return mBinding.getRoot();
    }

    @Override
    public void onStart() {
        super.onStart();
        mViewPager.addOnPageChangeListener(mViewPagerOnPageChangeListener);
    }

    @Override
    public void onStop() {
        mViewPager.removeOnPageChangeListener(mViewPagerOnPageChangeListener);
        super.onStop();
    }

    public boolean onBackPressed() {
        Fragment currentFragment = ((RootFragment) mViewPagerAdapter
                .getItem(mViewPager.getCurrentItem())).getCurrentFragment();
        switch (mViewPager.getCurrentItem()) {
            case Constants.VIEW_PAGER_INDEX_DISCOVER:
//                if (!((DiscoverFragment) currentFragment).onBackPressed()) {
//                    super.onBackPressed();
//                }
                return true;
            case Constants.VIEW_PAGER_INDEX_FAVORITES:
//                if (!((FavoritesFragment) currentFragment).onBackPressed()) {
//                    super.onBackPressed();
//                }
                return true;
            case Constants.VIEW_PAGER_INDEX_RECENT:
//                if (!((RecentFragment) currentFragment).onBackPressed()) {
//                    super.onBackPressed();
//                }
                return true;
            case Constants.VIEW_PAGER_INDEX_DOWNLOADS:
//                if (!((DownloadsFragment) currentFragment).onBackPressed()) {
//                    super.onBackPressed();
//                }
                return true;
            default:
                return true;
        }
    }

    private void setupViewPager(CustomViewPager viewPager) {
        mViewPagerAdapter = new ViewPagerAdapter(getChildFragmentManager());

        Fragment fragment1 = RootFragment.newInstance(Constants.VIEW_PAGER_INDEX_DISCOVER);
        ((RootFragment) fragment1).setNavigationClickListener(mNavigationToolbarButtonOnClickListener);
        ((RootFragment) fragment1).setMangaListInteractionListener(mMangaListInteractionListener);
        mViewPagerAdapter.addFragment(fragment1);

        Fragment fragment2 = RootFragment.newInstance(Constants.VIEW_PAGER_INDEX_FAVORITES);
        ((RootFragment) fragment1).setNavigationClickListener(mNavigationToolbarButtonOnClickListener);
        ((RootFragment) fragment2).setMangaListInteractionListener(mMangaListInteractionListener);
        mViewPagerAdapter.addFragment(fragment2);

        Fragment fragment3 = RootFragment.newInstance(Constants.VIEW_PAGER_INDEX_RECENT);
        ((RootFragment) fragment1).setNavigationClickListener(mNavigationToolbarButtonOnClickListener);
        ((RootFragment) fragment3).setMangaListInteractionListener(mMangaListInteractionListener);
        mViewPagerAdapter.addFragment(fragment3);

        Fragment fragment4 = RootFragment.newInstance(Constants.VIEW_PAGER_INDEX_DOWNLOADS);
        ((RootFragment) fragment1).setNavigationClickListener(mNavigationToolbarButtonOnClickListener);
        ((RootFragment) fragment4).setMangaListInteractionListener(mMangaListInteractionListener);
        mViewPagerAdapter.addFragment(fragment4);

        viewPager.setAdapter(mViewPagerAdapter);
    }
}
