package com.telemedicine.myclinic.models.homevisit;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.telemedicine.myclinic.models.BaseEntity;

public class AddressBookCreateResponse extends BaseEntity {
    @SerializedName("OPatientAddressBookId")
    @Expose
    private long oPatientAddressBookId;

    public long getOPatientAddressBookId() {
        return oPatientAddressBookId;
    }

    public void setOPatientAddressBookId(long oPatientAddressBookId) {
        this.oPatientAddressBookId = oPatientAddressBookId;
    }

    protected AddressBookCreateResponse(Parcel in) {
        super(in);
        oPatientAddressBookId = in.readLong();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeLong(oPatientAddressBookId);
    }

    public static final Parcelable.Creator<AddressBookCreateResponse> CREATOR = new Parcelable.Creator<AddressBookCreateResponse>() {
        @Override
        public AddressBookCreateResponse createFromParcel(Parcel in) {
            return new AddressBookCreateResponse(in);
        }

        @Override
        public AddressBookCreateResponse[] newArray(int size) {
            return new AddressBookCreateResponse[size];
        }
    };
}