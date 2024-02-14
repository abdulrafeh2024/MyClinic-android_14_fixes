
package com.telemedicine.myclinic.models.teleprice;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TelePriceDto implements Parcelable {

    @SerializedName("SecurityToken")
    @Expose
    public String securityToken;
    @SerializedName("IsCash")
    @Expose
    public Boolean isCash;
    @SerializedName("InsuranceId")
    @Expose
    public long InsuranceId;
    @SerializedName("DoctorId")
    @Expose
    public long DoctorId;

    @SerializedName("CompanyId")
    String companyId;

    /**
     * No args constructor for use in serialization
     */
    public TelePriceDto() {
    }

    /**
     * @param securityToken
     * @param isCash
     */
    public TelePriceDto(String securityToken, Boolean isCash, long insuranceId, long doctorId,String companyId) {
        super();
        this.securityToken = securityToken;
        this.isCash = isCash;
        InsuranceId = insuranceId;
        DoctorId = doctorId;
        this.companyId = companyId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.securityToken);
        dest.writeValue(this.isCash);
        dest.writeLong(this.InsuranceId);
        dest.writeString(this.companyId);
    }

    protected TelePriceDto(Parcel in) {
        this.securityToken = in.readString();
        this.isCash = (Boolean) in.readValue(Boolean.class.getClassLoader());
        this.InsuranceId = in.readLong();
        this.companyId = in.readString();
    }

    public static final Creator<TelePriceDto> CREATOR = new Creator<TelePriceDto>() {
        @Override
        public TelePriceDto createFromParcel(Parcel source) {
            return new TelePriceDto(source);
        }

        @Override
        public TelePriceDto[] newArray(int size) {
            return new TelePriceDto[size];
        }
    };
}
