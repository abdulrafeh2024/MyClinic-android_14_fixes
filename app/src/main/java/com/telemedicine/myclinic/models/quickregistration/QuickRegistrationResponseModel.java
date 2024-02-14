package com.telemedicine.myclinic.models.quickregistration;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;
import com.telemedicine.myclinic.models.BaseEntity;
import com.telemedicine.myclinic.models.patientMiniModels.PatientsMiniModel;

import java.util.ArrayList;

public class QuickRegistrationResponseModel extends BaseEntity implements Parcelable {

    @SerializedName("VerificationCode")
    String VerificationCode;

    @SerializedName("PatientId")
    String PatientId;

    @SerializedName("ApptNo")
    String ApptNo;

    @SerializedName("ApptId")
    String ApptId;


    protected QuickRegistrationResponseModel(Parcel in) {
        super(in);
        VerificationCode = in.readString();
        PatientId = in.readString();
        ApptNo = in.readString();
        ApptId = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeString(VerificationCode);
        dest.writeString(PatientId);
        dest.writeString(ApptNo);
        dest.writeString(ApptId);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<QuickRegistrationResponseModel> CREATOR = new Creator<QuickRegistrationResponseModel>() {
        @Override
        public QuickRegistrationResponseModel createFromParcel(Parcel in) {
            return new QuickRegistrationResponseModel(in);
        }

        @Override
        public QuickRegistrationResponseModel[] newArray(int size) {
            return new QuickRegistrationResponseModel[size];
        }
    };

    public String getVerificationCode() {
        return VerificationCode;
    }

    public void setVerificationCode(String verificationCode) {
        VerificationCode = verificationCode;
    }

    public String getPatientId() {
        return PatientId;
    }

    public void setPatientId(String patientId) {
        PatientId = patientId;
    }

    public String getApptNo() {
        return ApptNo;
    }

    public void setApptNo(String apptNo) {
        ApptNo = apptNo;
    }

    public String getApptId() {
        return ApptId;
    }

    public void setApptId(String apptId) {
        ApptId = apptId;
    }


}
