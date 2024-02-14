package com.telemedicine.myclinic.models.registermodels;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;
import com.telemedicine.myclinic.models.BaseEntity;
import com.telemedicine.myclinic.models.patientMiniModels.PatientsMiniModel;

import java.util.ArrayList;

public class RegisterTwoModel extends BaseEntity implements Parcelable {

    @SerializedName("ExistingPatients")
    ArrayList<PatientsMiniModel> patientsMiniModels;

    protected RegisterTwoModel(Parcel in) {
        super(in);
        patientsMiniModels = in.createTypedArrayList(PatientsMiniModel.CREATOR);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeTypedList(patientsMiniModels);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<RegisterTwoModel> CREATOR = new Creator<RegisterTwoModel>() {
        @Override
        public RegisterTwoModel createFromParcel(Parcel in) {
            return new RegisterTwoModel(in);
        }

        @Override
        public RegisterTwoModel[] newArray(int size) {
            return new RegisterTwoModel[size];
        }
    };

    public ArrayList<PatientsMiniModel> getPatientsMiniModels() {
        return patientsMiniModels;
    }

    public void setPatientsMiniModels(ArrayList<PatientsMiniModel> patientsMiniModels) {
        this.patientsMiniModels = patientsMiniModels;
    }
}
