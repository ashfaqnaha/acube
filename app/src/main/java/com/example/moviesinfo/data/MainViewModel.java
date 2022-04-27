package com.example.moviesinfo.data;

import android.app.Application;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class MainViewModel extends AndroidViewModel {
    private static FavoriteMoviesDatabase database;
    private LiveData<List<FavoriteMovie>> favoriteMovies;

    public MainViewModel(@NonNull Application application) {
        super(application);

        database = FavoriteMoviesDatabase.getInstance(getApplication());
        favoriteMovies = database.favoriteMovieDao().getAllFavoriteMovies();
    }

    //methods
    public LiveData<List<FavoriteMovie>> getFavoriteMovies() {
        return favoriteMovies;
    }

    public FavoriteMovie getFavoriteMovieById(int id) {
        try {
            return new GetMovieByIdTask().execute(id).get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void insertFavoriteMovie(FavoriteMovie favoriteMovie) {
        new InsertTask().execute(favoriteMovie);
    }

    public void deleteFavoriteMovie(FavoriteMovie favoriteMovie) {
        new DeleteTask().execute(favoriteMovie);
    }

    public void deleteAllFavoriteMovies(FavoriteMovie favoriteMovie) {
        new DeleteAllTask().execute();
    }

    //AsyncTasks
    private static class GetMovieByIdTask extends AsyncTask<Integer, Void, FavoriteMovie> {

        @Override
        protected FavoriteMovie doInBackground(Integer... integers) {
            return database.favoriteMovieDao().getFavoriteMovieById(integers[0]);
        }
    }

    private static class InsertTask extends AsyncTask<FavoriteMovie, Void, Void> {

        @Override
        protected Void doInBackground(FavoriteMovie... favoriteMovies) {
            FavoriteMovie favoriteMovie = favoriteMovies[0];
            database.favoriteMovieDao().insertFavoriteMovie(favoriteMovie);
            return null;
        }
    }

    private static class DeleteTask extends AsyncTask<FavoriteMovie, Void, Void> {

        @Override
        protected Void doInBackground(FavoriteMovie... favoriteMovies) {
            FavoriteMovie favoriteMovie = favoriteMovies[0];
            database.favoriteMovieDao().deleteFavoriteMovie(favoriteMovie);
            return null;
        }
    }

    private static class DeleteAllTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            database.favoriteMovieDao().deleteAllFavoriteMovies();
            return null;
        }
    }
}
