package com.example.trile.poc;

import android.support.design.widget.BottomNavigationView;
import android.support.v4.view.ViewPager;

/**
 * Manga Rock's Constants.
 *
 * @author trile
 * @since 5/22/18 at 14:21
 */
public class Constants {
    // Manga Rock Server's Base URL.
    public static final String MANGA_ROCK_SERVER_BASE_URL =
            "http://apidev.mangarockhd.com/query/debug400/";
    public static final int GET_ALL_MANGAS_MSID_CODE = 71;
    public static final String GET_ALL_MANGAS_COUNTRY_CODE = "Vietnam";
    public static final String MANGA_ROCK_THUMBNAIL_BASE_URL =
            "https://mrthumb.nabstudio.com/t/%1$d/%2$d.jpg";

    /**
     * Constants related to {@link ViewPager} and {@link BottomNavigationView}.
     */
    public static final int VIEW_PAGER_OFF_SCREEN_PAGE_LIMIT = 3;
    public static final int VIEW_PAGER_INDEX_DISCOVER = 0;
    public static final int VIEW_PAGER_INDEX_FAVORITES = 1;
    public static final int VIEW_PAGER_INDEX_RECENT = 2;
    public static final int VIEW_PAGER_INDEX_DOWNLOADS = 3;
    public static final int NAVIGATION_INDEX_DISCOVER = R.id.bottom_navigation_discover;
    public static final int NAVIGATION_INDEX_FAVORITES = R.id.bottom_navigation_favorites;
    public static final int NAVIGATION_INDEX_RECENT = R.id.bottom_navigation_recent;
    public static final int NAVIGATION_INDEX_DOWNLOADS = R.id.bottom_navigation_downloads;
    public static final String ROOT_FRAGMENT_VIEW_PAGER_INDEX_PARAM =
            "ROOT_FRAGMENT_VIEW_PAGER_INDEX_PARAM";
}
