package com.telemedicine.myclinic.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class RenewSecurityToken implements Parcelable {

    @SerializedName("SecurityToken")
    String SecurityToken;

    @SerializedName("ExpiryDate")
    String ExpiryDate;

    @SerializedName("Result")
    Integer Result;

    @SerializedName("ResultDesc")
    String ResultDesc;

    @SerializedName("BusinessErrorMessageEn")
    String BusinessErrorMessageEn;

    @SerializedName("BusinessErrorMessageAr")
    String BusinessErrorMessageAr;

    @SerializedName("TechnicalErrorMessage")
    String TechnicalErrorMessage;

    @SerializedName("ErrorException")
    String ErrorException;

    public String getSecurityToken() {
        return SecurityToken;
    }

    public void setSecurityToken(String securityToken) {
        SecurityToken = securityToken;
    }

    public String getExpiryDate() {
        return ExpiryDate;
    }

    public void setExpiryDate(String expiryDate) {
        ExpiryDate = expiryDate;
    }

    public Integer getResult() {
        return Result;
    }

    public void setResult(Integer result) {
        Result = result;
    }

    public String getResultDesc() {
        return ResultDesc;
    }

    public void setResultDesc(String resultDesc) {
        ResultDesc = resultDesc;
    }

    public String getBusinessErrorMessageEn() {
        return BusinessErrorMessageEn;
    }

    public void setBusinessErrorMessageEn(String businessErrorMessageEn) {
        BusinessErrorMessageEn = businessErrorMessageEn;
    }

    public String getBusinessErrorMessageAr() {
        return BusinessErrorMessageAr;
    }

    public void setBusinessErrorMessageAr(String businessErrorMessageAr) {
        BusinessErrorMessageAr = businessErrorMessageAr;
    }

    public String getTechnicalErrorMessage() {
        return TechnicalErrorMessage;
    }

    public void setTechnicalErrorMessage(String technicalErrorMessage) {
        TechnicalErrorMessage = technicalErrorMessage;
    }

    public String getErrorException() {
        return ErrorException;
    }

    public void setErrorException(String errorException) {
        ErrorException = errorException;
    }

    protected RenewSecurityToken(Parcel in) {
        SecurityToken = in.readString();
        ExpiryDate = in.readString();
        if (in.readByte() == 0) {
            Result = null;
        } else {
            Result = in.readInt();
        }
        ResultDesc = in.readString();
        BusinessErrorMessageEn = in.readString();
        BusinessErrorMessageAr = in.readString();
        TechnicalErrorMessage = in.readString();
        ErrorException = in.readString();
    }

    public static final Creator<RenewSecurityToken> CREATOR = new Creator<RenewSecurityToken>() {
        @Override
        public RenewSecurityToken createFromParcel(Parcel in) {
            return new RenewSecurityToken(in);
        }

        @Override
        public RenewSecurityToken[] newArray(int size) {
            return new RenewSecurityToken[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(SecurityToken);
        dest.writeString(ExpiryDate);
        if (Result == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(Result);
        }
        dest.writeString(ResultDesc);
        dest.writeString(BusinessErrorMessageEn);
        dest.writeString(BusinessErrorMessageAr);
        dest.writeString(TechnicalErrorMessage);
        dest.writeString(ErrorException);
    }
}
