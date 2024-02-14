package com.telemedicine.myclinic.models;

import android.os.Parcel;

import com.google.gson.annotations.SerializedName;

public class TeleInstantEnqueueResult extends BaseEntity {
    @SerializedName("EnqueueId")
    int enqueueId;

    @SerializedName("WaitingTimeMin")
    int waitingTimeMin;

    @SerializedName("SessionId")
    String SessionId;

    @SerializedName("TokenId")
    String TokenId;

    public int getEnqueueId() {
        return enqueueId;
    }

    public void setEnqueueId(int enqueueId) {
        this.enqueueId = enqueueId;
    }

    public int getWaitingTimeMin() {
        return waitingTimeMin;
    }

    public void setWaitingTimeMin(int waitingTimeMin) {
        this.waitingTimeMin = waitingTimeMin;
    }

    public String getSessionId() {
        return SessionId;
    }

    public void setSessionId(String sessionId) {
        SessionId = sessionId;
    }

    public String getTokenId() {
        return TokenId;
    }

    public void setTokenId(String tokenId) {
        TokenId = tokenId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeInt(this.enqueueId);
        dest.writeInt(this.waitingTimeMin);
        dest.writeString(this.SessionId);
        dest.writeString(this.TokenId);
    }

    public TeleInstantEnqueueResult() {
    }

    protected TeleInstantEnqueueResult(Parcel in) {
        super(in);
        this.enqueueId = in.readInt();
        this.waitingTimeMin = in.readInt();
        this.SessionId = in.readString();
        this.TokenId = in.readString();
    }

    public static final Creator<TeleInstantEnqueueResult> CREATOR = new Creator<TeleInstantEnqueueResult>() {
        @Override
        public TeleInstantEnqueueResult createFromParcel(Parcel source) {
            return new TeleInstantEnqueueResult(source);
        }

        @Override
        public TeleInstantEnqueueResult[] newArray(int size) {
            return new TeleInstantEnqueueResult[size];
        }
    };
}
