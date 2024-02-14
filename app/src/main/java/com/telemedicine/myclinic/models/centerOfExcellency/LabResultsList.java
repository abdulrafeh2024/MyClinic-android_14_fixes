package com.telemedicine.myclinic.models.centerOfExcellency;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class LabResultsList implements Parcelable {

    @SerializedName("LabOrderSyncResultId")
    long LabOrderSyncResultId;

    @SerializedName("LabOrderSyncId")
    long LabOrderSyncId;

    @SerializedName("TestCode")
    String TestCode;

    @SerializedName("TestName")
    String TestName;

    @SerializedName("RefRange")
    String RefRange;

    @SerializedName("NonNumRange")
    String NonNumRange;

    @SerializedName("ReviewDate")
    String ReviewDate;

    @SerializedName("Result")
    double Result;

    public LabResultsList() {

    }

    protected LabResultsList(Parcel in) {
        LabOrderSyncResultId = in.readLong();
        LabOrderSyncId = in.readLong();
        TestCode = in.readString();
        TestName = in.readString();
        RefRange = in.readString();
        NonNumRange = in.readString();
        ReviewDate = in.readString();
        Result = in.readDouble();
    }

    public static final Creator<LabResultsList> CREATOR = new Creator<LabResultsList>() {
        @Override
        public LabResultsList createFromParcel(Parcel in) {
            return new LabResultsList(in);
        }

        @Override
        public LabResultsList[] newArray(int size) {
            return new LabResultsList[size];
        }
    };

    public long getLabOrderSyncResultId() {
        return LabOrderSyncResultId;
    }

    public void setLabOrderSyncResultId(long labOrderSyncResultId) {
        LabOrderSyncResultId = labOrderSyncResultId;
    }

    public long getLabOrderSyncId() {
        return LabOrderSyncId;
    }

    public void setLabOrderSyncId(long labOrderSyncId) {
        LabOrderSyncId = labOrderSyncId;
    }

    public String getTestCode() {
        return TestCode;
    }

    public void setTestCode(String testCode) {
        TestCode = testCode;
    }

    public String getTestName() {
        return TestName;
    }

    public void setTestName(String testName) {
        TestName = testName;
    }

    public String getRefRange() {
        return RefRange;
    }

    public void setRefRange(String refRange) {
        RefRange = refRange;
    }

    public String getNonNumRange() {
        return NonNumRange;
    }

    public void setNonNumRange(String nonNumRange) {
        NonNumRange = nonNumRange;
    }

    public String getReviewDate() {
        return ReviewDate;
    }

    public void setReviewDate(String reviewDate) {
        ReviewDate = reviewDate;
    }

    public double getResult() {
        return Result;
    }

    public void setResult(double result) {
        Result = result;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(LabOrderSyncResultId);
        dest.writeLong(LabOrderSyncId);
        dest.writeString(TestCode);
        dest.writeString(TestName);
        dest.writeString(RefRange);
        dest.writeString(NonNumRange);
        dest.writeString(ReviewDate);
        dest.writeDouble(Result);
    }
}
