package com.telemedicine.myclinic.models.earliestslots;

import android.os.Parcel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.telemedicine.myclinic.models.BaseEntity;
import com.telemedicine.myclinic.models.weekschedule.WeekScheduleResponse;

import java.util.List;

public class EarliestSlotsResponseModel extends BaseEntity {

    @SerializedName("ApptGetEarlistSlotBySpecialityResponse")
    @Expose
    private List<EarliestSlotsMiniModel> earliestSlotsMiniModel = null;

    public List<EarliestSlotsMiniModel> getEarliestSlotsMiniModel() {
        return earliestSlotsMiniModel;
    }

    public void setEarliestSlotsMiniModel(List<EarliestSlotsMiniModel> earliestSlotsMiniModel) {
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

    public EarliestSlotsResponseModel() {
    }

    protected EarliestSlotsResponseModel(Parcel in) {
        super(in);
        this.earliestSlotsMiniModel = in.createTypedArrayList(EarliestSlotsMiniModel.CREATOR);
    }

    public static final Creator<EarliestSlotsResponseModel> CREATOR = new Creator<EarliestSlotsResponseModel>() {
        @Override
        public EarliestSlotsResponseModel createFromParcel(Parcel source) {
            return new EarliestSlotsResponseModel(source);
        }

        @Override
        public EarliestSlotsResponseModel[] newArray(int size) {
            return new EarliestSlotsResponseModel[size];
        }
    };
}
