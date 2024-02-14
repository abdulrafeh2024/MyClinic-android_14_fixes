package com.telemedicine.myclinic.models.appointments;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;
import com.telemedicine.myclinic.models.BaseEntity;
import com.telemedicine.myclinic.models.Card;
import com.telemedicine.myclinic.models.CardModel;

import java.util.ArrayList;

public class ApptsModel extends BaseEntity {

    @SerializedName("ApptMinis")
    ArrayList<ApptsMiniModel> apptsMiniModel;

    public ArrayList<ApptsMiniModel> getApptsMiniModel() {
        return apptsMiniModel;
    }

    public void setApptsMiniModel(ArrayList<ApptsMiniModel> apptsMiniModel) {
        this.apptsMiniModel = apptsMiniModel;
    }


    @SerializedName("Cards")
    ArrayList<CardModel> cards;

    public ArrayList<CardModel> getCardsModel() {
        return cards;
    }

    public void setCardsModel(ArrayList<CardModel> cardsModel) {
        this.cards = cardsModel;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    public ApptsModel() {
    }

    protected ApptsModel(Parcel in) {
        super(in);
        this.apptsMiniModel = in.createTypedArrayList(ApptsMiniModel.CREATOR);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(this.apptsMiniModel);
    }

    public static final Creator<ApptsModel> CREATOR = new Creator<ApptsModel>() {
        @Override
        public ApptsModel createFromParcel(Parcel source) {
            return new ApptsModel(source);
        }

        @Override
        public ApptsModel[] newArray(int size) {
            return new ApptsModel[size];
        }
    };
}
