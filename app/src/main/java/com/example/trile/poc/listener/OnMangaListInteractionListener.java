package com.example.trile.poc.listener;

import com.example.trile.poc.database.entity.MangaItemEntity;

public interface OnMangaListInteractionListener {
    void onOpenMangaInfo(MangaItemEntity mangaItem);
    void onOpenMangaChapter(MangaItemEntity mangaItem);
}
