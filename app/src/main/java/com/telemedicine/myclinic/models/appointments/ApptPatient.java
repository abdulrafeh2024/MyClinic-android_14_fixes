package com.telemedicine.myclinic.models.appointments;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;
import com.telemedicine.myclinic.models.patientMiniModels.InsuranceCardModel;

import java.util.ArrayList;

public class ApptPatient implements Parcelable {

    @SerializedName("PatientId")
    long PatientId;

    @SerializedName("EncryptedPatientId")
    long EncryptedPatientId;

    @SerializedName("Relation")
    long Relation;

    @SerializedName("AgeInYear")
    int AgeInYear;

    @SerializedName("AppointmentStatusDescription")
    String AppointmentStatusDescription;

    @SerializedName("PatientStatus")
    String PatientStatus;

    @SerializedName("Company")
    String Company;

    @SerializedName("Nationality")
    long Nationality;

    @SerializedName("RegCardUrl")
    String RegCardUrl;

    @SerializedName("FirstName")
    String FirstName;

    @SerializedName("LastName")
    long LastName;

    @SerializedName("StringPatientCategory")
    long StringPatientCategory;

    @SerializedName("IsNewPatient")
    boolean IsNewPatient;

    @SerializedName("EmergencyNo")
    String EmergencyNo;

    @SerializedName("DistrictId")
    long DistrictId;

    @SerializedName("DistrictName")
    String DistrictName;

    @SerializedName("Salutation")
    int Salutation;

    @SerializedName("NationalityType")
    int NationalityType;

    @SerializedName("Language")
    String Language;

    @SerializedName("NationalityExpiry")
    String NationalityExpiry;

    @SerializedName("PlanName")
    String PlanName;

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

    @SerializedName("InsuranceCards")
    ArrayList<InsuranceCardModel> InsuranceCards;

    protected ApptPatient(Parcel in) {
        PatientId = in.readLong();
        EncryptedPatientId = in.readLong();
        Relation = in.readLong();
        AgeInYear = in.readInt();
        AppointmentStatusDescription = in.readString();
        PatientStatus = in.readString();
        Company = in.readString();
        Nationality = in.readLong();
        RegCardUrl = in.readString();
        FirstName = in.readString();
        LastName = in.readLong();
        StringPatientCategory = in.readLong();
        IsNewPatient = in.readByte() != 0;
        EmergencyNo = in.readString();
        DistrictId = in.readLong();
        DistrictName = in.readString();
        Salutation = in.readInt();
        NationalityType = in.readInt();
        Language = in.readString();
        NationalityExpiry = in.readString();
        PlanName = in.readString();
        NameEn = in.readString();
        NameAr = in.readString();
        MRN = in.readString();
        MaritalStatus = in.readInt();
        Gender = in.readInt();
        BirthDate = in.readString();
        PatientCategory = in.readInt();
        Mobile = in.readString();
        InsuranceCards = in.createTypedArrayList(InsuranceCardModel.CREATOR);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(PatientId);
        dest.writeLong(EncryptedPatientId);
        dest.writeLong(Relation);
        dest.writeInt(AgeInYear);
        dest.writeString(AppointmentStatusDescription);
        dest.writeString(PatientStatus);
        dest.writeString(Company);
        dest.writeLong(Nationality);
        dest.writeString(RegCardUrl);
        dest.writeString(FirstName);
        dest.writeLong(LastName);
        dest.writeLong(StringPatientCategory);
        dest.writeByte((byte) (IsNewPatient ? 1 : 0));
        dest.writeString(EmergencyNo);
        dest.writeLong(DistrictId);
        dest.writeString(DistrictName);
        dest.writeInt(Salutation);
        dest.writeInt(NationalityType);
        dest.writeString(Language);
        dest.writeString(NationalityExpiry);
        dest.writeString(PlanName);
        dest.writeString(NameEn);
        dest.writeString(NameAr);
        dest.writeString(MRN);
        dest.writeInt(MaritalStatus);
        dest.writeInt(Gender);
        dest.writeString(BirthDate);
        dest.writeInt(PatientCategory);
        dest.writeString(Mobile);
        dest.writeTypedList(InsuranceCards);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<ApptPatient> CREATOR = new Creator<ApptPatient>() {
        @Override
        public ApptPatient createFromParcel(Parcel in) {
            return new ApptPatient(in);
        }

        @Override
        public ApptPatient[] newArray(int size) {
            return new ApptPatient[size];
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

    public ArrayList<InsuranceCardModel> getInsuranceCards() {
        return InsuranceCards;
    }

    public long getEncryptedPatientId() {
        return EncryptedPatientId;
    }

    public void setEncryptedPatientId(long encryptedPatientId) {
        EncryptedPatientId = encryptedPatientId;
    }

    public long getRelation() {
        return Relation;
    }

    public void setRelation(long relation) {
        Relation = relation;
    }

    public int getAgeInYear() {
        return AgeInYear;
    }

    public void setAgeInYear(int ageInYear) {
        AgeInYear = ageInYear;
    }

    public String getAppointmentStatusDescription() {
        return AppointmentStatusDescription;
    }

    public void setAppointmentStatusDescription(String appointmentStatusDescription) {
        AppointmentStatusDescription = appointmentStatusDescription;
    }

    public String getPatientStatus() {
        return PatientStatus;
    }

    public void setPatientStatus(String patientStatus) {
        PatientStatus = patientStatus;
    }

    public String getCompany() {
        return Company;
    }

    public void setCompany(String company) {
        Company = company;
    }

    public long getNationality() {
        return Nationality;
    }

    public void setNationality(long nationality) {
        Nationality = nationality;
    }

    public String getRegCardUrl() {
        return RegCardUrl;
    }

    public void setRegCardUrl(String regCardUrl) {
        RegCardUrl = regCardUrl;
    }

    public String getFirstName() {
        return FirstName;
    }

    public void setFirstName(String firstName) {
        FirstName = firstName;
    }

    public long getLastName() {
        return LastName;
    }

    public void setLastName(long lastName) {
        LastName = lastName;
    }

    public long getStringPatientCategory() {
        return StringPatientCategory;
    }

    public void setStringPatientCategory(long stringPatientCategory) {
        StringPatientCategory = stringPatientCategory;
    }

    public boolean isNewPatient() {
        return IsNewPatient;
    }

    public void setNewPatient(boolean newPatient) {
        IsNewPatient = newPatient;
    }

    public String getEmergencyNo() {
        return EmergencyNo;
    }

    public void setEmergencyNo(String emergencyNo) {
        EmergencyNo = emergencyNo;
    }

    public long getDistrictId() {
        return DistrictId;
    }

    public void setDistrictId(long districtId) {
        DistrictId = districtId;
    }

    public String getDistrictName() {
        return DistrictName;
    }

    public void setDistrictName(String districtName) {
        DistrictName = districtName;
    }

    public int getSalutation() {
        return Salutation;
    }

    public void setSalutation(int salutation) {
        Salutation = salutation;
    }

    public int getNationalityType() {
        return NationalityType;
    }

    public void setNationalityType(int nationalityType) {
        NationalityType = nationalityType;
    }

    public String getLanguage() {
        return Language;
    }

    public void setLanguage(String language) {
        Language = language;
    }

    public String getNationalityExpiry() {
        return NationalityExpiry;
    }

    public void setNationalityExpiry(String nationalityExpiry) {
        NationalityExpiry = nationalityExpiry;
    }

    public String getPlanName() {
        return PlanName;
    }

    public void setPlanName(String planName) {
        PlanName = planName;
    }

    public void setInsuranceCards(ArrayList<InsuranceCardModel> insuranceCards) {
        InsuranceCards = insuranceCards;
    }


}
