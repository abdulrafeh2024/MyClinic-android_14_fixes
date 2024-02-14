package com.telemedicine.myclinic.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;
import com.telemedicine.myclinic.models.homevisit.AddressBookCreateResponse;

public class BaseEntity implements Parcelable {

    @SerializedName("MobileOpResult")
    MobileOpResult mobileOpResult;

    protected BaseEntity(Parcel in) {
        mobileOpResult = in.readParcelable(MobileOpResult.class.getClassLoader());
    }

    public MobileOpResult getMobileOpResult() {
        return mobileOpResult;
    }

    public void setMobileOpResult(MobileOpResult mobileOpResult) {
        this.mobileOpResult = mobileOpResult;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(mobileOpResult, flags);
    }

    public BaseEntity() {
    }
}