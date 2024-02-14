package com.telemedicine.myclinic.models.homevisit;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PatientAddressBookList implements Parcelable {

    @SerializedName("District")
    @Expose
    private String District;
    @SerializedName("Street")
    @Expose
    private String Street;
    @SerializedName("AprtNo")
    @Expose
    private long AprtNo;
    @SerializedName("AprtId")
    @Expose
    private String AprtId;
    @SerializedName("BuildingName")
    @Expose
    private String BuildingName;
    @SerializedName("BuildingNo")
    @Expose
    private long BuildingNo;
    @SerializedName("AddressNotes")
    @Expose
    private String AddressNotes;

    @SerializedName("PatientId")
    @Expose
    private long PatientId;
    @SerializedName("ApptId")
    @Expose
    private String ApptId;
    @SerializedName("Action")
    @Expose
    private String Action;
    @SerializedName("Status")
    @Expose
    private int Status;
    @SerializedName("PatientName")
    @Expose
    private String PatientName;
    @SerializedName("PatientPhone")
    @Expose
    private String PatientPhone;
    @SerializedName("Lable")
    @Expose
    private String Lable;
    @SerializedName("AreaName")
    @Expose
    private String AreaName;
    @SerializedName("Lat")
    @Expose
    private double Lat;
    @SerializedName("Lng")
    @Expose
    private double Lng;
    @SerializedName("Id")
    @Expose
    private long Id;
    @SerializedName("CreateStamp")
    @Expose
    private String CreateStamp;
    @SerializedName("UpdateStamp")
    @Expose
    private String UpdateStamp;
    @SerializedName("CreatedById")
    @Expose
    private long CreatedById;
    @SerializedName("UpdatedById")
    @Expose
    private long UpdatedById;


    @SerializedName("MedicationsList")
    @Expose
    private List<MedicationsList> MedicationsList = null;

    protected PatientAddressBookList(Parcel in) {
        District = in.readString();
        Street = in.readString();
        AprtNo = in.readInt();
        AprtId = in.readString();
        BuildingName = in.readString();
        BuildingNo = in.readInt();
        AddressNotes = in.readString();
        PatientId = in.readInt();
        ApptId = in.readString();
        Action = in.readString();
        Status = in.readInt();
        PatientName = in.readString();
        PatientPhone = in.readString();
        Lable = in.readString();
        AreaName = in.readString();
        Lat = in.readDouble();
        Lng = in.readDouble();
        Id = in.readLong();
        CreateStamp = in.readString();
        UpdateStamp = in.readString();
        CreatedById = in.readLong();
        UpdatedById = in.readLong();
        MedicationsList = in.createTypedArrayList(com.telemedicine.myclinic.models.homevisit.MedicationsList.CREATOR);
        District = in.readString();
        Street = in.readString();
        AprtNo = in.readInt();
        AprtId = in.readString();
        BuildingName = in.readString();
        BuildingNo = in.readInt();
        AddressNotes = in.readString();
        PatientId = in.readInt();
        ApptId = in.readString();
        Action = in.readString();
        Status = in.readInt();
        PatientName = in.readString();
        PatientPhone = in.readString();
        Lable = in.readString();
        AreaName = in.readString();
        Lat = in.readDouble();
        Lng = in.readDouble();
        Id = in.readLong();
        CreateStamp = in.readString();
        UpdateStamp = in.readString();
        CreatedById = in.readLong();
        UpdatedById = in.readLong();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(District);
        dest.writeString(Street);
        dest.writeLong(AprtNo);
        dest.writeString(AprtId);
        dest.writeString(BuildingName);
        dest.writeLong(BuildingNo);
        dest.writeString(AddressNotes);
        dest.writeLong(PatientId);
        dest.writeString(ApptId);
        dest.writeString(Action);
        dest.writeInt(Status);
        dest.writeString(PatientName);
        dest.writeString(PatientPhone);
        dest.writeString(Lable);
        dest.writeString(AreaName);
        dest.writeDouble(Lat);
        dest.writeDouble(Lng);
        dest.writeLong(Id);
        dest.writeString(CreateStamp);
        dest.writeString(UpdateStamp);
        dest.writeLong(CreatedById);
        dest.writeLong(UpdatedById);
        dest.writeTypedList(MedicationsList);
        dest.writeString(District);
        dest.writeString(Street);
        dest.writeLong(AprtNo);
        dest.writeString(AprtId);
        dest.writeString(BuildingName);
        dest.writeLong(BuildingNo);
        dest.writeString(AddressNotes);
        dest.writeLong(PatientId);
        dest.writeString(ApptId);
        dest.writeString(Action);
        dest.writeInt(Status);
        dest.writeString(PatientName);
        dest.writeString(PatientPhone);
        dest.writeString(Lable);
        dest.writeString(AreaName);
        dest.writeDouble(Lat);
        dest.writeDouble(Lng);
        dest.writeLong(Id);
        dest.writeString(CreateStamp);
        dest.writeString(UpdateStamp);
        dest.writeLong(CreatedById);
        dest.writeLong(UpdatedById);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<PatientAddressBookList> CREATOR = new Creator<PatientAddressBookList>() {
        @Override
        public PatientAddressBookList createFromParcel(Parcel in) {
            return new PatientAddressBookList(in);
        }

        @Override
        public PatientAddressBookList[] newArray(int size) {
            return new PatientAddressBookList[size];
        }
    };

    public List<com.telemedicine.myclinic.models.homevisit.MedicationsList> getMedicationsList() {
        return MedicationsList;
    }

    public void setMedicationsList(List<com.telemedicine.myclinic.models.homevisit.MedicationsList> medicationsList) {
        MedicationsList = medicationsList;
    }

    public String getDistrict() {
        return District;
    }

    public void setDistrict(String district) {
        District = district;
    }

    public String getStreet() {
        return Street;
    }

    public void setStreet(String street) {
        Street = street;
    }

    public long getAprtNo() {
        return AprtNo;
    }

    public void setAprtNo(int aprtNo) {
        AprtNo = aprtNo;
    }

    public String getAprtId() {
        return AprtId;
    }

    public void setAprtId(String aprtId) {
        AprtId = aprtId;
    }

    public String getBuildingName() {
        return BuildingName;
    }

    public void setBuildingName(String buildingName) {
        BuildingName = buildingName;
    }

    public long getBuildingNo() {
        return BuildingNo;
    }

    public void setBuildingNo(int buildingNo) {
        BuildingNo = buildingNo;
    }

    public String getAddressNotes() {
        return AddressNotes;
    }

    public void setAddressNotes(String addressNotes) {
        AddressNotes = addressNotes;
    }

    public long getPatientId() {
        return PatientId;
    }

    public void setPatientId(int patientId) {
        PatientId = patientId;
    }

    public String getApptId() {
        return ApptId;
    }

    public void setApptId(String apptId) {
        ApptId = apptId;
    }

    public String getAction() {
        return Action;
    }

    public void setAction(String action) {
        Action = action;
    }

    public int getStatus() {
        return Status;
    }

    public void setStatus(int status) {
        Status = status;
    }

    public String getPatientName() {
        return PatientName;
    }

    public void setPatientName(String patientName) {
        PatientName = patientName;
    }

    public String getPatientPhone() {
        return PatientPhone;
    }

    public void setPatientPhone(String patientPhone) {
        PatientPhone = patientPhone;
    }

    public String getLable() {
        return Lable;
    }

    public void setLable(String lable) {
        Lable = lable;
    }

    public String getAreaName() {
        return AreaName;
    }

    public void setAreaName(String areaName) {
        AreaName = areaName;
    }

    public double getLat() {
        return Lat;
    }

    public void setLat(double lat) {
        Lat = lat;
    }

    public double getLng() {
        return Lng;
    }

    public void setLng(double lng) {
        Lng = lng;
    }

    public long getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
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

    public long getCreatedById() {
        return CreatedById;
    }

    public void setCreatedById(long createdById) {
        CreatedById = createdById;
    }

    public long getUpdatedById() {
        return UpdatedById;
    }

    public void setUpdatedById(long updatedById) {
        UpdatedById = updatedById;
    }

}
