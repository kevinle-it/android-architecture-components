package com.example.trile.poc.database;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.migration.Migration;
import android.content.Context;
import android.support.annotation.VisibleForTesting;

import com.example.trile.poc.AppExecutors;
import com.example.trile.poc.database.dao.MangaDetailDAO;
import com.example.trile.poc.database.dao.MangaItemDAO;
import com.example.trile.poc.database.entity.MangaDetailEntity;
import com.example.trile.poc.database.entity.MangaItemEntity;

import java.util.List;

/**
 * Provides an API for handling all local database operations.
 *
 * @author trile
 * @since 5/22/18 at 11:47
 */
@Database(entities = {MangaItemEntity.class, MangaDetailEntity.class}, version = 2)
public abstract class AppDatabase extends RoomDatabase {

    private static AppDatabase sInstance;

    @VisibleForTesting
    public static final String DATABASE_NAME = "manga-item-db";

    public abstract MangaItemDAO mangaItemDAO();

    public abstract MangaDetailDAO mangaDetailDAO();

    private AppExecutors mExecutors;

    private final MutableLiveData<Boolean> mIsDatabaseCreated = new MutableLiveData<>();

    /**
     * Get the singleton for this class
     */
    public static AppDatabase getInstance(final Context context, final AppExecutors executors) {
        if (sInstance == null) {
            synchronized (AppDatabase.class) {
                if (sInstance == null) {
//                    sInstance = buildDatabase(context.getApplicationContext(), executors);
                    /**
                     * Build the database. {@link Builder#build()} only sets up the
                     * database configuration and creates a new instance of the database.
                     * The SQLite database is only created when it's accessed for the first time.
                     */
                    sInstance = Room.databaseBuilder
                            (
                                    context.getApplicationContext(),
                                    AppDatabase.class,
                                    DATABASE_NAME
                            )
                            .addMigrations(MIGRATION_1_2)
                            .build();
                    sInstance.setExecutors(executors);
                    sInstance.updateDatabaseCreated(context.getApplicationContext());
                }
            }
        }
        return sInstance;
    }

    /**
     * Check whether the database already exists and expose it via {@link #getDatabaseCreated()}
     */
    private void updateDatabaseCreated(final Context context) {
        if (context.getDatabasePath(DATABASE_NAME).exists()) {
            setDatabaseCreated();
        }
    }

    public void setExecutors(AppExecutors mExecutors) {
        this.mExecutors = mExecutors;
    }

    private void setDatabaseCreated(){
        mIsDatabaseCreated.postValue(true);
    }

    private static void insertData(final AppDatabase database,
                                   final List<MangaItemEntity> mangaItemEntities,
                                   final List<MangaDetailEntity> mangaDetailEntities) {
        database.runInTransaction(() -> {
            database.mangaItemDAO().insertAll(mangaItemEntities);
            database.mangaDetailDAO().insertAll(mangaDetailEntities);
        });
    }

    /**
     * Expose this for others to get notified immediately when the database has just been created.
     * Others access this Live Data can only observe for the changes and cannot change the data and
     * can neither {@link MutableLiveData#setValue(Object)}
     * nor {@link MutableLiveData#postValue(Object)}.
     *
     * @return Live Data for others to observe the changes.
     */
    public LiveData<Boolean> getDatabaseCreated() {
        return mIsDatabaseCreated;
    }

    public static final Migration MIGRATION_1_2 = new Migration(1, 2) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            database.beginTransaction();
            database.execSQL("ALTER TABLE MangaItemEntity RENAME TO MangaItemEntity_OLD");
            database.execSQL("CREATE TABLE MangaItemEntity" +
                    "(" +
                        "Id INTEGER NOT NULL," +
                        "Name TEXT," +
                        "Author TEXT," +
                        "Rank INTEGER NOT NULL DEFAULT 0," +
                        "PRIMARY KEY(Id)" +
                    ")"
            );
            database.execSQL("INSERT INTO MangaItemEntity(Id, Name, Author) " +
                    "SELECT Id, Title, Author FROM MangaItemEntity_OLD");
            database.execSQL("DROP TABLE MangaItemEntity_OLD");
            database.setTransactionSuccessful();
            database.endTransaction();
        }
    };
}
