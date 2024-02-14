package com.telemedicine.myclinic.models.appointments;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class QTicketLabModel implements Parcelable {

    @SerializedName("QLineId")
    long QLineId;

    @SerializedName("QSiteType")
    String QSiteType;

    @SerializedName("QStatus")
    String QStatus;

    @SerializedName("TicketNo")
    String TicketNo;

    @SerializedName("TicketsWaiting")
    long TicketsWaiting;

    protected QTicketLabModel(Parcel in) {
        QLineId = in.readLong();
        QSiteType = in.readString();
        QStatus = in.readString();
        TicketNo = in.readString();
        TicketsWaiting = in.readLong();
    }

    public static final Creator<QTicketLabModel> CREATOR = new Creator<QTicketLabModel>() {
        @Override
        public QTicketLabModel createFromParcel(Parcel in) {
            return new QTicketLabModel(in);
        }

        @Override
        public QTicketLabModel[] newArray(int size) {
            return new QTicketLabModel[size];
        }
    };

    public long getQLineId() {
        return QLineId;
    }

    public void setQLineId(long QLineId) {
        this.QLineId = QLineId;
    }

    public String getQSiteType() {
        return QSiteType;
    }

    public void setQSiteType(String QSiteType) {
        this.QSiteType = QSiteType;
    }

    public String getQStatus() {
        return QStatus;
    }

    public void setQStatus(String QStatus) {
        this.QStatus = QStatus;
    }

    public String getTicketNo() {
        return TicketNo;
    }

    public void setTicketNo(String ticketNo) {
        TicketNo = ticketNo;
    }

    public long getTicketsWaiting() {
        return TicketsWaiting;
    }

    public void setTicketsWaiting(long ticketsWaiting) {
        TicketsWaiting = ticketsWaiting;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(QLineId);
        dest.writeString(QSiteType);
        dest.writeString(QStatus);
        dest.writeString(TicketNo);
        dest.writeLong(TicketsWaiting);
    }
}
