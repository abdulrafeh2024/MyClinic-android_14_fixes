package com.telemedicine.myclinic.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;
import com.telemedicine.myclinic.models.appointments.ApptsMiniModel;

import java.util.ArrayList;


public class ApptValidationResponseModel extends BaseEntity {

    @SerializedName("ApptMinis")
    ApptsMiniValidationModel apptsMiniModel;

    protected ApptValidationResponseModel(Parcel in) {
        apptsMiniModel = in.readParcelable(ApptsMiniModel.class.getClassLoader());
    }

    public static final Creator<ApptValidationResponseModel> CREATOR = new Creator<ApptValidationResponseModel>() {
        @Override
        public ApptValidationResponseModel createFromParcel(Parcel in) {
            return new ApptValidationResponseModel(in);
        }

        @Override
        public ApptValidationResponseModel[] newArray(int size) {
            return new ApptValidationResponseModel[size];
        }
    };

    public ApptsMiniValidationModel getApptsMiniModel() {
        return apptsMiniModel;
    }

    public void setApptsMiniModel(ApptsMiniValidationModel apptsMiniModel) {
        this.apptsMiniModel = apptsMiniModel;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

    }
}
