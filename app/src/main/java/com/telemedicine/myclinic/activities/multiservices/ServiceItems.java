package com.telemedicine.myclinic.activities.multiservices;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class ServiceItems implements Parcelable {

    public String getItemId() {
        return ItemId;
    }

    public void setItemId(String itemId) {
        ItemId = itemId;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getBillStatus() {
        return BillStatus;
    }

    public void setBillStatus(String billStatus) {
        BillStatus = billStatus;
    }

    public String getServiceStatus() {
        return ServiceStatus;
    }

    public void setServiceStatus(String serviceStatus) {
        ServiceStatus = serviceStatus;
    }

    public String getResponsibility() {
        return Responsibility;
    }

    public void setProductType(String ProductType) {
        ProductType = ProductType;
    }

    public String getProductType() {
        return ProductType;
    }

    public void setProductTypeAr(String productTypeAr) {
        ProductTypeAr = productTypeAr;
    }

    public String getProductTypeAr() {
        return ProductTypeAr;
    }


    public void setResponsibility(String responsibility) {
        Responsibility = responsibility;
    }

    public double getAmount() {
        return Amount;
    }

    public void setAmount(double amount) {
        Amount = amount;
    }

    public boolean isPaid() {
        return IsPaid;
    }

    public void setPaid(boolean paid) {
        IsPaid = paid;
    }

    public boolean isPayAllowed() {
        return IsPayAllowed;
    }

    public void setPayAllowed(boolean payAllowed) {
        IsPayAllowed = payAllowed;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean isSelected) {
        this.isSelected = isSelected;
    }

    public long getCaseTransId() {
        return CaseTransId;
    }

    public void setCaseTransId(long caseTransId) {
        CaseTransId = caseTransId;
    }

    @SerializedName("ItemId")
    String ItemId;

    @SerializedName("Name")
    String Name;

    @SerializedName("BillStatus")
    String BillStatus;

    @SerializedName("ServiceStatus")
    String ServiceStatus;

    @SerializedName("Responsibility")
    String Responsibility;

    @SerializedName("ProductType")
    String ProductType;

    @SerializedName("ProductTypeAr")
    String ProductTypeAr;


    @SerializedName("Amount")
    double Amount;

    @SerializedName("IsPaid")
    boolean IsPaid;

    @SerializedName("IsPayAllowed")
    boolean IsPayAllowed;

    boolean isSelected = false;

    @SerializedName("CaseTransId")
    long CaseTransId;

    protected ServiceItems(Parcel in) {
        ItemId = in.readString();
        Name = in.readString();
        BillStatus = in.readString();
        ServiceStatus = in.readString();
        Responsibility = in.readString();
        ProductType = in.readString();
        ProductTypeAr = in.readString();
        Amount = in.readDouble();
        IsPaid = in.readByte() != 0;
        IsPayAllowed = in.readByte() != 0;
        isSelected= in.readByte() != 0;
        CaseTransId = in.readLong();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(ItemId);
        dest.writeString(Name);
        dest.writeString(BillStatus);
        dest.writeString(ServiceStatus);
        dest.writeString(Responsibility);
        dest.writeString(ProductType);
        dest.writeString(ProductTypeAr);
        dest.writeDouble(Amount);
        dest.writeByte((byte) (IsPaid ? 1 : 0));
        dest.writeByte((byte) (IsPayAllowed ? 1 : 0));
        dest.writeByte((byte) (isSelected ? 1 : 0));
        dest.writeLong(CaseTransId);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<ServiceItems> CREATOR = new Creator<ServiceItems>() {
        @Override
        public ServiceItems createFromParcel(Parcel in) {
            return new ServiceItems(in);
        }

        @Override
        public ServiceItems[] newArray(int size) {
            return new ServiceItems[size];
        }
    };
}
