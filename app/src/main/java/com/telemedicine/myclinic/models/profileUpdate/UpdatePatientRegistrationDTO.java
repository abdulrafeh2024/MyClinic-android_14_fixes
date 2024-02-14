package com.telemedicine.myclinic.models.profileUpdate;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;
import com.telemedicine.myclinic.models.profilecreatoinmodels.InsuranceDTO;

public class UpdatePatientRegistrationDTO implements Parcelable {


    @SerializedName("Country")
    String Country;

    @SerializedName("NationalId")
    String NationalId;

    @SerializedName("Salutation")
    String Salutation;

    @SerializedName("DistrictRecId")
    String DistrictRecId;

    @SerializedName("Nationality")
    String Nationality;

    @SerializedName("NationalIdExpiryDate")
    String NationalIdExpiryDate;

    @SerializedName("Insurance")
    InsuranceDTO Insurance;

    @SerializedName("FirstName")
    String FirstName;

    @SerializedName("IsInsuranceHolder")
    boolean IsInsuranceHolder;

    @SerializedName("MiddleName")
    String MiddleName;

    @SerializedName("IdCardScanBase64")
    String IdCardScanBase64;

    @SerializedName("Gender")
    String Gender;

    @SerializedName("BirthDate")
    String BirthDate;

    @SerializedName("NationalIdType")
    String NationalIdType;

    @SerializedName("ZipCode")
    String ZipCode;

    @SerializedName("City")
    String City;

    @SerializedName("LastName")
    String LastName;

    @SerializedName("MRN")
    String MRN;

    @SerializedName("PatientRecId")
    String PatientRecId;

    @SerializedName("IsTentativePatient")
    boolean IsTentativePatient;

    public String getMRN() {
        return MRN;
    }

    public void setMRN(String MRN) {
        this.MRN = MRN;
    }

    public String getPatientRecId() {
        return PatientRecId;
    }

    public void setPatientRecId(String patientRecId) {
        PatientRecId = patientRecId;
    }

    public boolean isTentativePatient() {
        return IsTentativePatient;
    }

    public void setTentativePatient(boolean tentativePatient) {
        IsTentativePatient = tentativePatient;
    }

    public UpdatePatientRegistrationDTO(String country, String nationalId, String salutation, String districtRecId, String nationality, String nationalIdExpiryDate, InsuranceDTO insurance, String firstName, boolean isInsuranceHolder, String middleName, String idCardScanBase64, String gender, String birthDate, String nationalIdType, String zipCode, String city, String lastName, String MRN, String patientRecId, boolean isTentativePatient) {
        Country = country;
        NationalId = nationalId;
        Salutation = salutation;
        DistrictRecId = districtRecId;
        Nationality = nationality;
        NationalIdExpiryDate = nationalIdExpiryDate;
        Insurance = insurance;
        FirstName = firstName;
        IsInsuranceHolder = isInsuranceHolder;
        MiddleName = middleName;
        IdCardScanBase64 = idCardScanBase64;
        Gender = gender;
        BirthDate = birthDate;
        NationalIdType = nationalIdType;
        ZipCode = zipCode;
        City = city;
        LastName = lastName;
        this.MRN = MRN;
        PatientRecId = patientRecId;
        IsTentativePatient = isTentativePatient;
    }

    protected UpdatePatientRegistrationDTO(Parcel in) {
        Country = in.readString();
        NationalId = in.readString();
        Salutation = in.readString();
        DistrictRecId = in.readString();
        Nationality = in.readString();
        NationalIdExpiryDate = in.readString();
        FirstName = in.readString();
        IsInsuranceHolder = in.readByte() != 0;
        MiddleName = in.readString();
        IdCardScanBase64 = in.readString();
        Gender = in.readString();
        BirthDate = in.readString();
        NationalIdType = in.readString();
        ZipCode = in.readString();
        City = in.readString();
        LastName = in.readString();
        MRN = in.readString();
        PatientRecId = in.readString();
        IsTentativePatient = in.readByte() != 0;
    }

    public static final Creator<UpdatePatientRegistrationDTO> CREATOR = new Creator<UpdatePatientRegistrationDTO>() {
        @Override
        public UpdatePatientRegistrationDTO createFromParcel(Parcel in) {
            return new UpdatePatientRegistrationDTO(in);
        }

        @Override
        public UpdatePatientRegistrationDTO[] newArray(int size) {
            return new UpdatePatientRegistrationDTO[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(Country);
        parcel.writeString(NationalId);
        parcel.writeString(Salutation);
        parcel.writeString(DistrictRecId);
        parcel.writeString(Nationality);
        parcel.writeString(NationalIdExpiryDate);
        parcel.writeString(FirstName);
        parcel.writeByte((byte) (IsInsuranceHolder ? 1 : 0));
        parcel.writeString(MiddleName);
        parcel.writeString(IdCardScanBase64);
        parcel.writeString(Gender);
        parcel.writeString(BirthDate);
        parcel.writeString(NationalIdType);
        parcel.writeString(ZipCode);
        parcel.writeString(City);
        parcel.writeString(LastName);
        parcel.writeString(MRN);
        parcel.writeString(PatientRecId);
        parcel.writeByte((byte) (IsTentativePatient ? 1 : 0));
    }


    public String getCountry() {
        return Country;
    }

    public void setCountry(String country) {
        Country = country;
    }

    public String getNationalId() {
        return NationalId;
    }

    public void setNationalId(String nationalId) {
        NationalId = nationalId;
    }

    public String getSalutation() {
        return Salutation;
    }

    public void setSalutation(String salutation) {
        Salutation = salutation;
    }

    public String getDistrictRecId() {
        return DistrictRecId;
    }

    public void setDistrictRecId(String districtRecId) {
        DistrictRecId = districtRecId;
    }

    public String getNationality() {
        return Nationality;
    }

    public void setNationality(String nationality) {
        Nationality = nationality;
    }

    public String getNationalIdExpiryDate() {
        return NationalIdExpiryDate;
    }

    public void setNationalIdExpiryDate(String nationalIdExpiryDate) {
        NationalIdExpiryDate = nationalIdExpiryDate;
    }

    public InsuranceDTO getInsurance() {
        return Insurance;
    }

    public void setInsurance(InsuranceDTO insurance) {
        Insurance = insurance;
    }

    public String getFirstName() {
        return FirstName;
    }

    public void setFirstName(String firstName) {
        FirstName = firstName;
    }

    public boolean isInsuranceHolder() {
        return IsInsuranceHolder;
    }

    public void setInsuranceHolder(boolean insuranceHolder) {
        IsInsuranceHolder = insuranceHolder;
    }

    public String getMiddleName() {
        return MiddleName;
    }

    public void setMiddleName(String middleName) {
        MiddleName = middleName;
    }

    public String getIdCardScanBase64() {
        return IdCardScanBase64;
    }

    public void setIdCardScanBase64(String idCardScanBase64) {
        IdCardScanBase64 = idCardScanBase64;
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

    public String getNationalIdType() {
        return NationalIdType;
    }

    public void setNationalIdType(String nationalIdType) {
        NationalIdType = nationalIdType;
    }

    public String getZipCode() {
        return ZipCode;
    }

    public void setZipCode(String zipCode) {
        ZipCode = zipCode;
    }

    public String getCity() {
        return City;
    }

    public void setCity(String city) {
        City = city;
    }

    public String getLastName() {
        return LastName;
    }

    public void setLastName(String lastName) {
        LastName = lastName;
    }
}
