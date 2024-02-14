package com.telemedicine.myclinic.models.profilecreatoinmodels;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class Nationalities implements Parcelable {

    @SerializedName("Id")
    Long Id;

    @SerializedName("NameEn")
    String NameEn;

    @SerializedName("NameAr")
    String NameAr;

    protected Nationalities(Parcel in) {
        if (in.readByte() == 0) {
            Id = null;
        } else {
            Id = in.readLong();
        }
        NameEn = in.readString();
        NameAr = in.readString();
    }

    public static final Creator<Nationalities> CREATOR = new Creator<Nationalities>() {
        @Override
        public Nationalities createFromParcel(Parcel in) {
            return new Nationalities(in);
        }

        @Override
        public Nationalities[] newArray(int size) {
            return new Nationalities[size];
        }
    };

    public Long getId() {
        return Id;
    }

    public void setId(Long id) {
        Id = id;
    }

    public String getNameEn() {
        return NameEn;
    }

    public void setNameEn(String nameEn) {
        NameEn = nameEn;
    }

    public String getNameAr() {
        return NameAr;
    }

    public void setNameAr(String nameAr) {
        NameAr = nameAr;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (Id == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeLong(Id);
        }
        dest.writeString(NameEn);
        dest.writeString(NameAr);
    }
}
