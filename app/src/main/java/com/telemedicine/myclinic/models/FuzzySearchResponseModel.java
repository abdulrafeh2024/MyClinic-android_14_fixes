package com.telemedicine.myclinic.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;
import com.telemedicine.myclinic.models.appointments.ApptsMiniModel;

import java.util.ArrayList;

public class FuzzySearchResponseModel extends BaseEntity {

    @SerializedName("FuzzySearchResponse")
    ArrayList<FuzzySearchMinModel> fuzzySearchList;

    public ArrayList<FuzzySearchMinModel> getFuzzySearchList() {
        return fuzzySearchList;
    }

    public void setFuzzySearchList(ArrayList<FuzzySearchMinModel> fuzzySearchList) {
        this.fuzzySearchList = fuzzySearchList;
    }

    public FuzzySearchResponseModel() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    protected FuzzySearchResponseModel(Parcel in) {
        super(in);
        fuzzySearchList = in.createTypedArrayList(FuzzySearchMinModel.CREATOR);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
      //  super.writeToParcel(dest, flags);
        dest.writeTypedList(fuzzySearchList);
    }

    public static final Creator<FuzzySearchResponseModel> CREATOR = new Creator<FuzzySearchResponseModel>() {
        @Override
        public FuzzySearchResponseModel createFromParcel(Parcel in) {
            return new FuzzySearchResponseModel(in);
        }

        @Override
        public FuzzySearchResponseModel[] newArray(int size) {
            return new FuzzySearchResponseModel[size];
        }
    };
}
