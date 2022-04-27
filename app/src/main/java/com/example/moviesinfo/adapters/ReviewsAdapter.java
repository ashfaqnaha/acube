package com.example.moviesinfo.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.moviesinfo.R;
import com.example.moviesinfo.data.Review;

import java.util.ArrayList;

public class ReviewsAdapter extends RecyclerView.Adapter<ReviewsAdapter.ReviewsViewHolder> {

    ArrayList<Review> reviews;

    public ArrayList<Review> getReviews() {
        return reviews;
    }

    public void setReviews(ArrayList<Review> reviews) {
        this.reviews = reviews;
        notifyDataSetChanged();
    }

    //Adapter
    @NonNull
    @Override
    public ReviewsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.review_item, parent, false);
        return new ReviewsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewsViewHolder holder, int position) {
        Review review = reviews.get(position);
        holder.textViewReview.setText(review.getContent());
    }

    @Override
    public int getItemCount() {
        return reviews.size();
    }

    //ViewHolder
    class ReviewsViewHolder extends RecyclerView.ViewHolder {
        private TextView textViewReview;

        public ReviewsViewHolder(@NonNull View itemView) {
            super(itemView);

            textViewReview = itemView.findViewById(R.id.textViewReview);
        }
    }
}
