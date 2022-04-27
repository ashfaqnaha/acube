package com.example.moviesinfo.utils;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.loader.content.AsyncTaskLoader;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.ExecutionException;

public class NetworkUtils {
    //variables for building URL
    private static final String BASE_URL = "https://api.themoviedb.org/3/discover/movie";
    private static final String QUERY_API_KEY = "api_key";
    private static final String QUERY_LANGUAGE = "language";
    private static final String QUERY_SORT_BY = "sort_by";
    private static final String QUERY_MIN_VOTE_COUNT = "vote_count.gte";
    private static final String QUERY_PAGE = "page";

    //Getting JSONObject for movie info (title, overview, etc.)
    private static final String API_KEY = "dab2aa2fdcf16d071eb9f7e3bf35c484";
    public static final boolean SORT_BY_POPULARITY = false;
    public static final boolean SORT_BY_RATING = true;
    public static final int MIN_VOTE_COUNT = 500;

    //In this method we creating URL in buildURL, and then getting JSONObject from getJSONTask
    public static JSONObject getJSONObject(boolean METHOD_OF_SORT, int PAGE, String LANGUAGE) {
        JSONObject jsonObject = null;

        URL url = buildURL(METHOD_OF_SORT, PAGE, LANGUAGE);
        try {
            jsonObject = new getJSONTask().execute(url).get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return jsonObject;
    }

    //This method builds URL by using variables we created earlier
    public static URL buildURL(boolean METHOD_OF_SORT, int PAGE, String LANGUAGE) {
        URL url = null;

        String SORT_BY;
        if (METHOD_OF_SORT) {
            SORT_BY = "vote_average.desc";
        } else {
            SORT_BY = "popularity.desc";
        }

        Uri uri = Uri.parse(BASE_URL).buildUpon()
                .appendQueryParameter(QUERY_API_KEY, API_KEY)
                .appendQueryParameter(QUERY_LANGUAGE, LANGUAGE)
                .appendQueryParameter(QUERY_SORT_BY, SORT_BY)
                .appendQueryParameter(QUERY_PAGE, String.valueOf(PAGE))
                .appendQueryParameter(QUERY_MIN_VOTE_COUNT, String.valueOf(MIN_VOTE_COUNT))
                .build();
        try {
            url = new URL(uri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return url;
    }

    public static class JSONLoader extends AsyncTaskLoader<JSONObject> {

        private Bundle bundle;
        private OnStartLoadingListener onStartLoadingListener;

        public void setOnStartLoadingListener(OnStartLoadingListener onStartLoadingListener) {
            this.onStartLoadingListener = onStartLoadingListener;
        }

        public interface OnStartLoadingListener {
            void onStartLoading();
        }

        public JSONLoader(@NonNull Context context, Bundle bundle) {
            super(context);
            this.bundle = bundle;
        }

        @Override
        protected void onStartLoading() {
            super.onStartLoading();
            if (onStartLoadingListener != null) {
                onStartLoadingListener.onStartLoading();
            }
            forceLoad();
        }

        @Nullable
        @Override
        public JSONObject loadInBackground() {
            if (bundle == null) {
                return null;
            }

            String urlString = bundle.getString("url");
            URL url = null;
            try {
                url = new URL(urlString);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            if (url == null) {
                return null;
            }

            JSONObject jsonObject = null;
            HttpURLConnection httpURLConnection = null;
            try {
                httpURLConnection = (HttpURLConnection) url.openConnection();
                InputStream inputStream = httpURLConnection.getInputStream();
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                StringBuilder JSON = new StringBuilder();
                String line = bufferedReader.readLine();
                while (line != null) {
                    JSON.append(line);
                    line = bufferedReader.readLine();
                }

                try {
                    jsonObject = new JSONObject(JSON.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (httpURLConnection != null) {
                    httpURLConnection.disconnect();
                }
            }

            return jsonObject;
        }
    }

    //This class creates JSONObject by using URL, which was created in buildURL method
    private static class getJSONTask extends AsyncTask<URL, Void, JSONObject> {

        @Override
        protected JSONObject doInBackground(URL... urls) {
            JSONObject jsonObject = null;

            HttpURLConnection httpURLConnection = null;
            if (urls != null && urls.length > 0) {
                try {
                    httpURLConnection = (HttpURLConnection) urls[0].openConnection();
                    InputStream inputStream = httpURLConnection.getInputStream();
                    InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                    BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                    StringBuilder JSON = new StringBuilder();
                    String line = bufferedReader.readLine();
                    while (line != null) {
                        JSON.append(line);
                        line = bufferedReader.readLine();
                    }

                    try {
                        jsonObject = new JSONObject(JSON.toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    if (httpURLConnection != null) {
                        httpURLConnection.disconnect();
                    }
                }
            }
            return jsonObject;
        }

    }

    //Getting JSONObject for videos
    private static final String BASE_VIDEOS_URL = "https://api.themoviedb.org/3/movie/%s/videos";

    public static JSONObject getJSONObjectForVideos(int id, String LANGUAGE) {
        JSONObject jsonObject = null;

        URL url = buildVideosURL(id, LANGUAGE);
        try {
            jsonObject = new getJSONTask().execute(url).get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return jsonObject;
    }

    public static URL buildVideosURL(int id, String LANGUAGE) {
        URL url = null;

        Uri uri = Uri.parse(String.format(BASE_VIDEOS_URL, id)).buildUpon()
                .appendQueryParameter(QUERY_API_KEY, API_KEY)
                .appendQueryParameter(QUERY_LANGUAGE, LANGUAGE)
                .build();
        try {
            url = new URL(uri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return url;
    }

    //Getting JSONObject for reviews
    private static final String BASE_REVIEWS_URL = "https://api.themoviedb.org/3/movie/%s/reviews";

    public static JSONObject getJSONObjectForReviews(int id, String LANGUAGE) {
        JSONObject jsonObject = null;

        URL url = buildReviewsURL(id, LANGUAGE);
        try {
            jsonObject = new getJSONTask().execute(url).get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return jsonObject;
    }

    public static URL buildReviewsURL(int id, String LANGUAGE) {
        URL url = null;

        Uri uri = Uri.parse(String.format(BASE_REVIEWS_URL, id)).buildUpon()
                .appendQueryParameter(QUERY_API_KEY, API_KEY)
                .appendQueryParameter(QUERY_LANGUAGE, LANGUAGE)
                .build();
        try {
            url = new URL(uri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return url;
    }
}
