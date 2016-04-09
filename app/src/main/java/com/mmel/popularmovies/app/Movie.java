package com.mmel.popularmovies.app;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * It represents a movie element got from TheMovieDB
 *
 * @author michael.melachridis@gmail.com
 */
public class Movie implements Parcelable {

    private String posterPath;
    private String title;
    private String releaseDate;
    private double voteAverage;
    private String overview;

    public Movie(String posterPath, String title, String releaseDate,
                     double voteAverage, String overview) {
        this.posterPath = posterPath;
        this.title = title;
        this.releaseDate = releaseDate;
        this.voteAverage = voteAverage;
        this.overview = overview;
    }

    protected Movie(Parcel in) {
        posterPath = in.readString();
        title = in.readString();
        releaseDate = in.readString();
        voteAverage = in.readDouble();
        overview = in.readString();
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
        dest.writeString(posterPath);
        dest.writeString(title);
        dest.writeString(releaseDate);
        dest.writeDouble(voteAverage);
        dest.writeString(overview);
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

    public String getPosterPath() {
        return posterPath;
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

    /**
     * It returns a string representation of all local attributes
     * which constitute the Movie Object
     *
     * @return  a string representation of all local attributes
     */
    public String toString() {
        String str = "Poster Path: " + getPosterPath() + " \n" +
                "Original Title: " + getTitle() + " \n" +
                "Release Date: " + getReleaseDate() + " \n" +
                "Average Vote: " + getVoteAverage() + " \n" +
                "Overview: " + getOverview() + " \n";
        return  str;
    }
}
