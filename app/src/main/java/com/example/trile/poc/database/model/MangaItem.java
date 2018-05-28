package com.example.trile.poc.database.model;

/**
 * Only expose getters for others.
 *
 * @author trile
 * @since 5/22/18 at 14:08
 */
public interface MangaItem {
    int getId();
    String getTitle();
    String getAuthor();
    int getRank();
//    boolean getFavorited();
//    String getRecentEpisode();
//    Long getReadStartTime();
//    Long getReadElapsedTime();
//    int getNumChapterDownloaded();
}
