package com.telemedicine.myclinic.models;

import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.RequiresApi;

import com.google.gson.annotations.SerializedName;

public class NotificationMiniModel implements Parcelable {


    @SerializedName("Id")
    long Id;

    @SerializedName("CampaignName")
    String CampaignName;

    @SerializedName("TitleEn")
    String Title;

    @SerializedName("TitleAr")
    String TitleAr;

    @SerializedName("DateTime")
    String DateTime;

    @SerializedName("Url")
    String Url;

    @SerializedName("BodyEn")
    String Body;

    @SerializedName("BodyAr")
    String BodyAr;

    @SerializedName("IsRead")
    Boolean IsRead;

    public long getId() {
        return Id;
    }

    public void setId(long id) {
        Id = id;
    }

    public String getCampaignName() {
        return CampaignName;
    }

    public void setCampaignName(String campaignName) {
        CampaignName = campaignName;
    }

    public String getTitle() {
        return Title;
    }

    public String getTitleAr() {
        return TitleAr;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public void setTitleAr(String titleAr) {
        TitleAr = titleAr;
    }


    public boolean getIsRead() {
        return IsRead;
    }

    public void setIsRead(boolean isRead) {
        IsRead = isRead;
    }

    public String getDateTime() {
        return DateTime;
    }

    public void setDateTime(String dateTime) {
        DateTime = dateTime;
    }

    public String getUrl() {
        return Url;
    }

    public void setUrl(String url) {
        Url = url;
    }

    public String getBody() {
        return Body;
    }

    public String getBodyAr() {
        return BodyAr;
    }

    public void setBody(String body) {
        Body = body;
    }

    public void setBodyAr(String bodyAr) {
        BodyAr = bodyAr;
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    protected NotificationMiniModel(Parcel in) {
        Id = in.readLong();
        CampaignName = in.readString();
        IsRead = in.readBoolean();
        Title = in.readString();
        TitleAr = in.readString();
        DateTime = in.readString();
        Url = in.readString();
        Body = in.readString();
        BodyAr = in.readString();
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(Id);
        dest.writeString(CampaignName);
        dest.writeString(Title);
        dest.writeString(TitleAr);
        dest.writeString(BodyAr);
        dest.writeString(DateTime);
        dest.writeString(Url);
        dest.writeString(Body);
        dest.writeBoolean(IsRead);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<NotificationMiniModel> CREATOR = new Creator<NotificationMiniModel>() {
        @RequiresApi(api = Build.VERSION_CODES.Q)
        @Override
        public NotificationMiniModel createFromParcel(Parcel in) {
            return new NotificationMiniModel(in);
        }

        @Override
        public NotificationMiniModel[] newArray(int size) {
            return new NotificationMiniModel[size];
        }
    };

}
