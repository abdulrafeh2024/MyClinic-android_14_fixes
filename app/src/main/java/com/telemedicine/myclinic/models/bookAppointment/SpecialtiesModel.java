package com.telemedicine.myclinic.models.bookAppointment;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class SpecialtiesModel implements Parcelable {

    @SerializedName("Id")
    long Id;

    @SerializedName("NameEn")
    String NameEn;

    @SerializedName("NameAr")
    String NameAr;

    protected SpecialtiesModel(Parcel in) {
        Id = in.readLong();
        NameEn = in.readString();
        NameAr = in.readString();
    }

    public static final Creator<SpecialtiesModel> CREATOR = new Creator<SpecialtiesModel>() {
        @Override
        public SpecialtiesModel createFromParcel(Parcel in) {
            return new SpecialtiesModel(in);
        }

        @Override
        public SpecialtiesModel[] newArray(int size) {
            return new SpecialtiesModel[size];
        }
    };

    public long getId() {
        return Id;
    }

    public void setId(long id) {
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
        dest.writeLong(Id);
        dest.writeString(NameEn);
        dest.writeString(NameAr);
    }
}
