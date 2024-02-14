package com.telemedicine.myclinic.models.profilecreatoinmodels;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class RefPatientProfile {

    @SerializedName("Cities")
    ArrayList<Cities> cities;

    @SerializedName("Districts")
    ArrayList<District> Districts;

    @SerializedName("InsuranceCarriers")
    ArrayList<InsuranceCarriers> insuranceCarriers;

    @SerializedName("Nationalities")
    ArrayList<Nationalities> nationalities;

    public ArrayList<Cities> getCities() {
        return cities;
    }

    public void setCities(ArrayList<Cities> cities) {
        this.cities = cities;
    }

    public ArrayList<District> getDistricts() {
        return Districts;
    }

    public void setDistricts(ArrayList<District> districts) {
        Districts = districts;
    }

    public ArrayList<InsuranceCarriers> getInsuranceCarriers() {
        return insuranceCarriers;
    }

    public void setInsuranceCarriers(ArrayList<InsuranceCarriers> insuranceCarriers) {
        this.insuranceCarriers = insuranceCarriers;
    }

    public ArrayList<Nationalities> getNationalities() {
        return nationalities;
    }

    public void setNationalities(ArrayList<Nationalities> nationalities) {
        this.nationalities = nationalities;
    }
}
