package com.neeru.client.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by brajendra on 04/08/17.
 */

public class Review implements Parcelable {


    public int rating;
    public String feedback;
    public int userId;
    public String firstName;
    public String createdAt;
    public String updatedAt;


    protected Review(Parcel in) {
        this.rating = in.readInt();
        this.feedback = in.readString();
        this.firstName = in.readString();
        this.userId = in.readInt();
        this.createdAt = in.readString();
        this.updatedAt = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeInt(rating);
        dest.writeString(feedback);
        dest.writeInt(userId);
        dest.writeString(firstName);
        dest.writeString(createdAt);
        dest.writeString(updatedAt);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Review> CREATOR = new Creator<Review>() {
        @Override
        public Review createFromParcel(Parcel in) {
            return new Review(in);
        }

        @Override
        public Review[] newArray(int size) {
            return new Review[size];
        }
    };
}
