package com.example.moviesinfo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.example.moviesinfo.adapters.MovieAdapter;
import com.example.moviesinfo.data.FavoriteMovie;
import com.example.moviesinfo.data.MainViewModel;
import com.example.moviesinfo.data.Movie;

import java.util.ArrayList;
import java.util.List;

public class FavoriteMoviesActivity extends AppCompatActivity {

    private RecyclerView recyclerViewFavoriteMovies;
    private MovieAdapter adapter;
    private MainViewModel mainViewModel;

    //Menu stuff
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.itemHome:
                Intent goToHome = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(goToHome);
                break;
            case R.id.itemFavoriteMovies:
                Intent goToFavorites = new Intent(getApplicationContext(), FavoriteMoviesActivity.class);
                startActivity(goToFavorites);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite_movies);

        getSupportActionBar().setTitle(R.string.favorites_action_bar_title);

        //RecyclerView
        recyclerViewFavoriteMovies = findViewById(R.id.recyclerViewFavoriteMovies);
        recyclerViewFavoriteMovies.setLayoutManager(new GridLayoutManager(this, 2));
        adapter = new MovieAdapter();
        recyclerViewFavoriteMovies.setAdapter(adapter);

        //MainViewModel
        mainViewModel = new ViewModelProvider(this).get(MainViewModel.class);
        LiveData<List<FavoriteMovie>> favoriteMovies = mainViewModel.getFavoriteMovies();
        favoriteMovies.observe(this, new Observer<List<FavoriteMovie>>() {
            @Override
            public void onChanged(List<FavoriteMovie> favoriteMovies) {
                ArrayList<Movie> movies = new ArrayList<>();
                for (FavoriteMovie favoriteMovie : favoriteMovies) {
                    int id = favoriteMovie.getId();
                    String title = favoriteMovie.getTitle();
                    String overview = favoriteMovie.getOverview();
                    String release_date = favoriteMovie.getRelease_date();
                    double vote_average = favoriteMovie.getVote_average();
                    int vote_count = favoriteMovie.getVote_count();
                    String small_poster_path = favoriteMovie.getSmall_poster_path();
                    String big_poster_path = favoriteMovie.getBig_poster_path();
                    String backdrop_path = favoriteMovie.getBackdrop_path();

                    movies.add(new Movie(id, title, overview, release_date, vote_average, vote_count, small_poster_path, big_poster_path, backdrop_path));
                }

                adapter.setMovies(movies);
            }
        });

        adapter.setOnPosterClickListener(new MovieAdapter.onPosterClickListener() {
            @Override
            public void onPosterClick(int position) {
                Intent intent = new Intent(getApplicationContext(), MovieDetailsActivity.class);
                Movie movie = adapter.getMovies().get(position);
                intent.putExtra("id", movie.getId());
                intent.putExtra("title", movie.getTitle());
                intent.putExtra("release_date", movie.getRelease_date());
                intent.putExtra("vote_average", movie.getVote_average());
                intent.putExtra("vote_count", movie.getVote_count());
                intent.putExtra("overview", movie.getOverview());
                intent.putExtra("small_poster_path", movie.getSmall_poster_path());
                intent.putExtra("big_poster_path", movie.getBig_poster_path());
                intent.putExtra("backdrop_path", movie.getBackdrop_path());
                startActivity(intent);
            }
        });
    }
}