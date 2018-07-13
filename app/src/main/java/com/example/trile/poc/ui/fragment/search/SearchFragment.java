package com.example.trile.poc.ui.fragment.search;


import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.util.Pair;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;

import com.example.trile.poc.R;
import com.example.trile.poc.database.AppDatabase;
import com.example.trile.poc.database.model.Genre;
import com.example.trile.poc.database.model.MangaItem;
import com.example.trile.poc.databinding.FragmentSearchBinding;
import com.example.trile.poc.ui.adapter.MangaItemAdapter;
import com.example.trile.poc.ui.customview.CustomEditText;
import com.example.trile.poc.ui.customview.chip.Chip;
import com.example.trile.poc.ui.customview.chip.ChipGroup;
import com.example.trile.poc.ui.helper.CustomFastScroller;
import com.example.trile.poc.ui.helper.KeyboardHelper;
import com.example.trile.poc.ui.helper.RecyclerGridViewHelper;
import com.example.trile.poc.ui.helper.RecyclerViewSpacesItemDecoration;
import com.example.trile.poc.ui.listener.EndlessRecyclerViewScrollListener;
import com.example.trile.poc.ui.listener.OnMangaListInteractionListener;
import com.example.trile.poc.utils.InjectorUtils;
import com.example.trile.poc.utils.Objects;

import java.util.ArrayList;
import java.util.List;

import androidx.navigation.Navigation;

/**
 * For Seaching {@link MangaItem} in {@link AppDatabase}.
 * @author trile
 * @since 6/5/18 at 13:14
 */
public class SearchFragment extends Fragment {

    private FragmentSearchBinding mBinding;

    private Toolbar mToolbar;
    private CustomEditText mSearchField;
    private TextView mSetCustomFilterButton;

    private Handler mHandler;
    private Runnable mSearchByNameQueryRunnable;

    private MangaItemAdapter mMangaItemAdapter;
    private RecyclerView mRecyclerView;
    private EndlessRecyclerViewScrollListener mRecyclerViewScrollListener;
    private OnMangaListInteractionListener mListener;

    private View mFilterDialogLayout;
    private List<? extends Genre> mMangaGenres;

    private SearchViewModel mViewModel;

    public SearchFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mBinding = DataBindingUtil
                .inflate(inflater, R.layout.fragment_search, container, false);

        mToolbar = mBinding.fragmentSearchToolbar;
        ((AppCompatActivity) getActivity()).setSupportActionBar(mToolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        // Setting this Navigation OnClickListener is Optional due to internal handling
        // by setDisplayHomeAsUpEnabled(true) by default.
        mToolbar.setNavigationOnClickListener(view -> {
            // Must hide keyboard before navigateUp(), otherwise getActivity().getCurrentFocus()
            // in hideSoftkeyBoard() will return NULL because this fragment is popped off the
            // backstack, so no focused view exists.
            KeyboardHelper.hideSoftKeyboard(getActivity(), true);
            Navigation.findNavController(view).navigateUp();
        });

        mSearchField = mBinding.searchField;
        KeyboardHelper.showSoftKeyboard(getActivity(), mSearchField);

        mHandler = new Handler();
        mSearchField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (Objects.nonNull(mSearchByNameQueryRunnable)) {
                    mHandler.removeCallbacks(mSearchByNameQueryRunnable);
                }
                mSearchByNameQueryRunnable = () -> mViewModel.filterMangaByName(
                        s.toString(),
                        getString(R.string.discover_all_tab_sort_by_rank)
                );
                mHandler.postDelayed(mSearchByNameQueryRunnable, 1000);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        mSearchField.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                KeyboardHelper.hideSoftKeyboard(getActivity(), true);
                return true;
            }
            return false;
        });

        mRecyclerView = mBinding.searchResult;

        Pair<Integer, Integer> pair = RecyclerGridViewHelper.calculateFitScreenSpanCount(getContext());

        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), pair.first);
        mRecyclerView.setLayoutManager(gridLayoutManager);

        mRecyclerView.addItemDecoration(new RecyclerViewSpacesItemDecoration(getResources()
                .getDimensionPixelSize(R.dimen.recycler_grid_view_item_spacing)));

        mRecyclerViewScrollListener = new EndlessRecyclerViewScrollListener(gridLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                // Triggered only when new data needs to be appended to the list
                // Add whatever code is needed to append new items to the bottom of the list
//                loadMoreOlderHousingsFromApi(page);
            }
        };
        StateListDrawable verticalThumbDrawable = (StateListDrawable) ContextCompat.getDrawable
                (getContext(), R.drawable.thumb_drawable);
        Drawable verticalTrackDrawable = ContextCompat.getDrawable(getContext(), R.drawable
                .line_drawable);
        StateListDrawable horizontalThumbDrawable = (StateListDrawable) ContextCompat.getDrawable
                (getContext(), R.drawable.thumb_drawable);
        Drawable horizontalTrackDrawable = ContextCompat.getDrawable(getContext(), R.drawable
                .line_drawable);
        int defaultWidth = getResources().getDimensionPixelSize(android.support.v7.recyclerview.R.dimen
                .fastscroll_default_thickness);
        int scrollbarMinimumRange = getResources().getDimensionPixelSize(android.support.v7.recyclerview.R.dimen
                .fastscroll_minimum_range);
        int margin = getResources().getDimensionPixelOffset(android.support.v7.recyclerview.R.dimen
                .fastscroll_margin);
        new CustomFastScroller(getContext(), mRecyclerView, verticalThumbDrawable,
                verticalTrackDrawable, horizontalThumbDrawable, horizontalTrackDrawable,
                defaultWidth, scrollbarMinimumRange, margin);

        mMangaItemAdapter = new MangaItemAdapter(getContext(), mListener, pair.second);

        mRecyclerView.addOnScrollListener(mRecyclerViewScrollListener);
        mRecyclerView.setAdapter(mMangaItemAdapter);

        mViewModel = ViewModelProviders
                .of(
                        this,
                        InjectorUtils.provideSearchViewModelFactory(getActivity().getApplication())
                )
                .get(SearchViewModel.class);
        mViewModel.getMangaGenres().observe(
                this,
                mangaGenres -> mMangaGenres = mangaGenres
        );
        mViewModel.getMangaResults().observe(
                this,
                mangaItems -> {
                    mMangaItemAdapter.submitList(mangaItems);
                }
        );

        mSetCustomFilterButton = mBinding.setCustomFilterButton;
        mSetCustomFilterButton.setOnClickListener(v -> {

            mFilterDialogLayout = LayoutInflater
                    .from(getContext())
                    .inflate(R.layout.filter_dialog, null);

            ChipGroup chipGroup = mFilterDialogLayout.findViewById(R.id.chip_group);
            if (Objects.nonNull(chipGroup) && Objects.nonNull(mMangaGenres)) {
                for (Genre genre : mMangaGenres) {
                    Chip chip = new Chip(getContext());
                    chip.setId(genre.getId());
                    chip.setText(genre.getGenre());
                    chipGroup.addView(chip);
                }

                new AlertDialog.Builder(getContext())
                        .setTitle(R.string.search_fragment_filter_dialog_title)
                        .setView(mFilterDialogLayout)
                        .setNegativeButton(R.string.search_fragment_filter_dialog_negative,
                                (dialog, which) -> dialog.dismiss()
                        )
                        .setPositiveButton(R.string.search_fragment_filter_dialog_positive,
                                (dialog, which) -> {
                                    List<Integer> includeGenres = new ArrayList<>();
                                    List<Integer> excludeGenres = new ArrayList<>();
                                    for (Chip chip : chipGroup.getGenres()) {
                                        if (chip.getState() == Chip.State.STATE_INCLUDE) {
                                            includeGenres.add(chip.getId());
                                        } else if (chip.getState() == Chip.State.STATE_EXCLUDE) {
                                            excludeGenres.add(chip.getId());
                                        }
                                    }
                                    mViewModel.filterMangaByGenre(
                                            includeGenres,
                                            excludeGenres,
                                            getString(R.string.discover_all_tab_sort_by_rank)
                                    );
                                })
                        .show();
            }
        });

        return mBinding.getRoot();
    }

}
