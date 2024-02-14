package com.telemedicine.myclinic.models.homevisit;

import com.google.gson.annotations.SerializedName;


public class AddressBookDeleteDTO {

    @SerializedName("SecurityToken")
    String SecurityToken;

    @SerializedName("OPatientAddressBookId")
    long OPatientAddressBookId;

    public AddressBookDeleteDTO(String securityToken, long oPatientAddressBookId) {
        SecurityToken = securityToken;
        OPatientAddressBookId = oPatientAddressBookId;
    }
}