package com.telemedicine.myclinic.models.dashboardModels;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class DoctorProfile implements Parcelable {

    @SerializedName("DoctorId")
    long DoctorId;

    @SerializedName("ProfilePicUrl")
    String ProfilePicUrl;

    @SerializedName("NameEn")
    String NameEn;

    @SerializedName("NameAr")
    String NameAr;

    @SerializedName("TitleEn")
    String TitleEn;

    @SerializedName("TitleAr")
    String TitleAr;

    @SerializedName("SpecialtyEn")
    String SpecialtyEn;

    @SerializedName("SpecialtyAr")
    String SpecialtyAr;

    @SerializedName("QualificationEn")
    String QualificationEn;

    @SerializedName("QualificationAr")
    String QualificationAr;

    @SerializedName("HasTelemedicine")
    boolean HasTelemedicine;

    public long getDoctorId() {
        return DoctorId;
    }

    public void setDoctorId(long doctorId) {
        DoctorId = doctorId;
    }

    public String getProfilePicUrl() {
        return ProfilePicUrl;
    }

    public void setProfilePicUrl(String profilePicUrl) {
        ProfilePicUrl = profilePicUrl;
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

    public String getTitleEn() {
        return TitleEn;
    }

    public void setTitleEn(String titleEn) {
        TitleEn = titleEn;
    }

    public String getTitleAr() {
        return TitleAr;
    }

    public void setTitleAr(String titleAr) {
        TitleAr = titleAr;
    }

    public String getSpecialtyEn() {
        return SpecialtyEn;
    }

    public void setSpecialtyEn(String specialtyEn) {
        SpecialtyEn = specialtyEn;
    }

    public String getSpecialtyAr() {
        return SpecialtyAr;
    }

    public void setSpecialtyAr(String specialtyAr) {
        SpecialtyAr = specialtyAr;
    }

    public String getQualificationEn() {
        return QualificationEn;
    }

    public void setQualificationEn(String qualificationEn) {
        QualificationEn = qualificationEn;
    }

    public String getQualificationAr() {
        return QualificationAr;
    }

    public void setQualificationAr(String qualificationAr) {
        QualificationAr = qualificationAr;
    }

    public boolean isHasTelemedicine() {
        return HasTelemedicine;
    }

    public void setHasTelemedicine(boolean hasTelemedicine) {
        HasTelemedicine = hasTelemedicine;
    }

    protected DoctorProfile(Parcel in) {
        DoctorId = in.readLong();
        ProfilePicUrl = in.readString();
        NameEn = in.readString();
        NameAr = in.readString();
        TitleEn = in.readString();
        TitleAr = in.readString();
        SpecialtyEn = in.readString();
        SpecialtyAr = in.readString();
        QualificationEn = in.readString();
        QualificationAr = in.readString();
        HasTelemedicine = in.readByte() != 0;
    }

    public static final Creator<DoctorProfile> CREATOR = new Creator<DoctorProfile>() {
        @Override
        public DoctorProfile createFromParcel(Parcel in) {
            return new DoctorProfile(in);
        }

        @Override
        public DoctorProfile[] newArray(int size) {
            return new DoctorProfile[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(DoctorId);
        dest.writeString(ProfilePicUrl);
        dest.writeString(NameEn);
        dest.writeString(NameAr);
        dest.writeString(TitleEn);
        dest.writeString(TitleAr);
        dest.writeString(SpecialtyEn);
        dest.writeString(SpecialtyAr);
        dest.writeString(QualificationEn);
        dest.writeString(QualificationAr);
        dest.writeByte((byte) (HasTelemedicine ? 1 : 0));
    }
}
