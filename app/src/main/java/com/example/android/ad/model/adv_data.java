package com.example.android.ad.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;


@Entity(tableName = "ad")
public class adv_data implements Parcelable {

    private String adv_name;
    private String adv_category;
    private String adv_description;
    private String location;
    private String owner_name;
    private String email;
    private String phone;
    private String photoUrl;

    private String userId;
    private String date;
    private double price;
    @PrimaryKey
    @NonNull
    private String key;

    @Ignore
    public adv_data() {

    }


    public adv_data(String adv_name, String adv_category, String adv_description, String location, String email, String owner_name, String phone, String photoUrl, String userId, String date, double price,String key) {
        this.adv_name = adv_name;
        this.adv_category = adv_category;
        this.adv_description = adv_description;
        this.location = location;
        this.email = email;
        this.owner_name = owner_name;
        this.phone = phone;
        this.photoUrl = photoUrl;
        this.userId = userId;
        this.date = date;
        this.price = price;
        this.key=key;


    }

    protected adv_data(Parcel in) {
        adv_name = in.readString();
        adv_category = in.readString();
        adv_description = in.readString();
        location = in.readString();
        email = in.readString();
        owner_name = in.readString();
        phone = in.readString();
        photoUrl = in.readString();
        userId = in.readString();
        date = in.readString();
        price = in.readDouble();
        key=in.readString();


    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getAdv_name() {
        return adv_name;
    }

    public void setAdv_name(String adv_name) {
        this.adv_name = adv_name;
    }

    public String getAdv_category() {
        return adv_category;
    }

    public void setAdv_category(String adv_category) {
        this.adv_category = adv_category;
    }

    public String getAdv_description() {
        return adv_description;
    }

    public void setAdv_description(String adv_description) {
        this.adv_description = adv_description;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getOwner_name() {
        return owner_name;
    }

    public void setOwner_name(String owner_name) {
        this.owner_name = owner_name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }


    @Override
    public int describeContents() {
        return 0;
    }


    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(adv_name);
        dest.writeString(adv_category);
        dest.writeString(adv_description);
        dest.writeString(location);
        dest.writeString(owner_name);
        dest.writeString(email);
        dest.writeString(phone);
        dest.writeString(photoUrl);
        dest.writeString(userId);
        dest.writeString(date);
        dest.writeDouble(price);
        dest.writeString(key);



    }

    public static final Parcelable.Creator<adv_data> CREATOR = new Parcelable.Creator<adv_data>() {
        @Override
        public adv_data createFromParcel(Parcel in) {
            return new adv_data(in);
        }

        @Override
        public adv_data[] newArray(int size) {
            return new adv_data[size];
        }
    };
}
