package com.telemedicine.myclinic.models.profileUpdate;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class InsuranceUpdateModel implements Parcelable {

    @SerializedName("CarrierName")
    String CarrierName;

    @SerializedName("CarrierId")
    long CarrierId;

    @SerializedName("ContractId")
    long ContractId;

    @SerializedName("ContractNo")
    String ContractNo;

    @SerializedName("ClassPlanId")
    long ClassPlanId;

    @SerializedName("MembershipNo")
    String MembershipNo;

    @SerializedName("ExpiryDate")
    String ExpiryDate;

    @SerializedName("InsuranceCardScanBase64")
    String InsuranceCardScanBase64;

    @SerializedName("Eligible")
    boolean Eligible;

    @SerializedName("NotEligibleDesc")
    String NotEligibleDesc;

    @SerializedName("StartDate")
    String StartDate;

    @SerializedName("MemberName")
    String MemberName;

    @SerializedName("MobileNumber")
    String MobileNumber;

    @SerializedName("Deductible")
    int Deductible;

    @SerializedName("NationalId")
    String NationalId;

    @SerializedName("DateOfBirth")
    String DateOfBirth;

    @SerializedName("ContractName")
    String ContractName;

    @SerializedName("ClassPlan")
    String ClassPlan;

    @SerializedName("Gender")
    String Gender;

    public String getCarrierName() {
        return CarrierName;
    }

    public void setCarrierName(String carrierName) {
        CarrierName = carrierName;
    }

    public long getCarrierId() {
        return CarrierId;
    }

    public void setCarrierId(long carrierId) {
        CarrierId = carrierId;
    }

    public long getContractId() {
        return ContractId;
    }

    public void setContractId(long contractId) {
        ContractId = contractId;
    }

    public String getContractNo() {
        return ContractNo;
    }

    public void setContractNo(String contractNo) {
        ContractNo = contractNo;
    }

    public long getClassPlanId() {
        return ClassPlanId;
    }

    public void setClassPlanId(long classPlanId) {
        ClassPlanId = classPlanId;
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

    public String getInsuranceCardScanBase64() {
        return InsuranceCardScanBase64;
    }

    public void setInsuranceCardScanBase64(String insuranceCardScanBase64) {
        InsuranceCardScanBase64 = insuranceCardScanBase64;
    }

    public boolean isEligible() {
        return Eligible;
    }

    public void setEligible(boolean eligible) {
        Eligible = eligible;
    }

    public String getNotEligibleDesc() {
        return NotEligibleDesc;
    }

    public void setNotEligibleDesc(String notEligibleDesc) {
        NotEligibleDesc = notEligibleDesc;
    }

    public String getStartDate() {
        return StartDate;
    }

    public void setStartDate(String startDate) {
        StartDate = startDate;
    }

    public String getMemberName() {
        return MemberName;
    }

    public void setMemberName(String memberName) {
        MemberName = memberName;
    }

    public String getMobileNumber() {
        return MobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        MobileNumber = mobileNumber;
    }

    public int getDeductible() {
        return Deductible;
    }

    public void setDeductible(int deductible) {
        Deductible = deductible;
    }

    public String getNationalId() {
        return NationalId;
    }

    public void setNationalId(String nationalId) {
        NationalId = nationalId;
    }

    public String getDateOfBirth() {
        return DateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        DateOfBirth = dateOfBirth;
    }

    public String getContractName() {
        return ContractName;
    }

    public void setContractName(String contractName) {
        ContractName = contractName;
    }

    public String getClassPlan() {
        return ClassPlan;
    }

    public void setClassPlan(String classPlan) {
        ClassPlan = classPlan;
    }

    public String getGender() {
        return Gender;
    }

    public void setGender(String gender) {
        Gender = gender;
    }

    protected InsuranceUpdateModel(Parcel in) {
        CarrierName = in.readString();
        CarrierId = in.readLong();
        ContractId = in.readLong();
        ContractNo = in.readString();
        ClassPlanId = in.readLong();
        MembershipNo = in.readString();
        ExpiryDate = in.readString();
        InsuranceCardScanBase64 = in.readString();
        Eligible = in.readByte() != 0;
        NotEligibleDesc = in.readString();
        StartDate = in.readString();
        MemberName = in.readString();
        MobileNumber = in.readString();
        Deductible = in.readInt();
        NationalId = in.readString();
        DateOfBirth = in.readString();
        ContractName = in.readString();
        ClassPlan = in.readString();
        Gender = in.readString();
    }

    public static final Creator<InsuranceUpdateModel> CREATOR = new Creator<InsuranceUpdateModel>() {
        @Override
        public InsuranceUpdateModel createFromParcel(Parcel in) {
            return new InsuranceUpdateModel(in);
        }

        @Override
        public InsuranceUpdateModel[] newArray(int size) {
            return new InsuranceUpdateModel[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(CarrierName);
        dest.writeLong(CarrierId);
        dest.writeLong(ContractId);
        dest.writeString(ContractNo);
        dest.writeLong(ClassPlanId);
        dest.writeString(MembershipNo);
        dest.writeString(ExpiryDate);
        dest.writeString(InsuranceCardScanBase64);
        dest.writeByte((byte) (Eligible ? 1 : 0));
        dest.writeString(NotEligibleDesc);
        dest.writeString(StartDate);
        dest.writeString(MemberName);
        dest.writeString(MobileNumber);
        dest.writeInt(Deductible);
        dest.writeString(NationalId);
        dest.writeString(DateOfBirth);
        dest.writeString(ContractName);
        dest.writeString(ClassPlan);
        dest.writeString(Gender);
    }
}
