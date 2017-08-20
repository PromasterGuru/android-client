package com.neeru.client.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by brajendra on 13/07/17.
 */

public class Address implements Parcelable {
    public String fullName;
    public String address;
    public String line1;
    public String line2;
    public String landMark;


    public Address(){

    }

    protected Address(Parcel in) {
        fullName = in.readString();
        address = in.readString();
        line1 = in.readString();
        line2 = in.readString();
        landMark = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(fullName);
        dest.writeString(address);
        dest.writeString(line1);
        dest.writeString(line2);
        dest.writeString(landMark);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Address> CREATOR = new Creator<Address>() {
        @Override
        public Address createFromParcel(Parcel in) {
            return new Address(in);
        }

        @Override
        public Address[] newArray(int size) {
            return new Address[size];
        }
    };
}
