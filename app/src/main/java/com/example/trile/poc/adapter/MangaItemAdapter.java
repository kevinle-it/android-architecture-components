package com.example.trile.poc.adapter;

import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.example.trile.poc.R;
import com.example.trile.poc.database.model.MangaItem;
import com.example.trile.poc.databinding.MangaItemBinding;
import com.example.trile.poc.listener.MangaItemClickCallback;

import java.util.List;
import java.util.Objects;

public class MangaItemAdapter extends RecyclerView.Adapter<MangaItemAdapter.MangaItemViewHolder> {

    List<? extends MangaItem> mMangaList;

    @Nullable
    private final MangaItemClickCallback mMangaItemClickCallback;

    public MangaItemAdapter(@Nullable MangaItemClickCallback mMangaItemClickCallback) {
        this.mMangaItemClickCallback = mMangaItemClickCallback;
    }

    public void setMangaList(final List<? extends MangaItem> mangaList) {
        if (mMangaList == null) {
            mMangaList = mangaList;
            notifyItemRangeInserted(0, mangaList.size());
        } else {
            DiffUtil.DiffResult result = DiffUtil.calculateDiff(new DiffUtil.Callback() {
                @Override
                public int getOldListSize() {
                    return mMangaList.size();
                }

                @Override
                public int getNewListSize() {
                    return mangaList.size();
                }

                @Override
                public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
                    return mMangaList.get(oldItemPosition).getId() ==
                            mangaList.get(newItemPosition).getId();
                }

                @Override
                public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
                    MangaItem newMangaItem = mangaList.get(newItemPosition);
                    MangaItem oldMangaItem = mMangaList.get(oldItemPosition);
                    return newMangaItem.getId() == oldMangaItem.getId()
                            && Objects.equals(newMangaItem.getTitle(), oldMangaItem.getTitle())
                            && Objects.equals(newMangaItem.getAuthor(), oldMangaItem.getAuthor());
                }
            });
            mMangaList = mangaList;
            result.dispatchUpdatesTo(this);
        }
    }

    @NonNull
    @Override
    public MangaItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        MangaItemBinding binding = DataBindingUtil
                .inflate(LayoutInflater.from(parent.getContext()), R.layout.manga_item,
                        parent, false);
        binding.setCallback(mMangaItemClickCallback);
        return new MangaItemViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MangaItemViewHolder holder, int position) {
        holder.mBinding.setManga(mMangaList.get(position));
        holder.mBinding.executePendingBindings();
    }

    @Override
    public int getItemCount() {
        return mMangaList == null ? 0 : mMangaList.size();
    }

    public class MangaItemViewHolder extends RecyclerView.ViewHolder {

        final MangaItemBinding mBinding;

        public MangaItemViewHolder(MangaItemBinding mBinding) {
            super(mBinding.getRoot());
            this.mBinding = mBinding;
        }
    }
}
