package com.telemedicine.myclinic.models.earliestslotstemp;

import android.os.Parcel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.telemedicine.myclinic.models.BaseEntity;
import com.telemedicine.myclinic.models.earliestslots.EarliestSlotsMiniModel;

import java.util.List;

public class EarliestSlotsResponseModelTemp extends BaseEntity {

    @SerializedName("ApptGetEarlistSlotBySpecialityResponse")
    @Expose
    private List<EarliestSlotsMiniModelTemp> earliestSlotsMiniModel = null;

    public List<EarliestSlotsMiniModelTemp> getEarliestSlotsMiniModel() {
        return earliestSlotsMiniModel;
    }

    public void setEarliestSlotsMiniModel(List<EarliestSlotsMiniModelTemp> earliestSlotsMiniModel) {
        this.earliestSlotsMiniModel = earliestSlotsMiniModel;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeTypedList(this.earliestSlotsMiniModel);
    }

    public EarliestSlotsResponseModelTemp() {
    }

    protected EarliestSlotsResponseModelTemp(Parcel in) {
        super(in);
        this.earliestSlotsMiniModel = in.createTypedArrayList(EarliestSlotsMiniModelTemp.CREATOR);
    }

    public static final Creator<EarliestSlotsResponseModelTemp> CREATOR = new Creator<EarliestSlotsResponseModelTemp>() {
        @Override
        public EarliestSlotsResponseModelTemp createFromParcel(Parcel source) {
            return new EarliestSlotsResponseModelTemp(source);
        }

        @Override
        public EarliestSlotsResponseModelTemp[] newArray(int size) {
            return new EarliestSlotsResponseModelTemp[size];
        }
    };
}
