package com.telemedicine.myclinic.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;
import com.telemedicine.myclinic.models.appointments.ApptPatient;
import com.telemedicine.myclinic.models.appointments.RadOrderModel;
import com.telemedicine.myclinic.models.homemodels.DoctorsProfile;

public class ApptsMiniValidationModel implements Parcelable {

    @SerializedName("ApptId")
    long ApptId;

    @SerializedName("ApptNo")
    String ApptNo;

    @SerializedName("ApptDate")
    String ApptDate;

    @SerializedName("AppointmentType")
    int AppointmentType;

    @SerializedName("ApptBookType")
    int ApptBookType;

    @SerializedName("ApptStatus")
    int ApptStatus;

    @SerializedName("IsTelemedicine")
    boolean IsTelemedicine;

    @SerializedName("IsPaid")
    boolean IsPaid;

    @SerializedName("EstWaitingMin")
    int EstWaitingMin;

    @SerializedName("IsInsurance")
    boolean IsInsurance;

    @SerializedName("Patient")
    AddPatientValidationModel Patient;

    @SerializedName("DoctorProfile")
    DoctorsProfile DoctorProfile;

    @SerializedName("RadOrder")
    RadOrderModel RadOrder;

    @SerializedName("IsTelemedicineCompleted")
    boolean IsTelemedicineCompleted;

    @SerializedName("Company")
    String Company;

    @SerializedName("CompanyNameAr")
    String CompanyNameAr;

    @SerializedName("CompanyNameEn")
    String CompanyNameEn;

    @SerializedName("MeetingId")
    String MeetingId;

    @SerializedName("Password")
    String Password;

    public boolean isTelemedicineCompleted() {
        return IsTelemedicineCompleted;
    }

    public void setTelemedicineCompleted(boolean telemedicineCompleted) {
        IsTelemedicineCompleted = telemedicineCompleted;
    }

    public long getApptId() {
        return ApptId;
    }

    public void setApptId(long apptId) {
        ApptId = apptId;
    }

    public String getApptNo() {
        return ApptNo;
    }

    public void setApptNo(String apptNo) {
        ApptNo = apptNo;
    }

    public String getApptDate() {
        return ApptDate;
    }

    public void setApptDate(String apptDate) {
        ApptDate = apptDate;
    }

    public int getAppointmentType() {
        return AppointmentType;
    }

    public void setAppointmentType(int appointmentType) {
        AppointmentType = appointmentType;
    }

    public int getApptBookType() {
        return ApptBookType;
    }

    public void setApptBookType(int apptBookType) {
        ApptBookType = apptBookType;
    }

    public int getApptStatus() {
        return ApptStatus;
    }

    public void setApptStatus(int apptStatus) {
        ApptStatus = apptStatus;
    }

    public boolean isTelemedicine() {
        return IsTelemedicine;
    }

    public void setTelemedicine(boolean telemedicine) {
        IsTelemedicine = telemedicine;
    }

    public boolean isPaid() {
        return IsPaid;
    }

    public void setPaid(boolean paid) {
        IsPaid = paid;
    }

    public int getEstWaitingMin() {
        return EstWaitingMin;
    }

    public void setEstWaitingMin(int estWaitingMin) {
        EstWaitingMin = estWaitingMin;
    }

    public boolean isInsurance() {
        return IsInsurance;
    }

    public void setInsurance(boolean insurance) {
        IsInsurance = insurance;
    }

    public AddPatientValidationModel getPatient() {
        return Patient;
    }

    public void setPatient(AddPatientValidationModel patient) {
        Patient = patient;
    }

    public DoctorsProfile getDoctorProfile() {
        return DoctorProfile;
    }

    public void setDoctorProfile(DoctorsProfile doctorProfile) {
        DoctorProfile = doctorProfile;
    }

    public RadOrderModel getRadOrder() {
        return RadOrder;
    }

    public void setRadOrder(RadOrderModel radOrder) {
        RadOrder = radOrder;
    }

    public String getCompany() {
        return Company;
    }

    public String getCompanyNameAr() {
        return CompanyNameAr;
    }

    public void setCompanyNameAr(String companyNameAr) {
        CompanyNameAr = companyNameAr;
    }

    public String getCompanyNameEn() {
        return CompanyNameEn;
    }

    public void setCompanyNameEn(String companyNameEn) {
        CompanyNameEn = companyNameEn;
    }

    public void setCompany(String company) {
        Company = company;
    }

    public String getMeetingId() {
        return MeetingId;
    }

    public void setMeetingId(String meetingId) {
        MeetingId = meetingId ;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password ;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.ApptId);
        dest.writeString(this.ApptNo);
        dest.writeString(this.ApptDate);
        dest.writeInt(this.AppointmentType);
        dest.writeInt(this.ApptBookType);
        dest.writeInt(this.ApptStatus);
        dest.writeByte(this.IsTelemedicine ? (byte) 1 : (byte) 0);
        dest.writeByte(this.IsPaid ? (byte) 1 : (byte) 0);
        dest.writeInt(this.EstWaitingMin);
        dest.writeByte(this.IsInsurance ? (byte) 1 : (byte) 0);
        dest.writeParcelable(this.Patient, flags);
        dest.writeParcelable(this.DoctorProfile, flags);
        dest.writeParcelable(this.RadOrder, flags);
        dest.writeByte(this.IsTelemedicineCompleted ? (byte) 1 : (byte) 0);
        dest.writeString(this.Company);
        dest.writeString(this.CompanyNameAr);
        dest.writeString(this.CompanyNameEn);
        dest.writeString(this.Password);
        dest.writeString(this.MeetingId);

    }

    public ApptsMiniValidationModel() {
    }

    protected ApptsMiniValidationModel(Parcel in) {
        this.ApptId = in.readLong();
        this.ApptNo = in.readString();
        this.ApptDate = in.readString();
        this.AppointmentType = in.readInt();
        this.ApptBookType = in.readInt();
        this.ApptStatus = in.readInt();
        this.IsTelemedicine = in.readByte() != 0;
        this.IsPaid = in.readByte() != 0;
        this.EstWaitingMin = in.readInt();
        this.IsInsurance = in.readByte() != 0;
        this.Patient = in.readParcelable(ApptPatient.class.getClassLoader());
        this.DoctorProfile = in.readParcelable(DoctorsProfile.class.getClassLoader());
        this.RadOrder = in.readParcelable(RadOrderModel.class.getClassLoader());
        this.IsTelemedicineCompleted = in.readByte() != 0;
        this.Company = in.readString();
        this.CompanyNameAr = in.readString();
        this.CompanyNameEn = in.readString();
        this.Password = in.readString();
        this.MeetingId = in.readString();
    }

    public static final Creator<com.telemedicine.myclinic.models.ApptsMiniValidationModel> CREATOR = new Creator<com.telemedicine.myclinic.models.ApptsMiniValidationModel>() {
        @Override
        public com.telemedicine.myclinic.models.ApptsMiniValidationModel createFromParcel(Parcel source) {
            return new com.telemedicine.myclinic.models.ApptsMiniValidationModel(source);
        }

        @Override
        public com.telemedicine.myclinic.models.ApptsMiniValidationModel[] newArray(int size) {
            return new com.telemedicine.myclinic.models.ApptsMiniValidationModel[size];
        }
    };
}
