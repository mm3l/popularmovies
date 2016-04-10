package com.mmel.popularmovies.app.data;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * It represents a movie element got from TheMovieDB
 *
 * @author michael.melachridis@gmail.com
 */
public class Movie implements Parcelable {

    private String posterPath;
    private String backdrop_path;
    private String title;
    private String releaseDate;
    private double voteAverage;
    private String overview;
    private String videos;

    public Movie(String posterPath, String backdrop_path, String title, String releaseDate,
                 double voteAverage, String overview /*String videos*/) {
        this.posterPath = posterPath;
        this.backdrop_path = backdrop_path;
        this.title = title;
        this.releaseDate = releaseDate;
        this.voteAverage = voteAverage;
        this.overview = overview;
        this.videos = "";
    }

    protected Movie(Parcel in) {
        posterPath = in.readString();
        backdrop_path = in.readString();
        title = in.readString();
        releaseDate = in.readString();
        voteAverage = in.readDouble();
        overview = in.readString();
        videos = in.readString();
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
        dest.writeString(backdrop_path);
        dest.writeString(title);
        dest.writeString(releaseDate);
        dest.writeDouble(voteAverage);
        dest.writeString(overview);
        dest.writeString(videos);
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

    public String getVideos() { return  videos; }

    /**
     * It returns a string representation of all local attributes
     * which constitute the Movie Object
     *
     * @return  a string representation of all local attributes
     */
    public String toString() {
        String str = "Poster Path: " + getPosterPath() + " \n" +
                "BackDrop Path: " + getBackdropPath() + " \n" +
                "Original Title: " + getTitle() + " \n" +
                "Release Date: " + getReleaseDate() + " \n" +
                "Average Vote: " + getVoteAverage() + " \n" +
                "Overview: " + getOverview() + " \n" +
                "Videos: " + getVideos() + " \n";
        return  str;
    }
}
