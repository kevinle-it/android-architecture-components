package com.example.trile.poc.api.service;

import com.example.trile.poc.api.service.genre.GenreClient;
import com.example.trile.poc.api.service.manga.MangaClient;
import com.example.trile.poc.database.entity.MangaItemEntity;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

/**
 * Provides an API for handling all network operations.
 *
 * @author trile
 * @since 5/22/18 at 11:36
 */
public class MangaNetworkDataSource {

    private static MangaNetworkDataSource sInstance;

    public MangaNetworkDataSource() {
    }

    /**
     * Get the singleton for this class
     */
    public static MangaNetworkDataSource getInstance() {
        if (sInstance == null) {
            synchronized (MangaNetworkDataSource.class) {
                if (sInstance == null) {
                    sInstance = new MangaNetworkDataSource();
                }
            }
        }
        return sInstance;
    }

    public List<MangaItemEntity> startFetchAllMangaItems() throws IOException {
        return MangaClient.getAllMangaItems();
    }

    public HashMap<Integer, String> startFetchAllGenres() throws IOException {
        return GenreClient.getAllGenres();
    }
}
