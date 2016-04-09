package com.mmel.popularmovies.app;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


/**
 * It displays a grid view of popular movies based on the sort criteria
 *
 * @author michael.melachridis@gmail.com
 */
public class MoviesFragment extends Fragment {

    private static final String TAG = MoviesFragment.class.getSimpleName();

    enum SortOption {
        SORT_BY_MOST_POPULAR,
        SORT_BY_HIGHEST_RATED
    }

    private SortOption mSelectedSortOption;

    private CustomArrayAdapter mMoviesAdapter;

    public MoviesFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.moviesfragment, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_refresh) {

            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        Spinner sortSpinner = (Spinner) rootView.findViewById(R.id.spinner_sort);
        ArrayAdapter<CharSequence> sortAdapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.sort_options, android.R.layout.simple_spinner_item);
        sortAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sortSpinner.setAdapter(sortAdapter);
        sortSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                switch (position) {
                    case 0:
                        mSelectedSortOption = SortOption.SORT_BY_MOST_POPULAR;
                        break;
                    case 1:
                        mSelectedSortOption = SortOption.SORT_BY_HIGHEST_RATED;
                        break;
                }
                updateMovies();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        mMoviesAdapter = new CustomArrayAdapter(getActivity(),
                R.layout.grid_item_movie, new ArrayList<Movie>());
        GridView moviesGridView = (GridView) rootView.findViewById(R.id.gridview_movies);
        moviesGridView.setAdapter(mMoviesAdapter);
        moviesGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Toast.makeText(getActivity(), mMoviesAdapter.getItem(position).getTitle(),
                        Toast.LENGTH_SHORT).show();

                /*Movie movie = mMoviesAdapter.getItem(position);
                Intent intent = new Intent(parent.getContext(), DetailActivity.class);
                intent.putExtra("movie_info", movieInfo);
                startActivity(intent);*/
            }
        });

        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        updateMovies();
    }

    private void updateMovies() {
        FetchMoviesTask movieTask = new FetchMoviesTask();
        //SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        //String location = preferences.getString(getString(R.string.pref_location_key), getString(R.string.pref_location_default));
        movieTask.execute();
    }

    private class FetchMoviesTask extends AsyncTask<Void, Void, ArrayList<Movie>> {

        private final String LOG_TAG = FetchMoviesTask.class.getSimpleName();

        private static final String IMAGE_URL_BASE = "http://image.tmdb.org/t/p/";
        private static final String API_URL_BASE = "http://api.themoviedb.org/3/";
        private static final String ENDPOINT_DISCOVER_MOVIE = "discover/movie";

        @Override
        protected ArrayList<Movie> doInBackground(Void... params) {

            ArrayList<Movie> movies;

            getActivity().runOnUiThread(new Runnable() {
                public void run() {

                    Toast.makeText(getActivity().getApplicationContext(),
                            "Fetching data...",
                            Toast.LENGTH_SHORT).show();
                }
            });

            movies = discoverMovies(
                    mSelectedSortOption != null ? mSelectedSortOption : SortOption.SORT_BY_MOST_POPULAR);
            return (movies);
        }

        @Override
        protected void onPostExecute(ArrayList<Movie> movieInfos) {
            mMoviesAdapter.clear();
            if (movieInfos != null) {
                for (Movie movieInfo : movieInfos) {
                    mMoviesAdapter.add(movieInfo);
                }
            }
        }

        public ArrayList<Movie> discoverMovies(SortOption sort) {
            String sortByParam;
            switch (sort) {
                case SORT_BY_HIGHEST_RATED:
                    sortByParam = "vote_average.desc";
                    break;
                case SORT_BY_MOST_POPULAR:
                    sortByParam = "popularity.desc";
                    break;
                default:
                    throw new AssertionError("This is impossible");
            }
            Uri uri = Uri.parse(API_URL_BASE + ENDPOINT_DISCOVER_MOVIE).buildUpon()
                    .appendQueryParameter("api_key", BuildConfig.THE_MOVIE_DB_API_KEY)
                    .appendQueryParameter("sort_by", sortByParam)
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
                            movieInfoJson.getString("poster_path"),
                            movieInfoJson.getString("original_title"),
                            movieInfoJson.getString("release_date"),
                            movieInfoJson.getDouble("vote_average"),
                            movieInfoJson.getString("overview")
                    ));
                }
            } catch (JSONException e) {
                Log.e(LOG_TAG, ENDPOINT_DISCOVER_MOVIE + " discoverMoviesJson not expected format: \n" + discoverMoviesJson.toString());
                return null;
            }
            return movies;
        }
    }
}
