package com.telemedicine.myclinic.models.homevisit;

import com.google.gson.annotations.SerializedName;

public class AddressBookUpdateDTO {

    @SerializedName("OPatientAddressBookId")
    Long OPatientAddressBookId;

    @SerializedName("SecurityToken")
    String securityToken;

    @SerializedName("AddressNotes")
    String addressNotes;

    @SerializedName("AprtId")
    Integer aprtId;

    @SerializedName("AprtNo")
    String aprtNo;

    @SerializedName("BuildingName")
    String buildingName;

    @SerializedName("BuildingNo")
    String buildingNo;

    @SerializedName("District")
    String district;

    @SerializedName("PatientId")
    Long patientId;

    @SerializedName("Street")
    String street;

    @SerializedName("Lable")
    String label;

    @SerializedName("AreaName")
    String areaName;

    @SerializedName("Lat")
    Double lat;

    @SerializedName("Lng")
    Double lng;

    public AddressBookUpdateDTO( String securityToken,Long oPatientAddressBookId, String addressNotes, Integer aprtId, String aprtNo,
                                String buildingName, String buildingNo, String district, Long patientId,
                                String street, String label, String areaName, Double lat, Double lng) {

        this.securityToken = securityToken;
        this.OPatientAddressBookId = oPatientAddressBookId;
        this.addressNotes = addressNotes;
        this.aprtId = aprtId;
        this.aprtNo = aprtNo;
        this.buildingName = buildingName;
        this.buildingNo = buildingNo;
        this.district = district;
        this.patientId = patientId;
        this.street = street;
        this.label = label;
        this.areaName = areaName;
        this.lat = lat;
        this.lng = lng;
    }

}