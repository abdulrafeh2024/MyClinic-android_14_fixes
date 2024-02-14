package com.telemedicine.myclinic.models.profileUpdate;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;
import com.telemedicine.myclinic.models.BaseEntity;

public class PatientRegistrationUpdate implements Parcelable {

    @SerializedName("PatientRecId")
    long PatientRecId;

    @SerializedName("Gender")
    int Gender;

    @SerializedName("Salutation")
    int Salutation;

    @SerializedName("BirthDate")
    String BirthDate;

    @SerializedName("FirstName")
    String FirstName;

    @SerializedName("MiddleName")
    String MiddleName;

    @SerializedName("LastName")
    String LastName;

    @SerializedName("NationalIdType")
    int NationalIdType;

    @SerializedName("Insurance")
    InsuranceUpdateModel Insurance;

    @SerializedName("DistrictRecId")
    long DistrictRecId;

    @SerializedName("FullName")
    String FullName;

    @SerializedName("Email")
    String Email;

    @SerializedName("Mobile")
    String Mobile;

    @SerializedName("IsWhatsAppNumber")
    boolean IsWhatsAppNumber;

    @SerializedName("IsInsuranceHolder")
    boolean IsInsuranceHolder;

    @SerializedName("City")
    String City;

    @SerializedName("ZipCode")
    String ZipCode;

    @SerializedName("NationalId")
    String NationalId;

    @SerializedName("MRN")
    String MRN;

    @SerializedName("NationalIdExpiryDate")
    String NationalIdExpiryDate;

    @SerializedName("IsTentativePatient")
    boolean IsTentativePatient;

   /* @SerializedName("IdCardScanBase64")
    String IdCardScanBase64;*/

    @SerializedName("Nationality")
    String Nationality;

    @SerializedName("Country")
    String Country;

    @SerializedName("Language")
    String Language;

    public long getPatientRecId() {
        return PatientRecId;
    }

    public void setPatientRecId(long patientRecId) {
        PatientRecId = patientRecId;
    }

    public int getGender() {
        return Gender;
    }

    public void setGender(int gender) {
        Gender = gender;
    }

    public int getSalutation() {
        return Salutation;
    }

    public void setSalutation(int salutation) {
        Salutation = salutation;
    }

    public String getBirthDate() {
        return BirthDate;
    }

    public void setBirthDate(String birthDate) {
        BirthDate = birthDate;
    }

    public String getFirstName() {
        return FirstName;
    }

    public void setFirstName(String firstName) {
        FirstName = firstName;
    }

    public String getMiddleName() {
        return MiddleName;
    }

    public void setMiddleName(String middleName) {
        MiddleName = middleName;
    }

    public String getLastName() {
        return LastName;
    }

    public void setLastName(String lastName) {
        LastName = lastName;
    }

    public int getNationalIdType() {
        return NationalIdType;
    }

    public void setNationalIdType(int nationalIdType) {
        NationalIdType = nationalIdType;
    }

    public InsuranceUpdateModel getInsurance() {
        return Insurance;
    }

    public void setInsurance(InsuranceUpdateModel insurance) {
        Insurance = insurance;
    }

    public long getDistrictRecId() {
        return DistrictRecId;
    }

    public void setDistrictRecId(long districtRecId) {
        DistrictRecId = districtRecId;
    }

    public String getFullName() {
        return FullName;
    }

    public void setFullName(String fullName) {
        FullName = fullName;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getMobile() {
        return Mobile;
    }

    public void setMobile(String mobile) {
        Mobile = mobile;
    }

    public boolean isWhatsAppNumber() {
        return IsWhatsAppNumber;
    }

    public void setWhatsAppNumber(boolean whatsAppNumber) {
        IsWhatsAppNumber = whatsAppNumber;
    }

    public boolean isInsuranceHolder() {
        return IsInsuranceHolder;
    }

    public void setInsuranceHolder(boolean insuranceHolder) {
        IsInsuranceHolder = insuranceHolder;
    }

    public String getCity() {
        return City;
    }

    public void setCity(String city) {
        City = city;
    }

    public String getZipCode() {
        return ZipCode;
    }

    public void setZipCode(String zipCode) {
        ZipCode = zipCode;
    }

    public String getNationalId() {
        return NationalId;
    }

    public void setNationalId(String nationalId) {
        NationalId = nationalId;
    }

    public String getMRN() {
        return MRN;
    }

    public void setMRN(String MRN) {
        this.MRN = MRN;
    }

    public String getNationalIdExpiryDate() {
        return NationalIdExpiryDate;
    }

    public void setNationalIdExpiryDate(String nationalIdExpiryDate) {
        NationalIdExpiryDate = nationalIdExpiryDate;
    }

    public boolean isTentativePatient() {
        return IsTentativePatient;
    }

    public void setTentativePatient(boolean tentativePatient) {
        IsTentativePatient = tentativePatient;
    }

   /* public String getIdCardScanBase64() {
        return IdCardScanBase64;
    }

    public void setIdCardScanBase64(String idCardScanBase64) {
        IdCardScanBase64 = idCardScanBase64;
    }*/

    public String getNationality() {
        return Nationality;
    }

    public void setNationality(String nationality) {
        Nationality = nationality;
    }

    public String getCountry() {
        return Country;
    }

    public void setCountry(String country) {
        Country = country;
    }

    public String getLanguage() {
        return Language;
    }

    public void setLanguage(String language) {
        Language = language;
    }

    protected PatientRegistrationUpdate(Parcel in) {
        PatientRecId = in.readLong();
        Gender = in.readInt();
        Salutation = in.readInt();
        BirthDate = in.readString();
        FirstName = in.readString();
        MiddleName = in.readString();
        LastName = in.readString();
        NationalIdType = in.readInt();
        Insurance = in.readParcelable(InsuranceUpdateModel.class.getClassLoader());
        DistrictRecId = in.readLong();
        FullName = in.readString();
        Email = in.readString();
        Mobile = in.readString();
        IsWhatsAppNumber = in.readByte() != 0;
        IsInsuranceHolder = in.readByte() != 0;
        City = in.readString();
        ZipCode = in.readString();
        NationalId = in.readString();
        MRN = in.readString();
        NationalIdExpiryDate = in.readString();
        IsTentativePatient = in.readByte() != 0;
        //IdCardScanBase64 = in.readString();
        Nationality = in.readString();
        Country = in.readString();
        Language = in.readString();
    }

    public static final Creator<PatientRegistrationUpdate> CREATOR = new Creator<PatientRegistrationUpdate>() {
        @Override
        public PatientRegistrationUpdate createFromParcel(Parcel in) {
            return new PatientRegistrationUpdate(in);
        }

        @Override
        public PatientRegistrationUpdate[] newArray(int size) {
            return new PatientRegistrationUpdate[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(PatientRecId);
        dest.writeInt(Gender);
        dest.writeInt(Salutation);
        dest.writeString(BirthDate);
        dest.writeString(FirstName);
        dest.writeString(MiddleName);
        dest.writeString(LastName);
        dest.writeInt(NationalIdType);
        dest.writeParcelable(Insurance, flags);
        dest.writeLong(DistrictRecId);
        dest.writeString(FullName);
        dest.writeString(Email);
        dest.writeString(Mobile);
        dest.writeByte((byte) (IsWhatsAppNumber ? 1 : 0));
        dest.writeByte((byte) (IsInsuranceHolder ? 1 : 0));
        dest.writeString(City);
        dest.writeString(ZipCode);
        dest.writeString(NationalId);
        dest.writeString(MRN);
        dest.writeString(NationalIdExpiryDate);
        dest.writeByte((byte) (IsTentativePatient ? 1 : 0));
        // dest.writeString(IdCardScanBase64);
        dest.writeString(Nationality);
        dest.writeString(Country);
        dest.writeString(Language);
    }
}
