package com.telemedicine.myclinic.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class CardModel implements Parcelable {

    @SerializedName("CardBrand")
    String CardBrand;

    @SerializedName("CardHolder")
    String CardHolder;

    @SerializedName("CardHolderRegID")
    String CardHolderRegID;

    @SerializedName("ExpiryMonth")
    String ExpiryMonth;

    @SerializedName("ExpiryYear")
    String ExpiryYear;

    @SerializedName("Last4Digit")
    String Last4Digit;

    @SerializedName("CardNumber")
    String CardNumber;

    @SerializedName("PaymentCardId")
    int PaymentCardId;

    public void setCardBrand(String CardBrand) {
        this.CardBrand = CardBrand;
    }
    public void setCardNumber(String cardNumber) {
        this.CardNumber = cardNumber;
    }

    public void setCardHolder(String CardHolder) {
        this.CardHolder = CardHolder;
    }
    public void setCardHolderRegID(String CardHolderRegID) {
        this.CardHolderRegID = CardHolderRegID;
    }
    public void setExpiryMonth(String ExpiryMonth) {
        this.ExpiryMonth = ExpiryMonth;
    }
    public void setExpiryYear(String ExpiryYear) {
        this.ExpiryYear = ExpiryYear;
    }
    public void setLast4Digit(String Last4Digit) {
        this.Last4Digit = Last4Digit;
    }
    public void setPaymentCardId(int PaymentCardId) {
        this.PaymentCardId = PaymentCardId;
    }

    public String getCardBrand() {
        return CardBrand;
    }

    public String getCardHolder() {
        return CardHolder;
    }
    public String getCardHolderRegID() {
        return CardHolderRegID;
    }
    public String getExpiryMonth() {
        return ExpiryMonth;
    }
    public String getExpiryYear() {
        return ExpiryYear;
    }
    public String getLast4Digit() {
        return Last4Digit;
    }
    public String getCardNumber() {
        return CardNumber;
    }
    public int getPaymentCardId() {
        return PaymentCardId;
    }
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.CardBrand);
        dest.writeString(this.CardHolder);
        dest.writeString(this.CardHolderRegID);
        dest.writeString(this.ExpiryMonth);
        dest.writeString(this.ExpiryYear);
        dest.writeString(this.Last4Digit);
        dest.writeString(this.CardNumber);
        dest.writeInt(this.PaymentCardId);
    }

    public CardModel() {
    }

    protected CardModel(Parcel in) {
        this.CardBrand = in.readString();
        this.CardHolder = in.readString();
        this.CardHolderRegID = in.readString();
        this.ExpiryMonth = in.readString();
        this.ExpiryYear = in.readString();
        this.Last4Digit = in.readString();
        this.CardNumber = in.readString();
        this.PaymentCardId = in.readInt();
    }

    public static final Creator<CardModel> CREATOR = new Creator<CardModel>() {
        @Override
        public CardModel createFromParcel(Parcel source) {
            return new CardModel(source);
        }

        @Override
        public CardModel[] newArray(int size) {
            return new CardModel[size];
        }
    };
}
