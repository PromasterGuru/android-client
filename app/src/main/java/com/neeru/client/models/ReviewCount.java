package com.neeru.client.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by brajendra on 04/08/17.
 */

public class ReviewCount implements Parcelable {
    public int count;
    public float rating;

    protected ReviewCount(Parcel in) {
        count = in.readInt();
        rating = in.readFloat();
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(count);
        dest.writeFloat(rating);
    }

    public static final Creator<ReviewCount> CREATOR = new Creator<ReviewCount>() {
        @Override
        public ReviewCount createFromParcel(Parcel in) {
            return new ReviewCount(in);
        }

        @Override
        public ReviewCount[] newArray(int size) {
            return new ReviewCount[size];
        }
    };

}
