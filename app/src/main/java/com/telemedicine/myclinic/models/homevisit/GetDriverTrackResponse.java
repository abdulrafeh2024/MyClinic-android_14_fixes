package com.telemedicine.myclinic.models.homevisit;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.telemedicine.myclinic.models.BaseEntity;

import java.util.List;

public class GetDriverTrackResponse extends BaseEntity {

    @SerializedName("DriverTrack")
    @Expose
    private DriverTrack DriverTrack = null;

    public com.telemedicine.myclinic.models.homevisit.DriverTrack getDriverTrack() {
        return DriverTrack;
    }

    public void setDriverTrack(com.telemedicine.myclinic.models.homevisit.DriverTrack driverTrack) {
        DriverTrack = driverTrack;
    }

    protected GetDriverTrackResponse(Parcel in) {
        DriverTrack = in.readParcelable(com.telemedicine.myclinic.models.homevisit.DriverTrack.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(DriverTrack, flags);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<GetDriverTrackResponse> CREATOR = new Creator<GetDriverTrackResponse>() {
        @Override
        public GetDriverTrackResponse createFromParcel(Parcel in) {
            return new GetDriverTrackResponse(in);
        }

        @Override
        public GetDriverTrackResponse[] newArray(int size) {
            return new GetDriverTrackResponse[size];
        }
    };

}
