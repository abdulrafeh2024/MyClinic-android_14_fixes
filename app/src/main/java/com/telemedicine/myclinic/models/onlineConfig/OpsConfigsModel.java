package com.telemedicine.myclinic.models.onlineConfig;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class OpsConfigsModel implements Parcelable {

    @SerializedName("OpsConfigId")
    long OpsConfigId;

    @SerializedName("Category")
    String Category;

    @SerializedName("Key")
    String Key;

    @SerializedName("Val")
    String Val;

    @SerializedName("CreateStamp")
    String CreateStamp;

    @SerializedName("UpdateStamp")
    String UpdateStamp;

    protected OpsConfigsModel(Parcel in) {
        OpsConfigId = in.readLong();
        Category = in.readString();
        Key = in.readString();
        Val = in.readString();
        CreateStamp = in.readString();
        UpdateStamp = in.readString();
    }

    public static final Creator<OpsConfigsModel> CREATOR = new Creator<OpsConfigsModel>() {
        @Override
        public OpsConfigsModel createFromParcel(Parcel in) {
            return new OpsConfigsModel(in);
        }

        @Override
        public OpsConfigsModel[] newArray(int size) {
            return new OpsConfigsModel[size];
        }
    };

    public long getOpsConfigId() {
        return OpsConfigId;
    }

    public void setOpsConfigId(long opsConfigId) {
        OpsConfigId = opsConfigId;
    }

    public String getCategory() {
        return Category;
    }

    public void setCategory(String category) {
        Category = category;
    }

    public String getKey() {
        return Key;
    }

    public void setKey(String key) {
        Key = key;
    }

    public String getVal() {
        return Val;
    }

    public void setVal(String val) {
        Val = val;
    }

    public String getCreateStamp() {
        return CreateStamp;
    }

    public void setCreateStamp(String createStamp) {
        CreateStamp = createStamp;
    }

    public String getUpdateStamp() {
        return UpdateStamp;
    }

    public void setUpdateStamp(String updateStamp) {
        UpdateStamp = updateStamp;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(OpsConfigId);
        dest.writeString(Category);
        dest.writeString(Key);
        dest.writeString(Val);
        dest.writeString(CreateStamp);
        dest.writeString(UpdateStamp);
    }
}
