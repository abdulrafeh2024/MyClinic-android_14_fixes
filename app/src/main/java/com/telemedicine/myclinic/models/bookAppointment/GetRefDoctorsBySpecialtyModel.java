package com.telemedicine.myclinic.models.bookAppointment;

import com.google.gson.annotations.SerializedName;
import com.telemedicine.myclinic.models.BaseEntity;
import com.telemedicine.myclinic.models.dashboardModels.DoctorProfile;
import com.telemedicine.myclinic.models.homemodels.DoctorsProfile;

import java.util.ArrayList;

public class GetRefDoctorsBySpecialtyModel extends BaseEntity {

    @SerializedName("DoctorProfiles")
    ArrayList<DoctorsProfile> doctorsProfileArrayList;

    public ArrayList<DoctorsProfile> getDoctorsProfileArrayList() {
        return doctorsProfileArrayList;
    }

    public void setDoctorsProfileArrayList(ArrayList<DoctorsProfile> doctorsProfileArrayList) {
        this.doctorsProfileArrayList = doctorsProfileArrayList;
    }
}
