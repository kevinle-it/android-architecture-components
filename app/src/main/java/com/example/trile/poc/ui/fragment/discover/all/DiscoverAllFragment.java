package com.example.trile.poc.ui.fragment.discover.all;


import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.example.trile.poc.R;
import com.example.trile.poc.databinding.FragmentDiscoverAllBinding;
import com.example.trile.poc.ui.adapter.MangaItemAdapter;
import com.example.trile.poc.ui.helper.InjectorUtils;
import com.example.trile.poc.ui.listener.EndlessRecyclerViewScrollListener;
import com.example.trile.poc.ui.listener.OnMangaListInteractionListener;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DiscoverAllFragment#newInstance} factory method to
 * create an instance of this fragment.
 *
 * @author trile
 * @since 5/22/18 at 14:11
 */
public class DiscoverAllFragment extends Fragment {

    private static final String ARG_COLUMN_COUNT = "column-count";
    private int mColumnCount = 1;

    private FragmentDiscoverAllBinding mBinding;

    private SwipeRefreshLayout mSwipeRefreshContainer;
    private ProgressBar mLoadingIndicator;
    private MangaItemAdapter mMangaItemAdapter;
    private RecyclerView mRecyclerView;
    private EndlessRecyclerViewScrollListener mRecyclerViewScrollListener;
    private OnMangaListInteractionListener mListener;

    private DiscoverAllViewModel mViewModel;

    public DiscoverAllFragment() {
        // Required empty public constructor
    }

    public static DiscoverAllFragment newInstance(int columnCount) {
        DiscoverAllFragment fragment = new DiscoverAllFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mBinding = DataBindingUtil
                .inflate(inflater, R.layout.fragment_discover_all, container, false);

        mSwipeRefreshContainer = mBinding.fragmentDiscoverAllSwipeRefreshContainer;
        mSwipeRefreshContainer.setColorSchemeResources(
                R.color.colorPrimary,
                R.color.colorPrimaryLight,
                R.color.colorSecondary
        );
        mSwipeRefreshContainer.setOnRefreshListener(() -> {
            mSwipeRefreshContainer.setRefreshing(false);
        });

        mLoadingIndicator = mBinding.pbLoadingIndicator;

        mRecyclerView = mBinding.fragmentDiscoverAllList;
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), mColumnCount);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(
                mRecyclerView.getContext(),
                gridLayoutManager.getOrientation()
        );

        mRecyclerView.setLayoutManager(gridLayoutManager);
//            mRecyclerView.addItemDecoration(dividerItemDecoration);
        // Retain an instance so that you can call `resetState()` for fresh searches
        mRecyclerViewScrollListener = new EndlessRecyclerViewScrollListener(gridLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                // Triggered only when new data needs to be appended to the list
                // Add whatever code is needed to append new items to the bottom of the list
//                loadMoreOlderHousingsFromApi(page);
            }
        };

        mMangaItemAdapter = new MangaItemAdapter(getContext(), mListener);

        mRecyclerView.addOnScrollListener(mRecyclerViewScrollListener);
        mRecyclerView.setAdapter(mMangaItemAdapter);
        showLoading();

        mViewModel = ViewModelProviders
                .of(this, InjectorUtils.provideDiscoverAllViewModelFactory(getContext()))
                .get(DiscoverAllViewModel.class);
        mViewModel.getAllMangaItems().observe(this, mangaItems -> {
            mMangaItemAdapter.setMangaList(mangaItems);
            showMangaItemView();
        });

        return mBinding.getRoot();
    }

    public void setMangaListInteractionListener(OnMangaListInteractionListener listener) {
        mListener = listener;
    }

    private void showMangaItemView() {
        // First, hide the loading indicator
        mLoadingIndicator.setVisibility(View.INVISIBLE);
        // Finally, make sure the weather data is visible
        mRecyclerView.setVisibility(View.VISIBLE);
    }

    private void showLoading() {
        // Then, hide the weather data
        mRecyclerView.setVisibility(View.INVISIBLE);
        // Finally, show the loading indicator
        mLoadingIndicator.setVisibility(View.VISIBLE);
    }
}
