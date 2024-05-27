package com.azyu.shop.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ModelM implements Parcelable, Serializable {
    @SerializedName("id")
    @Expose
    int id;

    @SerializedName("title")
    @Expose
    String title;

    @SerializedName("description")
    @Expose
    String description;

    @SerializedName("image")
    @Expose
    String image;

    @SerializedName("price")
    @Expose
    Double price;

    public ModelM(int id, String title, String description, String image, Double price) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.image = image;
        this.price = price;
    }

    protected ModelM(Parcel in) {
        id = in.readInt();
        title = in.readString();
        description = in.readString();
        image = in.readString();
        if (in.readByte() == 0) {
            price = null;
        } else {
            price = in.readDouble();
        }
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(title);
        dest.writeString(description);
        dest.writeString(image);
        if (price == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeDouble(price);
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<ModelM> CREATOR = new Creator<ModelM>() {
        @Override
        public ModelM createFromParcel(Parcel in) {
            return new ModelM(in);
        }

        @Override
        public ModelM[] newArray(int size) {
            return new ModelM[size];
        }
    };

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getImage() {
        return image;
    }

    public Double getPrice() {
        return price;
    }
}
