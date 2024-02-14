package com.telemedicine.myclinic.models.logonmodels;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class SecurityToken implements Parcelable {

    @SerializedName("Token")
    String Token;

    @SerializedName("ExpiryDate")
    String ExpiryDate;

    protected SecurityToken(Parcel in) {
        Token = in.readString();
        ExpiryDate = in.readString();
    }

    public static final Creator<SecurityToken> CREATOR = new Creator<SecurityToken>() {
        @Override
        public SecurityToken createFromParcel(Parcel in) {
            return new SecurityToken(in);
        }

        @Override
        public SecurityToken[] newArray(int size) {
            return new SecurityToken[size];
        }
    };

    public String getToken() {
        return Token;
    }

    public void setToken(String token) {
        Token = token;
    }

    public String getExpiryDate() {
        return ExpiryDate;
    }

    public void setExpiryDate(String expiryDate) {
        ExpiryDate = expiryDate;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(Token);
        dest.writeString(ExpiryDate);
    }
}
