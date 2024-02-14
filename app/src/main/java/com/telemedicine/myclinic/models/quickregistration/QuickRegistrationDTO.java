package com.telemedicine.myclinic.models.quickregistration;

import com.google.gson.annotations.SerializedName;

public class QuickRegistrationDTO {

    @SerializedName("Email")
    String Email;

    @SerializedName("Mobile")
    String Mobile;

    @SerializedName("Password")
    String Password;

    @SerializedName("BirthDate")
    String BirthDate;

    @SerializedName("FirstName")
    String FirstName;

    @SerializedName("MiddleName")
    String MiddleName;

    @SerializedName("LastName")
    String LastName;

    @SerializedName("Gender")
    int Gender;

    @SerializedName("OAuthUserId")
    String OAuthUserId;

    @SerializedName("IsOkaDoc")
    boolean IsOkaDoc;

    @SerializedName("IsSaudiNational")
    boolean IsSaudiNational;

    @SerializedName("OAuthProvider")
    int OAuthProvider;

    @SerializedName("PreferredLanguage")
    int PreferredLanguage;

    @SerializedName("SecurityToken")
    String SecurityToken;

    @SerializedName("IsTelemedicine")
    boolean IsTelemedicine;


    @SerializedName("SpecialtyId")
    String SpecialtyId;

    @SerializedName("DoctorId")
    String DoctorId;

    @SerializedName("CompanyId")
    String companyId;

    @SerializedName("ApptDate")
    String ApptDate;


    public QuickRegistrationDTO(String SecurityToken, String email, String mobile, String password,
                                String BirthDate, String FullName,String middleName, String lastName,
                               int Gender, String oAuthToken,
                                boolean IsOkaDoc, int OAuthProvider, int PreferredLanguage, boolean IsSaudiNational,
                                String specialtyId, String doctorId,boolean isTelemedicine, String companyId, String apptDate) {
        Email = email;
        Mobile = mobile;
        Password = password;
        this.BirthDate = BirthDate;
        this.FirstName = FullName;
        this.LastName = lastName;
        this.MiddleName = middleName;
        this.Gender = Gender;
        OAuthUserId = oAuthToken;
        this.SecurityToken = SecurityToken;
        this.IsOkaDoc = IsOkaDoc;
        this.OAuthProvider = OAuthProvider;
        this.PreferredLanguage = PreferredLanguage;
        this.IsSaudiNational = IsSaudiNational;

        SpecialtyId = specialtyId;
        DoctorId = doctorId;
        IsTelemedicine = isTelemedicine;
        this.companyId = companyId;
        ApptDate = apptDate;
    }
}