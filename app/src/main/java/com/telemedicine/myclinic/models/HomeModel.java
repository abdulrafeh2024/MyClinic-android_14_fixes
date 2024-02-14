package com.telemedicine.myclinic.models;

import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;
import com.telemedicine.myclinic.models.dashboardModels.MarketingClickableUrlsModel;
import com.telemedicine.myclinic.models.homemodels.DoctorsProfile;
import com.telemedicine.myclinic.models.homemodels.PatientComments;
import com.telemedicine.myclinic.models.logonmodels.SecurityToken;

import java.util.ArrayList;

public class HomeModel extends BaseEntity implements Parcelable {

    @SerializedName("MarketingUrls")
    ArrayList<Object> MarketingUrls;

    @SerializedName("AvailableDates")
    ArrayList<Object> AvailableDates;

    @SerializedName("DoctorProfiles")
    ArrayList<DoctorsProfile> doctorsProfiles;

    @SerializedName("PatientComments")
    ArrayList<PatientComments> patientComments;

/*    @SerializedName("SecurityToken")
    String securityToken;

    public String getSecurityToken() {
        return securityToken;
    }

    public void setSecurityToken(String securityToken) {
        this.securityToken = securityToken;
    }*/

    @SerializedName("SecurityToken")
    SecurityToken securityToken;

    public SecurityToken getSecurityToken() {
        return securityToken;
    }

    public void setSecurityToken(SecurityToken securityToken) {
        this.securityToken = securityToken;
    }

    public ArrayList<Object> getAvailableDates() {
        return AvailableDates;
    }

    public ArrayList<Object> getMarketingUrls() {
        return MarketingUrls;
    }

    public void setAvailableDates(ArrayList<Object> AvailableDates) {
        this.AvailableDates = AvailableDates;
    }

    @SerializedName("MarketingClickableUrls")
    ArrayList<MarketingClickableUrlsModel> marketingClickableUrlsModels;

    public ArrayList<MarketingClickableUrlsModel> getMarketingClickableUrlsModels() {
        return marketingClickableUrlsModels;
    }

    public void setMarketingClickableUrlsModels(ArrayList<MarketingClickableUrlsModel> marketingClickableUrlsModels) {
        this.marketingClickableUrlsModels = marketingClickableUrlsModels;
    }

    public void setMarketingUrls(ArrayList<Object> marketingUrls) {
        MarketingUrls = marketingUrls;
    }

    public ArrayList<DoctorsProfile> getDoctorsProfiles() {
        return doctorsProfiles;
    }

    public void setDoctorsProfiles(ArrayList<DoctorsProfile> doctorsProfiles) {
        this.doctorsProfiles = doctorsProfiles;
    }

    public ArrayList<PatientComments> getPatientComments() {
        return patientComments;
    }

    public void setPatientComments(ArrayList<PatientComments> patientComments) {
        this.patientComments = patientComments;
    }
}
