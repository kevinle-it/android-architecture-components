package com.example.trile.poc.database.model;

public interface MangaItem {
    int getId();
    String getTitle();
    String getAuthor();
    boolean getFavorited();
    String getRecentEpisode();
    Long getReadStartTime();
    Long getReadElapsedTime();
    int getNumChapterDownloaded();
}
