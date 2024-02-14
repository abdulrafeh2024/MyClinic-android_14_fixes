package com.telemedicine.myclinic.models.topupfee;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;
import com.telemedicine.myclinic.models.BaseEntity;
import com.telemedicine.myclinic.models.logonmodels.SecurityToken;
import com.telemedicine.myclinic.models.patientMiniModels.PatientsMiniModel;

import java.util.ArrayList;

public class TopUpFeeResponseModel extends BaseEntity implements Parcelable {

    protected TopUpFeeResponseModel(Parcel in) {
        super(in);
        securityToken = in.readParcelable(SecurityToken.class.getClassLoader());
        topUpFeeModel = in.createTypedArrayList(TopUpFeeModel.CREATOR);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeParcelable(securityToken, flags);
        dest.writeTypedList(topUpFeeModel);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<TopUpFeeResponseModel> CREATOR = new Creator<TopUpFeeResponseModel>() {
        @Override
        public TopUpFeeResponseModel createFromParcel(Parcel in) {
            return new TopUpFeeResponseModel(in);
        }

        @Override
        public TopUpFeeResponseModel[] newArray(int size) {
            return new TopUpFeeResponseModel[size];
        }
    };

    public SecurityToken getSecurityToken() {
        return securityToken;
    }

    public void setSecurityToken(SecurityToken securityToken) {
        this.securityToken = securityToken;
    }

    public ArrayList<TopUpFeeModel> getTopUpFeeModel() {
        return topUpFeeModel;
    }

    public void setTopUpFeeModel(ArrayList<TopUpFeeModel> topUpFeeModel) {
        this.topUpFeeModel = topUpFeeModel;
    }

    @SerializedName("SecurityToken")
    SecurityToken securityToken;

    @SerializedName("TopUpFees")
    ArrayList<TopUpFeeModel> topUpFeeModel;

}
