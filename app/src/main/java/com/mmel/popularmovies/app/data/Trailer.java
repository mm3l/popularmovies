package com.mmel.popularmovies.app.data;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * It represents a movie trailer
 *
 * @author michael.melachridis@gmail.com
 */
public class Trailer implements Parcelable {

    private int movieID;
    private String trailerId;
    private String key;
    private String name;
    private int size;

    public Trailer(int movieID, String trailerId, String key, String name, int size) {
        this.movieID = movieID;
        this.trailerId = trailerId;
        this.key = key;
        this.name = name;
        this.size = size;
    }

    protected Trailer(Parcel in) {
        movieID = in.readInt();
        trailerId = in.readString();
        key = in.readString();
        name = in.readString();
        size = in.readInt();
    }

    public static final Creator<Trailer> CREATOR = new Creator<Trailer>() {
        @Override
        public Trailer createFromParcel(Parcel in) {
            return new Trailer(in);
        }

        @Override
        public Trailer[] newArray(int size) {
            return new Trailer[size];
        }
    };

    public int getMovieID() {
        return movieID;
    }

    public String getTrailerId() {
        return trailerId;
    }

    public String getKey() {
        return key;
    }

    public String getName() {
        return name;
    }

    public int getSize() {
        return size;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(movieID);
        dest.writeString(trailerId);
        dest.writeString(key);
        dest.writeString(name);
        dest.writeInt(size);
    }

    public String toString() {
        StringBuilder builder = new StringBuilder();

        builder.append("ID: " + getTrailerId() + " \n");
        builder.append("Key: " + getKey() + " \n");
        builder.append("Name: " + getName() + " \n");
        builder.append("Size: " + getSize() + " \n");

        return  builder.toString();
    }
}
