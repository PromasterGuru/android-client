package com.neeru.client.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by brajendra on 02/08/17.
 */

public class Location implements Parcelable {

    public int id;
    public String name;
    public String description;
    public String city;
    public String createdAt;
    public String updatedAt;


    private Location(Parcel in) {
        this.id = in.readInt();
        this.name = in.readString();
        this.description = in.readString();
        this.city = in.readString();
        this.createdAt = in.readString();
        this.updatedAt = in.readString();
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeInt(id);
        dest.writeString(name);
        dest.writeString(description);
        dest.writeString(city);
        dest.writeString(createdAt);
        dest.writeString(updatedAt);
    }


    @Override
    public String toString() {
        return name;
    }


    public static final Parcelable.Creator<Location> CREATOR = new Parcelable.Creator<Location>() {

        @Override
        public Location createFromParcel(Parcel source) {
            return new Location(source);
        }

        @Override
        public Location[] newArray(int size) {
            return new Location[size];
        }
    };
}
