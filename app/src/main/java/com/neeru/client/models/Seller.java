package com.neeru.client.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by brajendra on 03/08/17.
 */

public class Seller implements Parcelable {


    public int id;
    public String avatar;
    public String contact;
    public String firstName;
    public String lastName;
    public String description;


    private Seller(Parcel in) {
        this.id = in.readInt();
        this.avatar = in.readString();
        this.contact = in.readString();
        this.firstName = in.readString();
        this.lastName = in.readString();
        this.description = in.readString();
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(avatar);
        dest.writeString(contact);
        dest.writeString(firstName);
        dest.writeString(lastName);
        dest.writeString(description);
    }


    public static final Parcelable.Creator<Seller> CREATOR = new Parcelable.Creator<Seller>() {

        @Override
        public Seller createFromParcel(Parcel source) {
            return new Seller(source);
        }

        @Override
        public Seller[] newArray(int size) {
            return new Seller[size];
        }
    };


}
