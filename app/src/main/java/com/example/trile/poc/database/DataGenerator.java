/*
 * Copyright 2017, The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.trile.poc.database;

import com.example.trile.poc.database.entity.MangaDetailEntity;
import com.example.trile.poc.database.entity.MangaItemEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * Generates data to pre-populate the database
 */
public class DataGenerator {

    private static final String[] TITLE = new String[]{
            "One Piece", "Fairy Tail", "One Punch-Man", "Bleach", "Attack On Titan",
            "Naruto", "Shokugeki no Souma", "The Seven Deadly Sins", "The Gamer", "Horimiya",
            "My Hero Academia", "The Rising of the Shield", "Tokyo Ghoul: Re", "Nisekoi", "Monster Musume: Everyday Life With Monster Girls",
            "Akame ga KILL!", "Is it Wrong to Try to Pick Up Girls in a Dungeon?", "Noragami: Stray God", "Berserk", "Naruto: The Seventh Hokage and the Scarlet Spring",
            "Prison School", "Hunter x Hunter", "Last Game", "Yamada-kun and the Seven Witches", "Tokyo Ghoul",
            "Ao Haru Ride", "Battle Through The Heavens", "Douluo Dalu I", "Noblesse", "Wolf Girl & Black Prince",
    };
    private static final String[] AUTHOR = new String[]{
            "Eiichiro Oda", "Hiro Mashima", "ONE", "Tite Kubo", "Hajime Isayama"};
    private static final String[] DESCRIPTION = new String[]{
            "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. " +
                    "Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. " +
                    "Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. " +
                    "Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum."};

    public static List<MangaItemEntity> generateMangaItems() {
        List<MangaItemEntity> mangaItems = new ArrayList<>(TITLE.length * AUTHOR.length);
        for (int i = 0; i < TITLE.length; i++) {
            for (int j = 0; j < AUTHOR.length; j++) {
                MangaItemEntity mangaItem = new MangaItemEntity();
                mangaItem.setTitle(TITLE[i]);
                mangaItem.setAuthor(AUTHOR[j]);
                mangaItems.add(mangaItem);
            }
        }
        return mangaItems;
    }

    public static List<MangaDetailEntity> generateMangaDetails() {
        List<MangaDetailEntity> mangaDetails = new ArrayList<>(TITLE.length);
        for (int i = 0; i < TITLE.length; i++) {
            MangaDetailEntity mangaItem = new MangaDetailEntity();
            mangaItem.setDescription(DESCRIPTION[0]);
            mangaDetails.add(mangaItem);
        }
        return mangaDetails;
    }
}
