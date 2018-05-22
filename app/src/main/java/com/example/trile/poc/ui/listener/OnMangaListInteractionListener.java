package com.example.trile.poc.ui.listener;

import com.example.trile.poc.database.model.MangaItem;

/**
 * @author trile
 * @since 5/22/18 at 14:22
 */
public interface OnMangaListInteractionListener {
    void onOpenMangaInfo(MangaItem mangaItem);
    void onOpenMangaChapter(MangaItem mangaItem);
}
