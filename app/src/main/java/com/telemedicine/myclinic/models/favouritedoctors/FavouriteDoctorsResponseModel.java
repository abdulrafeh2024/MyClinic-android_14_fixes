package com.telemedicine.myclinic.models.favouritedoctors;

import android.os.Parcel;

import com.google.gson.annotations.SerializedName;
import com.telemedicine.myclinic.models.BaseEntity;
import com.telemedicine.myclinic.models.FuzzySearchMinModel;
import com.telemedicine.myclinic.models.FuzzySearchResponseModel;

import java.util.ArrayList;

public class FavouriteDoctorsResponseModel extends BaseEntity {

    @SerializedName("OPtFavDocsList")
    ArrayList<FavouriteDoctorsMiniModel> favouriteDoctorsMiniModels;

    public ArrayList<FavouriteDoctorsMiniModel> getFavouriteDoctorsMiniModels() {
        return favouriteDoctorsMiniModels;
    }

    public void setFavouriteDoctorsMiniModels(ArrayList<FavouriteDoctorsMiniModel> favouriteDoctorsMiniModels) {
        this.favouriteDoctorsMiniModels = favouriteDoctorsMiniModels;
    }

    public FavouriteDoctorsResponseModel() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    protected FavouriteDoctorsResponseModel(Parcel in) {
        super(in);
        favouriteDoctorsMiniModels = in.createTypedArrayList(FavouriteDoctorsMiniModel.CREATOR);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        //  super.writeToParcel(dest, flags);
        dest.writeTypedList(favouriteDoctorsMiniModels);
    }

    public static final Creator<FavouriteDoctorsResponseModel> CREATOR = new Creator<FavouriteDoctorsResponseModel>() {
        @Override
        public FavouriteDoctorsResponseModel createFromParcel(Parcel in) {
            return new FavouriteDoctorsResponseModel(in);
        }

        @Override
        public FavouriteDoctorsResponseModel[] newArray(int size) {
            return new FavouriteDoctorsResponseModel[size];
        }
    };
}
