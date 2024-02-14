package com.telemedicine.myclinic.models.homevisit;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.telemedicine.myclinic.models.BaseEntity;

import java.util.List;

public class PatientAddressBookResponse extends BaseEntity {

    @SerializedName("PatientAddressBookList")
    @Expose
    private List<PatientAddressBookList> PatientAddressBookList = null;

    protected PatientAddressBookResponse(Parcel in) {
        PatientAddressBookList = in.createTypedArrayList(com.telemedicine.myclinic.models.homevisit.PatientAddressBookList.CREATOR);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(PatientAddressBookList);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<PatientAddressBookResponse> CREATOR = new Creator<PatientAddressBookResponse>() {
        @Override
        public PatientAddressBookResponse createFromParcel(Parcel in) {
            return new PatientAddressBookResponse(in);
        }

        @Override
        public PatientAddressBookResponse[] newArray(int size) {
            return new PatientAddressBookResponse[size];
        }
    };

    public List<PatientAddressBookList> getPatientAddressBookList() {
        return PatientAddressBookList;
    }

    public void setPatientAddressBookList(List<PatientAddressBookList> OrdersList) {
        this.PatientAddressBookList = OrdersList;
    }
}
