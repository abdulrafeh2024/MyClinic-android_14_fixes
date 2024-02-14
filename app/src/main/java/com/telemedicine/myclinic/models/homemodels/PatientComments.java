package com.telemedicine.myclinic.models.homemodels;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class PatientComments implements Parcelable {

    @SerializedName("CommentEn")
    String CommentEn;

    @SerializedName("CommentAr")
    String CommentAr;

    protected PatientComments(Parcel in) {
        CommentEn = in.readString();
        CommentAr = in.readString();
    }

    public static final Creator<PatientComments> CREATOR = new Creator<PatientComments>() {
        @Override
        public PatientComments createFromParcel(Parcel in) {
            return new PatientComments(in);
        }

        @Override
        public PatientComments[] newArray(int size) {
            return new PatientComments[size];
        }
    };

    public String getCommentEn() {
        return CommentEn;
    }

    public void setCommentEn(String commentEn) {
        CommentEn = commentEn;
    }

    public String getCommentAr() {
        return CommentAr;
    }

    public void setCommentAr(String commentAr) {
        CommentAr = commentAr;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(CommentEn);
        dest.writeString(CommentAr);
    }
}
