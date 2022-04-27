package com.example.moviesinfo.utils;

import com.example.moviesinfo.data.Movie;
import com.example.moviesinfo.data.Review;
import com.example.moviesinfo.data.Video;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class JSONUtils {
    //Keys for getting data from JSONArray
    private static final String KEY_RESULTS = "results";
    private static final String KEY_ID = "id";
    private static final String KEY_TITLE = "title";
    private static final String KEY_OVERVIEW = "overview";
    private static final String KEY_RELEASE_DATE = "release_date";
    private static final String KEY_VOTE_AVERAGE = "vote_average";
    private static final String KEY_VOTE_COUNT = "vote_count";
    private static final String KEY_POSTER_PATH = "poster_path";
    private static final String KEY_BACKDROP_PATH = "backdrop_path";

    //Getting JSONObject for movie info (title, overview, etc.)
    //Stuff for big and small posters
    private static final String BASE_POSTER_URL = "https://image.tmdb.org/t/p/";
    private static final String SMALL_POSTER_SIZE = "w342";
    private static final String BIG_POSTER_SIZE = "original";

    //This method gets info from JSONArray, creates Movie object, and add it into movies ArrayList
    public static ArrayList<Movie> getMoviesFromJSON(JSONObject jsonObject) {
        ArrayList<Movie> movies = new ArrayList<>();

        try {
            JSONArray jsonArray = jsonObject.getJSONArray(KEY_RESULTS);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObjectFromArray = jsonArray.getJSONObject(i);

                int id = jsonObjectFromArray.getInt(KEY_ID);
                String title = jsonObjectFromArray.getString(KEY_TITLE);
                String overview = jsonObjectFromArray.getString(KEY_OVERVIEW);
                String release_date = jsonObjectFromArray.getString(KEY_RELEASE_DATE);
                double vote_average = jsonObjectFromArray.getDouble(KEY_VOTE_AVERAGE);
                int vote_count = jsonObjectFromArray.getInt(KEY_VOTE_COUNT);
                String small_poster_path = BASE_POSTER_URL + SMALL_POSTER_SIZE + jsonObjectFromArray.getString(KEY_POSTER_PATH);
                String big_poster_path = BASE_POSTER_URL + BIG_POSTER_SIZE + jsonObjectFromArray.getString(KEY_POSTER_PATH);
                String backdrop_path = jsonObjectFromArray.getString(KEY_BACKDROP_PATH);

                movies.add(new Movie(id, title, overview, release_date, vote_average, vote_count, small_poster_path, big_poster_path, backdrop_path));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return movies;
    }

    //Videos
    private static final String BASE_VIDEO_URL = "https://www.youtube.com/watch?v=";
    private static final String KEY_VIDEO_KEY = "key";
    private static final String KEY_NAME = "name";

    public static ArrayList<Video> getVideosFromJSON(JSONObject jsonObject) {
        ArrayList<Video> videos = new ArrayList<>();

        try {
            JSONArray jsonArray = jsonObject.getJSONArray(KEY_RESULTS);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObjectFromArray = jsonArray.getJSONObject(i);

                String video_key = BASE_VIDEO_URL + jsonObjectFromArray.getString(KEY_VIDEO_KEY);
                String video_name = jsonObjectFromArray.getString(KEY_NAME);

                videos.add(new Video(video_key, video_name));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return videos;
    }

    //Reviews
    private static final String KEY_CONTENT = "content";

    public static ArrayList<Review> getReviewsFromJSON(JSONObject jsonObject) {
        ArrayList<Review> reviews = new ArrayList<>();

        try {
            JSONArray jsonArray = jsonObject.getJSONArray(KEY_RESULTS);
            for (int i = 0; i < jsonArray.length(); i++) {

                JSONObject jsonObjectFromArray = jsonArray.getJSONObject(i);
                String content = jsonObjectFromArray.getString(KEY_CONTENT);

                reviews.add(new Review(content));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return reviews;
    }
}
