package com.example.trile.poc.database.model;

import android.content.Context;

import com.example.trile.poc.database.entity.MangaItemEntity;
import com.example.trile.poc.repository.DataRepository;
import com.example.trile.poc.repository.paging.discover.all.DiscoverAllBoundaryCallback;

import java.util.HashMap;
import java.util.List;

import io.reactivex.disposables.CompositeDisposable;

/**
 * Use for posting the results of 2 API Requests which are Zipped and Sent together in
 * {@link DiscoverAllBoundaryCallback}. The results will be Observed in
 * {@link DataRepository#getAllMangaItems(Context, String, CompositeDisposable)}
 *
 * @author trile
 * @since 6/12/18 at 10:40
 */
public class MangaItemsAndGenres {
    private List<MangaItemEntity> MangaItems;
    private HashMap<Integer, String> Genres;

    public MangaItemsAndGenres(List<MangaItemEntity> mangaItems, HashMap<Integer, String> genres) {
        MangaItems = mangaItems;
        Genres = genres;
    }

    public List<MangaItemEntity> getMangaItems() {
        return MangaItems;
    }

    public HashMap<Integer, String> getGenres() {
        return Genres;
    }
}
