package com.telemedicine.myclinic.models;


import android.os.Parcel;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class CardsListModel extends BaseEntity {

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

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeTypedList(this.cards);
    }

    public CardsListModel() {
    }

    protected CardsListModel(Parcel in) {
        super(in);
        this.cards = in.createTypedArrayList(CardModel.CREATOR);
    }


    public static final Creator<CardModel> CREATOR = new Creator<CardModel>() {
        @Override
        public CardModel createFromParcel(Parcel source) {
            return new CardModel(source);
        }

        @Override
        public CardModel[] newArray(int size) {
            return new CardModel[size];
        }
    };
}

