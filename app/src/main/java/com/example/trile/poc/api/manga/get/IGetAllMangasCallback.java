package com.example.trile.poc.api.manga.get;

import com.example.trile.poc.database.entity.MangaItemEntity;

import java.util.List;

public interface IGetAllMangasCallback {
    void onGetComplete(List<MangaItemEntity> mangaItems);
    void onGetFailure(Throwable t);
}
