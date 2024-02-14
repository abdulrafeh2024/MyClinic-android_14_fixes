package com.telemedicine.myclinic.models.dashboardModels;

import android.annotation.SuppressLint;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;
import com.telemedicine.myclinic.models.BaseEntity;
import com.telemedicine.myclinic.models.appointments.RadOrderModel;
import com.telemedicine.myclinic.models.homemodels.DoctorsProfile;

import java.util.ArrayList;

public class ApptGetFollowUpsModel implements Parcelable {


    @SerializedName("Patient")
    Patient Patient;

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

    @SerializedName("DoctorProfile")
    DoctorsProfile DoctorProfile;

    public DoctorsProfile getDoctorProfile() {
        return DoctorProfile;
    }

    public void setDoctorProfile(DoctorsProfile doctorProfile) {
        DoctorProfile = doctorProfile;
    }

    @SerializedName("IsTelemedicine")
    boolean IsTelemedicine;

    @SerializedName("IsPaid")
    boolean IsPaid;

    @SerializedName("EstWaitingMin")
    int EstWaitingMin;

    @SerializedName("IsInsurance")
    boolean IsInsurance;

    @SerializedName("RadOrder")
    RadOrderModel RadOrder;

    @SerializedName("ApptMinis")
    ArrayList<Long> ApptMinis;

    protected ApptGetFollowUpsModel(Parcel in) {
        Patient = in.readParcelable(com.telemedicine.myclinic.models.dashboardModels.Patient.class.getClassLoader());
        ApptId = in.readLong();
        ApptNo = in.readString();
        ApptDate = in.readString();
        AppointmentType = in.readInt();
        ApptBookType = in.readInt();
        ApptStatus = in.readInt();
        DoctorProfile = in.readParcelable(com.telemedicine.myclinic.models.dashboardModels.DoctorProfile.class.getClassLoader());
        IsTelemedicine = in.readByte() != 0;
        IsPaid = in.readByte() != 0;
        EstWaitingMin = in.readInt();
        IsInsurance = in.readByte() != 0;
        RadOrder = in.readParcelable(RadOrderModel.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(Patient, flags);
        dest.writeLong(ApptId);
        dest.writeString(ApptNo);
        dest.writeString(ApptDate);
        dest.writeInt(AppointmentType);
        dest.writeInt(ApptBookType);
        dest.writeInt(ApptStatus);
        dest.writeParcelable(DoctorProfile, flags);
        dest.writeByte((byte) (IsTelemedicine ? 1 : 0));
        dest.writeByte((byte) (IsPaid ? 1 : 0));
        dest.writeInt(EstWaitingMin);
        dest.writeByte((byte) (IsInsurance ? 1 : 0));
        dest.writeParcelable(RadOrder, flags);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<ApptGetFollowUpsModel> CREATOR = new Creator<ApptGetFollowUpsModel>() {
        @Override
        public ApptGetFollowUpsModel createFromParcel(Parcel in) {
            return new ApptGetFollowUpsModel(in);
        }

        @Override
        public ApptGetFollowUpsModel[] newArray(int size) {
            return new ApptGetFollowUpsModel[size];
        }
    };

    public com.telemedicine.myclinic.models.dashboardModels.Patient getPatient() {
        return Patient;
    }

    public void setPatient(com.telemedicine.myclinic.models.dashboardModels.Patient patient) {
        Patient = patient;
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

    public RadOrderModel getRadOrder() {
        return RadOrder;
    }

    public void setRadOrder(RadOrderModel radOrder) {
        RadOrder = radOrder;
    }

    public ArrayList<Long> getApptMinis() {
        return ApptMinis;
    }

    public void setApptMinis(ArrayList<Long> apptMinis) {
        ApptMinis = apptMinis;
    }
}
