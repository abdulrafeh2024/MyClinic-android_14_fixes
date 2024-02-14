package com.telemedicine.myclinic.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;
import com.telemedicine.myclinic.models.logonmodels.SecurityToken;

public class LabReportsResponseModel implements Parcelable {

    @SerializedName("ApptId")
    long ApptId;

    @SerializedName("ApptNo")
    String ApptNo;

    @SerializedName("ApptDate")
    String ApptDate;

    @SerializedName("DoctorId")
    long DoctorId;

    @SerializedName("DoctorNameEn")
    String DoctorNameEn;

    @SerializedName("DoctorNameAr")
    String DoctorNameAr;

    @SerializedName("SpecialtyEn")
    String SpecialtyEn;

    @SerializedName("SpecialtyAr")
    String SpecialtyAr;

    @SerializedName("OrderType")
    int OrderType;

    @SerializedName("OrderId")
    long OrderId;

    @SerializedName("ItemCode")
    String ItemCode;

    @SerializedName("Name")
    String Name;

    @SerializedName("OrderDate")
    String OrderDate;

    @SerializedName("OrderNumber")
    String OrderNumber;


    @SerializedName("ReportUrl")
    String ReportUrl;

    @SerializedName("EncrypetPatientId")
    String EncrypetPatientId;

    @SerializedName("Company")
    String Company;

    @SerializedName("CompanyNameEn")
    String CompanyNameEn;

    @SerializedName("CompanyNameAr")
    String CompanyNameAr;

    public LabReportsResponseModel() {
    }
    protected LabReportsResponseModel(Parcel in) {
        ApptId = in.readLong();
        ApptNo = in.readString();
        ApptDate = in.readString();
        DoctorId = in.readLong();
        DoctorNameEn = in.readString();
        DoctorNameAr = in.readString();
        SpecialtyEn = in.readString();
        SpecialtyAr = in.readString();
        OrderType = in.readInt();
        OrderId = in.readLong();
        ItemCode = in.readString();
        Name = in.readString();
        OrderDate = in.readString();
        OrderNumber = in.readString();
        ReportUrl = in.readString();
        EncrypetPatientId = in.readString();
        Company = in.readString();
        CompanyNameEn = in.readString();
        CompanyNameAr = in.readString();
    }

    public static final Creator<LabReportsResponseModel> CREATOR = new Creator<LabReportsResponseModel>() {
        @Override
        public LabReportsResponseModel createFromParcel(Parcel in) {
            return new LabReportsResponseModel(in);
        }

        @Override
        public LabReportsResponseModel[] newArray(int size) {
            return new LabReportsResponseModel[size];
        }
    };

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

    public long getDoctorId() {
        return DoctorId;
    }

    public void setDoctorId(long doctorId) {
        DoctorId = doctorId;
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

    public int getOrderType() {
        return OrderType;
    }

    public void setOrderType(int orderType) {
        OrderType = orderType;
    }

    public long getOrderId() {
        return OrderId;
    }

    public void setOrderId(long orderId) {
        OrderId = orderId;
    }

    public String getItemCode() {
        return ItemCode;
    }

    public void setItemCode(String itemCode) {
        ItemCode = itemCode;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getOrderDate() {
        return OrderDate;
    }

    public String getOrderNumber() {
        return OrderNumber;
    }

    public void setOrderDate(String orderDate) {
        OrderDate = orderDate;
    }

    public void setOrderNumber(String orderNumber) {
        OrderNumber = orderNumber;
    }

    public String getReportUrl() {
        return ReportUrl;
    }

    public void setReportUrl(String reportUrl) {
        ReportUrl = reportUrl;
    }

    public String getEncrypetPatientId() {
        return EncrypetPatientId;
    }

    public void setEncrypetPatientId(String encrypetPatientId) {
        EncrypetPatientId = encrypetPatientId;
    }

    public String getCompany() {
        return Company;
    }

    public void setCompany(String company) {
        Company = company;
    }

    public String getCompanyNameEn() {
        return CompanyNameEn;
    }

    public void setCompanyNameEn(String companyNameEn) {
        CompanyNameEn = companyNameEn;
    }

    public String getCompanyNameAr() {
        return CompanyNameAr;
    }

    public void setCompanyNameAr(String companyNameAr) {
        CompanyNameAr = companyNameAr;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(ApptId);
        dest.writeString(ApptNo);
        dest.writeString(ApptDate);
        dest.writeLong(DoctorId);
        dest.writeString(DoctorNameEn);
        dest.writeString(DoctorNameAr);
        dest.writeString(SpecialtyEn);
        dest.writeString(SpecialtyAr);
        dest.writeInt(OrderType);
        dest.writeLong(OrderId);
        dest.writeString(ItemCode);
        dest.writeString(Name);
        dest.writeString(OrderDate);
        dest.writeString(OrderNumber);
        dest.writeString(ReportUrl);
        dest.writeString(EncrypetPatientId);
        dest.writeString(Company);
        dest.writeString(CompanyNameEn);
        dest.writeString(CompanyNameAr);
    }
}
