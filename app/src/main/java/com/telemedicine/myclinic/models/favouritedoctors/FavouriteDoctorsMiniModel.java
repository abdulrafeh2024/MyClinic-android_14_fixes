package com.telemedicine.myclinic.models.favouritedoctors;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class FavouriteDoctorsMiniModel implements Parcelable {

    @SerializedName("ODocFavId")
    long ODocFavId;

    @SerializedName("DoctorId")
    long DoctorId;

    @SerializedName("PatientId")
    long PatientId;

    @SerializedName("SpecialtyId")
    long SpecialtyId;

    @SerializedName("SpecialtyMasterId")
    long SpecialtyMasterId;

    @SerializedName("DoctorNameEn")
    String DoctorNameEn;

    @SerializedName("DoctorNameAr")
    String DoctorNameAr;

    @SerializedName("SpecialtyEn")
    String SpecialtyEn;

    @SerializedName("SpecialtyAr")
    String SpecialtyAr;

    @SerializedName("DoctorProfilePic")
    String DoctorProfilePic;

    @SerializedName("IsFavorite")
    boolean IsFavorite;

    @SerializedName("CreateStamp")
    String CreateStamp;

    @SerializedName("UpdateStamp")
    String UpdateStamp;

    @SerializedName("CreatedById")
    String CreatedById;

    @SerializedName("UpdatedById")
    String UpdatedById;

    @SerializedName("CreatedBy")
    String CreatedBy;

    @SerializedName("UpdatedBy")
    String UpdatedBy;

    public long getODocFavId() {
        return ODocFavId;
    }

    public void setODocFavId(long ODocFavId) {
        this.ODocFavId = ODocFavId;
    }

    public long getDoctorId() {
        return DoctorId;
    }

    public void setDoctorId(long doctorId) {
        DoctorId = doctorId;
    }

    public long getPatientId() {
        return PatientId;
    }

    public void setPatientId(long patientId) {
        PatientId = patientId;
    }

    public long getSpecialtyId() {
        return SpecialtyId;
    }

    public void setSpecialtyId(long specialtyId) {
        SpecialtyId = specialtyId;
    }

    public long getSpecialtyMasterId() {
        return SpecialtyMasterId;
    }

    public void setSpecialtyMasterId(long specialtyMasterId) {
        SpecialtyMasterId = specialtyMasterId;
    }

    public String getDoctorNameEn() {
        return DoctorNameEn;
    }

    public void setDoctorNameEn(String doctorNameEn) {
        DoctorNameEn = doctorNameEn;
    }

    public String getDoctorNameAr() {
        return DoctorNameAr;
    }

    public void setDoctorNameAr(String doctorNameAr) {
        DoctorNameAr = doctorNameAr;
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

    public String getDoctorProfilePic() {
        return DoctorProfilePic;
    }

    public void setDoctorProfilePic(String doctorProfilePic) {
        DoctorProfilePic = doctorProfilePic;
    }

    public boolean isFavorite() {
        return IsFavorite;
    }

    public void setFavorite(boolean favorite) {
        IsFavorite = favorite;
    }

    public String getCreateStamp() {
        return CreateStamp;
    }

    public void setCreateStamp(String createStamp) {
        CreateStamp = createStamp;
    }

    public String getUpdateStamp() {
        return UpdateStamp;
    }

    public void setUpdateStamp(String updateStamp) {
        UpdateStamp = updateStamp;
    }

    public String getCreatedById() {
        return CreatedById;
    }

    public void setCreatedById(String createdById) {
        CreatedById = createdById;
    }

    public String getUpdatedById() {
        return UpdatedById;
    }

    public void setUpdatedById(String updatedById) {
        UpdatedById = updatedById;
    }

    public String getCreatedBy() {
        return CreatedBy;
    }

    public void setCreatedBy(String createdBy) {
        CreatedBy = createdBy;
    }

    public String getUpdatedBy() {
        return UpdatedBy;
    }

    public void setUpdatedBy(String updatedBy) {
        UpdatedBy = updatedBy;
    }


    public FavouriteDoctorsMiniModel() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(ODocFavId);
        dest.writeLong(DoctorId);
        dest.writeLong(PatientId);
        dest.writeLong(SpecialtyId);
        dest.writeString(DoctorNameEn);
        dest.writeString(DoctorNameAr);
        dest.writeString(SpecialtyEn);
        dest.writeString(SpecialtyAr);
        dest.writeString(DoctorProfilePic);
        dest.writeByte((byte) (IsFavorite ? 1 : 0));
        dest.writeString(CreateStamp);
        dest.writeString(UpdateStamp);
        dest.writeString(CreatedById);
        dest.writeString(UpdatedById);
        dest.writeString(CreatedBy);
        dest.writeString(UpdatedBy);
    }

    protected FavouriteDoctorsMiniModel(Parcel in) {
        ODocFavId = in.readLong();
        DoctorId = in.readLong();
        PatientId = in.readLong();
        SpecialtyId = in.readLong();
        DoctorNameEn = in.readString();
        DoctorNameAr = in.readString();
        SpecialtyEn = in.readString();
        SpecialtyAr = in.readString();
        DoctorProfilePic = in.readString();
        IsFavorite = in.readByte() != 0;
        CreateStamp = in.readString();
        UpdateStamp = in.readString();
        CreatedById = in.readString();
        UpdatedById = in.readString();
        CreatedBy = in.readString();
        UpdatedBy = in.readString();
    }

    public static final Creator<FavouriteDoctorsMiniModel> CREATOR = new Creator<FavouriteDoctorsMiniModel>() {
        @Override
        public FavouriteDoctorsMiniModel createFromParcel(Parcel in) {
            return new FavouriteDoctorsMiniModel(in);
        }

        @Override
        public FavouriteDoctorsMiniModel[] newArray(int size) {
            return new FavouriteDoctorsMiniModel[size];
        }
    };
}
