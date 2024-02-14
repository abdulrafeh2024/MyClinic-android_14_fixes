package com.telemedicine.myclinic.models.patientMiniModels;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class MaritalStatus implements Parcelable {
    @SerializedName("UNKNOWN")
    int UNKNOWN;

    @SerializedName("Single")
    int Single;

    @SerializedName("Married")
    int Married;

    public int getUNKNOWN() {
        return UNKNOWN;
    }

    public void setUNKNOWN(int UNKNOWN) {
        this.UNKNOWN = UNKNOWN;
    }

    public int getSingle() {
        return Single;
    }

    public void setSingle(int single) {
        Single = single;
    }

    public int getMarried() {
        return Married;
    }

    public void setMarried(int married) {
        Married = married;
    }

    protected MaritalStatus(Parcel in) {
        UNKNOWN = in.readInt();
        Single = in.readInt();
        Married = in.readInt();
    }

    public static final Creator<MaritalStatus> CREATOR = new Creator<MaritalStatus>() {
        @Override
        public MaritalStatus createFromParcel(Parcel in) {
            return new MaritalStatus(in);
        }

        @Override
        public MaritalStatus[] newArray(int size) {
            return new MaritalStatus[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(UNKNOWN);
        dest.writeInt(Single);
        dest.writeInt(Married);
    }
}
