package com.telemedicine.myclinic.models.topupfee;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class TopUpFeeModel  implements Parcelable{

    protected TopUpFeeModel(Parcel in) {
        ItemId = in.readString();
        Description = in.readString();
        Code = in.readString();
        Price = in.readDouble();
    }

    public static final Creator<TopUpFeeModel> CREATOR = new Creator<TopUpFeeModel>() {
        @Override
        public TopUpFeeModel createFromParcel(Parcel in) {
            return new TopUpFeeModel(in);
        }

        @Override
        public TopUpFeeModel[] newArray(int size) {
            return new TopUpFeeModel[size];
        }
    };

    public String getItemId() {
        return ItemId;
    }

    public void setItemId(String itemId) {
        ItemId = itemId;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getCode() {
        return Code;
    }

    public void setCode(String code) {
        Code = code;
    }

    public double getPrice() {
        return Price;
    }

    public void setPrice(double price) {
        Price = price;
    }

    @SerializedName("ItemId")
    String ItemId;

    @SerializedName("Description")
    String Description;

    @SerializedName("Code")
    String Code;

    @SerializedName("Price")
    double Price;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(ItemId);
        dest.writeString(Description);
        dest.writeString(Code);
        dest.writeDouble(Price);
    }
}
