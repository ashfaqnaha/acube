package com.example.moviesinfo.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.moviesinfo.R;
import com.example.moviesinfo.data.Movie;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder> {

    private ArrayList<Movie> movies;
    private onPosterClickListener onPosterClickListener;
    private onReachEndListener onReachEndListener;

    public MovieAdapter() {
        movies = new ArrayList<>();
    }

    public ArrayList<Movie> getMovies() {
        return movies;
    }

    public void setMovies(ArrayList<Movie> movies) {
        this.movies = movies;
        notifyDataSetChanged();
    }

    public void addMovies(ArrayList<Movie> movies) {
        this.movies.addAll(movies);
        notifyDataSetChanged();
    }

    public void clear() {
        this.movies.clear();
        notifyDataSetChanged();
    }

    public interface onPosterClickListener {
        void onPosterClick(int position); //method which activated when user clicks on poster
    }

    public void setOnPosterClickListener(MovieAdapter.onPosterClickListener onPosterClickListener) {
        this.onPosterClickListener = onPosterClickListener;
    }

    public interface onReachEndListener {
        void onReachEnd(); //Method which get activated when user reaches end of page
    }

    public void setOnReachEndListener(MovieAdapter.onReachEndListener onReachEndListener) {
        this.onReachEndListener = onReachEndListener;
    }

    //Adapter
    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.movie_item, parent, false);
        return new MovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieViewHolder holder, int position) {
        if (movies.size() >= 20 && position == movies.size() - 4) { //it would be better to start download movies from next page before user reached the end. That's why onReachedEnd is activating when position = 4
            onReachEndListener.onReachEnd();
        }
        Movie movie = movies.get(position);
        Picasso.get().load(movie.getSmall_poster_path()).into(holder.imageViewSmallPoster);

    }

    @Override
    public int getItemCount() {
        return movies.size();
    }

    //ViewHolder
    class MovieViewHolder extends RecyclerView.ViewHolder {
        private ImageView imageViewSmallPoster;

        public MovieViewHolder(@NonNull View itemView) {
            super(itemView);

            imageViewSmallPoster = itemView.findViewById(R.id.imageViewSmallPoster);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onPosterClickListener.onPosterClick(getAdapterPosition());
                }
            });
        }
    }
}
