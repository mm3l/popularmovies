package com.mmel.popularmovies.app.data;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * It represents a movie element got from TheMovieDB
 *
 * @author michael.melachridis@gmail.com
 */
public class Movie implements Parcelable {

    private int id;
    private String posterPath;
    private String backdrop_path;
    private String title;
    private String releaseDate;
    private double voteAverage;
    private String overview;

    private ArrayList<Trailer> trailers = new ArrayList<Trailer>();

    public Movie(int id, String posterPath, String backdrop_path, String title, String releaseDate,
                 double voteAverage, String overview) {
        this.id = id;
        this.posterPath = posterPath;
        this.backdrop_path = backdrop_path;
        this.title = title;
        this.releaseDate = releaseDate;
        this.voteAverage = voteAverage;
        this.overview = overview;
    }

    protected Movie(Parcel in) {
        id = in.readInt();
        posterPath = in.readString();
        backdrop_path = in.readString();
        title = in.readString();
        releaseDate = in.readString();
        voteAverage = in.readDouble();
        overview = in.readString();

        trailers = in.createTypedArrayList(Trailer.CREATOR);
    }

    public static final Creator<Movie> CREATOR = new Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(posterPath);
        dest.writeString(backdrop_path);
        dest.writeString(title);
        dest.writeString(releaseDate);
        dest.writeDouble(voteAverage);
        dest.writeString(overview);
        dest.writeTypedList(trailers);
    }

    public enum ImageSize {
        w92 ("w92"),
        w154 ("w154"),
        w185 ("w185"),
        w342 ("w342"),
        w500 ("w500"),
        w780 ("w780"),
        original ("original");
        private String mString;
        ImageSize (String string) {
            mString = string;
        }
        public String toString() {
            return mString;
        }
    }

    public int getId() { return id; }

    public String getPosterPath() {
        return posterPath;
    }

    public String getBackdropPath() {
        return backdrop_path;
    }

    public String getTitle() {
        return title;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public double getVoteAverage() {
        return voteAverage;
    }

    public String getOverview() {
        return overview;
    }

    public List<Trailer> getTrailers() { return trailers; }

    public void setTrailers(ArrayList<Trailer> trailers) { this.trailers = trailers; }


    /**
     * It returns a string representation of all local attributes
     * which constitute the Movie Object
     *
     * @return  a string representation of all local attributes
     */
    public String toString() {
        StringBuilder builder = new StringBuilder();

        builder.append("ID: " + getId() + " \n");
        builder.append("Poster Path: " + getPosterPath() + " \n");
        builder.append("BackDrop Path: " + getBackdropPath() + " \n");
        builder.append("Original Title: " + getTitle() + " \n");
        builder.append("Release Date: " + getReleaseDate() + " \n");
        builder.append("Average Vote: " + getVoteAverage() + " \n");
        builder.append("Overview: " + getOverview() + " \n");
        builder.append("Videos: ");

        for(int i = 0; i < getTrailers().size(); i++) {
            builder.append(trailers.get(i).toString() + " \n");
        }
        return  builder.toString();
    }
}
