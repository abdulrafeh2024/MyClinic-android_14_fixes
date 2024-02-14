package com.telemedicine.myclinic.models.patientMiniModels;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class PatientsMiniModel implements Parcelable {

    @SerializedName("PatientId")
    long PatientId;

    @SerializedName("NameEn")
    String NameEn;

    @SerializedName("NameAr")
    String NameAr;

    @SerializedName("Gender")
    String Gender;

    @SerializedName("BirthDate")
    String BirthDate;

    @SerializedName("Mobile")
    String Mobile;

    @SerializedName("MaritalStatus")
    int MaritalStatus;

    @SerializedName("MRN")
    String MRN;

    @SerializedName("RegCardUrl")
    String RegCardUrl;



    @SerializedName("PatientCategory")
    int PatientCategory;


    public PatientsMiniModel() {
    }

    public int getPatientCategory() {
        return PatientCategory;
    }

    public void setPatientCategory(int patientCategory) {
        PatientCategory = patientCategory;
    }

    @SerializedName("InsuranceCards")
    ArrayList<InsuranceCardModel> InsuranceCards;

    protected PatientsMiniModel(Parcel in) {
        PatientId = in.readLong();
        RegCardUrl = in.readString();
        NameEn = in.readString();
        NameAr = in.readString();
        Gender = in.readString();
        BirthDate = in.readString();
        Mobile = in.readString();
        MaritalStatus = in.readInt();
        MRN = in.readString();
        InsuranceCards = in.createTypedArrayList(InsuranceCardModel.CREATOR);
        PatientCategory = in.readInt();

    }

    public static final Creator<PatientsMiniModel> CREATOR = new Creator<PatientsMiniModel>() {
        @Override
        public PatientsMiniModel createFromParcel(Parcel in) {
            return new PatientsMiniModel(in);
        }

        @Override
        public PatientsMiniModel[] newArray(int size) {
            return new PatientsMiniModel[size];
        }
    };

    public ArrayList<InsuranceCardModel> getInsuranceCards() {
        return InsuranceCards;
    }

    public void setInsuranceCards(ArrayList<InsuranceCardModel> insuranceCards) {
        InsuranceCards = insuranceCards;
    }

    public long getPatientId() {
        return PatientId;
    }

    public void setPatientId(long patientId) {
        PatientId = patientId;
    }

    public String getNameEn() {
        return NameEn;
    }

    public void setNameEn(String nameEn) {
        NameEn = nameEn;
    }

    public String getRegCardUrl() {
        return RegCardUrl;
    }

    public void setRegCardUrl(String regCardUrl) {
        RegCardUrl = regCardUrl;
    }


    public String getNameAr() {
        return NameAr;
    }

    public void setNameAr(String nameAr) {
        NameAr = nameAr;
    }

    public String getGender() {
        return Gender;
    }

    public void setGender(String gender) {
        Gender = gender;
    }

    public String getBirthDate() {
        return BirthDate;
    }

    public void setBirthDate(String birthDate) {
        BirthDate = birthDate;
    }

    public String getMobile() {
        return Mobile;
    }

    public void setMobile(String mobile) {
        Mobile = mobile;
    }

    public int getMaritalStatus() {
        return MaritalStatus;
    }

    public void setMaritalStatus(int maritalStatus) {
        MaritalStatus = maritalStatus;
    }

    public String getMRN() {
        return MRN;
    }

    public void setMRN(String MRN) {
        this.MRN = MRN;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(PatientId);
        dest.writeString(RegCardUrl);
        dest.writeString(NameEn);
        dest.writeString(NameAr);
        dest.writeString(Gender);
        dest.writeString(BirthDate);
        dest.writeString(Mobile);
        dest.writeInt(MaritalStatus);
        dest.writeString(MRN);
        dest.writeTypedList(InsuranceCards);
        dest.writeInt(PatientCategory);
    }
}
