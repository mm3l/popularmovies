package com.mmel.popularmovies.app;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
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
import android.widget.Toast;

import com.mmel.popularmovies.app.api.TheMovieDbApi;

import java.util.ArrayList;
import java.util.HashMap;


/**
 * It displays a grid view of popular movies based on the sort criteria
 *
 * @author michael.melachridis@gmail.com
 */
public class MoviesFragment extends Fragment {

    private static final String LOG_TAG = MoviesFragment.class.getSimpleName();

    private CustomArrayAdapter mMoviesAdapter;

    private HashMap<String, TheMovieDbApi.SortOption> sortKeyMap =
            new HashMap<String, TheMovieDbApi.SortOption>();

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

        sortKeyMap.put(getActivity().getString(R.string.pref_sort_popular), TheMovieDbApi.SortOption.SORT_BY_MOST_POPULAR);
        sortKeyMap.put(getActivity().getString(R.string.pref_sort_top_rated), TheMovieDbApi.SortOption.SORT_BY_TOP_RATED);
        sortKeyMap.put(getActivity().getString(R.string.pref_sort_rating), TheMovieDbApi.SortOption.SORT_BY_HIGHEST_RATED);
        sortKeyMap.put(getActivity().getString(R.string.pref_sort_revenue), TheMovieDbApi.SortOption.SORT_BY_REVENUE);
        sortKeyMap.put(getActivity().getString(R.string.pref_sort_release_date), TheMovieDbApi.SortOption.SORT_BY_RELEASE_DATE);

        ArrayAdapter<CharSequence> sortAdapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.sort_options, android.R.layout.simple_spinner_item);
        sortAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

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
        Log.d(LOG_TAG, "onStart()");
        updateMovies();
    }

    private void updateMovies() {
        FetchMoviesTask movieTask = new FetchMoviesTask();

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String sort_pref = prefs.getString(getString(R.string.pref_sort_key),
                getString(R.string.pref_sort_popular));

        Log.d(LOG_TAG, "updateMovies(): " + "Selection: " + sort_pref);

        movieTask.execute(sort_pref);
    }

    private class FetchMoviesTask extends AsyncTask<String, Void, ArrayList<Movie>> {

        private final String LOG_TAG = FetchMoviesTask.class.getSimpleName();

        private TheMovieDbApi api = new TheMovieDbApi();

        @Override
        protected ArrayList<Movie> doInBackground(String... params) {

            ArrayList<Movie> movies;

            getActivity().runOnUiThread(new Runnable() {
                public void run() {

                    Toast.makeText(getActivity().getApplicationContext(),
                            "Fetching data...",
                            Toast.LENGTH_SHORT).show();
                }
            });

            TheMovieDbApi.SortOption sortOption = sortKeyMap.get(params[0]);

            Log.d(LOG_TAG, "FetchMoviesTask: " + sortOption);

            movies = api.discoverMovies(
                    sortOption != null ? sortOption : TheMovieDbApi.SortOption.SORT_BY_MOST_POPULAR);

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
    }
}
