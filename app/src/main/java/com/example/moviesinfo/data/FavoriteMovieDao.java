package com.example.moviesinfo.data;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface FavoriteMovieDao {

    @Query("SELECT * FROM favorite_movies")
    LiveData<List<FavoriteMovie>> getAllFavoriteMovies();

    @Query("SELECT * FROM favorite_movies WHERE id == :id")
    FavoriteMovie getFavoriteMovieById(int id);

    @Insert
    void insertFavoriteMovie(FavoriteMovie favoriteMovie);

    @Delete
    void deleteFavoriteMovie(FavoriteMovie favoriteMovie);

    @Query("DELETE FROM favorite_movies")
    void deleteAllFavoriteMovies();
}
