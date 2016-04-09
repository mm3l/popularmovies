package com.mmel.popularmovies.app;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.mmel.popularmovies.app.api.TheMovieDbApi;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * It displays thumbnail images from fetched bitmaps
 *
 * @author michael.melachridis@gmail.com
 */
public class CustomArrayAdapter extends ArrayAdapter<Movie> {

    private static final String LOG_TAG = CustomArrayAdapter.class.getSimpleName();

    private Context context;
    private ArrayList<Movie> movies;

    public CustomArrayAdapter(Context context, int resourceId,
                              List<Movie> items) {
        super(context, resourceId, items);
        this.context = context;
        this.movies = (ArrayList<Movie>) items;
    }

    /**
     * A View holder class
     */
    private class ViewHolder {
        ImageView imageView;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        Movie thumb = getItem(position);

        LayoutInflater mInflater = (LayoutInflater) context
                .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.grid_item_movie, null);
            holder = new ViewHolder();

            holder.imageView = (ImageView) convertView.findViewById(R.id.thumb);

            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        //Load Image into the ImageView
        Picasso.with(context)
                .load(TheMovieDbApi.getImageUrl(thumb.getPosterPath(), Movie.ImageSize.w154))
                .into(holder.imageView);

        return convertView;
    }

    @Override
    public int getCount() {
        return super.getCount();
    }

    @Override
    public int getPosition(Movie item) {
        return super.getPosition(item);
    }
}
