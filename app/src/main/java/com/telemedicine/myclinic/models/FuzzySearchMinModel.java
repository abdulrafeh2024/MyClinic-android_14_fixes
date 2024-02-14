package com.telemedicine.myclinic.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class FuzzySearchMinModel implements Parcelable {

    @SerializedName("Name")
    String Name;

    @SerializedName("ArabicName")
    String ArabicName;

    @SerializedName("Description")
    String Description;

    @SerializedName("Id")
    String Id;

    @SerializedName("SpecialtyId")
    String SpecialtyId;

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getSpecialtyId() {
        return SpecialtyId;
    }

    public void setSpecialtyId(String specialtyId) {
        SpecialtyId = specialtyId;
    }


    public String getArabicName() {
        return ArabicName;
    }

    public void setArabicName(String arabicName) {
        ArabicName = arabicName;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public FuzzySearchMinModel() {
    }

    protected FuzzySearchMinModel(Parcel in) {
        Name = in.readString();
        ArabicName = in.readString();
        Description = in.readString();
        Id = in.readString();
    }

    public static final Creator<FuzzySearchMinModel> CREATOR = new Creator<FuzzySearchMinModel>() {
        @Override
        public FuzzySearchMinModel createFromParcel(Parcel in) {
            return new FuzzySearchMinModel(in);
        }

        @Override
        public FuzzySearchMinModel[] newArray(int size) {
            return new FuzzySearchMinModel[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(Name);
        dest.writeString(ArabicName);
        dest.writeString(Description);
        dest.writeString(Id);
    }
}
