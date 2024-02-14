package com.telemedicine.myclinic.models.profilecreatoinmodels;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class InsuranceDTO implements Parcelable {

    @SerializedName("ContractId")
    String ContractId;

    @SerializedName("MembershipNo")
    String MembershipNo;

    @SerializedName("InsuranceCardScanBase64")
    String InsuranceCardScanBase64;

    @SerializedName("ExpiryDate")
    String ExpiryDate;

    @SerializedName("ClassPlanId")
    String ClassPlanId;

    @SerializedName("CarrierName")
    String CarrierName;

    @SerializedName("CarrierId")
    String CarrierId;

    public InsuranceDTO(String contractId, String membershipNo, String insuranceCardScanBase64, String expiryDate, String classPlanId, String carrierName, String carrierId) {
        ContractId = contractId;
        MembershipNo = membershipNo;
        InsuranceCardScanBase64 = insuranceCardScanBase64;
        ExpiryDate = expiryDate;
        ClassPlanId = classPlanId;
        CarrierName = carrierName;
        CarrierId = carrierId;
    }

    protected InsuranceDTO(Parcel in) {
        ContractId = in.readString();
        MembershipNo = in.readString();
        InsuranceCardScanBase64 = in.readString();
        ExpiryDate = in.readString();
        ClassPlanId = in.readString();
        CarrierName = in.readString();
        CarrierId = in.readString();
    }

    public static final Creator<InsuranceDTO> CREATOR = new Creator<InsuranceDTO>() {
        @Override
        public InsuranceDTO createFromParcel(Parcel in) {
            return new InsuranceDTO(in);
        }

        @Override
        public InsuranceDTO[] newArray(int size) {
            return new InsuranceDTO[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(ContractId);
        dest.writeString(MembershipNo);
        dest.writeString(InsuranceCardScanBase64);
        dest.writeString(ExpiryDate);
        dest.writeString(ClassPlanId);
        dest.writeString(CarrierName);
        dest.writeString(CarrierId);
    }
}
