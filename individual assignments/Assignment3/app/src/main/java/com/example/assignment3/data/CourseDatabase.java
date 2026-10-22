package com.example.assignment3.data;

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {CourseEntity.class}, version = 1, exportSchema = false)
public abstract class CourseDatabase extends RoomDatabase {
    public abstract CourseDao courseDao();

    private static volatile CourseDatabase INSTANCE;

    public static CourseDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (CourseDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(
                            context.getApplicationContext(),
                            CourseDatabase.class,
                            "course_manager.db"
                    ).build();
                }
            }
        }
        return INSTANCE;
    }
}
