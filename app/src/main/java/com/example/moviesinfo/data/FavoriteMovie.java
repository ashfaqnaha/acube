package com.example.moviesinfo.data;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "favorite_movies")
public class FavoriteMovie {
    @PrimaryKey(autoGenerate = true)
    private int uniqueId;
    private int id;
    private String title;
    private String overview;
    private String release_date;
    private double vote_average;
    private int vote_count;
    private String small_poster_path;
    private String big_poster_path;
    private String backdrop_path;

    public FavoriteMovie(int uniqueId, int id, String title, String overview, String release_date, double vote_average, int vote_count, String small_poster_path, String big_poster_path, String backdrop_path) {
        this.uniqueId = uniqueId;
        this.id = id;
        this.title = title;
        this.overview = overview;
        this.release_date = release_date;
        this.vote_average = vote_average;
        this.vote_count = vote_count;
        this.small_poster_path = small_poster_path;
        this.big_poster_path = big_poster_path;
        this.backdrop_path = backdrop_path;
    }

    @Ignore
    public FavoriteMovie(int id, String title, String overview, String release_date, double vote_average, int vote_count, String small_poster_path, String big_poster_path, String backdrop_path) {
        this.id = id;
        this.title = title;
        this.overview = overview;
        this.release_date = release_date;
        this.vote_average = vote_average;
        this.vote_count = vote_count;
        this.small_poster_path = small_poster_path;
        this.big_poster_path = big_poster_path;
        this.backdrop_path = backdrop_path;
    }

    public int getUniqueId() {
        return uniqueId;
    }

    public void setUniqueId(int uniqueId) {
        this.uniqueId = uniqueId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getRelease_date() {
        return release_date;
    }

    public void setRelease_date(String release_date) {
        this.release_date = release_date;
    }

    public double getVote_average() {
        return vote_average;
    }

    public void setVote_average(double vote_average) {
        this.vote_average = vote_average;
    }

    public int getVote_count() {
        return vote_count;
    }

    public void setVote_count(int vote_count) {
        this.vote_count = vote_count;
    }

    public String getSmall_poster_path() {
        return small_poster_path;
    }

    public void setSmall_poster_path(String small_poster_path) {
        this.small_poster_path = small_poster_path;
    }

    public String getBig_poster_path() {
        return big_poster_path;
    }

    public void setBig_poster_path(String big_poster_path) {
        this.big_poster_path = big_poster_path;
    }

    public String getBackdrop_path() {
        return backdrop_path;
    }

    public void setBackdrop_path(String backdrop_path) {
        this.backdrop_path = backdrop_path;
    }
}
