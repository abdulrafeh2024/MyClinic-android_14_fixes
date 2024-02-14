package com.telemedicine.myclinic.models.patientMiniModels;

import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.RequiresApi;

import com.google.gson.annotations.SerializedName;

public class InsuranceCardModel implements Parcelable {

    @SerializedName("PatientId")
    long PatientId;

    @SerializedName("EPatientId")
    long EPatientId;

    @SerializedName("PatientName")
    String PatientName;

    @SerializedName("IsDTNotEligible")
    boolean IsDTNotEligible;

    @SerializedName("IsNphiesActive")
    boolean IsNphiesActive;

    @SerializedName("InsuranceId")
    long InsuranceId;

    @SerializedName("EInsuranceId")
    long EInsuranceId;

    @SerializedName("InsuranceCarrier")
    String InsuranceCarrier;

    @SerializedName("MembershipNo")
    String MembershipNo;

    @SerializedName("ExpiryDate")
    String ExpiryDate;

    @SerializedName("ContractName")
    String ContractName;

    @SerializedName("ContractNo")
    String ContractNo;

    @SerializedName("CarrierId")
    long CarrierId;

    public InsuranceCardModel() {

    }

    public long getCarrierId() {
        return CarrierId;
    }

    public void setCarrierId(long carrierId) {
        CarrierId = carrierId;
    }

    public long getPatientId() {
        return PatientId;
    }

    public void setPatientId(long patientId) {
        PatientId = patientId;
    }

    public long getEPatientId() {
        return EPatientId;
    }

    public void setEPatientId(long patientId) {
        EPatientId = patientId;
    }


    public String getPatientName() {
        return PatientName;
    }

    public void setPatientName(String patientName) {
        PatientName = patientName;
    }

    public boolean getIsDTNotEligible() {
        return IsDTNotEligible;
    }

    public void setIsDTNotEligible(boolean patientName) {
        IsDTNotEligible = patientName;
    }

    public boolean getIsNphiesActive() {
        return IsNphiesActive;
    }

    public void setIsNphiesActive(boolean patientName) {
        IsNphiesActive = patientName;
    }

    public long getInsuranceId() {
        return InsuranceId;
    }

    public void setInsuranceId(long insuranceId) {
        InsuranceId = insuranceId;
    }

    public long getEInsuranceId() {
        return EInsuranceId;
    }

    public void setEInsuranceId(long insuranceId) {
        EInsuranceId = insuranceId;
    }


    public String getInsuranceCarrier() {
        return InsuranceCarrier;
    }

    public void setInsuranceCarrier(String insuranceCarrier) {
        InsuranceCarrier = insuranceCarrier;
    }

    public String getMembershipNo() {
        return MembershipNo;
    }

    public void setMembershipNo(String membershipNo) {
        MembershipNo = membershipNo;
    }

    public String getExpiryDate() {
        return ExpiryDate;
    }

    public void setExpiryDate(String expiryDate) {
        ExpiryDate = expiryDate;
    }

    public String getContractName() {
        return ContractName;
    }

    public void setContractName(String contractName) {
        ContractName = contractName;
    }

    public String getContractNo() {
        return ContractNo;
    }

    public void setContractNo(String contractNo) {
        ContractNo = contractNo;
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    protected InsuranceCardModel(Parcel in) {
        PatientId = in.readLong();
        PatientName = in.readString();
        EPatientId = in.readLong();
        EInsuranceId = in.readLong();
        IsDTNotEligible = in.readByte() != 0;
        IsNphiesActive = in.readByte() != 0;
        InsuranceId = in.readLong();
        InsuranceCarrier = in.readString();
        MembershipNo = in.readString();
        ExpiryDate = in.readString();
        ContractName = in.readString();
        ContractNo = in.readString();
        CarrierId = in.readLong();
    }

    public static final Creator<InsuranceCardModel> CREATOR = new Creator<InsuranceCardModel>() {
        @RequiresApi(api = Build.VERSION_CODES.Q)
        @Override
        public InsuranceCardModel createFromParcel(Parcel in) {
            return new InsuranceCardModel(in);
        }

        @Override
        public InsuranceCardModel[] newArray(int size) {
            return new InsuranceCardModel[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(PatientId);
        dest.writeString(PatientName);
        dest.writeLong(EPatientId);
        dest.writeLong(EInsuranceId);
        dest.writeByte(IsDTNotEligible ? (byte) 1 : (byte) 0);
        dest.writeByte(IsNphiesActive ? (byte) 1 : (byte) 0);
        dest.writeLong(InsuranceId);
        dest.writeString(InsuranceCarrier);
        dest.writeString(MembershipNo);
        dest.writeString(ExpiryDate);
        dest.writeString(ContractName);
        dest.writeString(ContractNo);
        dest.writeLong(CarrierId);
    }
}
