package com.telemedicine.myclinic.models.centerOfExcellency;

import com.google.gson.annotations.SerializedName;
import com.telemedicine.myclinic.models.BaseEntity;

import java.util.ArrayList;

public class CoEDMPSugarTestModel extends BaseEntity {

    @SerializedName("SugarTests")
    ArrayList<SugarTests> sugarTests;

    @SerializedName("SugarTestIndicator")
    int SugarTestIndicator;

    public ArrayList<SugarTests> getSugarTests() {
        return sugarTests;
    }

    public void setSugarTests(ArrayList<SugarTests> sugarTests) {
        this.sugarTests = sugarTests;
    }

    public int getSugarTestIndicator() {
        return SugarTestIndicator;
    }

    public void setSugarTestIndicator(int sugarTestIndicator) {
        SugarTestIndicator = sugarTestIndicator;
    }
}
