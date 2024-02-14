package com.telemedicine.myclinic.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;
import com.telemedicine.myclinic.models.BaseEntity;
import com.telemedicine.myclinic.models.appointments.ApptsMiniModel;

import java.util.ArrayList;

public class NotificationModel  extends BaseEntity{

    @SerializedName("PushNotifications")
    ArrayList<NotificationMiniModel> PushNotifications;

    protected NotificationModel(Parcel in) {
        super(in);
        PushNotifications = in.createTypedArrayList(NotificationMiniModel.CREATOR);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeTypedList(PushNotifications);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<NotificationModel> CREATOR = new Creator<NotificationModel>() {
        @Override
        public NotificationModel createFromParcel(Parcel in) {
            return new NotificationModel(in);
        }

        @Override
        public NotificationModel[] newArray(int size) {
            return new NotificationModel[size];
        }
    };

    public ArrayList<NotificationMiniModel> getPushNotifications() {
        return PushNotifications;
    }

    public void setPushNotifications(ArrayList<NotificationMiniModel> PushNotifications) {
        this.PushNotifications = PushNotifications;
    }

}
