package com.example.trile.poc.ui.fragment.discover;


import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.trile.poc.R;
import com.example.trile.poc.adapter.ViewPagerAdapter;
import com.example.trile.poc.customview.CustomViewPager;
import com.example.trile.poc.databinding.FragmentDiscoverBinding;
import com.example.trile.poc.listener.OnMangaListInteractionListener;
import com.example.trile.poc.ui.fragment.discover.all.DiscoverAllFragment;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DiscoverFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DiscoverFragment extends Fragment {

    public static final String TAG = DiscoverFragment.class.getSimpleName();

    private FragmentDiscoverBinding mBinding;

    private Toolbar mToolbar;
    private View.OnClickListener mNavigationClickListener;
    private TabLayout mTabLayout;
    private CustomViewPager mViewPager;
    private ViewPagerAdapter mViewPagerAdapter;

    private OnMangaListInteractionListener mMangaListListener;

    public DiscoverFragment() {
        // Required empty public constructor
    }

    public static DiscoverFragment newInstance() {
        DiscoverFragment fragment = new DiscoverFragment();
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
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_discover, container, false);

        mToolbar = mBinding.fragmentDiscoverToolbar;
        mToolbar.setNavigationIcon(ContextCompat.getDrawable(getContext(), R.drawable.ic_menu_black_24dp));
        ((AppCompatActivity) getActivity()).setSupportActionBar(mToolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(R.string.manga_rock);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowTitleEnabled(true);
        mToolbar.setNavigationOnClickListener(mNavigationClickListener);



        mTabLayout = mBinding.fragmentDiscoverTabLayout;
        mTabLayout.addTab(mTabLayout.newTab().setText(R.string.discover_all));
        mTabLayout.addTab(mTabLayout.newTab().setText(R.string.discover_latest));
        mTabLayout.setTabTextColors(ContextCompat.getColor(getContext(), R.color.textMuted), ContextCompat.getColor(getContext(), R.color.colorSecondary));
        mTabLayout.setSelectedTabIndicatorColor(ContextCompat.getColor(getContext(), R.color.colorSecondary));
        mTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                mViewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        mViewPager = mBinding.fragmentDiscoverViewPager;
        mViewPager.setPagingEnabled(false);
        mViewPager.setOffscreenPageLimit(1);

        mViewPagerAdapter = new ViewPagerAdapter(getChildFragmentManager());

        Fragment fragment1 = DiscoverAllFragment.newInstance(3);
        ((DiscoverAllFragment) fragment1).setMangaListInteractionListener(mMangaListListener);
        mViewPagerAdapter.addFragment(fragment1);

        Fragment fragment2 = DiscoverAllFragment.newInstance(3);
        ((DiscoverAllFragment) fragment2).setMangaListInteractionListener(mMangaListListener);
        mViewPagerAdapter.addFragment(fragment2);

        mViewPager.setAdapter(mViewPagerAdapter);

        return mBinding.getRoot();
    }

    public void setNavigationOnClickListener(View.OnClickListener listener) {
        mNavigationClickListener = listener;
    }

    public void setMangaListInteractionListener(OnMangaListInteractionListener listener) {
        mMangaListListener = listener;
    }
}
