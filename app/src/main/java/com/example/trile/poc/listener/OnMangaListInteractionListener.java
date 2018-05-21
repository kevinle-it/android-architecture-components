package com.example.trile.poc.listener;

import com.example.trile.poc.database.model.MangaItem;

public interface OnMangaListInteractionListener {
    void onOpenMangaInfo(MangaItem mangaItem);
    void onOpenMangaChapter(MangaItem mangaItem);
}
