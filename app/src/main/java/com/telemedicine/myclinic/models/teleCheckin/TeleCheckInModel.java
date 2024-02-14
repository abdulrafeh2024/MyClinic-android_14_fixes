package com.telemedicine.myclinic.models.teleCheckin;

import android.os.Parcel;

import com.google.gson.annotations.SerializedName;
import com.telemedicine.myclinic.models.BaseEntity;
import com.telemedicine.myclinic.models.MobileOpResult;

public class TeleCheckInModel extends BaseEntity {

    @SerializedName("VSeeUsername")
    String VSeeUsername;

    public String getVSeeUsername() {
        return VSeeUsername;
    }

    public void setVSeeUsername(String VSeeUsername) {
        this.VSeeUsername = VSeeUsername;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeString(this.VSeeUsername);
    }

    public TeleCheckInModel() {
    }

    protected TeleCheckInModel(Parcel in) {
        super(in);
        this.VSeeUsername = in.readString();
    }

    public static final Creator<TeleCheckInModel> CREATOR = new Creator<TeleCheckInModel>() {
        @Override
        public TeleCheckInModel createFromParcel(Parcel source) {
            return new TeleCheckInModel(source);
        }

        @Override
        public TeleCheckInModel[] newArray(int size) {
            return new TeleCheckInModel[size];
        }
    };
}
