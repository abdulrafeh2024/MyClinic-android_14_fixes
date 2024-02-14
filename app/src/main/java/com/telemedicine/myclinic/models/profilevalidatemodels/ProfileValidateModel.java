package com.telemedicine.myclinic.models.profilevalidatemodels;

import com.google.gson.annotations.SerializedName;
import com.telemedicine.myclinic.models.BaseEntity;

public class ProfileValidateModel extends BaseEntity {

    @SerializedName("IsMembershipNoExists")
    boolean IsMembershipNoExists;

    @SerializedName("IsNationalIdExists")
    boolean IsNationalIdExists;

    public boolean isMembershipNoExists() {
        return IsMembershipNoExists;
    }

    public void setMembershipNoExists(boolean membershipNoExists) {
        IsMembershipNoExists = membershipNoExists;
    }

    public boolean isNationalIdExists() {
        return IsNationalIdExists;
    }

    public void setNationalIdExists(boolean nationalIdExists) {
        IsNationalIdExists = nationalIdExists;
    }
}
