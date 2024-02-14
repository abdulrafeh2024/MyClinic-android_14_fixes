package com.telemedicine.myclinic.models;

public class DoctorsModel {

    String imageUrl;
    String doctorsName;
    String doctorsProfession;
    String doctorsDegree;

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getDoctorsName() {
        return doctorsName;
    }

    public void setDoctorsName(String doctorsName) {
        this.doctorsName = doctorsName;
    }

    public String getDoctorsProfession() {
        return doctorsProfession;
    }

    public void setDoctorsProfession(String doctorsProfession) {
        this.doctorsProfession = doctorsProfession;
    }

    public String getDoctorsDegree() {
        return doctorsDegree;
    }

    public void setDoctorsDegree(String doctorsDegree) {
        this.doctorsDegree = doctorsDegree;
    }
}
