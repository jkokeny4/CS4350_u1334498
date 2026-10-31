package com.example.assignment4.data;

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

@Database(entities = {FunFact.class}, version = 2, exportSchema = false)
public abstract class FunFactsDatabase extends RoomDatabase {
    public abstract FunFactDao funFactDao();

    private static volatile FunFactsDatabase INSTANCE;

    // Keep saved facts when upgrading from the original Java entity.
    private static final Migration MIGRATION_1_2 = new Migration(1, 2) {
        @Override
        public void migrate(SupportSQLiteDatabase db) {
            db.execSQL("CREATE TABLE fun_facts_new (id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, "
                    + "text TEXT NOT NULL, fetchedAt INTEGER NOT NULL)");
            db.execSQL("INSERT INTO fun_facts_new (id, text, fetchedAt) "
                    + "SELECT id, COALESCE(text, ''), fetchedAt FROM fun_facts");
            db.execSQL("DROP TABLE fun_facts");
            db.execSQL("ALTER TABLE fun_facts_new RENAME TO fun_facts");
        }
    };

    public static FunFactsDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (FunFactsDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(
                            context.getApplicationContext(), FunFactsDatabase.class, "fun_facts.db"
                    ).addMigrations(MIGRATION_1_2).build();
                }
            }
        }
        return INSTANCE;
    }
}
