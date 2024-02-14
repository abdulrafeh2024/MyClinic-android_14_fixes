package com.telemedicine.myclinic.models.bookAppointment;

import com.google.gson.annotations.SerializedName;
import com.telemedicine.myclinic.models.BaseEntity;

import java.util.ArrayList;

public class ApptGetSlotsModel extends BaseEntity {

    @SerializedName("AppointmentBookType")
    long AppointmentBookType;

    @SerializedName("OpenSlots")
    ArrayList<String> OpenSlots;

    @SerializedName("AMSlots")
    ArrayList<String> AMSlots;

    @SerializedName("PMSlots")
    ArrayList<String> PMSlots;

    public long getAppointmentBookType() {
        return AppointmentBookType;
    }

    public void setAppointmentBookType(long appointmentBookType) {
        AppointmentBookType = appointmentBookType;
    }

    public ArrayList<String> getOpenSlots() {
        return OpenSlots;
    }

    public void setOpenSlots(ArrayList<String> openSlots) {
        OpenSlots = openSlots;
    }

    public ArrayList<String> getAMSlots() {
        return AMSlots;
    }

    public void setAMSlots(ArrayList<String> AMSlots) {
        this.AMSlots = AMSlots;
    }

    public ArrayList<String> getPMSlots() {
        return PMSlots;
    }

    public void setPMSlots(ArrayList<String> PMSlots) {
        this.PMSlots = PMSlots;
    }
}
