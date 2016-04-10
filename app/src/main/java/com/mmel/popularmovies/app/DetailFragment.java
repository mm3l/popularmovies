package com.mmel.popularmovies.app;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.ShareActionProvider;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.VideoView;

import com.mmel.popularmovies.app.api.TheMovieDbApi;
import com.mmel.popularmovies.app.data.Movie;
import com.squareup.picasso.Picasso;

/**
 * It creates a details screen with additional movie information such as:
 * - original title
 * - movie poster image thumbnail
 * - A plot synopsis
 * - user rating
 * - release date
 *
 * @author michael.melachridis@gmail.com
 */
public class DetailFragment extends Fragment {

    private static final String LOG_TAG = DetailFragment.class.getSimpleName();

    public final static String DETAIL_MOVIE_KEY = "movie_info";

    private Movie movie;

    public DetailFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Inflate the menu; this adds items to the action bar if it is present.
        inflater.inflate(R.menu.detail_fragment_menu, menu);

        MenuItem menuItem = menu.findItem(R.id.action_share);
        ShareActionProvider mShareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(menuItem);


        if (mShareActionProvider != null) {
            mShareActionProvider.setShareIntent(createShareForecastIntent());
        } else {
            Log.d(LOG_TAG, "Share Action Provider is null?");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_detail, container, false);

        if (savedInstanceState == null || !savedInstanceState.containsKey(DETAIL_MOVIE_KEY)) {

            Bundle data = getActivity().getIntent().getExtras();

            if (data != null) {
                movie = (Movie) data.getParcelable(DETAIL_MOVIE_KEY);
                Log.d(LOG_TAG, "Movie info: " + movie.toString());
            }

        } else {
            movie = savedInstanceState.getParcelable(DETAIL_MOVIE_KEY);
        }

        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (movie != null) {
            ImageView backDropImgView = (ImageView) getView().findViewById(R.id.backdrop_view);
            Picasso.with(getActivity().getBaseContext())
                    .load(TheMovieDbApi.getImageUrl(movie.getBackdropPath(), Movie.ImageSize.w500))
                            .into(backDropImgView);

            ((TextView) getView().findViewById(R.id.title_view))
                    .setText(movie.getTitle());

            ((TextView) getView().findViewById(R.id.rating_view))
                    .setText("Rating: " + Double.toString(movie.getVoteAverage()));

            ((TextView) getView().findViewById(R.id.release_view))
                    .setText("Released: " + movie.getReleaseDate());

            ImageView posterView = (ImageView) getView().findViewById(R.id.poster_view);
            Picasso.with(getActivity().getBaseContext())
                    .load(TheMovieDbApi.getImageUrl(movie.getPosterPath(), Movie.ImageSize.w92))
                    .into(posterView);

            ((TextView) getView().findViewById(R.id.synopsis_view))
                    .setText(movie.getOverview());


            ((VideoView) getView().findViewById(R.id.trailer_view)).setVideoURI(null);
        }
    }


    private Intent createShareForecastIntent() {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, movie.toString());
        return shareIntent;
    }
}
