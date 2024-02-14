package com.telemedicine.myclinic.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;
import com.telemedicine.myclinic.models.patientMiniModels.InsuranceCardModel;

public class AddPatientValidationModel implements Parcelable {

    @SerializedName("PatientId")
    long PatientId;

    @SerializedName("NameEn")
    String NameEn;

    @SerializedName("NameAr")
    String NameAr;

    @SerializedName("MRN")
    String MRN;

    @SerializedName("MaritalStatus")
    int MaritalStatus;

    @SerializedName("Gender")
    int Gender;

    @SerializedName("BirthDate")
    String BirthDate;

    @SerializedName("PatientCategory")
    int PatientCategory;

    @SerializedName("Mobile")
    String Mobile;

   /* @SerializedName("InsuranceCards")
    InsuranceCardModel InsuranceCards;
*/
    protected AddPatientValidationModel(Parcel in) {
        PatientId = in.readLong();
        NameEn = in.readString();
        NameAr = in.readString();
        MRN = in.readString();
        MaritalStatus = in.readInt();
        Gender = in.readInt();
        BirthDate = in.readString();
        PatientCategory = in.readInt();
        Mobile = in.readString();
       // InsuranceCards = in.readParcelable(InsuranceCardModel.class.getClassLoader());
    }

    public static final Creator<com.telemedicine.myclinic.models.AddPatientValidationModel> CREATOR = new Creator<com.telemedicine.myclinic.models.AddPatientValidationModel>() {
        @Override
        public com.telemedicine.myclinic.models.AddPatientValidationModel createFromParcel(Parcel in) {
            return new com.telemedicine.myclinic.models.AddPatientValidationModel(in);
        }

        @Override
        public com.telemedicine.myclinic.models.AddPatientValidationModel[] newArray(int size) {
            return new com.telemedicine.myclinic.models.AddPatientValidationModel[size];
        }
    };

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

    public String getNameAr() {
        return NameAr;
    }

    public void setNameAr(String nameAr) {
        NameAr = nameAr;
    }

    public String getMRN() {
        return MRN;
    }

    public void setMRN(String MRN) {
        this.MRN = MRN;
    }

    public int getMaritalStatus() {
        return MaritalStatus;
    }

    public void setMaritalStatus(int maritalStatus) {
        MaritalStatus = maritalStatus;
    }

    public int getGender() {
        return Gender;
    }

    public void setGender(int gender) {
        Gender = gender;
    }

    public String getBirthDate() {
        return BirthDate;
    }

    public void setBirthDate(String birthDate) {
        BirthDate = birthDate;
    }

    public int getPatientCategory() {
        return PatientCategory;
    }

    public void setPatientCategory(int patientCategory) {
        PatientCategory = patientCategory;
    }

    public String getMobile() {
        return Mobile;
    }

    public void setMobile(String mobile) {
        Mobile = mobile;
    }

   /* public InsuranceCardModel getInsuranceCards() {
        return InsuranceCards;
    }

    public void setInsuranceCards(InsuranceCardModel insuranceCards) {
        InsuranceCards = insuranceCards;
    }*/

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(PatientId);
        dest.writeString(NameEn);
        dest.writeString(NameAr);
        dest.writeString(MRN);
        dest.writeInt(MaritalStatus);
        dest.writeInt(Gender);
        dest.writeString(BirthDate);
        dest.writeInt(PatientCategory);
        dest.writeString(Mobile);
      //  dest.writeParcelable(InsuranceCards, flags);
    }
}
