package com.telemedicine.myclinic.models.homevisit;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class HomeCreateDTO {

    @SerializedName("SecurityToken")
    String SecurityToken;

    @SerializedName("PatientId")
    String PatientId;

    @SerializedName("CoordLat")
    String CoordLat;

    @SerializedName("CoordLong")
    String CoordLong;

    @SerializedName("Address")
    String Address;

    @SerializedName("DistrictId")
    String DistrictId;

    @SerializedName("PrefVisitDateTime")
    String PrefVisitDateTime;

    @SerializedName("LabOrders")
    ArrayList<LabOrder> LabOrders;

    @SerializedName("CompanyId")
    String companyId;

    public HomeCreateDTO(String securityToken, String patientId, String coordLat, String coordLong, String address, String districtId, String prefVisitDateTime, ArrayList<LabOrder> labOrders,String companyId) {
        SecurityToken = securityToken;
        PatientId = patientId;
        CoordLat = coordLat;
        CoordLong = coordLong;
        Address = address;
        DistrictId = districtId;
        PrefVisitDateTime = prefVisitDateTime;
        LabOrders = labOrders;
        this.companyId = companyId;
    }
}
