package com.example.moviesinfo;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.TextView;


import com.example.moviesinfo.adapters.MovieAdapter;
import com.example.moviesinfo.data.Movie;
import com.example.moviesinfo.utils.JSONUtils;
import com.example.moviesinfo.utils.NetworkUtils;

import org.json.JSONObject;

import java.net.URL;
import java.util.ArrayList;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<JSONObject> {

    private RecyclerView recyclerViewMovies;
    private TextView textViewPopular;
    private TextView textViewTopRated;
    private Switch switchPopularTopRated;
    private ProgressBar progressBarLoading;

    private MovieAdapter adapter;
    private ArrayList<Movie> movies;

    private static final int LOADER_ID = 1;
    private LoaderManager loaderManager;

    private int PAGE = 1;
    private static String LANGUAGE;
    private static boolean isLoading = false;
    private static boolean SORT_BY = false;

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

    private int getSpanCount() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        int width = (int) (displayMetrics.widthPixels / displayMetrics.density);

        return width / 342 > 2 ? width / 342 : 2;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().setTitle(R.string.home_action_bar_title);

        LANGUAGE = Locale.getDefault().getLanguage();

        loaderManager = LoaderManager.getInstance(this);
        progressBarLoading = findViewById(R.id.progressBarLoading);

        //RecyclerView
        recyclerViewMovies = findViewById(R.id.recyclerViewMovies);
        recyclerViewMovies.setLayoutManager(new GridLayoutManager(this, getSpanCount()));
        adapter = new MovieAdapter();
        recyclerViewMovies.setAdapter(adapter);

        //Switch
        textViewPopular = findViewById(R.id.textViewPopular);
        textViewTopRated = findViewById(R.id.textViewTopRated);
        switchPopularTopRated = findViewById(R.id.switchPopularTopRated);
        switchPopularTopRated.setChecked(true);
        switchPopularTopRated.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                PAGE = 1;
                adapter.clear();
                if (isChecked) {
                    textViewPopular.setTextColor(getResources().getColor(R.color.white));
                    textViewTopRated.setTextColor(getResources().getColor(R.color.light_red));
                    SORT_BY = isChecked;
                } else {
                    textViewPopular.setTextColor(getResources().getColor(R.color.light_red));
                    textViewTopRated.setTextColor(getResources().getColor(R.color.white));
                    SORT_BY = isChecked;
                }
                downloadData(SORT_BY, PAGE);
            }
        });
        switchPopularTopRated.setChecked(false);

        //adapter methods
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
        adapter.setOnReachEndListener(new MovieAdapter.onReachEndListener() {
            @Override
            public void onReachEnd() {
                if (!isLoading) {
                    downloadData(SORT_BY, PAGE);
                }
            }
        });

    }

    private void downloadData(boolean SORT_BY, int PAGE) {
        URL url = NetworkUtils.buildURL(SORT_BY, PAGE, LANGUAGE);
        Bundle bundle = new Bundle();
        bundle.putString("url", url.toString());
        loaderManager.restartLoader(LOADER_ID, bundle, this);
    }

    //Activates when user presses textViewPopular
    public void switchToPopular(View view) { //user also can change switch by tapping text next to it
        switchPopularTopRated.setChecked(false);
    }

    //Activates when user presses textViewTopRated
    public void switchToTopRated(View view) { //user also can change switch by tapping text next to it
        switchPopularTopRated.setChecked(true);
    }

    //Loader
    @NonNull
    @Override
    public Loader<JSONObject> onCreateLoader(int id, @Nullable Bundle args) {
        NetworkUtils.JSONLoader jsonLoader = new NetworkUtils.JSONLoader(this, args);
        jsonLoader.setOnStartLoadingListener(new NetworkUtils.JSONLoader.OnStartLoadingListener() {
            @Override
            public void onStartLoading() {
                progressBarLoading.setVisibility(View.VISIBLE);
                isLoading = true;
            }
        });
        return jsonLoader;
    }

    @Override
    public void onLoadFinished(@NonNull Loader<JSONObject> loader, JSONObject data) {
        movies = JSONUtils.getMoviesFromJSON(data);
        if (movies != null && !movies.isEmpty()) {
            adapter.addMovies(movies);
            PAGE++;
        }
        isLoading = false;
        progressBarLoading.setVisibility(View.INVISIBLE);
        loaderManager.destroyLoader(LOADER_ID);
    }

    @Override
    public void onLoaderReset(@NonNull Loader<JSONObject> loader) {

    }
}