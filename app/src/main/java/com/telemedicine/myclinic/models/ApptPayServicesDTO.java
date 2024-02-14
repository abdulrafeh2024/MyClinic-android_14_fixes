package com.telemedicine.myclinic.models;

import com.google.gson.annotations.SerializedName;

public class ApptPayServicesDTO {

    @SerializedName("CaseTransRecId")
    long CaseTransRecId;

    @SerializedName("Amount")
    double Amount;

    @SerializedName("Company")
    String Company;

    @SerializedName("PaymentMode")
    String PaymentMode;

    public long getCaseTransRecId() {
        return CaseTransRecId;
    }

    public void setCaseTransRecId(long caseTransRecId) {
        CaseTransRecId = caseTransRecId;
    }

    public double getAmount() {
        return Amount;
    }

    public void setAmount(double amount) {
        Amount = amount;
    }

    public String getCompany() {
        return Company;
    }

    public void setCompany(String company) {
        Company = company;
    }

    public String getPaymentMode() {
        return PaymentMode;
    }

    public void setPaymentMode(String paymentMode) {
        PaymentMode = paymentMode;
    }

    public ApptPayServicesDTO(long CaseTransRecId, double Amount, String Company, String PaymentMode) {
        this.CaseTransRecId = CaseTransRecId;
        this.Amount = Amount;
        this.Company = Company;
        this.PaymentMode = PaymentMode;
    }

}
