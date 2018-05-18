package com.example.trile.poc.database;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.support.annotation.VisibleForTesting;

import com.example.trile.poc.AppExecutors;
import com.example.trile.poc.database.dao.MangaDetailDAO;
import com.example.trile.poc.database.dao.MangaItemDAO;
import com.example.trile.poc.database.entity.MangaDetailEntity;
import com.example.trile.poc.database.entity.MangaItemEntity;

import java.util.List;

@Database(entities = {MangaItemEntity.class, MangaDetailEntity.class}, version = 1)
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
                    sInstance = Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, DATABASE_NAME).build();
                    sInstance.setExecutors(executors);
                    sInstance.updateDatabaseCreated(context.getApplicationContext());
                }
            }
        }
        return sInstance;
    }

//    /**
//     * Build the database. {@link Builder#build()} only sets up the database configuration and
//     * creates a new instance of the database.
//     * The SQLite database is only created when it's accessed for the first time.
//     */
//    private static AppDatabase buildDatabase(final Context appContext,
//                                             final AppExecutors executors) {
//        return Room.databaseBuilder(appContext, AppDatabase.class, DATABASE_NAME)
//                .addCallback(new Callback() {
//                    @Override
//                    public void onCreate(@NonNull SupportSQLiteDatabase db) {
//                        super.onCreate(db);
//                        executors.diskIO().execute(() -> {
//                            // Add a delay to simulate a long-running operation
//                            addDelay();
//                            // Generate the data for pre-population
//                            AppDatabase database = AppDatabase.getInstance(appContext, executors);
//                            List<MangaItemEntity> mangaItemEntities = DataGenerator.generateMangaItems();
//                            List<MangaDetailEntity> mangaDetailEntities = DataGenerator.generateMangaDetails();
//
//                            insertData(database, mangaItemEntities, mangaDetailEntities);
//                            // notify that the database was created and it's ready to be used
//                            database.setDatabaseCreated();
//                        });
//                    }
//                }).build();
//    }

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

    private static void insertData(final AppDatabase database, final List<MangaItemEntity> mangaItemEntities,
                                   final List<MangaDetailEntity> mangaDetailEntities) {
        database.runInTransaction(() -> {
            database.mangaItemDAO().insertAll(mangaItemEntities);
            database.mangaDetailDAO().insertAll(mangaDetailEntities);
        });
    }

//    private static void addDelay() {
//        try {
//            Thread.sleep(4000);
//        } catch (InterruptedException ignored) {
//        }
//    }

    public LiveData<Boolean> getDatabaseCreated() {
        return mIsDatabaseCreated;
    }
}
