package com.example.moviesinfo.data;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {FavoriteMovie.class}, version = 2, exportSchema = false)
public abstract class FavoriteMoviesDatabase extends RoomDatabase {
    private static FavoriteMoviesDatabase database;
    private static final String DB_NAME = "favorite_movies.db";
    private static final Object LOCK = new Object();

    public static FavoriteMoviesDatabase getInstance(Context context) {
        synchronized (LOCK) {
            if (database == null) {
                database = Room.databaseBuilder(context, FavoriteMoviesDatabase.class, DB_NAME).fallbackToDestructiveMigration().build();
            }
        }
        return database;
    }

    public abstract FavoriteMovieDao favoriteMovieDao();
}
