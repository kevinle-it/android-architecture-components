package com.example.trile.poc.ui.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.trile.poc.R;
import com.example.trile.poc.database.model.MangaItem;
import com.example.trile.poc.databinding.MangaItemBinding;
import com.example.trile.poc.ui.listener.OnMangaListInteractionListener;

import java.util.List;
import java.util.Objects;

/**
 * Adapter for Manga Item showing on Discover, Favorites & Downloads Tabs.
 *
 * @author trile
 * @since 5/22/18 at 13:59
 */
public class MangaItemAdapter extends RecyclerView.Adapter<MangaItemAdapter.MangaItemViewHolder> {

    private final Context mContext;
    List<? extends MangaItem> mMangaList;
    private final int mMangaItemImageHeight;

    @Nullable
    private final OnMangaListInteractionListener mListener;

    public MangaItemAdapter(final Context context,
                            @Nullable OnMangaListInteractionListener listener,
                            final int mangaItemImageHeight) {
        mContext = context;
        mListener = listener;
        mMangaItemImageHeight = mangaItemImageHeight;
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
        return new MangaItemViewHolder(binding, mMangaItemImageHeight);
    }

    @Override
    public void onBindViewHolder(@NonNull MangaItemViewHolder holder, int position) {
        Glide.with(mContext)
                .load(R.drawable.dragonball_poster)
                .apply(new RequestOptions().fitCenter())
                .into(holder.mBinding.mangaImage);
        holder.mBinding.setManga(mMangaList.get(position));
        holder.mBinding.getRoot().setOnClickListener(v -> {
            if (mListener != null) {
                mListener.onOpenMangaInfo(mMangaList.get(position));
            }
        });
        holder.mBinding.executePendingBindings();
    }

    @Override
    public int getItemCount() {
        return mMangaList == null ? 0 : mMangaList.size();
    }

    /**
     * avoid using non-static inner class
     * watch here: https://www.youtube.com/watch?v=_CruQY55HOk
     */
    public static class MangaItemViewHolder extends RecyclerView.ViewHolder {

        final MangaItemBinding mBinding;

        public MangaItemViewHolder(MangaItemBinding mBinding, final int mangaItemImageHeight) {
            super(mBinding.getRoot());
            this.mBinding = mBinding;

            mBinding.mangaImage.getLayoutParams().height = mangaItemImageHeight;
        }
    }
}
