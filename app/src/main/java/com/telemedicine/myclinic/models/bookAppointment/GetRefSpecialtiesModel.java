package com.telemedicine.myclinic.models.bookAppointment;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;
import com.telemedicine.myclinic.models.BaseEntity;

import java.util.ArrayList;

public class GetRefSpecialtiesModel extends BaseEntity implements Parcelable {

    @SerializedName("Specialties")
    ArrayList<SpecialtiesModel> SpecialtiesList;

    protected GetRefSpecialtiesModel(Parcel in) {
        super(in);
        SpecialtiesList = in.createTypedArrayList(SpecialtiesModel.CREATOR);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeTypedList(SpecialtiesList);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<GetRefSpecialtiesModel> CREATOR = new Creator<GetRefSpecialtiesModel>() {
        @Override
        public GetRefSpecialtiesModel createFromParcel(Parcel in) {
            return new GetRefSpecialtiesModel(in);
        }

        @Override
        public GetRefSpecialtiesModel[] newArray(int size) {
            return new GetRefSpecialtiesModel[size];
        }
    };

    public ArrayList<SpecialtiesModel> getSpecialtiesList() {
        return SpecialtiesList;
    }

    public void setSpecialtiesList(ArrayList<SpecialtiesModel> specialtiesList) {
        SpecialtiesList = specialtiesList;
    }
}
