package com.telemedicine.myclinic.models.appointments;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class QTicketPharmModel implements Parcelable {

    @SerializedName("QLineId")
    long QLineId;

    @SerializedName("QSiteType")
    String QSiteType;

    @SerializedName("QStatus")
    String QStatus;

    @SerializedName("TicketNo")
    long TicketNo;

    @SerializedName("TicketsWaiting")
    long TicketsWaiting;

    public long getTicketsWaiting() {
        return TicketsWaiting;
    }

    public void setTicketsWaiting(long ticketsWaiting) {
        TicketsWaiting = ticketsWaiting;
    }

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

    public long getTicketNo() {
        return TicketNo;
    }

    public void setTicketNo(long ticketNo) {
        TicketNo = ticketNo;
    }

    protected QTicketPharmModel(Parcel in) {
        QLineId = in.readLong();
        QSiteType = in.readString();
        QStatus = in.readString();
        TicketNo = in.readLong();
    }

    public static final Creator<QTicketPharmModel> CREATOR = new Creator<QTicketPharmModel>() {
        @Override
        public QTicketPharmModel createFromParcel(Parcel in) {
            return new QTicketPharmModel(in);
        }

        @Override
        public QTicketPharmModel[] newArray(int size) {
            return new QTicketPharmModel[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(QLineId);
        dest.writeString(QSiteType);
        dest.writeString(QStatus);
        dest.writeLong(TicketNo);
    }
}
