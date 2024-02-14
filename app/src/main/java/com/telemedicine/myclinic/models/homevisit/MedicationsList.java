package com.telemedicine.myclinic.models.homevisit;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MedicationsList implements Parcelable {

    @SerializedName("Dosage")
    @Expose
    private String Dosage;
    @SerializedName("ItemId")
    @Expose
    private String ItemId;
    @SerializedName("Name")
    @Expose
    private String Name;
    @SerializedName("TotalAmount")
    @Expose
    private Double TotalAmount;

    protected MedicationsList(Parcel in) {
        Dosage = in.readString();
        ItemId = in.readString();
        Name = in.readString();
        if (in.readByte() == 0) {
            TotalAmount = null;
        } else {
            TotalAmount = in.readDouble();
        }
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(Dosage);
        dest.writeString(ItemId);
        dest.writeString(Name);
        if (TotalAmount == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeDouble(TotalAmount);
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<MedicationsList> CREATOR = new Creator<MedicationsList>() {
        @Override
        public MedicationsList createFromParcel(Parcel in) {
            return new MedicationsList(in);
        }

        @Override
        public MedicationsList[] newArray(int size) {
            return new MedicationsList[size];
        }
    };

    public String getDosage() {
        return Dosage;
    }

    public void setDosage(String dosage) {
        Dosage = dosage;
    }

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

    public Double getTotalAmount() {
        return TotalAmount;
    }

    public void setTotalAmount(Double totalAmount) {
        TotalAmount = totalAmount;
    }

}

