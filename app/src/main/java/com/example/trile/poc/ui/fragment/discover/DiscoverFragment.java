package com.example.trile.poc.ui.fragment.discover;


import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.util.Pair;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.trile.poc.R;
import com.example.trile.poc.databinding.FragmentDiscoverBinding;
import com.example.trile.poc.ui.adapter.ViewPagerAdapter;
import com.example.trile.poc.ui.customview.CustomViewPager;
import com.example.trile.poc.ui.fragment.discover.all.DiscoverAllFragment;
import com.example.trile.poc.ui.helper.RecyclerGridViewHelper;
import com.example.trile.poc.ui.listener.OnMangaListInteractionListener;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DiscoverFragment#newInstance} factory method to
 * create an instance of this fragment.
 *
 * @author trile
 * @since 5/22/18 at 14:13
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
        mBinding = DataBindingUtil
                .inflate(inflater, R.layout.fragment_discover, container, false);

        mToolbar = mBinding.fragmentDiscoverToolbar;
        mToolbar.setNavigationIcon(
                ContextCompat.getDrawable(getContext(), R.drawable.ic_menu_black_24dp)
        );
        mToolbar.setNavigationOnClickListener(mNavigationClickListener);
        ((AppCompatActivity) getActivity()).setSupportActionBar(mToolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowTitleEnabled(false);

        mTabLayout = mBinding.fragmentDiscoverTabLayout;
        mTabLayout.addTab(mTabLayout.newTab().setText(R.string.discover_all));
        mTabLayout.addTab(mTabLayout.newTab().setText(R.string.discover_latest));
        mTabLayout.setTabTextColors(
                ContextCompat.getColor(getContext(), R.color.textMuted),
                ContextCompat.getColor(getContext(), R.color.colorSecondary)
        );
        mTabLayout.setSelectedTabIndicatorColor(
                ContextCompat.getColor(getContext(), R.color.colorSecondary)
        );
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

        Pair<Integer, Integer> pair = RecyclerGridViewHelper.calculateFitScreenSpanCount(getContext());
        Fragment fragment1 = DiscoverAllFragment.newInstance(pair.first, pair.second);
        ((DiscoverAllFragment) fragment1).setMangaListInteractionListener(mMangaListListener);
        mViewPagerAdapter.addFragment(fragment1);

        Fragment fragment2 = DiscoverAllFragment.newInstance(pair.first, pair.second);
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
