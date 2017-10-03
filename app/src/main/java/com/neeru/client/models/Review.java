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
    public int productId;

    public Review() {
    }

    protected Review(Parcel in) {
        rating = in.readInt();
        feedback = in.readString();
        userId = in.readInt();
        firstName = in.readString();
        createdAt = in.readString();
        updatedAt = in.readString();
        productId = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(rating);
        dest.writeString(feedback);
        dest.writeInt(userId);
        dest.writeString(firstName);
        dest.writeString(createdAt);
        dest.writeString(updatedAt);
        dest.writeInt(productId);
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



