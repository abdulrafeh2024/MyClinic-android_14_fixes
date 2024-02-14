package com.telemedicine.myclinic.models.dashboardModels;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class MarketingClickableUrlsModel implements Parcelable {

    @SerializedName("Key")
    String Key;

    @SerializedName("Value")
    String Value;

    protected MarketingClickableUrlsModel(Parcel in) {
        Key = in.readString();
        Value = in.readString();
        BannerName = in.readString();
        BannerUrlEn = in.readString();
        BannerUrlAr = in.readString();
        ClickableUrlEn = in.readString();
        ClickableUrlAr = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(Key);
        dest.writeString(Value);
        dest.writeString(BannerName);
        dest.writeString(BannerUrlEn);
        dest.writeString(BannerUrlAr);
        dest.writeString(ClickableUrlEn);
        dest.writeString(ClickableUrlAr);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<MarketingClickableUrlsModel> CREATOR = new Creator<MarketingClickableUrlsModel>() {
        @Override
        public MarketingClickableUrlsModel createFromParcel(Parcel in) {
            return new MarketingClickableUrlsModel(in);
        }

        @Override
        public MarketingClickableUrlsModel[] newArray(int size) {
            return new MarketingClickableUrlsModel[size];
        }
    };

    public String getKey() {
        return Key;
    }

    public void setKey(String key) {
        Key = key;
    }

    public String getValue() {
        return Value;
    }

    public void setValue(String value) {
        Value = value;
    }

    @SerializedName("BannerName")
    String BannerName;

    @SerializedName("BannerUrlEn")
    String BannerUrlEn;

    @SerializedName("BannerUrlAr")
    String BannerUrlAr;

    @SerializedName("ClickableUrlEn")
    String ClickableUrlEn;

    @SerializedName("ClickableUrlAr")
    String ClickableUrlAr;

    public String getBannerName() {
        return BannerName;
    }

    public void setBannerName(String bannerName) {
        BannerName = bannerName;
    }

    public String getBannerUrlEn() {
        return BannerUrlEn;
    }

    public void setBannerUrlEn(String bannerUrlEn) {
        BannerUrlEn = bannerUrlEn;
    }

    public String getBannerUrlAr() {
        return BannerUrlAr;
    }

    public void setBannerUrlAr(String bannerUrlAr) {
        BannerUrlAr = bannerUrlAr;
    }

    public String getClickableUrlEn() {
        return ClickableUrlEn;
    }

    public void setClickableUrlEn(String clickableUrlEn) {
        ClickableUrlEn = clickableUrlEn;
    }

    public String getClickableUrlAr() {
        return ClickableUrlAr;
    }

    public void setClickableUrlAr(String clickableUrlAr) {
        ClickableUrlAr = clickableUrlAr;
    }
}
