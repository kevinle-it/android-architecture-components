package com.example.trile.poc;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.db.framework.FrameworkSQLiteOpenHelperFactory;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.testing.MigrationTestHelper;
import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.example.trile.poc.database.AppDatabase;
import com.example.trile.poc.database.dao.MangaItemDAO;
import com.example.trile.poc.database.entity.MangaItemEntity;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;
import static org.junit.Assert.*;

/**
 * @author trile
 * @since 5/30/18 at 17:03
 */
@RunWith(AndroidJUnit4.class)
public class RoomMigrationInstrumentedTest {
    @Rule
    public MigrationTestHelper mMigrationTestHelper = new MigrationTestHelper(
            InstrumentationRegistry.getInstrumentation(),
            AppDatabase.class.getCanonicalName(),
            new FrameworkSQLiteOpenHelperFactory()
    );

//    private MangaItemDAO mMangaItemDAO;
//    private AppDatabase mDatabase;

    private static final MangaItemEntity MANGA_ITEM_ENTITY = new MangaItemEntity(
            1, "Doraemon",
            "Fujiko F. Fujio",1
    );

//    @Before
//    public void createDb() {
//        Context context = InstrumentationRegistry.getTargetContext();
//        mDatabase = Room.inMemoryDatabaseBuilder(context, AppDatabase.class).build();
//        mMangaItemDAO = mDatabase.mangaItemDAO();
//    }
//
//    @After
//    public void closeDb() throws IOException {
//        mDatabase.close();
//    }

    @Test
    public void migrationFrom1To2_containsCorrectData() throws IOException {
        // Create the database in version 1
        SupportSQLiteDatabase db =
                mMigrationTestHelper.createDatabase(AppDatabase.DATABASE_NAME, 1);
        // Insert some data
        insertMangaItem(
                MANGA_ITEM_ENTITY.getId(),
                MANGA_ITEM_ENTITY.getName(),
                MANGA_ITEM_ENTITY.getAuthor(),
                db
        );
        //Prepare for the next version
        db.close();

        // Re-open the database with version 2 and provide MIGRATION_1_2 as the migration process.
        mMigrationTestHelper.runMigrationsAndValidate(AppDatabase.DATABASE_NAME, 2,
                true, AppDatabase.MIGRATION_1_2);

        // MigrationTestHelper automatically verifies the schema changes, but not the data validity.

        // Validate that the data was migrated properly.
        MangaItemEntity mangaItem = getMigratedRoomDatabase()
                .mangaItemDAO()
                .loadMangaItem(1);
        assertEquals(mangaItem.getId(), MANGA_ITEM_ENTITY.getId());
        assertEquals(mangaItem.getName(), MANGA_ITEM_ENTITY.getName());
        assertEquals(mangaItem.getAuthor(), MANGA_ITEM_ENTITY.getAuthor());
        // The Rank was missing in version 1, so it should be 0 (by default) in version 2.
        assertEquals(mangaItem.getRank(), 0);
    }

    private AppDatabase getMigratedRoomDatabase() {
        AppDatabase database = Room.databaseBuilder
                (
                        InstrumentationRegistry.getTargetContext(),
                        AppDatabase.class, AppDatabase.DATABASE_NAME
                )
                .addMigrations(AppDatabase.MIGRATION_1_2)
                .build();
        // close the database and release any stream resources when the test finishes
        mMigrationTestHelper.closeWhenFinished(database);
        return database;
    }

    private void insertMangaItem(int id, String title, String author, SupportSQLiteDatabase db) {
        ContentValues values = new ContentValues();
        values.put("Id", id);
        values.put("Title", title);
        values.put("Author", author);

        db.insert("MangaItemEntity", SQLiteDatabase.CONFLICT_REPLACE, values);
    }
}
