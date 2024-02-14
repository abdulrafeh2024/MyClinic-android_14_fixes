package com.telemedicine.myclinic.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class SendRadiologyResponseModel extends BaseEntity{
    @SerializedName("success")
    boolean success;


    public boolean getSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public SendRadiologyResponseModel() {
    }

    protected SendRadiologyResponseModel(Parcel in) {
        success = in.readByte() != 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByte((byte) (success ? 1 : 0));
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<SendRadiologyResponseModel> CREATOR = new Creator<SendRadiologyResponseModel>() {
        @Override
        public SendRadiologyResponseModel createFromParcel(Parcel in) {
            return new SendRadiologyResponseModel(in);
        }

        @Override
        public SendRadiologyResponseModel[] newArray(int size) {
            return new SendRadiologyResponseModel[size];
        }
    };
}
