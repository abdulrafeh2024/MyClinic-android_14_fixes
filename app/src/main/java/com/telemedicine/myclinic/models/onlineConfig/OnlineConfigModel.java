package com.telemedicine.myclinic.models.onlineConfig;

import android.os.Parcel;

import com.google.gson.annotations.SerializedName;
import com.telemedicine.myclinic.models.BaseEntity;

import java.util.ArrayList;

public class OnlineConfigModel extends BaseEntity {

    @SerializedName("OpsConfigs")
    ArrayList<Object> OpsConfigs;

    public ArrayList<Object> getOpsConfigs() {
        return OpsConfigs;
    }

    public void setOpsConfigs(ArrayList<Object> opsConfigs) {
        OpsConfigs = opsConfigs;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeList(this.OpsConfigs);
    }

    public OnlineConfigModel() {
    }

    protected OnlineConfigModel(Parcel in) {
        super(in);
        this.OpsConfigs = new ArrayList<Object>();
        in.readList(this.OpsConfigs, Object.class.getClassLoader());
    }

    public static final Creator<OnlineConfigModel> CREATOR = new Creator<OnlineConfigModel>() {
        @Override
        public OnlineConfigModel createFromParcel(Parcel source) {
            return new OnlineConfigModel(source);
        }

        @Override
        public OnlineConfigModel[] newArray(int size) {
            return new OnlineConfigModel[size];
        }
    };
}
