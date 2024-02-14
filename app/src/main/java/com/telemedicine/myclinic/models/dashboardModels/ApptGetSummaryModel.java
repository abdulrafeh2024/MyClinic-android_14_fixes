package com.telemedicine.myclinic.models.dashboardModels;

import com.google.gson.annotations.SerializedName;
import com.telemedicine.myclinic.models.BaseEntity;

import java.util.ArrayList;

public class ApptGetSummaryModel extends BaseEntity {

    @SerializedName("UpcomingCount")
    int UpcomingCount;

    @SerializedName("PastCount")
    int PastCount;

    @SerializedName("ReportCount")
    int ReportCount;

    @SerializedName("TeleConsentEn")
    String TeleConsentEn;

    @SerializedName("TeleConsentAr")
    String TeleConsentAr;

    @SerializedName("TeleTermsOfUseEn")
    String TeleTermsOfUseEn;

    @SerializedName("TeleTermsOfUseAr")
    String TeleTermsOfUseAr;

    @SerializedName("MarketingUrls")
    ArrayList<Object> MarketingUrls;

    @SerializedName("MarketingClickableUrls")
    ArrayList<MarketingClickableUrlsModel> marketingClickableUrlsModels;

    public ArrayList<MarketingClickableUrlsModel> getMarketingClickableUrlsModels() {
        return marketingClickableUrlsModels;
    }

    public void setMarketingClickableUrlsModels(ArrayList<MarketingClickableUrlsModel> marketingClickableUrlsModels) {
        this.marketingClickableUrlsModels = marketingClickableUrlsModels;
    }

    public int getUpcomingCount() {
        return UpcomingCount;
    }

    public void setUpcomingCount(int upcomingCount) {
        UpcomingCount = upcomingCount;
    }

    public int getPastCount() {
        return PastCount;
    }

    public void setPastCount(int pastCount) {
        PastCount = pastCount;
    }

    public int getReportCount() {
        return ReportCount;
    }

    public void setReportCount(int reportCount) {
        ReportCount = reportCount;
    }

    public String getTeleConsentEn() {
        return TeleConsentEn;
    }

    public void setTeleConsentEn(String teleConsentEn) {
        TeleConsentEn = teleConsentEn;
    }

    public String getTeleConsentAr() {
        return TeleConsentAr;
    }

    public void setTeleConsentAr(String teleConsentAr) {
        TeleConsentAr = teleConsentAr;
    }

    public String getTeleTermsOfUseEn() {
        return TeleTermsOfUseEn;
    }

    public void setTeleTermsOfUseEn(String teleTermsOfUseEn) {
        TeleTermsOfUseEn = teleTermsOfUseEn;
    }

    public String getTeleTermsOfUseAr() {
        return TeleTermsOfUseAr;
    }

    public void setTeleTermsOfUseAr(String teleTermsOfUseAr) {
        TeleTermsOfUseAr = teleTermsOfUseAr;
    }

    public ArrayList<Object> getMarketingUrls() {
        return MarketingUrls;
    }

    public void setMarketingUrls(ArrayList<Object> marketingUrls) {
        MarketingUrls = marketingUrls;
    }
}
