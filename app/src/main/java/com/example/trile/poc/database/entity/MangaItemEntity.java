package com.example.trile.poc.database.entity;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import com.example.trile.poc.database.model.MangaItem;

@Entity
public class MangaItemEntity implements MangaItem {

    @PrimaryKey(autoGenerate = true)
    private int Id;

    private String Title;
    private String Author;
    private boolean IsFavorited = false;
    private String RecentEpisode = "";
    private Long ReadStartTime = -1L;
    private Long ReadElapsedTime = -1L;
    private int NumChapterDownloaded = 0;

    public MangaItemEntity() {
    }

    public MangaItemEntity(String title, String author, boolean isFavorited, String recentEpisode, Long readStartTime, Long readElapsedTime, int numChapterDownloaded) {
        this.Title = title;
        this.Author = author;
        this.IsFavorited = isFavorited;
        this.RecentEpisode = recentEpisode;
        this.ReadStartTime = readStartTime;
        this.ReadElapsedTime = readElapsedTime;
        this.NumChapterDownloaded = numChapterDownloaded;
    }

    public MangaItemEntity(MangaItem mangaItem) {
        this.Id = mangaItem.getId();
        this.Title = mangaItem.getTitle();
        this.Author = mangaItem.getAuthor();
        this.IsFavorited = mangaItem.getFavorited();
        this.RecentEpisode = mangaItem.getRecentEpisode();
        this.ReadStartTime = mangaItem.getReadStartTime();
        this.ReadElapsedTime = mangaItem.getReadElapsedTime();
        this.NumChapterDownloaded = mangaItem.getNumChapterDownloaded();
    }

    @Override
    public int getId() {
        return Id;
    }

    @Override
    public String getTitle() {
        return Title;
    }

    @Override
    public String getAuthor() {
        return Author;
    }

    @Override
    public boolean getFavorited() {
        return IsFavorited;
    }

    @Override
    public String getRecentEpisode() {
        return RecentEpisode;
    }

    @Override
    public Long getReadStartTime() {
        return ReadStartTime;
    }

    @Override
    public Long getReadElapsedTime() {
        return ReadElapsedTime;
    }

    @Override
    public int getNumChapterDownloaded() {
        return NumChapterDownloaded;
    }

    public void setId(int id) {
        this.Id = id;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public void setAuthor(String author) {
        Author = author;
    }

    public void setFavorited(boolean favorited) {
        IsFavorited = favorited;
    }

    public void setRecentEpisode(String recentEpisode) {
        RecentEpisode = recentEpisode;
    }

    public void setReadStartTime(Long readStartTime) {
        ReadStartTime = readStartTime;
    }

    public void setReadElapsedTime(Long readElapsedTime) {
        ReadElapsedTime = readElapsedTime;
    }

    public void setNumChapterDownloaded(int numChapterDownloaded) {
        NumChapterDownloaded = numChapterDownloaded;
    }
}
