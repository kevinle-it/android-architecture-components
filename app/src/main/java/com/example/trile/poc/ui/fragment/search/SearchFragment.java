package com.example.trile.poc.ui.fragment.search;


import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.trile.poc.R;
import com.example.trile.poc.database.AppDatabase;
import com.example.trile.poc.database.model.MangaItem;
import com.example.trile.poc.databinding.FragmentSearchBinding;
import com.example.trile.poc.ui.customview.CustomEditText;
import com.example.trile.poc.ui.helper.KeyboardHelper;

import androidx.navigation.Navigation;

import static com.example.trile.poc.ui.adapter.MangaItemAdapter.TAG;

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

        mSearchField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // TODO: 6/5/18 Implement showing matching manga items in Database on User typing.
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        mSetCustomFilterButton = mBinding.setCustomFilterButton;
        mSetCustomFilterButton.setOnClickListener(v -> {
            // TODO: 6/5/18 Show a dialog with ChipGroup of Chips to select Genres for Filtering.
        });

        return mBinding.getRoot();
    }

}
