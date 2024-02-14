package com.telemedicine.myclinic.models;

public class PastAppointmentsModel {

    String imageUrl;
    String doctorsName;
    String doctorsProfession;

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    String dateTime;


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
}
