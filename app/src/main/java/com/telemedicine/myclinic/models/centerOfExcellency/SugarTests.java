package com.telemedicine.myclinic.models.centerOfExcellency;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class SugarTests implements Parcelable {

    @SerializedName("ODMPSugarTestId")
    String ODMPSugarTestId;

    @SerializedName("PatientId")
    long PatientId;

    @SerializedName("Result")
    int Result;

    @SerializedName("CreateStamp")
    String CreateStamp;


    public SugarTests() {


    }

    protected SugarTests(Parcel in) {
        ODMPSugarTestId = in.readString();
        PatientId = in.readLong();
        Result = in.readInt();
        CreateStamp = in.readString();
    }

    public static final Creator<SugarTests> CREATOR = new Creator<SugarTests>() {
        @Override
        public SugarTests createFromParcel(Parcel in) {
            return new SugarTests(in);
        }

        @Override
        public SugarTests[] newArray(int size) {
            return new SugarTests[size];
        }
    };

    public String getODMPSugarTestId() {
        return ODMPSugarTestId;
    }

    public void setODMPSugarTestId(String ODMPSugarTestId) {
        this.ODMPSugarTestId = ODMPSugarTestId;
    }

    public long getPatientId() {
        return PatientId;
    }

    public void setPatientId(long patientId) {
        PatientId = patientId;
    }

    public int getResult() {
        return Result;
    }

    public void setResult(int result) {
        Result = result;
    }

    public String getCreateStamp() {
        return CreateStamp;
    }

    public void setCreateStamp(String createStamp) {
        CreateStamp = createStamp;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(ODMPSugarTestId);
        dest.writeLong(PatientId);
        dest.writeInt(Result);
        dest.writeString(CreateStamp);
    }
}
