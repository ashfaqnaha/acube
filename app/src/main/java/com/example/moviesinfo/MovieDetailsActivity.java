package com.example.moviesinfo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.moviesinfo.adapters.ReviewsAdapter;
import com.example.moviesinfo.adapters.VideoAdapter;
import com.example.moviesinfo.data.FavoriteMovie;
import com.example.moviesinfo.data.MainViewModel;
import com.example.moviesinfo.data.Movie;
import com.example.moviesinfo.data.Review;
import com.example.moviesinfo.data.Video;
import com.example.moviesinfo.utils.JSONUtils;
import com.example.moviesinfo.utils.NetworkUtils;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Locale;

public class MovieDetailsActivity extends AppCompatActivity {

    private int id;
    private MainViewModel mainViewModel;
    FavoriteMovie favoriteMovie;

    private ImageView imageViewBigPoster;
    private TextView textViewTitle;
    private TextView textViewReleaseDate;
    private TextView textViewRating;
    private TextView textViewOverview;

    private FloatingActionButton floatingActionButtonFavorites;

    private RecyclerView recyclerViewVideos;
    private VideoAdapter videoAdapter;
    private RecyclerView recyclerViewReviews;
    private ReviewsAdapter reviewsAdapter;

    private View view2;
    private View view3;

    private static String LANGUAGE;

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
        setContentView(R.layout.activity_movie_details);

        getSupportActionBar().setTitle(R.string.movie_details_action_bar_title);

        LANGUAGE = Locale.getDefault().getLanguage();

        imageViewBigPoster = findViewById(R.id.imageViewBigPoster);
        floatingActionButtonFavorites = findViewById(R.id.floatingActionButtonFavorites);
        textViewTitle = findViewById(R.id.textViewTitle);
        textViewReleaseDate = findViewById(R.id.textViewReleaseDate);
        textViewRating = findViewById(R.id.textViewRating);
        textViewOverview = findViewById(R.id.textViewOverview);
        recyclerViewVideos = findViewById(R.id.recyclerViewVideos);
        recyclerViewReviews = findViewById(R.id.recyclerViewReviews);

        view2 = findViewById(R.id.view2);
        view3 = findViewById(R.id.view3);

        //getting all information about the movie from Intent
        Intent intent = getIntent();
        id = intent.getIntExtra("id", 0);
        String title = intent.getStringExtra("title");
        String overview = intent.getStringExtra("overview");
        String release_date = intent.getStringExtra("release_date");
        double vote_average = intent.getDoubleExtra("vote_average", -1);
        int vote_count = intent.getIntExtra("vote_count", -1);
        String small_poster_path = intent.getStringExtra("small_poster_path");
        String big_poster_path = intent.getStringExtra("big_poster_path");
        String backdrop_path = intent.getStringExtra("backdrop_path");

        //setting all displayable info
        Picasso.get().load(big_poster_path).placeholder(R.drawable.default_poster).into(imageViewBigPoster);
        textViewTitle.setText(title);
        textViewReleaseDate.setText(release_date);
        textViewRating.setText(String.valueOf(vote_average));
        textViewOverview.setText(overview);

        recyclerViewVideos.setLayoutManager(new LinearLayoutManager(this));
        videoAdapter = new VideoAdapter();
        recyclerViewVideos.setAdapter(videoAdapter);
        JSONObject jsonObjectForVideos = NetworkUtils.getJSONObjectForVideos(id, LANGUAGE);
        ArrayList<Video> videos = JSONUtils.getVideosFromJSON(jsonObjectForVideos);
        videoAdapter.setVideos(videos);
        if (videos.isEmpty()) {
            view2.setVisibility(View.INVISIBLE);
        }

        recyclerViewReviews.setLayoutManager(new LinearLayoutManager(this));
        reviewsAdapter = new ReviewsAdapter();
        recyclerViewReviews.setAdapter(reviewsAdapter);
        JSONObject jsonObjectForReviews = NetworkUtils.getJSONObjectForReviews(id, LANGUAGE);
        ArrayList<Review> reviews = JSONUtils.getReviewsFromJSON(jsonObjectForReviews);
        reviewsAdapter.setReviews(reviews);
        if (reviews.isEmpty()) {
            view3.setVisibility(View.INVISIBLE);
        }


        //MainViewModel
        mainViewModel = new ViewModelProvider(this).get(MainViewModel.class);
        favoriteMovie = mainViewModel.getFavoriteMovieById(id);

        //floatingActionButton
        floatingActionButtonFavorites.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (favoriteMovie == null) { //add movie to favorites
                    favoriteMovie = new FavoriteMovie(id, title, overview, release_date, vote_average, vote_count, small_poster_path, big_poster_path, backdrop_path);
                    mainViewModel.insertFavoriteMovie(favoriteMovie);
                } else { //remove movie from favorites
                    mainViewModel.deleteFavoriteMovie(favoriteMovie);
                }
                isFavorite();
            }
        });

        videoAdapter.setOnPlayClickListener(new VideoAdapter.onPlayClickListener() {
            @Override
            public void onPlayClick(String videoURL) {
                Intent intentToVideo = new Intent(Intent.ACTION_VIEW, Uri.parse(videoURL));
                startActivity(intentToVideo);
            }
        });

        isFavorite();
    }

    private void isFavorite() {
        favoriteMovie = mainViewModel.getFavoriteMovieById(id);
        if (favoriteMovie == null) {
            Picasso.get().load(R.drawable.add_to_favorite).into(floatingActionButtonFavorites);
        } else {
            Picasso.get().load(R.drawable.remove_from_favorite).into(floatingActionButtonFavorites);
        }
    }
}