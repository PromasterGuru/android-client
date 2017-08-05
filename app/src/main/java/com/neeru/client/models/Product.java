package com.neeru.client.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by brajendra on 14/07/17.
 */

public class Product implements Parcelable {

    public int id;
    public String name;
    public int price;
    public String avatar;
    public Seller seller;
    public String address;
    public int capacity;
    public String description;

    public ReviewCount reviews;


    public Product() {

    }


    private Product(Parcel in) {
        this.id = in.readInt();
        this.name = in.readString();
        this.price = in.readInt();
        this.avatar = in.readString();
        this.seller = in.readParcelable(Seller.class.getClassLoader());
        this.address = in.readString();
        this.capacity = in.readInt();
        this.description = in.readString();

        this.reviews = in.readParcelable(ReviewCount.class.getClassLoader());
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(name);
        dest.writeInt(price);
        dest.writeString(avatar);
        dest.writeParcelable(seller, flags);
        dest.writeString(address);
        dest.writeInt(capacity);
        dest.writeString(description);
        dest.writeParcelable(reviews, flags);
    }


    public static final Parcelable.Creator<Product> CREATOR = new Parcelable.Creator<Product>() {

        @Override
        public Product createFromParcel(Parcel source) {
            return new Product(source);
        }

        @Override
        public Product[] newArray(int size) {
            return new Product[size];
        }
    };
}
