package com.telemedicine.myclinic.models.appointments;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;
import com.telemedicine.myclinic.activities.multiservices.ServiceItems;
import com.telemedicine.myclinic.models.BaseEntity;
import com.telemedicine.myclinic.models.homemodels.DoctorsProfile;

import java.util.ArrayList;

public class ApptModel extends BaseEntity implements Parcelable {

    @SerializedName("QTicketLab")
    QTicketLabModel QTicketLab;

    @SerializedName("QTicketPharm")
    QTicketPharmModel QTicketPharm;

    @SerializedName("PreAuthStatus")
    long PreAuthStatus;

    @SerializedName("PreAuthDecision")
    long PreAuthDecision;

    @SerializedName("OrdersLab")
    ArrayList<OrdersLabModel> OrdersLab;

    @SerializedName("OrdersRad")
    ArrayList<OrdersRadModel> OrdersRad;

    @SerializedName("Procedures")
    ArrayList<ServiceItems> Procedures;

    @SerializedName("OtherServices")
    ArrayList<ServiceItems> OtherServices;

    @SerializedName("SpecialTest")
    ArrayList<ServiceItems> SpecialTest;

    @SerializedName("OrdersMed")
    ArrayList<OrdersMedModel> OrdersMed;

    @SerializedName("Patient")
    ApptPatient apptPatient;

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

    @SerializedName("RadOrder")
    RadOrderModel RadOrder;

    @SerializedName("DoctorProfile")
    DoctorsProfile DoctorProfile;

    protected ApptModel(Parcel in) {
        QTicketLab = in.readParcelable(QTicketLabModel.class.getClassLoader());
        PreAuthStatus = in.readLong();
        PreAuthDecision = in.readLong();
        OrdersLab = in.createTypedArrayList(OrdersLabModel.CREATOR);
        OrdersRad = in.createTypedArrayList(OrdersRadModel.CREATOR);
        Procedures = in.createTypedArrayList(ServiceItems.CREATOR);
        OtherServices = in.createTypedArrayList(ServiceItems.CREATOR);
        SpecialTest = in.createTypedArrayList(ServiceItems.CREATOR);
        OrdersMed = in.createTypedArrayList(OrdersMedModel.CREATOR);
        apptPatient = in.readParcelable(ApptPatient.class.getClassLoader());
        ApptId = in.readLong();
        ApptNo = in.readString();
        ApptDate = in.readString();
        AppointmentType = in.readInt();
        ApptBookType = in.readInt();
        ApptStatus = in.readInt();
        IsTelemedicine = in.readByte() != 0;
        IsPaid = in.readByte() != 0;
        EstWaitingMin = in.readInt();
        IsInsurance = in.readByte() != 0;
        RadOrder = in.readParcelable(RadOrderModel.class.getClassLoader());
        DoctorProfile = in.readParcelable(DoctorsProfile.class.getClassLoader());
    }

    public static final Creator<ApptModel> CREATOR = new Creator<ApptModel>() {
        @Override
        public ApptModel createFromParcel(Parcel in) {
            return new ApptModel(in);
        }

        @Override
        public ApptModel[] newArray(int size) {
            return new ApptModel[size];
        }
    };

    public QTicketLabModel getQTicketLab() {
        return QTicketLab;
    }

    public void setQTicketLab(QTicketLabModel QTicketLab) {
        this.QTicketLab = QTicketLab;
    }

    public QTicketPharmModel getQTicketPharm() {
        return QTicketPharm;
    }

    public void setQTicketPharm(QTicketPharmModel QTicketPharm) {
        this.QTicketPharm = QTicketPharm;
    }

    public long getPreAuthStatus() {
        return PreAuthStatus;
    }

    public void setPreAuthStatus(long preAuthStatus) {
        PreAuthStatus = preAuthStatus;
    }

    public long getPreAuthDecision() {
        return PreAuthDecision;
    }

    public void setPreAuthDecision(long preAuthDecision) {
        PreAuthDecision = preAuthDecision;
    }

    public ArrayList<OrdersLabModel> getOrdersLab() {
        return OrdersLab;
    }

    public void setOrdersLab(ArrayList<OrdersLabModel> ordersLab) {
        OrdersLab = ordersLab;
    }

    public ArrayList<OrdersRadModel> getOrdersRad() {
        return OrdersRad;
    }

    public void setProcedures(ArrayList<ServiceItems> procedures) {
        Procedures = procedures;
    }


    public ArrayList<ServiceItems> getProcedures() {
        return Procedures;
    }

    public void setOtherServices(ArrayList<ServiceItems> otherServices) {
        OtherServices = otherServices;
    }

    public ArrayList<ServiceItems> getOtherServices() {
        return OtherServices;
    }

    public void setSpecialTest(ArrayList<ServiceItems> specialTest) {
        SpecialTest = specialTest;
    }

    public ArrayList<ServiceItems> getSpecialTest() {
        return SpecialTest;
    }

    public void setOrdersRad(ArrayList<OrdersRadModel> ordersRad) {
        OrdersRad = ordersRad;
    }


    public ArrayList<OrdersMedModel> getOrdersMed() {
        return OrdersMed;
    }

    public void setOrdersMed(ArrayList<OrdersMedModel> ordersMed) {
        OrdersMed = ordersMed;
    }

    public ApptPatient getApptPatient() {
        return apptPatient;
    }

    public void setApptPatient(ApptPatient apptPatient) {
        this.apptPatient = apptPatient;
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

    public DoctorsProfile getDoctorProfile() {
        return DoctorProfile;
    }

    public void setDoctorProfile(DoctorsProfile doctorProfile) {
        DoctorProfile = doctorProfile;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(QTicketLab, flags);
        dest.writeLong(PreAuthStatus);
        dest.writeLong(PreAuthDecision);
        dest.writeTypedList(OrdersLab);
        dest.writeTypedList(OrdersRad);
        dest.writeTypedList(Procedures);
        dest.writeTypedList(OtherServices);
        dest.writeTypedList(SpecialTest);
        dest.writeTypedList(OrdersMed);
        dest.writeParcelable(apptPatient, flags);
        dest.writeLong(ApptId);
        dest.writeString(ApptNo);
        dest.writeString(ApptDate);
        dest.writeInt(AppointmentType);
        dest.writeInt(ApptBookType);
        dest.writeInt(ApptStatus);
        dest.writeByte((byte) (IsTelemedicine ? 1 : 0));
        dest.writeByte((byte) (IsPaid ? 1 : 0));
        dest.writeInt(EstWaitingMin);
        dest.writeByte((byte) (IsInsurance ? 1 : 0));
        dest.writeParcelable(RadOrder, flags);
        dest.writeParcelable(DoctorProfile, flags);
    }
}
