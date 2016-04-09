package com.mmel.popularmovies.app;

/**
 * It represents a movie element got from TheMovieDB
 *
 * @author michael.melachridis@gmail.com
 */
public class Movie {

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
}
