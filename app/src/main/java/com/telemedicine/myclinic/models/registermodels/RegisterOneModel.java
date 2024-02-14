package com.telemedicine.myclinic.models.registermodels;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;
import com.telemedicine.myclinic.models.BaseEntity;

public class RegisterOneModel extends BaseEntity implements Parcelable {

    @SerializedName("VerificationCode")
    String VerificationCode;

    protected RegisterOneModel(Parcel in) {
        VerificationCode = in.readString();
    }

    public static final Creator<RegisterOneModel> CREATOR = new Creator<RegisterOneModel>() {
        @Override
        public RegisterOneModel createFromParcel(Parcel in) {
            return new RegisterOneModel(in);
        }

        @Override
        public RegisterOneModel[] newArray(int size) {
            return new RegisterOneModel[size];
        }
    };

    public String getVerificationCode() {
        return VerificationCode;
    }

    public void setVerificationCode(String verificationCode) {
        VerificationCode = verificationCode;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(VerificationCode);
    }
}
