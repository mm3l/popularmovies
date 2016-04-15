package com.mmel.popularmovies.app.api;

import android.net.Uri;
import android.util.Log;

import com.mmel.popularmovies.app.BuildConfig;
import com.mmel.popularmovies.app.data.Movie;
import com.mmel.popularmovies.app.data.Trailer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * This class represents TheMovieDB API implementation
 *
 * @author michael.melachridis@gmail.com
 */
public class TheMovieDbApi {

    private static final String LOG_TAG = TheMovieDbApi.class.getSimpleName();

    private static final String apiKey = BuildConfig.THE_MOVIE_DB_API_KEY;

    private static final String IMAGE_URL_BASE = "http://image.tmdb.org/t/p/";
    private static final String API_URL_BASE = "http://api.themoviedb.org/3/";
    private static final String ENDPOINT_DISCOVER_MOVIE = "discover/movie";
    private static final String ENDPOINT_MOVIE = "movie/";
    private static final String VIDEOS = "/videos";

    private static final String VIDEO_URL_BASE = "https://www.youtube.com/watch";

    /* Required API parameters */
    private static final String API_KEY = "api_key";

    /* Optional API parameters */
    private static final String SORT_BY = "sort_by";

    /* Sorting parameters */
    private static final String HIGHEST_RATED = "vote_average.desc";
    private static final String TOP_RATED = "vote_count.desc";
    private static final String MOST_POPULAR = "popularity.desc";
    private static final String RELEASED_DATE = "release_date.desc";
    private static final String REVENUE = "revenue.desc";

    /* Image languages */
    private static final String BACKDROP_PATH = "backdrop_path";
    private static final String POSTER_PATH = "poster_path";

    private static final String MOVIE_ID = "id";
    private static final String ORIGINAL_TITLE = "original_title";
    private static final String RELEASE_DATE = "release_date";
    private static final String VOTE_AVERAGE = "vote_average";
    private static final String OVERVIEW = "overview";

    private static final String TRAILER_ID = "id";
    private static final String TRAILER_KEY = "key";
    private static final String TRAILER_NAME = "name";
    private static final String TRAILER_SIZE = "size";

    public enum SortOption {
        SORT_BY_MOST_POPULAR,
        SORT_BY_TOP_RATED,
        SORT_BY_HIGHEST_RATED,
        SORT_BY_REVENUE,
        SORT_BY_RELEASE_DATE
    }

    public TheMovieDbApi() {

    }

    public ArrayList<Trailer> getMovieTrailers(int movieId) {
        ArrayList<Trailer> trailers = new ArrayList<Trailer>();

        Uri uri = Uri.parse(API_URL_BASE + ENDPOINT_MOVIE + movieId + VIDEOS).buildUpon()
                .appendQueryParameter(API_KEY, BuildConfig.THE_MOVIE_DB_API_KEY)
                .build();

        //Log.d(LOG_TAG, "json query: " + uri.toString());

        HttpRequest.Response response = HttpRequest.get(uri.toString());

        if (response == null) {
            return null;
        }

        JSONObject discoverTrailersJson;
        try {
            discoverTrailersJson = response.json();

            //Log.d(LOG_TAG, "json response: " + discoverTrailersJson.toString());

        } catch (JSONException e) {
            Log.e(LOG_TAG, ENDPOINT_MOVIE + " came back with invalid JSON: \n"
                    + response.text);
            return null;
        }

        try {
            JSONArray results = discoverTrailersJson.getJSONArray("results");
            for (int i = 0; i < results.length(); i++) {
                JSONObject trailerInfoJson = results.getJSONObject(i);
                trailers.add(new Trailer(movieId,
                        trailerInfoJson.getString(TRAILER_ID),
                        trailerInfoJson.getString(TRAILER_KEY),
                        trailerInfoJson.getString(TRAILER_NAME),
                        trailerInfoJson.getInt(TRAILER_SIZE)
                ));
            }
        } catch (JSONException e) {
            Log.e(LOG_TAG, ENDPOINT_MOVIE + " discoverTrailersJson not expected format: \n" + discoverTrailersJson.toString());
            return null;
        }

        return trailers;
    }

    public ArrayList<Movie> discoverMovies(SortOption sort) {
        String sortByParam;
        switch (sort) {
            case SORT_BY_MOST_POPULAR:
                sortByParam = MOST_POPULAR;
                break;
            case SORT_BY_TOP_RATED:
                sortByParam = TOP_RATED;
                break;
            case SORT_BY_HIGHEST_RATED:
                sortByParam = HIGHEST_RATED;
                break;
            case SORT_BY_REVENUE:
                sortByParam = REVENUE;
                break;
            case SORT_BY_RELEASE_DATE:
                sortByParam = RELEASED_DATE;
                break;

            default:
                throw new AssertionError("This is impossible");
        }
        Uri uri = Uri.parse(API_URL_BASE + ENDPOINT_DISCOVER_MOVIE).buildUpon()
                .appendQueryParameter(API_KEY, BuildConfig.THE_MOVIE_DB_API_KEY)
                .appendQueryParameter(SORT_BY, sortByParam)
                .build();

        HttpRequest.Response response = HttpRequest.get(uri.toString());

        if (response == null) {
            return null;
        }
        JSONObject discoverMoviesJson;
        try {
            discoverMoviesJson = response.json();
        } catch (JSONException e) {
            Log.e(LOG_TAG, ENDPOINT_DISCOVER_MOVIE + " came back with invalid JSON: \n"
                    + response.text);
            return null;
        }

        return parseDiscoverMoviesJson(discoverMoviesJson);
    }

    public ArrayList<Movie> parseDiscoverMoviesJson(JSONObject discoverMoviesJson) {
        ArrayList<Movie> movies = new ArrayList<>();
        try {
            JSONArray results = discoverMoviesJson.getJSONArray("results");
            for (int i = 0; i < results.length(); i++) {
                JSONObject movieInfoJson = results.getJSONObject(i);
                movies.add(new Movie(
                        movieInfoJson.getInt(MOVIE_ID),
                        movieInfoJson.getString(POSTER_PATH),
                        movieInfoJson.getString(BACKDROP_PATH),
                        movieInfoJson.getString(ORIGINAL_TITLE),
                        movieInfoJson.getString(RELEASE_DATE),
                        movieInfoJson.getDouble(VOTE_AVERAGE),
                        movieInfoJson.getString(OVERVIEW)
                ));
            }
        } catch (JSONException e) {
            Log.e(LOG_TAG, ENDPOINT_DISCOVER_MOVIE + " discoverMoviesJson not expected format: \n" + discoverMoviesJson.toString());
            return null;
        }
        return movies;
    }

    public static String getImageUrl(String imageName, Movie.ImageSize imageSize) {
        return IMAGE_URL_BASE + imageSize.toString() + imageName;
    }

    public static Uri getTrailerUri(String key) {
        Uri uri = Uri.parse(VIDEO_URL_BASE).buildUpon()
                .appendQueryParameter("v", key)
                .build();

        Log.d(LOG_TAG, "getTrailerUri: " + uri.toString());
        return uri;
    }
}
