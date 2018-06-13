package com.example.trile.poc.database.model;

import java.util.ArrayList;

/**
 * Only expose getters for others.
 *
 * @author trile
 * @since 5/22/18 at 14:08
 */
public interface MangaItem {
    int getId();
    String getName();
    String getAuthor();
    int getRank();
    ArrayList<Integer> getGenres();
//    boolean getFavorited();
//    String getRecentEpisode();
//    Long getReadStartTime();
//    Long getReadElapsedTime();
//    int getNumChapterDownloaded();
}
