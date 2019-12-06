package com.infinitycare.health.login.model;

import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "IPlanDetails")
public class IpPlanDetails {

    public String _id;
    public String mName;
    public String mProvider;

    private String mPremium;
    private String mDeductible;
    private String mCoPayment;
    private String mAnnualOutOfPocketLimit;
    private String mLevel;

    public IpPlanDetails(String mName, String mProvider) {
        this._id = Integer.toString(mName.hashCode());
        this.mName = mName;
        this.mProvider = mProvider;
        this.mPremium = "";
        this.mDeductible = "";
        this.mCoPayment = "";
        this.mAnnualOutOfPocketLimit = "";
        this.mLevel = "";
    }

    public String getPremium() {
        return mPremium;
    }

    public String getDeductible() {
        return mDeductible;
    }

    public String getCoPayment() {
        return mCoPayment;
    }

    public String getAnnualOutOfPocketLimit() {
        return mAnnualOutOfPocketLimit;
    }

    public void setPremium(String premium) {
        mPremium = premium;
    }

    public void setDeductible(String mDeductible) {
        this.mDeductible = mDeductible;
    }

    public void setCoPayment(String mCoPayment) {
        this.mCoPayment = mCoPayment;
    }

    public void setAnnualOutOfPocketLimit(String mAnnualOutOfPocketLimit) {
        this.mAnnualOutOfPocketLimit = mAnnualOutOfPocketLimit;
    }

    public void setLevel(String mLevel) {
        this.mLevel = mLevel;
    }
}
